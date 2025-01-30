package com.sixthband.festival.serviceTeam.controller;

import com.sixthband.festival.dto.ImageDto;
import com.sixthband.festival.dto.RestResponseDto;
import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.serviceTeam.dto.*;
import com.sixthband.festival.serviceTeam.service.ServiceTeamService;
import com.sixthband.festival.ticket.dto.TicketReviewImageDto;
import com.sixthband.festival.util.ImageUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/service")
public class RestServiceTeamController {

    @Autowired
    private ServiceTeamService serviceTeamService;

    @RequestMapping("getUserInfo")
    public RestResponseDto getUserInfo(ServiceTeamDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("searchUserInfo", serviceTeamService.getServiceTeam(params));

//        /api/service/getUserInfo

        return restResponseDto;
    }

    @RequestMapping("getServiceTeamList")
    public RestResponseDto getServiceTeamList(@RequestParam("today") String today) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("serviceTeamList", serviceTeamService.getAllServiceTeamByToday(today));

//        /api/service/getServiceTeamList

        return restResponseDto;
    }

//    팀원 등록
    @RequestMapping("createServiceTeamMember")
    public RestResponseDto createServiceTeamMember(ServiceTeamDto params, @RequestParam(name = "inputImage")  MultipartFile inputImage) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

//        사진 처리 (없으면 기본 프로필 들어가게)
        if (inputImage.isEmpty()) {
            String filename = "/profile.png";
            params.setProfile(filename);
            serviceTeamService.createServiceTeamMember(params);
        } else {
            params.setProfile(ImageUtil.saveImageAndReturnLocation(inputImage));
            serviceTeamService.createServiceTeamMember(params);
        }
    //        /api/service/createServiceTeamMember

        return restResponseDto;
    }

//    팀원 한 명
    @RequestMapping("getServiceTeamById")
    public RestResponseDto getServiceTeamById(@RequestParam("service_id") int service_id, @RequestParam("today") String today) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("memberInfo", serviceTeamService.getServiceTeamById(service_id, today));

//        /api/service/getServiceTeamById

        return restResponseDto;
    }


//    로그인 확인
    @RequestMapping("getSessionServiceId")
    public RestResponseDto getSessionServiceId(HttpSession session) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        ServiceTeamDto loginMember = (ServiceTeamDto) session.getAttribute("loginServiceTeam");

        if (loginMember != null) {
            restResponseDto.add("id", loginMember.getId());
            restResponseDto.add("email", loginMember.getEmail());
        } else {
            restResponseDto.add("id", null);
            restResponseDto.add("email", null);
        }

//        /api/service/getSessionServiceId
        return restResponseDto;
    }

//    출근
    @RequestMapping("createServiceTeamAttendance")
    public RestResponseDto createServiceTeamAttendance(ServiceAttendanceManagementDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        serviceTeamService.createdServiceTeamAttendance(params);

//        /api/service/createServiceTeamAttendance

        return restResponseDto;
    }


//    팀원의 출퇴근 기록 조회
    @RequestMapping("getTodayAttendance")
    public RestResponseDto getTodayAttendance(@RequestParam("service_id") int service_id, @RequestParam("today") String today) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("todayAttendance", serviceTeamService.getIsTodayAttendance(service_id, today));
        restResponseDto.add("todayBreak", serviceTeamService.getIsTodayBreakTime(service_id, today));
        restResponseDto.add("breakInfo", serviceTeamService.getTodayBreakTime(service_id, today));
        restResponseDto.add("todayGoHome", serviceTeamService.getIsTodayGoHome(service_id, today));
//        /api/service/getTodayAttendance

        return restResponseDto;
    }

//    휴게시간 등록
    @RequestMapping("createServiceTeamBreakTime")
    public RestResponseDto createServiceTeamBreakTime(@RequestParam("service_id") int service_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        serviceTeamService.createServiceTeamBreakTime(service_id);

//        /api/service/createServiceTeamBreakTime

        return restResponseDto;
    }

//    휴게시간 종료
    @RequestMapping("updateBreakTimeDone")
    public RestResponseDto updateBreakTimeDone(@RequestParam("service_id") int service_id, @RequestParam("today") String today) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        serviceTeamService.updateIsDoneY(service_id, today);

//        /api/service/updateBreakTimeDone

        return restResponseDto;
    }

//    캘린더용
    @RequestMapping("getMyAttendance")
    public RestResponseDto getMyAttendance(@RequestParam("service_id") int service_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("MyAttendance", serviceTeamService.getAllAttendanceByServiceId(service_id));
        restResponseDto.add("MySchedule", serviceTeamService.getAllScheduleByServiceId(service_id));

//        /api/service/getMyAttendance

        return restResponseDto;
    }

