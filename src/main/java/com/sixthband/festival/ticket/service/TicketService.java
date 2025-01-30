package com.sixthband.festival.ticket.service;

import com.sixthband.festival.information.dto.FestivalDto;
import com.sixthband.festival.ticket.dto.*;
import com.sixthband.festival.ticket.mapper.TicketSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.*;

@Service
public class TicketService {

//    openAi api Key
    @Value("${openAiKey}")
    private String openAiKey;

    @Autowired
    private TicketSqlMapper ticketSqlMapper;

//    티켓 - 관리자
    public List<FestivalDto> getAllFestivalList() {
        return ticketSqlMapper.selectAllFestivalList();
    }

    public FestivalDto getFestivalById(int id) {
        return ticketSqlMapper.selectFestivalById(id);
    }

    public void createTicketInfo(TicketInfoDto ticketInfoDto) {
        ticketSqlMapper.insertTicket(ticketInfoDto);
    }

    public List<Map<String, Object>> getAllTicketInfoList(int page) {
        List<Map<String, Object>> ticketInfoList = ticketSqlMapper.selectAllTicketList(page);

//        resultType map으로 받음.. map으로 한 번 분해
        for(Map<String, Object> ticketInfo : ticketInfoList) {
            int eId = (int) ticketInfo.get("id");
            ticketInfo.put("purchaseQuantity", ticketSqlMapper.selectTicketPurchaseQuantity(eId));
        }

        return ticketInfoList;
    }

    public Map<String, Object> getTicketInfoById(int id) {
        return ticketSqlMapper.selectTicketById(id);
    }

    public void deleteTicketInfo(int id) {
        ticketSqlMapper.deleteTicketById(id);
    }

    public void createDiscount(TicketEarlybirdDiscountDto ticketEarlybirdDiscountDto) {
        ticketSqlMapper.insertDiscount(ticketEarlybirdDiscountDto);
    }

    public void deleteDiscount(int id) {
        ticketSqlMapper.deleteDiscountById(id);
    }

    public int getTicketPurchaseQuantity(int id) {
        return ticketSqlMapper.selectTicketPurchaseQuantity(id);
    }

    public TicketEarlybirdDiscountDto getEarlybirdDiscount(int id) {
        return ticketSqlMapper.selectEarlybirdDiscount(id);
    }

    public void updateRemainingQuantity(int count, int id) {
        ticketSqlMapper.updateRemainingQuantity(count, id);
    }

//    티켓 - 사용자
    public List<TicketInfoDto> getTicketInfoOneDayListByFestivalId(int festival_id) {
        return ticketSqlMapper.selectTicketOneDayListByFestivalId(festival_id);
    }

    public TicketInfoDto getTicketInfoAllDayByFestivalId(int festival_id) {
        return ticketSqlMapper.selectTicketAllDayByFestivalId(festival_id);
    }

    public Map<String, Object> getTicketInfoMinPrice(int festival_id) {
        Map<String, Object> map = new HashMap<>();

        int oneDayMin = ticketSqlMapper.selectMinYouthPriceByOneDay(festival_id);
        int allDayMin = ticketSqlMapper.selectMinYouthPriceByAllDay(festival_id);

        map.put("oneDayMin", oneDayMin);
        map.put("allDayMin", allDayMin);

        return map;
    }

    public void createTicketBooking(TicketBookingDto ticketBookingDto) {
        ticketSqlMapper.insertTicketBooking(ticketBookingDto);
    }

    public List<Map<String, Object>> getBookingByUserId(int user_id) {
        return ticketSqlMapper.selectBookingByUserId(user_id);
    }

    public List<Map<String, Object>> getIsUseNBookingByUserId(int user_id) {
        return ticketSqlMapper.selectIsUseNBookingByUserId(user_id);
    }

    public List<Map<String, Object>> getIsUseYBookingByUserId(int user_id) {
        return ticketSqlMapper.selectIsUseYBookingByUserId(user_id);
    }

    public Map<String, Object> getBookingById(int id) {
        return ticketSqlMapper.selectBookingById(id);
    }

