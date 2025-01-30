package com.sixthband.festival.funding.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

@Service
public class OpenAiGptService {

    // GPT : 요약 처리
    public String summarizeContent(String review) {
        
        // 서비스 생성
        String openaiAccessKey = "SECRET_KEY"; // OPEN AI KEY
        OpenAiService service = new OpenAiService(openaiAccessKey, Duration.ofSeconds(30));
       
        // ChatGpt에게 보내 내용 세팅
        List<ChatMessage> messages = new ArrayList<>();

        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setRole("system"); // system : gpt 에게 물어보기 전 제한을 큰 틀로 부여한 것
        systemMessage.setContent("당신은 내가 제시하는 리뷰를 요약해줘야합니다. 최대한 짧게 요약을 해야 합니다. 절대로 20글자를 넘어서서는 안됩니다.");
        messages.add(systemMessage);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRole("user"); // user : gpt 한테 review 와 함께 명령어를 해줘서 원하는 결과를 얻게 해줌
        chatMessage.setContent("다음 글을 요약해서 아주 아주 짧은 소감 한 줄 만들어줘! : " + review);
        messages.add(chatMessage);
   
        // 요청
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
            .model("gpt-4o-mini")
            .messages(messages)
            .build();

        // 응답 결과
        String result = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent();
        System.out.println("GPT 요약 결과: " + result);

        return result;
    }
}
