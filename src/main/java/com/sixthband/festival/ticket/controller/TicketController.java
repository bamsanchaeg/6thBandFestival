package com.sixthband.festival.ticket.controller;

import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.ticket.dto.TicketInfoDto;
import com.sixthband.festival.ticket.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/ticket")
public class TicketController {

//    프로퍼티에 키 숨기기 테스트
//    @Value("${test}")
//    private String test;

    @Autowired
    private TicketService ticketService;

    @RequestMapping("")
    public String mainPage(Model model) {
        model.addAttribute("minFestival",ticketService.getMinStartingDateFestival());
        model.addAttribute("likeTopThreeReview", ticketService.getReviewListLikeTopThree());
//        System.out.println("키가잘나오는지테스트" + test);
        return "ticket/mainPage";
    }

    @RequestMapping("selectPage")
    public String selectPage (@RequestParam("id") int festival_id, Model model) {
        model.addAttribute("festivalInfo", ticketService.getFestivalById(festival_id));

        model.addAttribute("ticketOneDayList", ticketService.getTicketInfoOneDayListByFestivalId(festival_id));
        model.addAttribute("ticketAllDayInfo", ticketService.getTicketInfoAllDayByFestivalId(festival_id));

        model.addAttribute("minPrice", ticketService.getTicketInfoMinPrice(festival_id));
        return "ticket/selectPage";
    }

    @RequestMapping("infoPage")
    public String infoPage (@RequestParam("id") int id, Model model) {
        model.addAttribute("ticketInfo", ticketService.getTicketInfoById(id));
        model.addAttribute("purchaseQuantity", ticketService.getTicketPurchaseQuantity(id));
        return "ticket/infoPage";
    }

    @RequestMapping("reviewPage")
    public String reviewPage (HttpSession session, Model model, @RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value="sort", defaultValue="1") int sort,
                              @RequestParam(value="searchWord", required=false) String searchWord) {
        if(session.getAttribute("loginUser") != null) {
            UserDto userInfo = (UserDto) session.getAttribute("loginUser");
            model.addAttribute("isBooking", ticketService.isBookingTickets(userInfo.getId()));
            model.addAttribute("bookingTicketInfo", ticketService.getBookingDataByUserId(userInfo.getId()));
        }

        model.addAttribute("reviewList", ticketService.getAllReviewList(page, sort, searchWord));
        return "ticket/reviewPage";
    }

    @RequestMapping("reviewDetailPage")
    public String reviewDetailPage (@RequestParam("id") int id, Model model) {
        ticketService.updateReviewReadCount(id);
        model.addAttribute("reviewInfo", ticketService.getReviewInfoById(id));
        model.addAttribute("reviewImages", ticketService.getReviewImagesByReviewId(id));
        model.addAttribute("likeCount", ticketService.getReviewLikeCount(id));
        model.addAttribute("reviewListFive", ticketService.getReviewListFiveById(id));

        model.addAttribute("pageId", id);

//        gpt 요약 코드
        model.addAttribute("gpt", ticketService.openAiTest(id));

        return "ticket/reviewDetailPage";
    }

    @RequestMapping("qrcodeCheckPage")
    public String qrcodeCheckPage(@RequestParam("id") int id) {

        return "ticket/qrcodeCheckPage";
    }

    @RequestMapping("paymentSuccessPage")
    public String paymentSuccessPage(@RequestParam("id") int id) {

        return "ticket/paymentSuccessPage";
    }

}
