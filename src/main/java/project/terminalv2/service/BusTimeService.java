package project.terminalv2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.terminalv2.domain.BusTime;
import project.terminalv2.domain.MyTime;
import project.terminalv2.domain.User;
import project.terminalv2.domain.type.RoleType;
import project.terminalv2.dto.bustime.NewBusTimeSaveRequest;
import project.terminalv2.exception.ApiException;
import project.terminalv2.exception.ApiResponse;
import project.terminalv2.exception.ErrorCode;
import project.terminalv2.respository.BusTimeRepository;
import project.terminalv2.respository.MyTimeRepository;
import project.terminalv2.vo.bustime.BusTimeInfoVo;
import project.terminalv2.vo.mytime.MyTimeVo;

import javax.servlet.http.HttpServletRequest;

import static project.terminalv2.domain.BusTime.makeBusTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusTimeService {

    private final BusTimeRepository busTimeRepository;
    private final MyTimeRepository myTimeRepository;
    private final UserService userService;
    private final ApiResponse apiResponse;

    // 관리자가 새로운 시간표 생성
    @Transactional
    public ApiResponse regNewBusTime(NewBusTimeSaveRequest request) {

        BusTime busTime = makeBusTime(request);
        busTimeRepository.save(busTime);
        BusTimeInfoVo busTimeInfoVo = busTime.toBusTimeInfoVo(busTime);

        return apiResponse.makeResponse(HttpStatus.OK, "4000", "새로운 시간표 등록 성공", busTimeInfoVo);
    }

    // 회원의 내 시간표 등록
    @Transactional
    public ApiResponse saveMyBusTime(Long busTimeNo, HttpServletRequest tokenInfo) {

        BusTime busTime = busTimeRepository.findById(busTimeNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BUSTIME));

        String userId = userService.getUserIdFromToken(tokenInfo);
        User user = userService.getUser(userId);

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

            MyTimeVo myTimeVo = myTime.toMyTimeVo(myTime);

            return apiResponse.makeResponse(HttpStatus.OK, "4000", "내 시간표로 등록 성공", myTimeVo);
        } else {
            throw new ApiException(ErrorCode.USER_UNAUTHORIZED);
        }
    }

    // 관리자만 해당 시간표 삭제
    @Transactional
    public ApiResponse deleteBusTime(Long busTimeNo, HttpServletRequest tokenInfo) {

        BusTime busTime = busTimeRepository.findById(busTimeNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BUSTIME));

        String userId = userService.getUserIdFromToken(tokenInfo);
        User user = userService.getUser(userId);

       // 만약 user의 권한이 ADMIN일 경우 삭제가 가능하도록 한다.
        if (user.getRole().equals(RoleType.ADMIN)) {

            // 먼저 내 시간표에 있는 해당 시간표를 다 삭제한다.
            myTimeRepository.deleteAllByBusTime_BusTimeNo(busTimeNo);

            // 그 다음에 해당 시간표를 삭제한다.
            busTimeRepository.deleteById(busTimeNo);

            return apiResponse.makeResponse(HttpStatus.OK, "4000", "버스 시간표 삭제 성공", null);
        } else {
            throw new ApiException(ErrorCode.USER_UNAUTHORIZED);
        }
    }

    @Transactional
    public ApiResponse getMyBusTimeList(Integer page, Integer size, HttpServletRequest tokenInfo) {

        String userId = userService.getUserIdFromToken(tokenInfo);

        // 해당 User 존재 여부 확인
        User user = userService.getUser(userId);

        if (userService.hasAccessAuth(user.getUserId(), tokenInfo)) {

            // 페이징 생성
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "myTimeNo"));

            // 페이징, 유저 번호를 통해 페이지 형태로 정보 가져오기
            Page<MyTime> myTimePage = myTimeRepository.findAllByUserUserNo(user.getUserNo(), pageable);

            // vo로 변환
            Page<MyTimeVo> myTimeVos = myTimePage.map(myTime -> myTime.toMyTimeVo(myTime));

            return apiResponse.makeResponse(HttpStatus.OK, "4000", "내 시간표 리스트 조회 성공", myTimeVos);
        } else {
            throw new ApiException(ErrorCode.USER_UNAUTHORIZED);
        }
    }


    @Transactional
    public ApiResponse deleteMyTime(Long myTimeNo, HttpServletRequest tokenInfo) {

        String userId = userService.getUserIdFromToken(tokenInfo);

        // 해당 User 존재 여부 확인
        User user = userService.getUser(userId);

        // 해당 myTime 존재 여부 확인
        MyTime myTime = myTimeRepository.findById(myTimeNo)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_BUSTIME));

        // 로그인한 유저의 내 시간표에 있는 시간표가 맞는지 확인하기
        if (myTime.getUser().getUserId().equals(userId)) {

            myTimeRepository.deleteById(myTimeNo);

            return apiResponse.makeResponse(HttpStatus.OK, "4000", "내 시간표 삭제 성공", null);
        } else {
            throw new ApiException(ErrorCode.USER_UNAUTHORIZED);
        }
    }
}
