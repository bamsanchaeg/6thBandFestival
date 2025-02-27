package com.sixthband.festival.exception;

import com.sixthband.festival.util.Utils;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class WebErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status != null) {
            int statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return  "redirect:" + Utils.FESTIVAL_URL + "/user/notFound";
            } else {
                return  "redirect:" + Utils.FESTIVAL_URL + "/user/error";
            }
        }

        return  "redirect:" + Utils.FESTIVAL_URL +"/user/error";
    }
}
