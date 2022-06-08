package project.terminalv2.service;

import io.swagger.annotations.Api;
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
import project.terminalv2.dto.UserLoginRequest;
import project.terminalv2.dto.UserSaveRequest;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.UserRepository;
import project.terminalv2.vo.UserInfoVo;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity getUserInfoOne(Long no) {

        // 회원 검색
        User user = userRepository.findById(no)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

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
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));  // 없으면 예외처리

        log.info("user={}", user);

        // 비밀번호 검증
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.OK).body("로그인 성공");
        } else {
            throw new ApiException(ErrorCode.NOT_ACCEPTED);
        }
    }

}
