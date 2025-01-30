package com.sixthband.festival.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//희원이 건들고 있습니다. 사용하실때 말해주세요(8/14)
public class MainController {

    @RequestMapping("")
    public String mainPage() {

        return "main/main";
    }
}
