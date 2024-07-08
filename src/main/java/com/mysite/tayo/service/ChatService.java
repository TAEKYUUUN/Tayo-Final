package com.mysite.tayo.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.tayo.entity.Chat;
import com.mysite.tayo.entity.ChatContents;
import com.mysite.tayo.entity.ChatContentsHasFile;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.repository.ChatContentsHasFileRepository;
import com.mysite.tayo.repository.ChatContentsRepository;
import com.mysite.tayo.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {

	private final ChatRepository chatRepository;
	private final ChatContentsRepository chatContentsRepository;
	private final ChatContentsHasFileRepository chatContentsHasFileRepository;

	private static final String URL_REGEX = "((http|https)://)?(www\\.)?([\\w-]+)\\.+[\\w]{2,}(\\S*)?";

	public List<Chat> getList() {
		return this.chatRepository.findByChatMemberListMemberMemberIdx(1L);
	}

	public Optional<Chat> getId(Long chatIdx) {
		return this.chatRepository.findById(chatIdx);
	}
	
	public ChatContents addChatContent(Long chatIdx, String chatContents, Member member, Long replyIdx, MultipartFile file) throws IOException {
	    Optional<Chat> chat = chatRepository.findById(chatIdx);
	    if (chat.isEmpty()) {
	        throw new RuntimeException("Chat not found");
	    }

	    String convertedText = convertLinks(chatContents);
	    boolean hasLink = !convertedText.equals(chatContents);

	    ChatContents chatContent = new ChatContents();
	    chatContent.setChat(chat.get());
	    chatContent.setText(convertedText);
	    chatContent.setTime(new Date());
	    chatContent.setMember(member);
	    chatContent.setLink(hasLink);

	    if (replyIdx != null) {
	        Optional<ChatContents> replyContent = chatContentsRepository.findById(replyIdx);
	        if (replyContent.isPresent()) {
	            chatContent.setIsReply(replyContent.get());
	        }
	    }

	    if (file != null && !file.isEmpty()) {
	        ChatContentsHasFile chatContentsHasFile = new ChatContentsHasFile();
	        chatContentsHasFile.setFileName(file.getOriginalFilename());
	        chatContentsHasFile.setFileType(file.getContentType());
	        byte[] a = file.getBytes();
	        System.out.println(a.length/1024.0);
	        chatContentsHasFile.setData(file.getBytes());

	        chatContentsHasFileRepository.save(chatContentsHasFile);
	        chatContent.setHasFile(chatContentsHasFile);
	    }

	    return chatContentsRepository.save(chatContent);
	}

	
	public void addNotice(Long chatContentsIdx, Member member) { // chatContentsIdx = "공지 설정할 메세지의 idx", memberIdx = "공지로 설정하는 사람 idx"
		Optional<ChatContents> chatContents =  chatContentsRepository.findById(chatContentsIdx);
		if(chatContents.isPresent()) {
			ChatContents chatContent = chatContents.get();
			ChatContents noticeContent = new ChatContents();
			
			noticeContent.setChat(chatContent.getChat());
			noticeContent.setHasFile(chatContent.getHasFile());
			noticeContent.setIsReply(chatContent.getIsReply());
			noticeContent.setText(chatContent.getText());
			noticeContent.setTime(new Date());
			noticeContent.setMember(member);
			noticeContent.setLink(chatContent.isLink());
			
			noticeContent.setNotice(1);
			
			
			chatContentsRepository.save(noticeContent);
		}
	}
	
	
	private String convertLinks(String message) {
	    if (message == null) {
	        return "";
	    }

	    Pattern pattern = Pattern.compile(URL_REGEX);
	    Matcher matcher = pattern.matcher(message);
	    StringBuffer result = new StringBuffer();

	    while (matcher.find()) {
	        String url = matcher.group(0);
	        String hyperlink = "<a href='" + url + "' target='_blank'>" + url + "</a>";
	        matcher.appendReplacement(result, hyperlink);
	    }
	    matcher.appendTail(result);
	    return result.toString();
	}
}