//    마이페이지
    @RequestMapping("getMyInformation")
    public RestResponseDto getMyInformation(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("MyAttendance", serviceTeamService.getMyInformationById(id));
//        /api/service/getMyInformation

        return restResponseDto;
    }

//    프로필 수정
    @RequestMapping("updateMyProfile")
    public RestResponseDto updateMyProfile(MultipartFile inputImage, ServiceTeamDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        params.setProfile(ImageUtil.saveImageAndReturnLocation(inputImage));
        serviceTeamService.updateProfileById(params);
//        /api/service/updateMyProfile

        return restResponseDto;
    }

//    비밀번호 수정
    @RequestMapping("updateMyPassword")
    public RestResponseDto updateMyPassword(ServiceTeamDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        serviceTeamService.updatePasswordById(params);
//        /api/service/updateMyPassword

        return restResponseDto;
    }

//    휴대폰번호 수정
    @RequestMapping("updateMyPhone")
    public RestResponseDto updateMyPhone(ServiceTeamDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        serviceTeamService.updatePhoneById(params);
//        /api/service/updateMyPhone

        return restResponseDto;
    }

//    일정 등록
    @RequestMapping("createMemberWork")
    public RestResponseDto createMemberWork(ServiceTeamScheduleDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        serviceTeamService.insertServiceTeamMemberWork(params);
//        /api/service/createMemberWork

        return restResponseDto;
    }

//    스케줄인 사람 목록
    @RequestMapping("getScheduleMemberList")
    public RestResponseDto getScheduleMemberList(@RequestParam("today") String today) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("todayScheduleMember", serviceTeamService.getScheduleMember(today));
//        /api/service/getScheduleMemberList

        return restResponseDto;
    }

//    공지사항 등록
    @RequestMapping("createServiceNotice")
    public RestResponseDto createServiceNotice(ServiceTeamNoticeDto params, HttpSession session, @RequestParam(value="inputImages", required = false) MultipartFile[] inputImages) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        params.setService_id(((ServiceTeamDto) session.getAttribute("loginServiceTeam")).getId());
        serviceTeamService.createServiceNotice(params);

        ServiceTeamNoticeImageDto serviceTeamNoticeImageDto = new ServiceTeamNoticeImageDto();

        if(inputImages != null && inputImages.length > 0) {
            List<ImageDto> multiImage = ImageUtil.saveImageAndReturnDtoList(inputImages);
            for (ImageDto imageDto : multiImage) {
                serviceTeamNoticeImageDto.setNotice_id(params.getId());
                serviceTeamNoticeImageDto.setUrl(imageDto.getLocation());
                serviceTeamService.createServiceNoticeImages(serviceTeamNoticeImageDto);
            }
        }

//        /api/service/createServiceNotice

        return restResponseDto;
    }

//    공지사항 목록
    @RequestMapping("getAllServiceTeamNotice")
    public RestResponseDto getAllServiceTeamNotice(@RequestParam(value = "page", defaultValue = "1") int page) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("serviceTeamNotice", serviceTeamService.getAllServiceTeamNotice((page - 1) * 15));
        restResponseDto.add("noticeCount", serviceTeamService.getNoticeCount());
//        /api/service/getAllServiceTeamNotice

        return restResponseDto;
    }

//    공지사항 목록 - 메인
    @RequestMapping("getFiveServiceTeamNotice")
    public RestResponseDto getFiveServiceTeamNotice() {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("serviceTeamNotice", serviceTeamService.getFiveServiceTeamNotice());
//        /api/service/getFiveServiceTeamNotice

        return restResponseDto;
    }

//    공지사항 상세
    @RequestMapping("getServiceTeamNoticeById")
    public RestResponseDto getServiceTeamNoticeById(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("serviceTeamNotice", serviceTeamService.getServiceTeamNoticeById(id));
//        /api/service/getServiceTeamNoticeById

        return restResponseDto;
    }

//    공지사항 삭제
    @RequestMapping("deleteNotice")
    public RestResponseDto deleteNotice(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        serviceTeamService.deleteServiceTeamNoticeById(id);
//        /api/service/deleteNotice

        return restResponseDto;
    }

//    공지사항 수정
    @RequestMapping("updateNotice")
    public RestResponseDto updateNotice(ServiceTeamNoticeDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        serviceTeamService.updateServiceTeamNoticeById(params);
//        /api/service/updateNotice

        return restResponseDto;
    }

