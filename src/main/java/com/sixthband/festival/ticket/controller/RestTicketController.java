package com.sixthband.festival.ticket.controller;


import com.sixthband.festival.dto.ImageDto;
import com.sixthband.festival.dto.RestResponseDto;
import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.ticket.dto.*;
import com.sixthband.festival.ticket.service.PaymentService;
import com.sixthband.festival.ticket.service.TicketService;
import com.sixthband.festival.util.ImageUtil;
import com.sixthband.festival.util.Utils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/ticket")
public class RestTicketController {

    @Value("${kakaoInit}")
    private String kakaoInit;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PaymentService paymentService;

    //    로그인 확인
    @RequestMapping("getSessionUserId")
    public RestResponseDto getSessionUserId(HttpSession session) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto loginUser = (UserDto) session.getAttribute("loginUser");

        if (loginUser != null) {
            restResponseDto.add("id", loginUser.getId());
            restResponseDto.add("email", loginUser.getEmail());
        } else {
            restResponseDto.add("id", null);
            restResponseDto.add("email", null);
        }

//        /api/ticket/getSessionUserId
        return restResponseDto;
    }

    @RequestMapping("getTicketInfo")
    public RestResponseDto getTicketInfo(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("ticketInfo", ticketService.getTicketInfoById(id));
//        restResponseDto.add("discountInfo", ticketService.getEarlybirdDiscount(id));

//        /api/ticket/getTicketInfo
        return restResponseDto;
    }

    @RequestMapping("createBooking")
    public RestResponseDto createBooking(TicketBookingDto params, HttpSession session) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        ticketService.createTicketBooking(params);

        Map<String, Object> tInfo = ticketService.getTicketInfoById(params.getInfo_id());
        String fName = (String) tInfo.get("festival_name");

        PaymentResponseDto paymentDto = new PaymentResponseDto();

        paymentDto.setPartner_order_id(String.valueOf(params.getId()));
        paymentDto.setPartner_user_id(String.valueOf(params.getUser_id()));
        paymentDto.setItem_name(fName + " Ticket");
        paymentDto.setQuantity(params.getAdult_quantity() + params.getYouth_quantity());
        paymentDto.setTotal_amount(params.getTotal_price());
        session.setAttribute("bookingId", params.getId());
        // 결제 준비 요청
        try {
            ReadyResponseDto paymentResponse = paymentService.readyPayment(paymentDto);
            restResponseDto.add("next_redirect_pc_url", paymentResponse.getNext_redirect_pc_url());
            restResponseDto.add("next_redirect_mobile_url", paymentResponse.getNext_redirect_mobile_url());
            session.setAttribute("tid", paymentResponse.getTid());
            session.setAttribute("partner_user_id", paymentDto.getPartner_user_id());
            session.setAttribute("partner_order_id", paymentDto.getPartner_order_id());
            System.out.println(paymentResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        /api/ticket/createBooking

        return restResponseDto;
    }

    @RequestMapping("updateRemainingQuantity")
    public RestResponseDto updateRemainingQuantity(@RequestParam("count") int count, @RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        ticketService.updateRemainingQuantity(count, id);

//        /api/ticket/updateRemainingQuantity

        return restResponseDto;
    }

    @RequestMapping("createFestivalReview")
    public RestResponseDto createFestivalReview(TicketReviewDto params, HttpSession session, @RequestParam(value="inputImages", required = false) MultipartFile[] inputImages) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        params.setUser_id(((UserDto) session.getAttribute("loginUser")).getId());
        ticketService.createFestivalReview(params);

        TicketReviewImageDto ticketReviewImageDto = new TicketReviewImageDto();

//        js에서 null처리를 해주고...이걸해야함.....
        if(inputImages != null && inputImages.length > 0) {
            List<ImageDto> multiImage = ImageUtil.saveImageAndReturnDtoList(inputImages);
            for (ImageDto imageDto : multiImage) {
                ticketReviewImageDto.setReview_id(params.getId());
                ticketReviewImageDto.setUrl(imageDto.getLocation());
                ticketService.createFestivalReviewImages(ticketReviewImageDto);
            }
        }

//        /api/ticket/createFestivalReview

        return restResponseDto;
    }

    @RequestMapping("getReviewList")
    public RestResponseDto getReviewList(@RequestParam("page") int page,
                                         @RequestParam(value="sort", defaultValue="1") int sort,
                                         @RequestParam(value="searchWord", required=false) String searchWord) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("reviewList", ticketService.getAllReviewList((page - 1) * 10, sort, searchWord));
        restResponseDto.add("reviewCount", ticketService.selectTicketReviewCount());
        restResponseDto.add("searchCount", ticketService.getSearchCount(searchWord));

