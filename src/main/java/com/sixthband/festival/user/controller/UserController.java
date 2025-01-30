package com.sixthband.festival.user.controller;

import com.sixthband.festival.serviceTeam.service.ServiceTeamService;
import com.sixthband.festival.ticket.service.TicketService;
import com.sixthband.festival.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.user.service.UserService;
import com.sixthband.festival.util.ImageUtil;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ServiceTeamService serviceTeamService;

    @RequestMapping("loginPage")
    public String loginPage(){
        return "user/loginPage";
    }

    @RequestMapping("logoutProcess")
    public String  logoutProcess(HttpSession session){
        session.invalidate();

        return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
//        return "user/loginPage";
    }

    @RequestMapping("registerPage")
    public String registerPage(){
        return "user/registerPage";
    }

    @RequestMapping("registerProcess")
    public String registerProcess(UserDto userDto,@RequestParam("uploadFile")MultipartFile uploadFile){

        if (uploadFile.isEmpty()) {
            String filename = "/profile.png";
            userDto.setProfile_img(filename);
            userService.userRegist(userDto);
        } else {
            String image= ImageUtil.saveImageAndReturnLocation(uploadFile);
            userDto.setProfile_img(image);
            userService.userRegist(userDto);
        }
        return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
//        return "redirect:/user/loginPage";

    }

    @RequestMapping("loginProcess")
    public String loginProcess(HttpSession session,UserDto userDto) {

        UserDto loginUser = userService.loginUserExist(userDto);

        if(loginUser == null) {
            return "user/loginFail";

        }else {
            // Id가 -1이면 관리자임
            if (loginUser.getId() == -1) {
                session.setAttribute("admin", loginUser);
                return "redirect:" + Utils.FESTIVAL_URL + "/admin";
//                return "admin/adminMainPage";
            } else{
                session.setAttribute("loginUser", loginUser);

                UserDto userInfo = (UserDto) session.getAttribute("loginUser");
                String active = userInfo.getIs_active();

                if (active.equals("Y")) {
                    return "redirect:" + Utils.FESTIVAL_URL;
//                    return "redirect:/";
                } else {
                    // 계정 정지를 먹으면 아예 세션날려버리기.. or 세션에서 getIs_active 해야하는지?
                     session.invalidate();
                    return "redirect:" + Utils.FESTIVAL_URL + "/user/notActivePage";
//                    return "redirect:/user/notActivePage";
                }

            }
        }
    }

//    티켓 마이페이지
    @RequestMapping("myTicketsPage")
    public String myPage(Model model, @RequestParam("id") int id) {

        model.addAttribute("userInfo", userService.getUserById(id));
        model.addAttribute("bookingInfo", ticketService.getBookingByUserId(id));
        model.addAttribute("reviewList", ticketService.getReviewListByUserId(id));

        return "user/myTicketsPage";
    }

//    활동정지
    @RequestMapping("notActivePage")
    public String notActivePage() {
        return "user/notActivePage";
    }

//    예약 상세
    @RequestMapping("bookingInfoPage")
    public String bookingInfoPage(Model model, @RequestParam("id") int id) {
        model.addAttribute("isUseNInfo", ticketService.getIsUseNBookingByUserId(id));
        model.addAttribute("isUseYInfo", ticketService.getIsUseYBookingByUserId(id));
        return "user/bookingInfoPage";
    }

//    QnA
    @RequestMapping("qnaPage")
    public String qnaPage(Model model) {
        model.addAttribute("qnaCategory", serviceTeamService.getAllQnaCategoryList());
        return "user/qnaPage";
    }

//    QnA 상세
    @RequestMapping("qnaDetailPage")
    public String qnaDetailPage(@RequestParam("id") int id, Model model) {
        model.addAttribute("qnaDetail", serviceTeamService.getQnAListById(id));
        model.addAttribute("qnaImages", serviceTeamService.getServiceTeamQnAImagesByBoardId(id));
        model.addAttribute("answer", serviceTeamService.getQnaAnswerForUserPage(id));
        model.addAttribute("answerImages", serviceTeamService.getQnaAnswerImagesForUserPage(id));
        return "user/qnaDetailPage";
    }

//    상담 신청
    @RequestMapping("chatPage")
    public String chatPage() {

        return "user/chatPage";
    }

//    상담방
    @RequestMapping("chatStartPage")
    public String chatStartPage(@RequestParam("id") int id, Model model) {
        model.addAttribute("roomInfo", serviceTeamService.getMyChatRoomInfoById(id));
        return "user/chatStartPage";
    }

//    상담 내역
    @RequestMapping("myChatListPage")
    public String myChatListPage(@RequestParam("id")int id, Model model){
        model.addAttribute("chatList", serviceTeamService.getUserChatList(id));
        return "user/myChatListPage";

    }

//    내가 쓴 리뷰
    @RequestMapping("myReviewsPage")
    public String myReviewsPage(@RequestParam("id")int id, Model model){
        model.addAttribute("reviewList", ticketService.getReviewListByUserId(id));
        return "user/myReviewsPage";
    }

//    내가 좋아요한 리뷰
    @RequestMapping("myLikeReviewsPage")
    public String myLikeReviewsPage(@RequestParam("id")int id, Model model){
        model.addAttribute("likeList", ticketService.getLikeReviewListByUserId(id));
        return "user/myLikeReviewsPage";
    }

    @RequestMapping("myPage2")
    public String myPage2(){

        return "user/myPage2";

    }

    @RequestMapping("profileRevise")
    public String profileRevise(@RequestParam("id")int id){

        return "user/profileRevisePage";

    }

//    진짜 마이페이지
    @RequestMapping("myPage")
    public String myPage(@RequestParam("id")int id, Model model){
        model.addAttribute("userInfo", userService.getUserById(id));
        return "user/myPage";

    }

    // interceptor 처리 : 수업때 진행한 페이지..
    @RequestMapping("sessionNullPage")
    public String sessionNullPage() {
        return "user/sessionNullPage";
    }


//    관리자 로그인
//    @RequestMapping("adminLoginProcess")
//    public String loginProcess(UserDto params, HttpSession session) {
//        UserDto loginUser = userService.loginUserExist(params);
//        session.setAttribute("admin", loginUser);
//
//        return "redirect:/admin";
//    }

    // error
    @RequestMapping("error")
    public String error() {

        return "user/error";
    }

    // 404
    @RequestMapping("notFound")
    public String notFound() {

        return "user/notFound";
    }
}   