//    qna 게시글 등록
    @RequestMapping("createQna")
    public RestResponseDto createQna(ServiceQnaBoardDto params, HttpSession session, @RequestParam(value="inputImages", required = false) MultipartFile[] inputImages) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto userDto = (UserDto) session.getAttribute("loginUser");
        params.setUser_id(userDto.getId());

        serviceTeamService.createQnaBoard(params);

        serviceQnaImageDto serviceQnaImageDto = new serviceQnaImageDto();
        if(inputImages != null && inputImages.length > 0) {
            List<ImageDto> multiImage = ImageUtil.saveImageAndReturnDtoList(inputImages);
            for (ImageDto imageDto : multiImage) {
                serviceQnaImageDto.setBoard_id(params.getId());
                serviceQnaImageDto.setUrl(imageDto.getLocation());
                serviceTeamService.createQnaImages(serviceQnaImageDto);
            }
        }


//        /api/service/createQna

        return restResponseDto;
    }

//    서비스팀 아이디
    @RequestMapping("getServiceId")
    public RestResponseDto getServiceId(@RequestParam("today") String today) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("serviceId", serviceTeamService.getQnABoardServiceId(today));
//        /api/service/getServiceId

        return restResponseDto;
    }

//    qna 리스트
    @RequestMapping("getAllQnAList")
    public RestResponseDto getAllQnAList(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("qnaList", serviceTeamService.getAllQnAList(id))    ;
//        /api/service/getAllQnAList

        return restResponseDto;
    }

//    qna 상세
    @RequestMapping("getQnADetail")
    public RestResponseDto getQnADetail(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("qna", serviceTeamService.getQnAListById(id));
        restResponseDto.add("qnaAnswer", serviceTeamService.getQnaAnswerForUserPage(id));
//        /api/service/getQnADetail

        return restResponseDto;
    }

//    qna - 서비스직원
    @RequestMapping("getQnAServiceId")
    public RestResponseDto getQnAServiceId(@RequestParam("service_id") int service_id, @RequestParam(value = "page", defaultValue = "1") int page) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("qna", serviceTeamService.getQnAListByServiceId(service_id, (page - 1) * 15));
        restResponseDto.add("qnaCount", serviceTeamService.getMineQnaCount(service_id));
//        /api/service/getQnAServiceId

        return restResponseDto;
    }

//    qna 상태 - 접수 완료
    @RequestMapping("updateQnaStatusComplete")
    public RestResponseDto updateQnaStatusComplete(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        serviceTeamService.updateQnAStatusCompleted(id);
//        /api/service/updateQnaStatusComplete

        return restResponseDto;
    }

//    qna - 서비스직원 상세
    @RequestMapping("getServiceQnADetail")
    public RestResponseDto getServiceQnADetail(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("serviceQnaDetail", serviceTeamService.getServiceTeamQnAById(id));
//        /api/service/getServiceQnADetail

        return restResponseDto;
    }

//    qna 답변 등록
    @RequestMapping("createQnaAnswer")
    public RestResponseDto createQnaAnswer(ServiceQnaAnswerDto params, @RequestParam(value="inputImages", required = false) MultipartFile[] inputImages) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        serviceTeamService.createQnAAnswer(params);

        ServiceQnaAnswerImageDto serviceQnaAnswerImageDto = new ServiceQnaAnswerImageDto();
        if(inputImages != null && inputImages.length > 0) {
            List<ImageDto> multiImage = ImageUtil.saveImageAndReturnDtoList(inputImages);
            for (ImageDto imageDto : multiImage) {
                serviceQnaAnswerImageDto.setAnswer_id(params.getId());
                serviceQnaAnswerImageDto.setUrl(imageDto.getLocation());
                serviceTeamService.createQnaAnswerImages(serviceQnaAnswerImageDto);
            }
        }

//        /api/service/createQnaAnswer

        return restResponseDto;
    }

//    qna 상태 - 답변 완료
    @RequestMapping("updateQnAStatusDone")
    public RestResponseDto updateQnAStatusDone(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        serviceTeamService.updateQnAStatusDone(id);
//        /api/service/updateQnAStatusDone

        return restResponseDto;
    }

//    qna 진행 상태
    @RequestMapping("getServiceQnAStatus")
    public RestResponseDto getServiceQnAStatus(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("qnaAccepting", serviceTeamService.getQnaStatusOneCount(id));
        restResponseDto.add("qnaApplicationCompleted", serviceTeamService.getQnaStatusTwoCount(id));
        restResponseDto.add("qnaAnswerCompleted", serviceTeamService.getQnaStatusThreeCount(id));
//        /api/service/getServiceQnAStatus

        return restResponseDto;
    }

//    qna 그래프
    @RequestMapping("getServiceQnACanvas")
    public RestResponseDto getServiceQnACanvas(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("qnaCanvas", serviceTeamService.getQnaStatusCanvasByServiceId(id));
//        /api/service/getServiceQnACanvas

        return restResponseDto;
    }

