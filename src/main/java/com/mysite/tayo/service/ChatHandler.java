package com.mysite.tayo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.repository.MemberRepository;

import lombok.extern.log4j.Log4j2;


@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler {
	private static List<WebSocketSession> list = new ArrayList<>();
    @Autowired
    private ChatService chatService;
    @Autowired
    private MemberRepository memberRepository;
    
    
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message ) throws Exception {
        String payload = message.getPayload();
        log.info("payload : " + payload);
//        ObjectMapper objectMapper = new ObjectMapper();
//        // JSON 파싱
//        JsonNode jsonNode = objectMapper.readTree(payload);
//        
//        String username = jsonNode.get("username").asText();
//        Long chatIdx = jsonNode.get("chatIdx").asLong();
//        String chatContent = jsonNode.get("chatContent").asText();
//        Long member = jsonNode.get("userIdx").asLong();
        
        
        
//        chatService.addChatContent(chatIdx, chatContent, , null, null);

        // 연결된 모든 세션에 메시지 전송
        for(WebSocketSession sess: list) {
            sess.sendMessage(message);
        }
    }
    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        list.add(session);
        log.info(session + " 클라이언트 접속");
    }
    /* Client가 접속 해제 시 호출되는 메서드드 */

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info(session + " 클라이언트 접속 해제");
        list.remove(session);
    }
}