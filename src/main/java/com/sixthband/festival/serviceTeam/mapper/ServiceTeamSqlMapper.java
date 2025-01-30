package com.sixthband.festival.serviceTeam.mapper;

import com.sixthband.festival.serviceTeam.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

@Mapper
public interface ServiceTeamSqlMapper {
//    서비스팀 로그인
    public ServiceTeamDto selectServiceTeamByEmailAndPassword(ServiceTeamDto serviceTeamDto);
//    서비스팀 인원수
    public int selectServiceTeamCount();
//    서비스팀 리스트
    public List<Map<String, Object>> selectAllServiceTeam();
//    서비스팀 팀원 등록
    public void insertServiceTeamMember(ServiceTeamDto serviceTeamDto);
//    서비스팀 한 명
    public Map<String, Object> selectServiceTeamById(@Param("service_id") int service_id, @Param("today") String today);

//    출근
    public void insertServiceTeamAttendance(ServiceAttendanceManagementDto serviceAttendanceManagementDto);
//    출퇴근 기록 조회
    public int selectIsTodayAttendance(@Param("service_id") int service_id, @Param("today") String today);
//    휴게시간
    public void insertServiceTeamBreakTime(int service_id);
//    휴게시간 조회
    public int selectIsTodayBreakTime(@Param("service_id") int service_id, @Param("today") String today);
    public ServiceBreakManagementDto selectTodayBreakTime(@Param("service_id") int service_id, @Param("today") String today);

//    휴게시간 종료
    public void updateIsDoneY(@Param("service_id") int service_id, @Param("today") String today);
//    퇴근 기록 조회
    public int selectIsTodayGoHome(@Param("service_id") int service_id, @Param("today") String today);

    public List<Map<String, Object>> selectAllServiceTeamByToday(@Param("today") String today);

//    캘린더 출퇴근
    public List<Map<String, Object>> selectAllAttendanceByServiceId(int service_id);
//    캘린더 스케줄
    public List<ServiceTeamScheduleDto> selectAllScheduleByServiceId(int service_id);

//    내 정보
    public ServiceTeamDto selectMyInformationById(int id);

//    프로필 수정
    public void updateProfileById(ServiceTeamDto serviceTeamDto);
//    프로필 수정
    public void updatePasswordById(ServiceTeamDto serviceTeamDto);
//    폰번호 수정
    public void updatePhoneById(ServiceTeamDto serviceTeamDto);

//    일정 등록
    public void insertServiceTeamMemberWork(ServiceTeamScheduleDto serviceTeamScheduleDto);

//    스케줄인 사람
    public List<Map<String, Object>> selectAllScheduleMember(@Param("today") String today);

//    공지사항 등록
    public void insertServiceNotice(ServiceTeamNoticeDto serviceTeamNoticeDto);
//    공지사항 이미지 등록
    public void insertServiceNoticeImages(ServiceTeamNoticeImageDto serviceTeamNoticeImageDto);
//    공지사항 목록
    public List<Map<String, Object>> selectAllServiceTeamNotice(@Param("page") int page);
//    공지사항 개수
    public int selectNoticeCount();
//    공지사항 목록 -메인
    public List<Map<String, Object>> selectFiveServiceTeamNotice();
//    공지사항 상세
    public Map<String, Object> selectServiceTeamNoticeById(int id);
//    조회수 업데이트
    public void updateServiceTeamNoticeReadCount(int id);
//    이미지 가져오기
    public List<ServiceTeamNoticeImageDto> selectServiceTeamNoticeImages(int id);
//    공지 삭제
    public void deleteServiceTeamNoticeById(int id);
//    공지 수정
    public void updateServiceTeamNoticeById(ServiceTeamNoticeDto serviceTeamNoticeDto);

//    Qna 카테고리
    public List<ServiceQnaCategoryDto> selectAllQnaCategoryList();
//    Qna 게시글 등록
    public void insertQnaBoard(ServiceQnaBoardDto serviceQnaBoardDto);
//    공지사항 이미지 등록
    public void insertQnaImages(serviceQnaImageDto serviceQnaImageDto);
//    제일 놀고있는 하나
    public int selectQnABoardServiceId(@Param("today") String today);
//    qna 리스트
    public List<Map<String, Object>> selectAllQnAList(int id);
//    qna 상세
    public Map<String, Object> selectQnAListById(int id);

//    qna - 서비스 직원에게 들어온 리스트
    public List<Map<String, Object>> selectQnAListByServiceId(int service_id, @Param("page") int page);
//    qna - 서비스 직원 상세
    public Map<String, Object> selectServiceTeamQnAById(int id);
//    qna 이미지
    public List<serviceQnaImageDto> selectServiceTeamQnAImagesByBoardId(int id);
//    qna 접수완료
    public void updateQnAStatusCompleted(int id);

//    qna 답변 등록
    public void insertQnAAnswer(ServiceQnaAnswerDto serviceQnaAnswerDto);
//    qna 답변 이미지
    public void insertQnaAnswerImages(ServiceQnaAnswerImageDto serviceQnaAnswerImageDto);
//    qna 답변 완료
    public void updateQnAStatusDone(int id);
//    qna 답변 있는지 확인
    public int selectIsQnaCount(int id);
//    qna 답변
    public ServiceQnaAnswerDto selectQnaAnswerByBoardId(int id);
//    qna 답변 이미지
    public List<ServiceQnaAnswerImageDto> selectQnAAnswerImagesByAnswerId(int id);

//    qna 답변 유저화면
    public Map<String, Object> selectQnaAnswerForUserPage(int id);
//    qna 답변 이미지 유저화면
    public List<Map<String, Object>> selectQnaAnswerImagesForUserPage(int id);

//    qna 처리상태
    public int selectQnaStatusOneCount(int id);
    public int selectQnaStatusTwoCount(int id);
    public int selectQnaStatusThreeCount(int id);

//    qna 그래프
    public List<Map<String, Object>> selectQnaStatusCanvasByServiceId(int id);
//    qna 카운터
    public int selectMineQnaCount(int id);

//    채팅 제일 일 없는 사람
    public int selectLiveChatServiceId(@Param("today") String today);
//    채팅방 생성
    public void insertLiveChatRoom(ServiceLiveChatRoomDto serviceLiveChatRoomDto);
//    채팅방 입장
    public int selectMyChatRoomId(ServiceLiveChatRoomDto serviceLiveChatRoomDto);
//    채팅방 정보
    public Map<String, Object> selectMyChatRoomInfoById(int id);
//    채팅 입력
    public void insertLiveChatMessage(ServiceLiveChatDto serviceLiveChatDto);
//    채팅창 리스트 -직원
    public List<Map<String, Object>> selectMyChatRoomList(int id);
//    채팅창 내역
    public List<ServiceLiveChatDto> selectLiveChatMessageByRoomId(int id);
//    메세지 읽음처리
    public void updateIsReadingYByRoomId(int id);
//    채팅 종료
    public void updateChatRoomIsActiveById(int id);
//    채팅창 리스트 -유저
    public List<Map<String, Object>> selectUserChatList(int id);

//    진행중인 채팅방 개수
    public int selectLiveChatActiveCount(int id);
//    진행종료된 채팅방 개수
    public int selectLiveChatNotActiveCount(int id);
//    채팅 그래프
    public List<Map<String, Object>> selectLiveChatCanvasByServiceId(int id);
}