//    qna 그래프
    @RequestMapping("getServiceQnAAnswer")
    public RestResponseDto getServiceQnAAnswer(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("answer", serviceTeamService.getQnaAnswerByBoardId(id));
//        /api/service/getServiceQnAAnswer

        return restResponseDto;
    }

//    qna 게시글 등록
    @RequestMapping("createChatRoom")
    public RestResponseDto createChatRoom(ServiceLiveChatRoomDto params, HttpSession session) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto userDto = (UserDto) session.getAttribute("loginUser");
        params.setUser_id(userDto.getId());

        serviceTeamService.createLiveChatRoom(params);

//        /api/service/createChatRoom

        return restResponseDto;
    }

//    서비스팀 아이디 - 채팅
    @RequestMapping("getChatServiceId")
    public RestResponseDto getChatServiceId(@RequestParam("today") String today) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("serviceId", serviceTeamService.getLiveChatServiceId(today));
//        /api/service/getChatServiceId

        return restResponseDto;
    }

//    채팅방 아이디 알아내기
    @RequestMapping("getChatRoomId")
    public RestResponseDto getChatRoomId(ServiceLiveChatRoomDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

//        System.out.println("유저아이디 : " + params.getUser_id());
//        System.out.println("직원아이디 : " + params.getService_id());
//        System.out.println("vvv : " + params);

        restResponseDto.add("roomId", serviceTeamService.getMyChatRoomId(params));
//        System.out.println("roomId : " + serviceTeamService.getMyChatRoomId(params));
//        /api/service/getChatRoomId

        return restResponseDto;
    }

//    채팅방 채팅 입력 - 유저
    @RequestMapping("createChatMessage")
    public RestResponseDto createChatMessage(ServiceLiveChatDto params, HttpSession session) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto userDto = (UserDto) session.getAttribute("loginUser");
        params.setFrom_email(userDto.getEmail());

        serviceTeamService.insertLiveChatMessage(params);

//        /api/service/createChatMessage

        return restResponseDto;
    }

//    채팅방 채팅 입력 - 서비스팀
    @RequestMapping("createChatMessageService")
    public RestResponseDto createChatMessageService(ServiceLiveChatDto params, HttpSession session) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        ServiceTeamDto loginServiceTeam = (ServiceTeamDto) session.getAttribute("loginServiceTeam");
        params.setFrom_email(loginServiceTeam.getEmail());

        serviceTeamService.insertLiveChatMessage(params);

//        /api/service/createChatMessageService

        return restResponseDto;
    }

//    채팅방 목록 리스트
    @RequestMapping("getChatRoomList")
    public RestResponseDto getChatRoomList(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("chatList", serviceTeamService.getMyChatRoomList(id));
//        /api/service/getChatRoomList

        return restResponseDto;
    }

//    채팅방 - 하나
    @RequestMapping("getChatRoomById")
    public RestResponseDto getChatRoomById(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("chat", serviceTeamService.getMyChatRoomInfoById(id));
//        /api/service/getChatRoomById

        return restResponseDto;
    }

//    채팅방 메세지
    @RequestMapping("getChatRoomMessage")
    public RestResponseDto getChatRoomMessage(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("message", serviceTeamService.getLiveChatMessageByRoomId(id));
//        /api/service/getChatRoomMessage

        return restResponseDto;
    }

//    채팅방 메세지 읽음처리
    @RequestMapping("updateMessageIsReading")
    public RestResponseDto updateMessageIsReading(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        serviceTeamService.updateIsReadingYByRoomId(id);

//        /api/service/updateMessageIsReading

        return restResponseDto;
    }

//    채팅 종료하기
    @RequestMapping("updateRoomIsActive")
    public RestResponseDto updateRoomIsActive(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        serviceTeamService.updateChatRoomIsActiveById(id);

//        /api/service/updateRoomIsActive

        return restResponseDto;
    }

//    채팅 진행 상태
    @RequestMapping("getServiceLiveChatStatus")
    public RestResponseDto getServiceLiveChatStatus(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("active", serviceTeamService.getLiveChatActiveCount(id));
        restResponseDto.add("notActive", serviceTeamService.getLiveChatNotActiveCount(id));
//        /api/service/getServiceLiveChatStatus

        return restResponseDto;
    }

//    채팅 그래프
    @RequestMapping("getServiceLiveChatCanvas")
    public RestResponseDto getServiceLiveChatCanvas(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("liveChat", serviceTeamService.getLiveChatCanvasByServiceId(id));
//        /api/service/getServiceLiveChatCanvas

        return restResponseDto;
    }

}
