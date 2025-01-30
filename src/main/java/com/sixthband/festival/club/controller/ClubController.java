package com.sixthband.festival.club.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import com.sixthband.festival.club.dto.BoardCategoryDto;
import com.sixthband.festival.club.dto.ClubBoardDto;
import com.sixthband.festival.club.dto.ClubCategoryDto;
import com.sixthband.festival.club.dto.ClubDto;
import com.sixthband.festival.club.dto.ClubMemberRegistDto;
import com.sixthband.festival.club.dto.ClubVoteOptionDto;
import com.sixthband.festival.club.service.ClubService;
import com.sixthband.festival.dto.ImageDto;
import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.util.ImageUtil;
import com.sixthband.festival.util.Utils;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("club")
public class ClubController {

    @Autowired
    private ClubService clubService;
    
    @RequestMapping("clubListAll")
    public String mainPage(Model model,@RequestParam(value = "searchWord", required = false) String searchWord){
        List<Map<String,Object>> clubList =clubService.appearClubDto(searchWord);
        model.addAttribute("clubList", clubList);

        List<Map<String,Object>> recommandClubList =clubService.recommandClubRandom();
        model.addAttribute("recommandClubList", recommandClubList);
        // model.addAttribute("festivalUrl", Utils.FESTIVAL_URL);

        return "club/clubListAll";
      
    }

    @RequestMapping("categoryList")
    public String categoryList(@RequestParam("categoryId")int categoryId){
        
        return "club/clubListAll";
    }
    
    @RequestMapping("myClub")
    public String myClub(){
        
        return "club/myClub";
    }

    @RequestMapping("clubBoardRegist")
    public String clubBoardRegist(){
        return"club/clubBoardRegist";
    }

    @RequestMapping("clubDetailPage")
    public String clubDetailPage(HttpSession session,Model model,@RequestParam("id")int id){
        UserDto userDto = (UserDto)session.getAttribute("loginUser");

        if(userDto == null){
            return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
        } 
        else{
            ClubMemberRegistDto clubMember = clubService.memberExistRegist(id,userDto.getId());
            model.addAttribute("clubMember", clubMember);
            return"club/board/clubDetailPage";
    
        }
        
        
    }

    @RequestMapping("clubUserMemberRegist")
    public String clubUserMemberRegist(ClubMemberRegistDto clubMemberRegistDto){
        
        return "redirect:/club/clubDetailPage";
    }

    @RequestMapping("clubRegistPage")
    public String clubRegistPage(Model model){

        List<ClubCategoryDto> clubCategoryList = clubService.clubCategoryList();
        model.addAttribute("clubCategoryList", clubCategoryList);

        return"club/clubRegistPage";
    }

    @RequestMapping("registProcess")
    public String registProcess(HttpSession session,ClubDto clubDto,@RequestParam(value="uploadFiles", required = false) MultipartFile uploadFiles){

        String clubLogoImg = ImageUtil.saveImageAndReturnLocation(uploadFiles);
        clubDto.setClub_logo(clubLogoImg);
        UserDto userDto = (UserDto)session.getAttribute("loginUser");
        clubDto.setUser_id(userDto.getId());

        clubService.registClub(clubDto);



        return"redirect:/club/mainPage";
    }

    @RequestMapping("registClubUserProcess")
    public String registClubUserProcess(Model model,@RequestParam("id")int id,HttpSession session,ClubMemberRegistDto clubMemberRegistDto){
        
        UserDto userDto = (UserDto)session.getAttribute("loginUser");
        clubMemberRegistDto.setUser_id(userDto.getId());
        clubMemberRegistDto.setClub_id(id);
        clubService.clubMemberRegist(clubMemberRegistDto);


        return"redirect:/club/clubDetailPage?id="+id;    
    }
    
    @RequestMapping("registClubMember")
    public String registClubMember(Model model,@RequestParam("user_id")int user_id,@RequestParam("club_id")int club_id){
        ClubDto clubDto = clubService.clubInfo(club_id);
        model.addAttribute("clubDto", clubDto);
        model.addAttribute("club_id", club_id);
        return "club/registClubUser";
    }
    
    @RequestMapping("memberList")
    public String memberList (@RequestParam("id")int id){
        
        return "club/memberList";
    }