//        /api/ticket/getReviewList

        return restResponseDto;
    }

    @RequestMapping("isReviewLike")
    public RestResponseDto isReviewLike(TicketReviewLikeDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("isLike", ticketService.isReviewLike(params));
        restResponseDto.add("likeCount", ticketService.getReviewLikeCount(params.getReview_id()));

//        /api/ticket/isReviewLike

        return restResponseDto;
    }

    @RequestMapping("createReviewLike")
    public RestResponseDto createReviewLike(TicketReviewLikeDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        ticketService.createReviewLike(params);

//        /api/ticket/createReviewLike

        return restResponseDto;
    }

    @RequestMapping("deleteReviewLike")
    public RestResponseDto deleteReviewLike(TicketReviewLikeDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        ticketService.deleteReviewLikeByUserIdAndReviewId(params);

//        /api/ticket/createReviewLike

        return restResponseDto;
    }

    @RequestMapping("deleteReview")
    public RestResponseDto deleteReview(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        ticketService.deleteReviewById(id);

//        /api/ticket/deleteReview

        return restResponseDto;
    }

    @RequestMapping("updateReview")
    public RestResponseDto updateReview(TicketReviewDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        ticketService.updateReviewById(params);

//        /api/ticket/updateReview

        return restResponseDto;
    }

    @RequestMapping("updateIsUse")
    public RestResponseDto updateIsUseByBookingId(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        ticketService.updateIsUseByBookingId(id);

//        /api/ticket/updateIsUse

        return restResponseDto;
    }

    @RequestMapping("getReviewById")
    public RestResponseDto getReviewById(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("reviewInfo", ticketService.getReviewInfoById(id));

//        /api/ticket/getReviewById

        return restResponseDto;
    }

    //    결제?
    @RequestMapping("/completed")
    public void payCompleted(@RequestParam("pg_token") String pgToken, HttpSession session, HttpServletResponse response, Model model) throws IOException {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        String tid = (String) session.getAttribute("tid");

//        지금 여기로 안들어와짐!
        System.out.println("토큰 : " + pgToken);
        System.out.println("고유번호 : " + tid);
        String oId = (String) session.getAttribute("partner_order_id");
        String uId = (String) session.getAttribute("partner_user_id");
        System.out.println("oID : " + oId);
        System.out.println("uID : " + uId);

        // 카카오 결제 요청하기
        AproveResponseDto aproveResponseDto = paymentService.payApprove(tid, pgToken, oId, uId);

//        이게 왜 넘어가면 String인지....이해가안가네..
//        model.addAttribute("bookingDone", aproveResponseDto);
//        Integer userId = Integer.parseInt(uId);
//        model.addAttribute("intUserId", userId);
//        System.out.println(userId);

        int bookingId = (int) session.getAttribute("bookingId");

//        예매 상태 업데이트
        ticketService.updatePaymentStatusY(bookingId);

//        db만들어서 넣기..
        String cid = "TC0ONETIME";
        TicketPaymentDto ticketPaymentDto = new TicketPaymentDto();
        ticketPaymentDto.setBooking_id(bookingId);
        ticketPaymentDto.setCid(cid);
        ticketPaymentDto.setTid(tid);
        ticketService.createTicketPayment(ticketPaymentDto);

        // RestController 리다이렉트.. 신기
//        모바일에서안됨
        response.sendRedirect(Utils.FESTIVAL_URL+ "/ticket/paymentSuccessPage?id=" + bookingId);
//        return "redirect:/ticket/paymentSuccessPage?id=" + bookingId;
    }

//    검색된 리뷰 결과
    @RequestMapping("getSearchCount")
    public RestResponseDto getSearchCount(@RequestParam(value="searchWord", required=false) String searchWord) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("searchCount", ticketService.getSearchCount(searchWord));

//        /api/ticket/getSearchCount

        return restResponseDto;
    }

//    카카오 api 키
    @RequestMapping("getKakaoKey")
    public RestResponseDto getKakaoKey() {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("kakao", kakaoInit);
//        Kakao.init(kakaoInit);

    //        /api/ticket/getKakaoKey

        return restResponseDto;
    }
}