    public FestivalDto getMinStartingDateFestival() {
        return ticketSqlMapper.selectMinStartingDateFestivalInfo();
    }

    public boolean isBookingTickets(int user_id) {
        return ticketSqlMapper.isBookingTickets(user_id) > 0;
    }

    public List<Map<String, Object>> getBookingDataByUserId(int user_id) {
        return ticketSqlMapper.selectBookingDataByUserId(user_id);
    }

    public void createFestivalReview(TicketReviewDto ticketReviewDto) {
        ticketSqlMapper.insertFestivalReview(ticketReviewDto);
    }

    public void createFestivalReviewImages(TicketReviewImageDto ticketReviewImageDto) {
        ticketSqlMapper.insertFestivalReviewImages(ticketReviewImageDto);
    }

    public List<Map<String, Object>> getAllReviewList(int page, int sort, String searchWord) {
        return ticketSqlMapper.selectAllReviewList(page, sort, searchWord);
    }

    public Map<String, Object> getReviewInfoById(int id) {
        return ticketSqlMapper.selectReviewInfoById(id);
    }

    public List<TicketReviewImageDto> getReviewImagesByReviewId(int id) {
        return ticketSqlMapper.selectReviewImagesByReviewId(id);
    }

    public void createReviewLike(TicketReviewLikeDto ticketReviewLikeDto) {
        ticketSqlMapper.insertReviewLike(ticketReviewLikeDto);
    }

    public boolean isReviewLike(TicketReviewLikeDto ticketReviewLikeDto) {
        return ticketSqlMapper.isReviewLike(ticketReviewLikeDto) > 0;
    }

    public void deleteReviewLikeByUserIdAndReviewId(TicketReviewLikeDto ticketReviewLikeDto) {
        ticketSqlMapper.deleteReviewLikeByUserIdAndReviewId(ticketReviewLikeDto);
    }

    public int getReviewLikeCount(int review_id) {
        return ticketSqlMapper.selectReviewLikeCount(review_id);
    }

    public void updateReviewReadCount(int id) {
        ticketSqlMapper.updateReviewReadCount(id);
    }

    public void deleteReviewById(int id) {
        ticketSqlMapper.deleteReviewById(id);
    }

    public void updateReviewById(TicketReviewDto ticketReviewDto) {
        ticketSqlMapper.updateReviewById(ticketReviewDto);
    }

    public List<Map<String, Object>> getReviewListLikeTopThree() {
        return ticketSqlMapper.selectReviewListLikeCountBestThree();
    }

    public List<Map<String, Object>> getReviewListFiveById(int id) {
        return ticketSqlMapper.selectReviewListFiveById(id);
    }

    public List<Map<String, Object>> getReviewListByUserId(int id) {
        return ticketSqlMapper.selectReviewListByUserId(id);
    }

    public List<Map<String, Object>> getLikeReviewListByUserId(int id) {
        return ticketSqlMapper.selectLikeReviewListByUserId(id);
    }

    public List<Map<String, Object>> getBookingList(int page, String isPayment, String searchOption, String searchWord) {
        return ticketSqlMapper.selectBookingList(page, isPayment, searchOption, searchWord);
    }

    public Map<String, Object> getAdminBookingById(int id) {
        return ticketSqlMapper.selectAdminBookingById(id);
    }

    public List<Map<String, Object>> getAdminReviewAll(int page) {
        return ticketSqlMapper.selectAdminReviewAll(page);
    }

    public void updateIsUseByBookingId(int id) {
        ticketSqlMapper.updateIsUseByBookingId(id);
    }

//    페이징
    public int countTickets() {
        return ticketSqlMapper.countTickets();
    }
    public int countBooking(String isPayment, String searchOption, String searchWord) {
        return ticketSqlMapper.countBooking(isPayment, searchOption, searchWord);
    }
    public int countReview() {
        return ticketSqlMapper.countReview();
    }

//    결제
    public void updatePaymentStatusY(int id) {
        ticketSqlMapper.updatePaymentStatusY(id);
    }

