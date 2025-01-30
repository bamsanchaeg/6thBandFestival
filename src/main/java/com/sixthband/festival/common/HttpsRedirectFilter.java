package com.sixthband.festival.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
//
//@Component
//public class HttpsRedirectFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        if (!httpRequest.isSecure()) {
//            String httpsUrl = "https://" + httpRequest.getServerName() + httpRequest.getRequestURI();
//            httpResponse.sendRedirect(httpsUrl);
//        } else {
//            chain.doFilter(request, response);
//        }
//    }
//}