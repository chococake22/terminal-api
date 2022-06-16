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
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.UserRepository;
import project.terminalv2.vo.user.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public ResponseEntity getUserInfoOne(Long no) {

        // 회원 검색
        User user = userRepository.findById(no)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));

        log.info("User = {}", user);

        // 해당 no의 회원이 존재하면
        UserInfoVo userInfoVo = UserInfoVo.builder()
                .userNo(user.getUserNo())
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(userInfoVo);
    }


    @Transactional
    public ResponseEntity saveUser(UserSaveRequest request) {

        // 비밀번호 동일 여부 체크
        if (!request.getPassword().equals(request.getChkPwd())) {
            throw new ApiException(ErrorCode.NOT_EQUAL_PWD);
        }

        // 해당 아이디가 중복인지 체크
        if (userRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new ApiException(ErrorCode.DUPLICATED_USERID);
        }

        User newUser = User.builder()
                .userId(request.getUserId())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.OK).body("회원가입 성공");
    }

    @Transactional
    public ResponseEntity getUserList(Integer page, Integer size) {

        if (page > 0) {
            page = page - 1;
        }

        // 어떤 페이지를 얼마나 가져오고 오름차순인지 내림차순인지 설정하는 객체
        Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "userNo"));

        Page<User> users = userRepository.findAll(pageable);

        Page<UserInfoVo> userInfoVos = users.map(user -> UserInfoVo.builder()
                .userNo(user.getUserNo())
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build()
        );

        return ResponseEntity.status(HttpStatus.OK).body(userInfoVos);
    }


    @Transactional
    public ResponseEntity login(UserLoginRequest request) throws IllegalAccessException {

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

            return ResponseEntity.status(HttpStatus.OK).body(map);
        } else {
            throw new ApiException(ErrorCode.WRONG_PWD);
        }
    }

    @Transactional
    public ResponseEntity updateUserInfo(UserUpdRequest request, HttpServletRequest tokenInfo) {

        String userId = getUserIdFromToken(tokenInfo);

        // 해당 회원이 존재하는지 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));  // 없으면 예외처리

        if(user.getUserId().equals(userId)) {
            // 회원정보 수정
            user.updateInfo(request);

            return ResponseEntity.status(HttpStatus.OK).body("비밀번호 수정");
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
