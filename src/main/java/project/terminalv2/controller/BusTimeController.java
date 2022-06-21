package project.terminalv2.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.terminalv2.dto.bustime.NewBusTimeSaveRequest;
import project.terminalv2.exception.ApiResponse;
import project.terminalv2.service.BusTimeService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class BusTimeController {

    private final BusTimeService busTimeService;

    // 본인 확인 필요
    @ApiOperation(value = "내 시간표 조회", notes = "내 버스 시간표를 조회힙니다.")
    @GetMapping("/api/v1/bustime")
    public ApiResponse getMyBusTimeList(@RequestParam Integer page, @RequestParam Integer size, HttpServletRequest tokenInfo) {
        return busTimeService.getMyBusTimeList(page, size, tokenInfo);
    }

    // 본인 확인 필요
    @ApiOperation(value = "내 시간표 등록", notes = "내 시간표를 등록합니다.")
    @PostMapping("/api/v1/bustime/{busTimeNo}")
    public ApiResponse saveMyBusTime(@PathVariable Long busTimeNo, HttpServletRequest tokenInfo) {
        return busTimeService.saveMyBusTime(busTimeNo, tokenInfo);
    }

    // 본인 확인 필요
    @ApiOperation(value = "내 시간표 삭제", notes = "내 시간표를 삭제합니다.")
    @DeleteMapping("/api/v1/bustime/{myTimeNo}")
    public ApiResponse deleteMyBusTime(@PathVariable Long myTimeNo, HttpServletRequest tokenInfo) {
        return busTimeService.deleteMyTime(myTimeNo, tokenInfo);
    }

    // 본인 확인 필요(권한이 관리자인 경우)
    @ApiOperation(value = "신규 버스 시간표 등록", notes = "새로운 버스 시간표를 추가합니다.")
    @PostMapping("/api/v1/bustime/admin")
    public ApiResponse regNewBusTime(@RequestBody NewBusTimeSaveRequest request) {
        return busTimeService.regNewBusTime(request);
    }

    // 본인 확인 필요(권한이 관리자인 경우)
    @ApiOperation(value = "시간표 삭제", notes = "시간표를 삭제합니다.")
    @DeleteMapping("/api/v1/bustime/admin/{busTimeNo}")
    public ApiResponse deleteBusTime(@PathVariable Long busTimeNo, HttpServletRequest tokenInfo) {
        return busTimeService.deleteBusTime(busTimeNo, tokenInfo);
    }
}
