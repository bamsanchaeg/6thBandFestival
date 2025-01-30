package com.sixthband.festival.serviceTeam.controller;

import com.sixthband.festival.serviceTeam.dto.ServiceTeamDto;
import com.sixthband.festival.serviceTeam.service.ServiceTeamService;
import com.sixthband.festival.util.Utils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/serviceTeam")
public class ServiceTeamController {

    @Autowired
    private ServiceTeamService serviceTeamService;

//    로그인 화면
    @RequestMapping("")
    public String loginPage() {
        return "serviceTeam/loginPage";
    }

//    로그인 로직
    @RequestMapping("loginProcess")
    public String loginProcess(ServiceTeamDto params, HttpSession session) {
        ServiceTeamDto serviceTeamDto = serviceTeamService.getServiceTeam(params);

        session.setAttribute("loginServiceTeam", serviceTeamDto);

        return "redirect:" + Utils.FESTIVAL_URL + "/serviceTeam/mainPage";
//        return "redirect:/serviceTeam/mainPage";
    }

    //    로그아웃 로직
    @RequestMapping("loginOutProcess")
    public String loginOutProcess(HttpSession session) {
        session.invalidate();

        return "redirect:" + Utils.FESTIVAL_URL + "/serviceTeam";
    }

//    메인 화면
    @RequestMapping("mainPage")
    public String mainPage(HttpSession session, Model model) {
//        유저 정보가 수정되면 바로 적용하게 session이 아닌 dto로 변경
        if (session.getAttribute("loginServiceTeam") != null) {
            ServiceTeamDto serviceTeamDto = (ServiceTeamDto) session.getAttribute("loginServiceTeam");
            model.addAttribute("serviceTeamDto", serviceTeamDto);
        }

        return "serviceTeam/mainPage";
    }

//    팀장 - 팀원 관리 페이지
    @RequestMapping("memberManagementPage")
    public String memberManagementPage(HttpSession session, Model model) {
        model.addAttribute("stCount", serviceTeamService.getServiceTeamCount());
        model.addAttribute("serviceTeamList", serviceTeamService.getAllServiceTeam());

        if (session.getAttribute("loginServiceTeam") != null) {
            ServiceTeamDto serviceTeamDto = (ServiceTeamDto) session.getAttribute("loginServiceTeam");
            model.addAttribute("serviceTeamDto", serviceTeamDto);
        }

        return "serviceTeam/memberManagementPage";
    }

//    팀원 캘린더
    @RequestMapping("memberCalendarPage")
    public String memberCalendarPage(HttpSession session, Model model) {
        if (session.getAttribute("loginServiceTeam") != null) {
            ServiceTeamDto serviceTeamDto = (ServiceTeamDto) session.getAttribute("loginServiceTeam");
            model.addAttribute("serviceTeamDto", serviceTeamDto);
        }

        return "serviceTeam/memberCalendarPage";
    }

//    개인정보 수정
    @RequestMapping("informationPage")
    public String informationPage(HttpSession session, Model model) {
        if (session.getAttribute("loginServiceTeam") != null) {
            ServiceTeamDto serviceTeamDto = (ServiceTeamDto) session.getAttribute("loginServiceTeam");
            model.addAttribute("serviceTeamDto", serviceTeamDto);
        }

        return "serviceTeam/informationPage";
    }

//    공지사항 게시판
    @RequestMapping("noticePage")
    public String noticePage(HttpSession session, Model model) {
        model.addAttribute("noticeCount", serviceTeamService.getNoticeCount());

        if (session.getAttribute("loginServiceTeam") != null) {
            ServiceTeamDto serviceTeamDto = (ServiceTeamDto) session.getAttribute("loginServiceTeam");
            model.addAttribute("serviceTeamDto", serviceTeamDto);
        }

        return "serviceTeam/noticePage";
    }

//    공지사항 상세
    @RequestMapping("noticeDetailPage")
    public String noticeDetailPage(@RequestParam("id") int id, Model model, HttpSession session) {
        serviceTeamService.updateServiceTeamNoticeReadCount(id);
        model.addAttribute("noticeInfo", serviceTeamService.getServiceTeamNoticeById(id));
        model.addAttribute("noticeImages", serviceTeamService.getServiceTeamNoticeImages(id));

        if (session.getAttribute("loginServiceTeam") != null) {
            ServiceTeamDto serviceTeamDto = (ServiceTeamDto) session.getAttribute("loginServiceTeam");
            model.addAttribute("serviceTeamDto", serviceTeamDto);
        }

        return "serviceTeam/noticeDetailPage";
    }

//    qna 확인
    @RequestMapping("qnaCheckPage")
    public String qnaCheckPage(HttpSession session, Model model) {
        ServiceTeamDto serviceTeamDto = (ServiceTeamDto) session.getAttribute("loginServiceTeam");
        if(serviceTeamDto != null) {
            model.addAttribute("count", serviceTeamService.getMineQnaCount(serviceTeamDto.getId()));
        }

        if (session.getAttribute("loginServiceTeam") != null) {
//            ServiceTeamDto serviceTeamDto = (ServiceTeamDto) session.getAttribute("loginServiceTeam");
            model.addAttribute("serviceTeamDto", serviceTeamDto);
        }

        return "serviceTeam/qnaCheckPage";
    }

//    qna 상세 - 직원
    @RequestMapping("mineQnaDetailPage")
    public String mineQnaDetailPage(@RequestParam("id") int id, Model model, HttpSession session) {
        model.addAttribute("serviceQnaDetail", serviceTeamService.getServiceTeamQnAById(id));
        model.addAttribute("serviceQnaImages", serviceTeamService.getServiceTeamQnAImagesByBoardId(id));
        model.addAttribute("isQna", serviceTeamService.getIsQnaCount(id));
        if (serviceTeamService.getIsQnaCount(id) != 0) {
            model.addAttribute("answer", serviceTeamService.getQnaAnswerByBoardId(id));
            model.addAttribute("answerImages", serviceTeamService.getQnAAnswerImagesByAnswerId(serviceTeamService.getQnaAnswerByBoardId(id).getId()));
        }

        if (session.getAttribute("loginServiceTeam") != null) {
            ServiceTeamDto serviceTeamDto = (ServiceTeamDto) session.getAttribute("loginServiceTeam");
            model.addAttribute("serviceTeamDto", serviceTeamDto);
        }

        return "serviceTeam/mineQnaDetailPage";
    }

//    상담 페이지
    @RequestMapping("liveChatPage")
    public String liveChatPage(HttpSession session, Model model) {
        if (session.getAttribute("loginServiceTeam") != null) {
            ServiceTeamDto serviceTeamDto = (ServiceTeamDto) session.getAttribute("loginServiceTeam");
            model.addAttribute("serviceTeamDto", serviceTeamDto);
        }

        return "serviceTeam/liveChatPage";
    }

}
