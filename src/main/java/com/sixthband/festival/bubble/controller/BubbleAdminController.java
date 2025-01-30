package com.sixthband.festival.bubble.controller;


import com.sixthband.festival.bubble.service.BubbleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//커밋 및 로컬 -> 원격 푸시
@Controller
@RequestMapping("admin/bubble")
public class BubbleAdminController {

    @Autowired
    BubbleService bubbleService;

    @RequestMapping("")
    public String bubbleHomePage() {
        return "admin/bubble/bubbleHome";
    }

    @RequestMapping("bubbleArtistPage")
    public String bubbleArtistPage() {
        return "admin/bubble/artistAcceptPage";
    }

    @RequestMapping("applicationDetail")
    public String applicationDetail() {
        return "admin/bubble/applicationDetail";
    }

}