    public void createTicketPayment(TicketPaymentDto ticketPaymentDto) {
        ticketSqlMapper.insertTicketPayment(ticketPaymentDto);
    }

//    그래프
    public List<Map<String, Object>> getDailySales() {
        return ticketSqlMapper.selectDailySales();
    }

    public List<Map<String, Object>> getMonthlySales() {
        return ticketSqlMapper.selectMonthlySales();
    }

    public List<Map<String, Object>> getAnnualSales() {
        return ticketSqlMapper.selectAnnualSales();
    }

    public int getOneDayRatio(int id) {
        return ticketSqlMapper.selectDayTypeOneDay(id);
    }

    public int getAllDayRatio(int id) {
        return ticketSqlMapper.selectDayTypeAllDay(id);
    }

    public int getOneDaySales(int id) {
        return ticketSqlMapper.selectOneDaySales(id);
    }

    public int getAllDaySales(int id) {
        return ticketSqlMapper.selectAllDaySales(id);
    }

    public List<Map<String, Object>> getOneDayAge(int id) {
        return ticketSqlMapper.selectOneDayAge(id);
    }

    public int getOneDayGenderF(int id) {
        return ticketSqlMapper.selectOneDayGenderF(id);
    }

    public int getOneDayGenderM(int id) {
        return ticketSqlMapper.selectOneDayGenderM(id);
    }

    public List<Map<String, Object>> getAllDayAge(int id) {
        return ticketSqlMapper.selectAllDayAge(id);
    }

    public int getAllDayGenderF(int id) {
        return ticketSqlMapper.selectAllDayGenderF(id);
    }

    public int getAllDayGenderM(int id) {
        return ticketSqlMapper.selectAllDayGenderM(id);
    }

//    보충
    public int getAllTicketCount() {
        return ticketSqlMapper.selectAllTicketCount();
    }

    public int getAllBookingCount() {
        return ticketSqlMapper.selectAllBookingCount();
    }

    public int getAllReviewCount() {
        return ticketSqlMapper.selectAllReviewCount();
    }


//    Open AI - 키 !!!
//    자정마다 돌게하는..
//    @Scheduled(cron = "0 0 2 * * ?")
    public String openAiTest(int id) {

        String review = ticketSqlMapper.selectGptReviewById(id).getContent();

        // 서비스 생성
        String openaiAccessKey = openAiKey;
        OpenAiService service = new OpenAiService(openaiAccessKey, Duration.ofSeconds(30));

        // ChatGpt에게 보낼 내용 세팅
        List<ChatMessage> messages = new ArrayList<>();

        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setRole("system");
        systemMessage.setContent("당신은 내가 제시하는 리뷰를 요약해줘야합니다. 최대한 짧게 요약을 해야 합니다. 절대로 20글자를 넘어서서는 안됩니다.");
        messages.add(systemMessage);


        ChatMessage chatMessage = new ChatMessage();
        // chatMessage.setContent("다음 글을 정말 짧게 요약해줘(무조건 10자 이내로 부탁해!!!!!!!!!!) : " + review + " ");
        // chatMessage.setContent("다음 글을 5글자 이내의 3개의 단어로 요약해줘!!!!!!!!!! : " + review + " ");
        chatMessage.setRole("user");
        chatMessage.setContent("다음 글을 요약해서 아주 아주 짧은 소감 한 줄 만들어줘!!!!!!!!!! : " + review);
        messages.add(chatMessage);

        System.out.println("메세지 : " + messages);
        // 요청
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                // 미니가 돈 덜나감!!!!! 반정도..
                .model("gpt-4o-mini")
                .messages(messages)
                .build();

        // 응답 결과
        String result = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent();
        System.out.println("결과: " + result);
        return result;
    }

//    리뷰 개수
    public int selectTicketReviewCount() {
        return ticketSqlMapper.selectTicketReviewCount();
    }

//    메인 대시보드용
    public Map<String, Object> getMainDashBoardTickets() {
        return ticketSqlMapper.selectMainDashBoardTickets();
    }

//    검색된 리뷰 개수
    public int getSearchCount(String searchWord) {
        return ticketSqlMapper.selectSearchCount(searchWord);
    }
}
