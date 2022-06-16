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

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusTimeService {

    private final BusTimeRepository busTimeRepository;
    private final UserRepository userRepository;
    private final MyTimeRepository myTimeRepository;
    private final UserService userService;

    // 관리자가 새로운 시간표 생성
    @Transactional
    public ResponseEntity regNewBusTime(NewBusTimeSaveRequest request) {

        BusTime busTime = BusTime.builder()
                .startTarget(request.getStartTarget())
                .arrivedTarget(request.getEndTarget())
                .startTime(request.getStartTime())
                .busCorp(request.getBusCorp())
                .layover(request.getLayover())
                .note(request.getNote())
                .build();

        busTimeRepository.save(busTime);

        return ResponseEntity.status(HttpStatus.OK).body("시간표 등록 성공");
    }

    // 회원의 내 시간표 등록
    @Transactional
    public ResponseEntity saveMyBusTime(Long busTimeNo, HttpServletRequest tokenInfo) {

        BusTime busTime = busTimeRepository.findById(busTimeNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BUSTIME));

        log.info("busTime = {}", busTime);

        String userId = userService.getUserIdFromToken(tokenInfo);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));

        if (userService.hasAccessAuth(userId, tokenInfo)) {

            // 이미 가지고 있는 시간표일 경우에는 예외 처리
            if (myTimeRepository.findByUserUserNoAndBusTimeBusTimeNo(user.getUserNo(), busTimeNo).isPresent()) {
                throw new ApiException(ErrorCode.DUPLICATED_BUSTIME);
            }

            // MyTime 객체 생성
            MyTime myTime = MyTime.builder()
                    .user(user)
                    .busTime(busTime)
                    .build();

            myTimeRepository.save(myTime);

            return ResponseEntity.status(HttpStatus.OK).body("내 시간표 저장 성공");
        } else {
            throw new ApiException(ErrorCode.USER_UNAUTHORIZED);
        }


    }

    // 관리자만 해당 시간표 삭제
    @Transactional
    public ResponseEntity deleteBusTime(Long busTimeNo, HttpServletRequest tokenInfo) {

        BusTime busTime = busTimeRepository.findById(busTimeNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BUSTIME));


        String userId = userService.getUserIdFromToken(tokenInfo);

       // 만약 userId가 admin일 경우에는 삭제하도록 한다.
        if (userId.equals("admin")) {
            // 먼저 내 시간표에 있는 해당 시간표를 다 삭제한다.
            myTimeRepository.deleteAllByBusTime_BusTimeNo(busTimeNo);

            // 그 다음에 해당 시간표를 삭제한다.
            busTimeRepository.deleteById(busTimeNo);

            return ResponseEntity.status(HttpStatus.OK).body("삭제 성공");
        } else {
            throw new ApiException(ErrorCode.USER_UNAUTHORIZED);
        }
    }

    @Transactional
    public ResponseEntity getMyBusTimeList(Integer page, Integer size, HttpServletRequest tokenInfo) {

        String userId = userService.getUserIdFromToken(tokenInfo);

        // 해당 User 존재 여부 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));

        if (userService.hasAccessAuth(user.getUserId(), tokenInfo)) {
            if (page > 0) {
                page = page - 1;
            }

            // 페이징 생성
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "myTimeNo"));

            // 페이징, 유저 번호를 통해 페이지 형태로 정보 가져오기
            Page<MyTime> myTimePage = myTimeRepository.findAllByUserUserNo(user.getUserNo(), pageable);

            // vo로 변환
            Page<MyTimeVo> myTimeVos = myTimePage.map(myTime -> MyTimeVo.builder()
                    .startTarget(myTime.getBusTime().getStartTarget())
                    .arrivedTarget(myTime.getBusTime().getArrivedTarget())
                    .startTime(myTime.getBusTime().getStartTime())
                    .busCorp(myTime.getBusTime().getBusCorp())
                    .layover(myTime.getBusTime().getLayover())
                    .note(myTime.getBusTime().getNote())
                    .build());

            return ResponseEntity.status(HttpStatus.OK).body(myTimeVos);

        } else {
            throw new ApiException(ErrorCode.USER_UNAUTHORIZED);
        }


    }


    @Transactional
    public ResponseEntity deleteMyTime(Long myTimeNo, HttpServletRequest tokenInfo) {

        String userId = userService.getUserIdFromToken(tokenInfo);

        // 해당 User 존재 여부 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_USER));

        // 해당 myTime 존재 여부 확인
        MyTime myTime = myTimeRepository.findById(myTimeNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BUSTIME));

        // 로그인한 유저의 내 시간표에 있는 시간표가 맞는지 확인하기
        if (myTime.getUser().getUserId().equals(userId)) {

            myTimeRepository.deleteById(myTimeNo);

            return ResponseEntity.status(HttpStatus.OK).body("mytime delete success");
        } else {
            throw new ApiException(ErrorCode.USER_UNAUTHORIZED);
        }
    }
}