    @RequestMapping("boardWritePage")
    public String boardWritePage (Model model,@RequestParam("id")int id){
        List<BoardCategoryDto> boardCategoryList = clubService.boardCategoryList();
        model.addAttribute("boardCategoryList", boardCategoryList);
        
        return "club/boardWritePage";
    }
// 자유게시판  
    @RequestMapping("detail/home")
    public String homePage(Model model,@RequestParam("id")int id){

        return "club/board/clubDetailPage";

    }

    @RequestMapping("detail/freeboard")
    public String freeBoardPage(Model model,@RequestParam("id")int id){

        return "club/board/freeboard";
    }

    @RequestMapping("detail/photo")
    public String photoPage(Model model,@RequestParam("id")int id){
        
        return "club/board/photo";

    }

    @RequestMapping("detail/vote")
    public String votePage(Model model,@RequestParam("id")int id){

        return "club/board/vote";

    }

    @RequestMapping("detail/schedule")
    public String schedulePage(Model model,@RequestParam("id")int id){

        return "club/board/schedule";

    }
    
    @RequestMapping("registSchedule")
    public String scheduleRegistPage(@RequestParam("id")int id){

        return "club/registSchedule";

    }



    @RequestMapping("board/detail")
    public String boardIdDetailPage(Model model,@RequestParam("id")int id){

        return "club/board/detailPage";

    }

    @RequestMapping("memberShowList")
    public String memberShowList(Model model,@RequestParam("id")int id){
        
        return "club/memberShowList";

    }

    @RequestMapping("update/content")
    public String updateContent(Model model,@RequestParam("id")int id,@RequestParam("club_id")int club_id){
        Map<String,Object> categoryInfo = clubService.clubCategoryId(id,club_id);
        model.addAttribute("categoryInfo", categoryInfo);

        List<BoardCategoryDto> boardCategoryList = clubService.boardCategoryList();
        model.addAttribute("boardCategoryList", boardCategoryList);
        return "club/board/updateContent";

    }
    // 게시글 수정
    @RequestMapping("updateProcess")
    public String updateProcess(Model model,ClubBoardDto clubBoardDto){
        
        clubService.updateProcess(clubBoardDto);

        return "redirect:/club/board/detail?id="+clubBoardDto.getId()+"&clubId="+clubBoardDto.getClub_id();

    }

    @RequestMapping("delete/content")
    public String deleteContent(Model model,@RequestParam("id")int id,@RequestParam("club_id")int club_id){
        
        clubService.deleteProcess(id,club_id);

        return "redirect:/club/detail/freeboard?id="+club_id;

    }

    @RequestMapping("registPhotoBook")
    public String registPhotoBook(Model model,@RequestParam("id")int id){
        
        return "club/registPhoto";

    }

    @RequestMapping("updateClub")
    public String updateClub(Model model,@RequestParam("id")int id){
       
        return "club/updateClub";
    }

    @RequestMapping("clubUpdateProcess")
    public String clubUpdateProcess(ClubDto clubDto){
       
        clubService.updateClub(clubDto);
        return "redirect:/club/clubDetailPage?id="+clubDto.getId();
    }
    
    @RequestMapping("registVote")
    public String registVote(Model model,@RequestParam("id")int id){
        
        return "club/registVote";

    }

    @RequestMapping("voteDetailPage")
    public String voteDetailPage(ClubVoteOptionDto clubVoteOptionDto){
        
        return "club/board/voteDetailPage";

    }

    //일정 상세페이지 이동..
    @RequestMapping("schedule/detail")
    public String schedulDetailPage(@RequestParam("club_id")int club_id,@RequestParam("event_id")int event_id){
        
        return "club/board/schedulDetailPage";

    }

    @RequestMapping("myPage/board")
    public String boardMyPage(@RequestParam("id")int id){
    
        return "club/myPage/boardMyPage";

    }
        
    @RequestMapping("myPage/comment")
    public String commentMyPage(@RequestParam("id")int id){
    
        return "club/myPage/commentMyPage";

    }
    @RequestMapping("schedule/chat")
    public String scheduleChat(@RequestParam("schedule_id")int id,@RequestParam("club_id")int club_id){
    
        return "club/board/chat";

    }
}

