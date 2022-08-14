package project.terminalv2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
import project.terminalv2.util.JwtManager;
import project.terminalv2.vo.user.UserDetailVo;
import project.terminalv2.vo.user.UserListVo;
import project.terminalv2.vo.user.UserLoginVo;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtManager jwtManager;
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

        log.info("입력 userId={}", request.getUserId());

        // 해당 회원이 존재하는지 확인
        User user = getUser(request.getUserId());  // 없으면 예외처리

        // 비밀번호 검증
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            log.info("접속 userId={}", request.getUserId());

            String accessToken = jwtManager.createToken(user.getUserId());
            String refreshToken = jwtManager.createRefreshToken(user.getUserId());

            // map -> vo로 묶을 필요가 있는지 점검
//            Map<String, Object> map = new LinkedHashMap<>();
//            map.put("accessToken", accessToken);
//            map.put("refreshToken", refreshToken);
//            map.put("userId", user.getUserId());
//            map.put("userNo", user.getUserNo());
//            map.put("role", user.getRole());

            UserLoginVo userLoginVo = UserLoginVo.builder()
                    .userNo(user.getUserNo())
                    .userId(user.getUserId())
                    .role(user.getRole())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            return apiResponse.makeResponse(HttpStatus.OK, "1000", "로그인 성공", userLoginVo);
        } else {
            throw new ApiException(ErrorCode.WRONG_PWD);
        }
    }

    public User getUser(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional
    public ApiResponse getUserInfoOne(Long userNo) {

        // 회원 검색
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));

        // 해당 no의 회원이 존재하면
        UserDetailVo userDetailVo = user.toUserDetailVo(user);

        return apiResponse.makeResponse(HttpStatus.OK, "1000", "개별 회원 정보 조회 성공", userDetailVo);
    }

    @Transactional
    public ApiResponse getUserList(Integer page, Integer size) {

        // 어떤 페이지를 얼마나 가져오고 오름차순인지 내림차순인지 설정하는 객체
        Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "userNo"));
        Page<User> users = userRepository.findAll(pageable);
        Page<UserListVo> userListVos = users.map(user -> user.toUserListVo(user));

        return apiResponse.makeResponse(HttpStatus.OK, "1000", "회원 목록 조회 성공", userListVos);
    }


    @Transactional
    public ApiResponse updateUserInfo(UserUpdRequest request, HttpServletRequest tokenInfo) {

        String userId = getUserIdFromToken(tokenInfo);

        // 해당 회원이 존재하는지 확인
        User user = getUser(userId);  // 없으면 예외처리

        if(user.getUserId().equals(userId)) {
            // 회원정보 수정
            user.updateInfo(request);
            UserDetailVo userDetailVo = user.toUserDetailVo(user);

            return apiResponse.makeResponse(HttpStatus.OK, "1000", "회원 정보 수정 성공", userDetailVo);
        } else {
            throw new ApiException(ErrorCode.USER_UNAUTHORIZED);
        }
    }


    // 로그인한 사용자가 접근 권한이 있는지 판단
    public boolean hasAccessAuth(String userId, HttpServletRequest tokenInfo) {
        String token = tokenInfo.getHeader("jwt");
        if(!userId.equals(jwtManager.getSubject(token))) {
            return false;
        }
        return true;
    }

    // 토큰으로부터 사용자 정보(아이디) 가져오기
    public String getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("jwt");
        return jwtManager.getSubject(token);
    }
}
