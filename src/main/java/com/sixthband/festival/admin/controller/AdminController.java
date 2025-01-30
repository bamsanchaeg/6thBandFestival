package com.sixthband.festival.admin.controller;

import com.sixthband.festival.admin.service.AdminService;
import com.sixthband.festival.club.service.ClubService;
import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.funding.service.FundingService;
import com.sixthband.festival.goods.service.GoodsService;
import com.sixthband.festival.rental.service.RentalService;
import com.sixthband.festival.ticket.service.TicketService;
import com.sixthband.festival.user.service.UserService;
import com.sixthband.festival.util.Utils;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private FundingService fundingService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private TicketService ticketService;

    // 메인 : 대시보드
    @RequestMapping("")
    public String adminMainPage(Model model) {
        // 페스티벌 정보
        Map<String, Object> festivalTicketData = ticketService.getMainDashBoardTickets();
        model.addAttribute("festivalTicketData", festivalTicketData);

        // 모임 정보
        model.addAttribute("clubRegistAcceptBeforeCount",clubService.acceptBeforeClubCount()); // 승인 대기 모임
        model.addAttribute("clubRegistAcceptCount",clubService.acceptClubCount()); // 승인된 모임
        model.addAttribute("newBoardCount",clubService.newBoardRegist()); // 신규 게시물

        // 굿즈 정보
        Map<String, Integer> goodsStatus = goodsService.countOrdersByStatus();
        model.addAttribute("goodsStatus", goodsStatus);

        // 대여 정보
        int rentalActive = rentalService.getRentalOrderStatusCount("대여 중");
        model.addAttribute("rentalActive", rentalActive);
        int rentalFinished = rentalService.getRentalOrderStatusCount("대여 완료");
        model.addAttribute("rentalFinished", rentalFinished);
        int returnFinished = rentalService.getRentalOrderStatusCount("반납 완료");
        model.addAttribute("returnFinished", returnFinished);

        // 펀딩 정보
        Map<String, Object> projectStatus = fundingService.getFundingProjectStatusCount();
        model.addAttribute("projectStatus", projectStatus);

        // 회원목록
        List<UserDto> userList = adminService.getUserListForDashboard();
        model.addAttribute("userList", userList);
        int userAllCount = adminService.userCount();
        model.addAttribute("userAllCount", userAllCount);
        int suspendedCount = adminService.getSuspendedUserCount();
        model.addAttribute("suspendedCount", suspendedCount);

        // 후기 정보
        List<Map<String, Object>> shopReviewList = adminService.getShopReviewListForDashboard();
        model.addAttribute("shopReviewList", shopReviewList);

        // return "admin/adminMainPage";
        return "admin/adminDashboard";
    }

    @RequestMapping("loginPage")
    public String adminLoginPage() {
        return "admin/adminLoginPage";
    }

    //    관리자 로그인
    @RequestMapping("loginProcess")
    public String loginProcess(UserDto params, HttpSession session) {

        UserDto loginUser = userService.loginUserExist(params);
        if (loginUser.getId() != -1) {
            return "redirect:" + Utils.FESTIVAL_URL + "/admin/loginPage";
//            return "redirect:/admin/loginPage";
        } else {
            session.setAttribute("admin", loginUser);
            return "redirect:" + Utils.FESTIVAL_URL + "/admin";
//            return "redirect:/admin";
        }
    }

    @RequestMapping("logoutProcess")
    public String  logoutProcess(HttpSession session){
        session.invalidate();
        return "redirect:" + Utils.FESTIVAL_URL + "/admin/loginPage";
//        return "redirect:/admin/loginPage";
    }

    @RequestMapping("clubAdminPage")
    public String clubAdminPage() {
        return "admin/clubAdminPage";
    }

//    @RequestMapping("rentalAdminPage")
//    public String rentalAdminPage(){
//        return "admin/rentalAdminPage";
//    }

    @RequestMapping("userControlPage")
    public String userControlPage(Model model) {
        model.addAttribute("userCount", adminService.userCount());
        return "admin/userControlPage";
    }

}