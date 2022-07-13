package project.terminalv2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.terminalv2.domain.User;
import project.terminalv2.dto.user.UserLoginRequest;
import project.terminalv2.dto.user.UserSaveRequest;
import project.terminalv2.dto.user.UserUpdRequest;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ApiResponse;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.UserRepository;
import project.terminalv2.vo.user.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ApiResponse apiResponse;

    @Transactional
    public ApiResponse saveUser(UserSaveRequest request) {

        // 비밀번호 동일 여부 체크
        if (!request.getPassword().equals(request.getChkPwd())) {
            throw new ApiException(ErrorCode.NOT_EQUAL_PWD);
        }

        // 해당 아이디가 중복인지 체크
        if (userRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new ApiException(ErrorCode.DUPLICATED_USERID);
        }

        User newUser = request.createUser(request);
        userRepository.save(newUser);

        return apiResponse.makeResponse(HttpStatus.OK, "1000", "회원가입 성공", newUser);
    }

    @Transactional
    public ApiResponse login(UserLoginRequest request) throws IllegalAccessException {

        // 해당 회원이 존재하는지 확인
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));  // 없으면 예외처리

        // 비밀번호 검증
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            log.info("user={}", user);

            String accessToken = jwtService.createToken(user.getUserId());
            String refreshToken = jwtService.createRefreshToken(user.getUserId());

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("accessToken", accessToken);
            map.put("refreshToken", refreshToken);
            map.put("userId", user.getUserId());

            return apiResponse.makeResponse(HttpStatus.OK, "1000", "로그인 성공", map);
        } else {
            throw new ApiException(ErrorCode.WRONG_PWD);
        }
    }

    @Transactional
    public ApiResponse getUserInfoOne(Long userNo) {

        // 회원 검색
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));

        log.info("User = {}", user);

        // 해당 no의 회원이 존재하면
        UserInfoVo userInfoVo = user.toUserInfoVo(user);

        return apiResponse.makeResponse(HttpStatus.OK, "1000", "개별 회원 정보 조회 성공", userInfoVo);
    }

    @Transactional
    public ApiResponse getUserList(Integer page, Integer size) {

        // 어떤 페이지를 얼마나 가져오고 오름차순인지 내림차순인지 설정하는 객체
        Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "userNo"));
        Page<User> users = userRepository.findAll(pageable);
        Page<UserInfoVo> userInfoVos = users.map(user -> user.toUserInfoVo(user));

        return apiResponse.makeResponse(HttpStatus.OK, "1000", "회원 목록 조회 성공", userInfoVos);
    }


    @Transactional
    public ApiResponse updateUserInfo(UserUpdRequest request, HttpServletRequest tokenInfo) {

        String userId = getUserIdFromToken(tokenInfo);

        // 해당 회원이 존재하는지 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));  // 없으면 예외처리

        if(user.getUserId().equals(userId)) {
            // 회원정보 수정
            user.updateInfo(request);
            UserInfoVo userInfoVo = user.toUserInfoVo(user);

            return apiResponse.makeResponse(HttpStatus.OK, "1000", "회원 정보 수정 성공", userInfoVo);
        } else {
            throw new ApiException(ErrorCode.USER_UNAUTHORIZED);
        }
    }


    public boolean hasAccessAuth(String userId, HttpServletRequest tokenInfo) {
        String token = tokenInfo.getHeader("jwt");
        if(!userId.equals(jwtService.getSubject(token))) {
            return false;
        }
        return true;
    }

    public String getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("jwt");
        return jwtService.getSubject(token);
    }
}
