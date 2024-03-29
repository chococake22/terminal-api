package project.terminalv2.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.terminalv2.dto.user.UserLoginRequest;
import project.terminalv2.dto.user.UserSaveRequest;
import project.terminalv2.dto.user.UserUpdRequest;
import project.terminalv2.exception.ApiResponse;
import project.terminalv2.service.UserService;
import project.terminalv2.util.JwtManager;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final JwtManager jwtManager;

    @ApiOperation(value = "회원 생성", notes = "회원을 생성합니다.")
    @PostMapping("/api/v1/user")
    public ApiResponse saveUser(@RequestBody @Valid UserSaveRequest request) {
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
    public ApiResponse updateUserInfo(@RequestBody @Valid UserUpdRequest request, HttpServletRequest tokenInfo) {
        return userService.updateUserInfo(request, tokenInfo);
    }

    @ApiOperation(value = "액세스 토큰 재발급", notes = "액세스 토큰을 재발급합니다.")
    @GetMapping("/api/v1/access-token")
    public Map<String, Object> getAccessToken(@RequestParam String token) {
        return jwtManager.getAccessToken(token);
    }
}
