package com.sixthband.festival.club.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sixthband.festival.club.dto.ClubDto;
import com.sixthband.festival.club.service.ClubService;
import com.sixthband.festival.util.Utils;

@Controller
@RequestMapping("admin/club")
public class AdminClubController {

    @Autowired
    private ClubService clubService;

    @RequestMapping("registClub")
    public String clubAdminPage(Model model, @RequestParam(value="p", defaultValue = "1") int p) {

        List<Map<String,Object>> clubListAll = clubService.appearClubList(p);
        model.addAttribute("clubListAll", clubListAll);

        int totalClubCount = clubService.totalClubCount();
        model.addAttribute("totalClubCount",totalClubCount);

        int lastPageNumber = (int)Math.ceil(totalClubCount/10.0);
        model.addAttribute("lastPageNumber", lastPageNumber);

        model.addAttribute("currentPage", p);

        int startPage = ((p-1)/5)*5+1;
        int endPage = ((p-1)/5+1)*5;

        if(endPage > lastPageNumber) {
            endPage = lastPageNumber;
        }

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/club/clubAdminPage";
    }


    @RequestMapping("updateRejectClub")
    public String updateRejectClub(@RequestParam("id")int id){
        clubService.rejectClubId(id);
        return "redirect:" + Utils.FESTIVAL_URL + "/admin/club/registClub";
    }

    @RequestMapping("updateApproveClub")
    public String updateApproveClub(@RequestParam("id")int id){
        clubService.updateApproveClub(id);
        return "redirect:" + Utils.FESTIVAL_URL + "/admin/club/registClub";

    }
    
    @RequestMapping("clubDetailPage")
    public String clubDetailPage(Model model,@RequestParam("id")int id){
        Map<String,Object> clubDetailInfo = clubService.clubAdminDetailInfo(id);

        model.addAttribute("clubDetailInfo", clubDetailInfo);
        return"admin/club/clubDetailPage";
    }
    
    @RequestMapping("freeBoardListManage")
    public String freeBoardListManage(){
        
        return"admin/club/freeBoardListManage";
    }

    @RequestMapping("reportDetailPage")
    public String reportDetailPage(){
        
        return"admin/club/reportDetailPage";
    }

   

    
}