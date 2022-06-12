package project.terminalv2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.terminalv2.domain.BusTime;
import project.terminalv2.domain.MyTime;
import project.terminalv2.domain.User;
import project.terminalv2.dto.bustime.NewBusTimeSaveRequest;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.BusTimeRepository;
import project.terminalv2.respository.MyTimeRepository;
import project.terminalv2.respository.UserRepository;
import project.terminalv2.vo.mytime.MyTimeVo;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusTimeService {

    private final BusTimeRepository busTimeRepository;
    private final UserRepository userRepository;
    private final MyTimeRepository myTimeRepository;

    @Transactional
    public ResponseEntity regNewBusTime(NewBusTimeSaveRequest request) {

        BusTime busTime = BusTime.builder()
                .startTarget(request.getStartTarget())
                .arrivedTarget(request.getEndTarget())
                .startDate(request.getStartDate())
                .busCorp(request.getBusCorp())
                .note(request.getNote())
                .build();

        busTimeRepository.save(busTime);

        return ResponseEntity.status(HttpStatus.OK).body("시간표 등록 성공");
    }

    @Transactional
    public ResponseEntity saveMyBusTime(Long busTimeNo) {

        BusTime busTime = busTimeRepository.findById(busTimeNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BUSTIME));

        log.info("busTime = {}", busTime);

        User user = userRepository.findById(3L)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));

        // 이미 가지고 있는 시간표일 경우에는 예외 처리
        if (myTimeRepository.findByUserUserNoAndBusTimeBusTimeNo(user.getUserNo(), busTimeNo).isPresent()) {
            throw new ApiException(ErrorCode.DUPLICATED_BUSTIME);
        }

        // MyTime 객체 생성
        MyTime myTime = MyTime.builder()
                .user(user)
                .busTime(busTime)
                .build();

        log.info("user = {}", user);

        myTimeRepository.save(myTime);

        return ResponseEntity.status(HttpStatus.OK).body("내 시간표 저장 성공");
    }

    @Transactional
    public ResponseEntity deleteBusTime(Long busTimeNo) {

        busTimeRepository.deleteById(busTimeNo);

        return ResponseEntity.status(HttpStatus.OK).body("삭제 성공");
    }

    @Transactional
    public ResponseEntity getMyBusTimeList(Long userNo, Integer page, Integer size) {

        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "myTimeNo"));

        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));


        Page<MyTime> myTimePage = myTimeRepository.findAllByUserUserNo(userNo, pageable);

        Page<MyTimeVo> myTimeVos = myTimePage.map(myTime -> MyTimeVo.builder()
                .startTarget(myTime.getBusTime().getStartTarget())
                .endTarget(myTime.getBusTime().getArrivedTarget())
                .startDate(myTime.getBusTime().getStartDate())
                .busCorp(myTime.getBusTime().getBusCorp())
                .note(myTime.getBusTime().getNote())
                .build());

        return ResponseEntity.status(HttpStatus.OK).body(myTimeVos);
    }
}
