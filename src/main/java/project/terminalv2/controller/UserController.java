package project.terminalv2.controller;

import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.terminalv2.dto.user.UserLoginRequest;
import project.terminalv2.dto.user.UserSaveRequest;
import project.terminalv2.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원 생성", notes = "회원을 생성합니다.")
    @PostMapping("/api/v1/user")
    public ResponseEntity saveUser(@RequestBody UserSaveRequest request) {
        return userService.saveUser(request);
    }

    @ApiOperation(value = "회원 목록 조회", notes = "회원 목록을 조회합니다.")
    @GetMapping("/api/v1/user/list")
    public ResponseEntity getUserList(@RequestParam Integer page, @RequestParam Integer size) {
        return userService.getUserList(page, size);
    }

    @ApiOperation(value = "회원 개별 조회", notes = "개별 회원을 조회합니다.")
    @GetMapping("/api/v1/user")
    public ResponseEntity getUserOne(@RequestParam Long id) {
        return userService.getUserInfoOne(id);
    }

    @ApiOperation(value = "로그인", notes = "로그인을 합니다.")
    @PostMapping("/api/v1/user/login")
    public ResponseEntity login(@RequestBody UserLoginRequest request) throws IllegalAccessException {
        return userService.login(request);
    }





}
