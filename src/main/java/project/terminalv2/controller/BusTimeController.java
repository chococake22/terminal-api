package project.terminalv2.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.terminalv2.dto.bustime.NewBusTimeSaveRequest;
import project.terminalv2.service.BusTimeService;

@RestController
@RequiredArgsConstructor
public class BusTimeController {

    private final BusTimeService busTimeService;

    @ApiOperation(value = "신규 버스 시간표 등록", notes = "새로운 버스 시간표를 추가합니다.")
    @PostMapping("/api/v1/bustime")
    public ResponseEntity regNewBusTime(@RequestBody NewBusTimeSaveRequest request) {
        return busTimeService.regNewBusTime(request);
    }

    @ApiOperation(value = "내 시간표 등록", notes = "내 시간표를 등록합니다.")
    @PostMapping("/api/v1/bustime/{busTimeNo}")
    public ResponseEntity saveMyBusTime(@PathVariable Long busTimeNo) {
        return busTimeService.saveMyBusTime(busTimeNo);
    }

    @ApiOperation(value = "시간표 삭제", notes = "시간표를 삭제합니다.")
    @DeleteMapping("/api/v1/bustime/{busTimeNo}")
    public ResponseEntity deleteBusTime(@PathVariable Long busTimeNo) {
        return busTimeService.deleteBusTime(busTimeNo);
    }

    @ApiOperation(value = "내 시간표 조회", notes = "내 버스 시간표를 조회힙니다.")
    @GetMapping("/api/v1/bustime/{userNo}")
    public ResponseEntity getMyBusTimeList(@PathVariable Long userNo, @RequestParam Integer page, @RequestParam Integer size) {
        return busTimeService.getMyBusTimeList(userNo, page, size);
    }
}
