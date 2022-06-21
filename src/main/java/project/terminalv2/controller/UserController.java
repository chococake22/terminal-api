package project.terminalv2.controller;

import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.terminalv2.dto.user.UserLoginRequest;
import project.terminalv2.dto.user.UserSaveRequest;
import project.terminalv2.dto.user.UserUpdRequest;
import project.terminalv2.exception.ApiResponse;
import project.terminalv2.service.JwtService;

import project.terminalv2.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @ApiOperation(value = "회원 생성", notes = "회원을 생성합니다.")
    @PostMapping("/api/v1/user")
    public ApiResponse saveUser(@RequestBody UserSaveRequest request) {
        return userService.saveUser(request);
    }

    @ApiOperation(value = "회원 목록 조회", notes = "회원 목록을 조회합니다.")
    @GetMapping("/api/v1/user/list")
    public ApiResponse getUserList(@RequestParam Integer page, @RequestParam Integer size) {
        return userService.getUserList(page, size);
    }

    @ApiOperation(value = "회원 개별 조회", notes = "개별 회원을 조회합니다.")
    @GetMapping("/api/v1/user/{userNo}")
    public ApiResponse getUserOne(@PathVariable Long userNo) {
        return userService.getUserInfoOne(userNo);
    }


    @ApiOperation(value = "로그인", notes = "로그인을 합니다.")
    @PostMapping("/api/v1/user/login")
    public ApiResponse login(@RequestBody UserLoginRequest request) throws IllegalAccessException {
        return userService.login(request);
    }

    // 본인 확인 필요
    @ApiOperation(value = "회원정보 수정", notes = "회원 정보를 수정합니다.")
    @PutMapping("/api/v1/user")
    public ApiResponse updateUserInfo(@RequestBody UserUpdRequest request, HttpServletRequest tokenInfo) {
        return userService.updateUserInfo(request, tokenInfo);
    }

    @ApiOperation(value = "토큰 발급 테스트", notes = "토큰을 테스트 합니다.")
    @GetMapping("/api/v1/token")
    public Map<String, Object> getSubject(@RequestParam(value = "token") String token) {
        String subject = jwtService.getSubject(token);
        Map<String, Object> map = new LinkedHashMap<>();

        // 토큰 유효성 검사
        jwtService.isValidToken(token);
        map.put("result", subject);
        return map;
    }

    @ApiOperation(value = "액세스 토큰 재발급", notes = "액세스 토큰을 재발급합니다.")
    @GetMapping("/api/v1/access-token")
    public Map<String, Object> getAccessToken(@RequestParam String token) {
        String accessToken = jwtService.reCreateAccessToken(token);
        Map<String, Object> map = new HashMap<>();
        map.put("reAccessToken", accessToken);
        return map;
    }
}
