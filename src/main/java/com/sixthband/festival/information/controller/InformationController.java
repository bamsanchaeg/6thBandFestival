package com.sixthband.festival.information.controller;
import java.util.*;

import com.sixthband.festival.information.dto.FestivalDto;
import com.sixthband.festival.information.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

//포워딩을 하는 컨트롤러
//이 어노테이션으로 내부적으로 컨트롤러 역할을 할 수 있다.
@Controller
//요청주소로 실제 주소를 매핑하는 어노테이션, 'getMapping'도 많이 씀
@RequestMapping("information")
public class InformationController {

    @Autowired
    InformationService informationService;

    //"" 요청이 날아오면 아래 로직을 실행하고 return값인 html 주소로 포워딩한다.
    //위에 선언된 request가 주소창을 형성한다고 보면 된다. MVC모델

    //information 메인페이지
    @RequestMapping("mainPage")
    public String mainPage(Model model){
        //페스티벌 리스트 불러오기
        List<Map<String,Object>> festivalDataList = informationService.getFestivallList();
        model.addAttribute("festivalDataList",festivalDataList);
        return "information/mainPage";
    }

    @RequestMapping("festivalHistory")
    public String festivalHistory(){
        return "information/festivalHistory";
    }

    @RequestMapping("mainfestivalDetail")
    public String mainfestivalDetail(){
        return "information/mainFestivalDetailPage";
    }

    @RequestMapping("festivalDetailPage")
    public String festivalDetailPage(Model model,int id){
        //새로운 맵 자리에 해당 값을 넣어주기
        //왜 맵의 세부정보를 찾지 못했을까..? 갑자기 궁금해짐
        Map<String,Object> festivalData = informationService.getFestivalDetailById(id);
        model.addAttribute("festivalData",festivalData);


        return "information/festivalDetailPage";
    }

    //라인업 페이지, 아티스트 리스트를 가져올예정
    @RequestMapping("festivalLineupPage")
    public String festivalLineupPage(){
        return "information/festivalLineupPage";
    }

    @RequestMapping("lineUpPage")
    public String lineUpPage(){
        return "information/mainLineUpPage";
    }

    @RequestMapping("artistDetailPage")
    public String artistDetailPage(){
        return"information/artistDetailPage";
    }

    @RequestMapping("festivalHistoryDetail")
    public String festivalHistoryDetail(){
        return "information/festivalHistoryDetailPage";
    }

    //페스티벌 히스토리 라인업 페이지
    @RequestMapping("festivalHistoryLineUp")
    public String festivalHistoryLineUp(){
        return "information/festivalHistoryLineupPage";
    }









}
