package com.sixthband.festival.serviceTeam.service;

import com.sixthband.festival.serviceTeam.dto.*;
import com.sixthband.festival.serviceTeam.mapper.ServiceTeamSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServiceTeamService {

    @Autowired
    private ServiceTeamSqlMapper serviceTeamSqlMapper;

//    로그인
    public ServiceTeamDto getServiceTeam(ServiceTeamDto serviceTeamDto) {
        return serviceTeamSqlMapper.selectServiceTeamByEmailAndPassword(serviceTeamDto);
    }

//    인원수
    public int getServiceTeamCount() {
        return serviceTeamSqlMapper.selectServiceTeamCount();
    }

//    서비스팀 목록
    public List<Map<String, Object>> getAllServiceTeam() {
        return serviceTeamSqlMapper.selectAllServiceTeam();
    }

//    서비스팀 등록
    public void createServiceTeamMember(ServiceTeamDto serviceTeamDto) {
        System.out.println("아아ㅏㅏ테스트 : " + serviceTeamDto);
        serviceTeamSqlMapper.insertServiceTeamMember(serviceTeamDto);
    }

//    서비스팀 한 명
    public Map<String, Object> getServiceTeamById(int service_id, String today) {
        return serviceTeamSqlMapper.selectServiceTeamById(service_id, today);
    }

//    출근
    public void createdServiceTeamAttendance(ServiceAttendanceManagementDto serviceAttendanceManagementDto) {
        serviceTeamSqlMapper.insertServiceTeamAttendance(serviceAttendanceManagementDto);
    }

//    출퇴근 기록 조회 ( 0이면 출근 버튼 보이게 할거임 )
    public boolean getIsTodayAttendance(int service_id, String today) {
        return serviceTeamSqlMapper.selectIsTodayAttendance(service_id,today) == 0;
    }

//    휴게
    public void createServiceTeamBreakTime(int service_id) {
        serviceTeamSqlMapper.insertServiceTeamBreakTime(service_id);
    }

//    휴게시간 조회
    public boolean getIsTodayBreakTime(int service_id, String today) {
        return serviceTeamSqlMapper.selectIsTodayBreakTime(service_id, today) == 0;
    }

    public ServiceBreakManagementDto getTodayBreakTime(int service_id, String today) {
        return serviceTeamSqlMapper.selectTodayBreakTime(service_id, today);
    }

//    휴게시간 종료
    public void updateIsDoneY(int service_id, String today) {
        serviceTeamSqlMapper.updateIsDoneY(service_id, today);
    }

//    퇴근 기록 조회
    public boolean getIsTodayGoHome(int service_id, String today) {
        return serviceTeamSqlMapper.selectIsTodayGoHome(service_id,today) == 0;
    }

    public List<Map<String, Object>> getAllServiceTeamByToday(String today) {
        return serviceTeamSqlMapper.selectAllServiceTeamByToday(today);
    }

//    캘린더
    public List<Map<String, Object>> getAllAttendanceByServiceId(int service_id) {
        return serviceTeamSqlMapper.selectAllAttendanceByServiceId(service_id);
    }

//    캘린더 스케줄
    public List<ServiceTeamScheduleDto> getAllScheduleByServiceId(int service_id) {
        return serviceTeamSqlMapper.selectAllScheduleByServiceId(service_id);
    }

//    내 정보
    public ServiceTeamDto getMyInformationById(int id) {
        return serviceTeamSqlMapper.selectMyInformationById(id);
    }

//    프로필 수정
    public void updateProfileById(ServiceTeamDto serviceTeamDto) {
        serviceTeamSqlMapper.updateProfileById(serviceTeamDto);
    }

//    프로필 수정
    public void updatePasswordById(ServiceTeamDto serviceTeamDto) {
        serviceTeamSqlMapper.updatePasswordById(serviceTeamDto);
    }

//    폰번호 수정
    public void updatePhoneById(ServiceTeamDto serviceTeamDto) {
        serviceTeamSqlMapper.updatePhoneById(serviceTeamDto);
    }

//    일정 등록
    public void insertServiceTeamMemberWork(ServiceTeamScheduleDto serviceTeamScheduleDto) {
        serviceTeamSqlMapper.insertServiceTeamMemberWork(serviceTeamScheduleDto);
    }

//    스케줄인 사람
    public List<Map<String, Object>> getScheduleMember(String today) {
        return serviceTeamSqlMapper.selectAllScheduleMember(today);
    }

//    공지사항 등록
    public void createServiceNotice(ServiceTeamNoticeDto serviceTeamNoticeDto) {
        serviceTeamSqlMapper.insertServiceNotice(serviceTeamNoticeDto);
    }

//    공지사항 이미지 등록
    public void createServiceNoticeImages(ServiceTeamNoticeImageDto serviceTeamNoticeImageDto) {
        serviceTeamSqlMapper.insertServiceNoticeImages(serviceTeamNoticeImageDto);
    }

//    공지사항 목록
    public List<Map<String, Object>> getAllServiceTeamNotice(int page) {
        return serviceTeamSqlMapper.selectAllServiceTeamNotice(page);
    }

//    공지사항 개수
    public int getNoticeCount() {
        return serviceTeamSqlMapper.selectNoticeCount();
    }

//    공지사항 목록 -메인
    public List<Map<String, Object>> getFiveServiceTeamNotice() {
        return serviceTeamSqlMapper.selectFiveServiceTeamNotice();
    }

//    공지사항 상세
    public Map<String, Object> getServiceTeamNoticeById(int id) {
        return serviceTeamSqlMapper.selectServiceTeamNoticeById(id);
    }

//    조회수 업데이트
    public void updateServiceTeamNoticeReadCount(int id) {
        serviceTeamSqlMapper.updateServiceTeamNoticeReadCount(id);
    }

//    이미지 가져오기
    public List<ServiceTeamNoticeImageDto> getServiceTeamNoticeImages(int id) {
        return serviceTeamSqlMapper.selectServiceTeamNoticeImages(id);
    }

//    공지 삭제
    public void deleteServiceTeamNoticeById(int id) {
        serviceTeamSqlMapper.deleteServiceTeamNoticeById(id);
    }

//    공지 수정
    public void updateServiceTeamNoticeById(ServiceTeamNoticeDto serviceTeamNoticeDto) {
        serviceTeamSqlMapper.updateServiceTeamNoticeById(serviceTeamNoticeDto);
    }

//    qna 카테고리
    public List<ServiceQnaCategoryDto> getAllQnaCategoryList() {
        return serviceTeamSqlMapper.selectAllQnaCategoryList();
    }

//    qna 게시글 등록
    public void createQnaBoard(ServiceQnaBoardDto serviceQnaBoardDto) {
        serviceTeamSqlMapper.insertQnaBoard(serviceQnaBoardDto);
    }

//    공지사항 이미지 등록
    public void createQnaImages(serviceQnaImageDto serviceQnaImageDto) {
        serviceTeamSqlMapper.insertQnaImages(serviceQnaImageDto);
    }

//    제일 일 없는 서비스팀원 아이디
    public int getQnABoardServiceId(String today) {
        return serviceTeamSqlMapper.selectQnABoardServiceId(today);
    }

//    qna 리스트
    public List<Map<String, Object>> getAllQnAList(int id) {
        return serviceTeamSqlMapper.selectAllQnAList(id);
    }

//    qna 상세
    public Map<String, Object> getQnAListById(int id) {
        return serviceTeamSqlMapper.selectQnAListById(id);
    }

//    qna - 직원 리스트
    public List<Map<String, Object>> getQnAListByServiceId(int service_id, int page) {
        return serviceTeamSqlMapper.selectQnAListByServiceId(service_id, page);
    }

//    qna - 직원 상세
    public Map<String, Object> getServiceTeamQnAById(int id) {
        return serviceTeamSqlMapper.selectServiceTeamQnAById(id);
    }

//    qna - 직원 상세 이미지
    public List<serviceQnaImageDto> getServiceTeamQnAImagesByBoardId(int id) {
        return serviceTeamSqlMapper.selectServiceTeamQnAImagesByBoardId(id);
    }

//    qna 접수 완료
    public void updateQnAStatusCompleted(int id) {
        serviceTeamSqlMapper.updateQnAStatusCompleted(id);
    }

//    qna 답변 등록
    public void createQnAAnswer(ServiceQnaAnswerDto serviceQnaAnswerDto) {
        serviceTeamSqlMapper.insertQnAAnswer(serviceQnaAnswerDto);
    }

//    qna 답변 이미지
    public void createQnaAnswerImages(ServiceQnaAnswerImageDto serviceQnaAnswerImageDto) {
        serviceTeamSqlMapper.insertQnaAnswerImages(serviceQnaAnswerImageDto);
    }

//    qna 답변 완료
    public void updateQnAStatusDone(int id) {
        serviceTeamSqlMapper.updateQnAStatusDone(id);
    }

//    qna 답변 있는지 확인
    public int getIsQnaCount(int id) {
        return serviceTeamSqlMapper.selectIsQnaCount(id);
    }

//    qna 답변
    public ServiceQnaAnswerDto getQnaAnswerByBoardId(int id) {
        return serviceTeamSqlMapper.selectQnaAnswerByBoardId(id);
    }

//    qna 답변 이미지
    public List<ServiceQnaAnswerImageDto> getQnAAnswerImagesByAnswerId(int id) {
        return serviceTeamSqlMapper.selectQnAAnswerImagesByAnswerId(id);
    }

//    qna 답변 유저화면
    public Map<String, Object> getQnaAnswerForUserPage(int id) {
        return serviceTeamSqlMapper.selectQnaAnswerForUserPage(id);
    }

//    qna 답변 이미지 유저화면
    public List<Map<String, Object>> getQnaAnswerImagesForUserPage(int id) {
        return serviceTeamSqlMapper.selectQnaAnswerImagesForUserPage(id);
    }

//    qna 처리상태
    public int getQnaStatusOneCount(int id) {
        return serviceTeamSqlMapper.selectQnaStatusOneCount(id);
    }

    public int getQnaStatusTwoCount(int id) {
        return serviceTeamSqlMapper.selectQnaStatusTwoCount(id);
    }

    public int getQnaStatusThreeCount(int id) {
        return serviceTeamSqlMapper.selectQnaStatusThreeCount(id);
    }

//    qna 그래프
    public List<Map<String, Object>> getQnaStatusCanvasByServiceId(int id) {
        return serviceTeamSqlMapper.selectQnaStatusCanvasByServiceId(id);
    }
//    qna 개수
    public int getMineQnaCount(int id) {
        return serviceTeamSqlMapper.selectMineQnaCount(id);
    }

//    채팅 - 일 없는 사람
    public int getLiveChatServiceId(String today) {
        return serviceTeamSqlMapper.selectLiveChatServiceId(today);
    }

//    채팅 - 방 생성
    public void createLiveChatRoom(ServiceLiveChatRoomDto serviceLiveChatRoomDto) {
        serviceTeamSqlMapper.insertLiveChatRoom(serviceLiveChatRoomDto);
    }

//    채팅 - 방 입장
    public int getMyChatRoomId(ServiceLiveChatRoomDto serviceLiveChatRoomDto) {
//        System.out.println("서비스 : " + serviceLiveChatRoomDto);
//        System.out.println("서비스 리턴 : " + serviceTeamSqlMapper.selectMyChatRoomId(serviceLiveChatRoomDto));
        return serviceTeamSqlMapper.selectMyChatRoomId(serviceLiveChatRoomDto);
    }

//    채팅 - 방 정보
    public Map<String, Object> getMyChatRoomInfoById(int id) {
        return serviceTeamSqlMapper.selectMyChatRoomInfoById(id);
    }

//    채팅 - 메세지 입력
    public void insertLiveChatMessage(ServiceLiveChatDto serviceLiveChatDto) {
        serviceTeamSqlMapper.insertLiveChatMessage(serviceLiveChatDto);
    }

//    채팅 - 방 목록 직원
    public List<Map<String, Object>> getMyChatRoomList(int id) {
        return serviceTeamSqlMapper.selectMyChatRoomList(id);
    }

//    채팅 - 메세지 내역
    public List<ServiceLiveChatDto> getLiveChatMessageByRoomId(int id) {
        return serviceTeamSqlMapper.selectLiveChatMessageByRoomId(id);
    }

//    채팅 - 메세지 읽음처리
    public void updateIsReadingYByRoomId(int id) {
        serviceTeamSqlMapper.updateIsReadingYByRoomId(id);
    }

//    채팅 - 채팅 종료하기
    public void updateChatRoomIsActiveById(int id) {
        serviceTeamSqlMapper.updateChatRoomIsActiveById(id);
    }

//    채팅 - 방 목록 유저
    public List<Map<String, Object>> getUserChatList(int id) {
        return serviceTeamSqlMapper.selectUserChatList(id);
    }

//    채팅 - 진행중인 채팅 개수
    public int getLiveChatActiveCount(int id) {
        return serviceTeamSqlMapper.selectLiveChatActiveCount(id);
    }

//    채팅 - 진행 종료된 채팅 개수
    public int getLiveChatNotActiveCount(int id) {
        return serviceTeamSqlMapper.selectLiveChatNotActiveCount(id);
    }

//    채팅 - 그래프
    public List<Map<String, Object>> getLiveChatCanvasByServiceId(int id) {
        return serviceTeamSqlMapper.selectLiveChatCanvasByServiceId(id);
    }
}
