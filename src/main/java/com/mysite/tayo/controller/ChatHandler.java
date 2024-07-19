package com.mysite.tayo.controller;

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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysite.tayo.entity.Chat;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.repository.MemberRepository;
import com.mysite.tayo.service.ChatService;

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
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.info("payload : " + payload);
		ObjectMapper objectMapper = new ObjectMapper();
		// JSON 파싱
		JsonNode jsonNode = objectMapper.readTree(payload);

		Long chatIdx = Long.parseLong(jsonNode.get("chatIdx").asText());
		String chatContent = jsonNode.get("content").asText();
		Long userIdx = Long.parseLong(jsonNode.get("userIdx").asText());
		String strReplyIdx = jsonNode.get("replyIdx").asText();
		Optional<Member> member = memberRepository.findById(userIdx);
		String strDelChatContentIdx = jsonNode.get("delChatContentIdx").asText();
		String strDelAllChatContentIdx = jsonNode.get("delAllChatContentIdx").asText();

		for (int i = 0; i < chatService.chatContentsList(chatIdx).size(); i++) {
			Long chatContentsIdx = chatService.chatContentsList(chatIdx).get(i);
			chatService.deleteChatUnreaders(chatContentsIdx, member.get());
		}
		Long replyIdx = null;
		if (!(strReplyIdx.equals("//~//null//~//"))) {
			replyIdx = Long.parseLong(strReplyIdx);
		}

			if (!chatContent.equals("//~//null//~//")) {
				chatService.addChatContent(chatIdx, chatContent, member.get(), replyIdx, null);

				Long maxChatContentIdx = chatService.maxChatContentIdx(userIdx, chatIdx);
				String curChatContent = chatService.curChatContent(maxChatContentIdx);
				String chatContentTime = chatService.chatContentTime(maxChatContentIdx);
				String replyChatContent = chatService.replyChatContent(replyIdx);
				String replyMemberName = chatService.replyMemberName(replyIdx);

				ArrayList<Long> chatMemberList = chatService.chatMemberList(chatIdx, userIdx);
				for (int i = 0; i < chatMemberList.size(); i++) {
					chatService.addChatUnreader(maxChatContentIdx, chatMemberList.get(i));
				}

				Integer chatUnreaders = chatService.ChatUnreaderCount(maxChatContentIdx);

				ObjectNode responseNode = objectMapper.createObjectNode();
				responseNode.put("curChatIdx", chatIdx);
				responseNode.put("curUserIdx", userIdx);
				responseNode.put("chatContentTime", chatContentTime);
				responseNode.put("curChatContent", curChatContent);
				responseNode.put("replyChatContent", replyChatContent);
				responseNode.put("replyMemberName", replyMemberName);
				responseNode.put("maxChatContentIdx", maxChatContentIdx);
				responseNode.put("chatUnreaders", chatUnreaders);

				String responseMessage = objectMapper.writeValueAsString(responseNode);
				System.out.println(responseMessage);

				// 연결된 모든 세션에 메시지 전송
				for (WebSocketSession sess : list) {
					sess.sendMessage(new TextMessage(responseMessage));
				}
			}

			if (!strDelChatContentIdx.equals("//~//null//~//")) {
				Long delChatContentIdx = Long.parseLong(strDelChatContentIdx);
				chatService.chatDeleteOnlyForMe(delChatContentIdx, userIdx);
			}
			
			if (!strDelAllChatContentIdx.equals("//~//null//~//")) {
				Long delAllChatContentIdx = Long.parseLong(strDelAllChatContentIdx);
				chatService.chatDeleteAll(delAllChatContentIdx);
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