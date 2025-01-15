package com.solution.calc.utils;

import com.solution.calc.api.money.dto.CalculateBotRequestDto;
import com.solution.calc.api.money.dto.DepositAlarmBotRequestDto;
import com.solution.calc.api.money.dto.DepositBotRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BotHttpUtils {
    private final RestTemplate restTemplate;


    public HttpStatusCode sendDepositRequest(DepositBotRequestDto requestDto) {
        // 요청 URL
        String url = "http://124.156.211.104:18989/bot/calc";


        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 바디와 헤더를 포함한 HTTP 엔티티 생성
        HttpEntity<DepositBotRequestDto> entity = new HttpEntity<>(requestDto, headers);

        // POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 응답 반환
        return response.getStatusCode();
    }

    public HttpStatusCode sendDepositAlarm(DepositAlarmBotRequestDto requestDto) {
        // 요청 URL
        String url = "http://124.156.211.104:18989/bot/deposit";



        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 엔티티 생성
        HttpEntity<DepositAlarmBotRequestDto> entity = new HttpEntity<>(requestDto, headers);

        // POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 응답 반환
        return response.getStatusCode();
    }

    public HttpStatusCode sendCalculateAlarm(List<CalculateBotRequestDto> requestDto) {
        // 요청 URL
        String url = "http://124.156.211.104:18989/bot/calculate";



        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 엔티티 생성
        HttpEntity<List<CalculateBotRequestDto>> entity = new HttpEntity<>(requestDto, headers);

        // POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 응답 반환
        return response.getStatusCode();
    }
}
