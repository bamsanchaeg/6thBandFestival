package com.sixthband.festival.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sixthband.festival.interceptor.SessionInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { // 이미지
        if(System.getProperty("os.name").charAt(0) == 'W') {
            registry.addResourceHandler("/images/**").addResourceLocations("file:///C://sixthBandImage/");
        } else if (System.getProperty("os.name").charAt(0) == 'M') {
            registry.addResourceHandler("/images/**")
                    .addResourceLocations("file:///Users/test/sixthBandImage/");
            System.out.println("OS:Mac");
        } else if (System.getProperty("os.name").charAt(0) == 'L') {
            registry.addResourceHandler("/images/**").addResourceLocations("file:///sixthBandImage/");
        }
    }

    @RequestScope
    public HttpServletRequest httpServletRequest() {
        return new HttpServletRequestWrapper((HttpServletRequest) RequestContextHolder.getRequestAttributes().resolveReference(RequestAttributes.REFERENCE_REQUEST));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) { // 인터셉터 : 로그인 필수 페이지 추가
        registry.addInterceptor(sessionInterceptor)
                // Admin : 로그인 제외 일괄
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/loginPage")
                .excludePathPatterns("/admin/loginProcess")
                .excludePathPatterns("/admin/logoutProcess")
                // User : 추가 항목 추가 필요
                .addPathPatterns("/user/myPage")
                // Funding
                .addPathPatterns("/funding/pledges")
                .addPathPatterns("/funding/projectsLiked")
                .addPathPatterns("/funding/projectsCreated")
                .addPathPatterns("/funding/projectEditorStart")
                .addPathPatterns("/funding/projectEditorManagement")
                .addPathPatterns("/funding/projectEditorDashboard")
                .addPathPatterns("/funding/projectEditorBackers")
                .addPathPatterns("/funding/projectEditorPledges")
                .addPathPatterns("/funding/management/**")
                .addPathPatterns("/funding/pledgePage")
                .addPathPatterns("/funding/pledgeCompleted")
                .addPathPatterns("/funding/pledgeDetail")
                //bubblePage
                .addPathPatterns("/bubble/postWritingPage")
                .addPathPatterns("/bubble/bubbleMainPage")
                .addPathPatterns("/bubble/bubbleChatPage")
                .addPathPatterns("/bubble/bubbleChatListPage")
                .addPathPatterns("/bubble/bubbleMyPage")
                .addPathPatterns("/bubble/bubbleMyPage")
                .addPathPatterns("/bubble/bubbleMyLikePage")
                .addPathPatterns("/bubble/bubbleMyAlarmPage")
                .addPathPatterns("/bubble/artistApplyPage")
                //GoodsPage
                .addPathPatterns("/goods/goodsCart")
                .addPathPatterns("/goods/goodsCartProcess")
                .addPathPatterns("/goods/ProductOrderProcess")
                .addPathPatterns("/goods/MyPayment")
                .addPathPatterns("/goods/MyPaymentDetail")
                .addPathPatterns("/goods/myWishList")
                // Tickets
                .addPathPatterns("/ticket/paymentSuccessPage")
                .addPathPatterns("/user/chatPage")
                .addPathPatterns("/user/chatStartPage")
                .addPathPatterns("/user/myChatListPage")
                .addPathPatterns("/user/qnaPage")
                .addPathPatterns("/user/qnaDetailPage")
                .addPathPatterns("/user/bookingInfoPage")
                .addPathPatterns("/user/myTicketsPage")
                .addPathPatterns("/user/myReviewsPage")
                .addPathPatterns("/user/myLikeReviewsPage")
                .addPathPatterns("/user/notActivePage")
                // 나중에 결제 실패 / 취소 페이지도 연결..
                //Community
                .addPathPatterns("/club/boardWritePage")
                .addPathPatterns("/club/clubRegistPage")
                .addPathPatterns("/club/registPhotoBook")
                .addPathPatterns("/club/registVote")
                .addPathPatterns("/club/registSchedule")
                .addPathPatterns("/club/voteDetailPage")
                .addPathPatterns("/club/board/detail")
                .addPathPatterns("/club/schedule/detail")
                .addPathPatterns("/club/registClubMember")
                // Rental
                .addPathPatterns("/rental/order")
                .addPathPatterns("/rental/payment-success")
                .addPathPatterns("/rental/myRentals")
                .addPathPatterns("/rental/myRentalsInfo")
                .addPathPatterns("/rental/review")
                // serviceTeam
                .addPathPatterns("/serviceTeam/mainPage")
                .addPathPatterns("/serviceTeam/memberManagementPage")
                .addPathPatterns("/serviceTeam/memberCalendarPage")
                .addPathPatterns("/serviceTeam/informationPage")
                .addPathPatterns("/serviceTeam/noticePage")
                .addPathPatterns("/serviceTeam/noticeDetailPage")
                .addPathPatterns("/serviceTeam/qnaCheckPage")
                .addPathPatterns("/serviceTeam/mineQnaDetailPage")
                .addPathPatterns("/serviceTeam/liveChatPage")
                ;
    }
}