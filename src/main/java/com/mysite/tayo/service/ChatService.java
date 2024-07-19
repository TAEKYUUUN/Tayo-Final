package com.mysite.tayo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.tayo.entity.Chat;
import com.mysite.tayo.entity.ChatContents;
import com.mysite.tayo.entity.ChatContentsHasFile;
import com.mysite.tayo.entity.ChatDeleteOnlyForMe;
import com.mysite.tayo.entity.ChatUnreader;
import com.mysite.tayo.entity.Member;
import com.mysite.tayo.repository.ChatContentsHasFileRepository;
import com.mysite.tayo.repository.ChatContentsRepository;
import com.mysite.tayo.repository.ChatDeleteOnlyForMeRepository;
import com.mysite.tayo.repository.ChatMemberRepository;
import com.mysite.tayo.repository.ChatRepository;
import com.mysite.tayo.repository.ChatUnreaderRepository;
import com.mysite.tayo.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {

	private final MemberRepository memberRepository;
	private final ChatRepository chatRepository;
	private final ChatContentsRepository chatContentsRepository;
	private final ChatContentsHasFileRepository chatContentsHasFileRepository;
	private final ChatDeleteOnlyForMeRepository chatDeleteOnlyForMeRepository;
	private final ChatMemberRepository chatMemberRepository;
	private final ChatUnreaderRepository chatUnreaderRepository;

	private static final String URL_REGEX = "((http|https)://)?(www\\.)?([\\w-]+)\\.+[\\w]{2,}(\\S*)?";

//	 로그인한 사람의 채팅방 리스트
	public List<Chat> getList(Member member) {
		return this.chatRepository.findByChatMemberListMemberMemberIdx(member.getMemberIdx());
	}

//	현재 들어온 채팅방 고유번호
	public Optional<Chat> getId(Long chatIdx) {
		return this.chatRepository.findById(chatIdx);
	}

//	보낸 사람의 가장 최근에 보낸 채팅내용 고유번호
	public Long maxChatContentIdx(Long userIdx, Long chatIdx) {
		return chatContentsRepository.findMaxChatContentIdxByUserIdx(userIdx, chatIdx);
	}

//	채팅내용 고유번호에대한 채팅내용 (링크여부 판별 후 링크를 띄워주기 위함)
	public String curChatContent(Long chatContentIdx) {
		return chatContentsRepository.findChatContentByChatContentIdx(chatContentIdx);
	}

//	채팅내용 고유번호에대한 채팅 보낸시간
	public String chatContentTime(Long chatContentIdx) {
		return chatContentsRepository.findChatContentTimeByChatContentIdx(chatContentIdx);
	}

//	답장할 채팅내용
	public String replyChatContent(Long replyIdx) {
		return chatContentsRepository.findChatContentByReplyIdx(replyIdx);
	}

//	누구한테 답장할지 
	public String replyMemberName(Long replyIdx) {
		return chatContentsRepository.findMemberNameByReplyIdx(replyIdx);
	}

//	가장 최근 공지사항
	public Long maxNotice(Long chatIdx) {
		return chatContentsRepository.findMaxNoticeChatContentIdxByChatContentIdx(chatIdx);
	}

//	나에게만 삭제 된 채팅내용
	public ArrayList<Long> chatDeleteOnlyForMeChatContentIdx(Member member) {
		return chatDeleteOnlyForMeRepository.findChatContentsIdxByMember(member);
	}

//	채팅방에 포함된 사람들
	public ArrayList<Long> chatMemberList(Long chatIdx, Long memberIdx) {
		return chatMemberRepository.findMemberByChatIdx(chatIdx, memberIdx);
	}

//	채팅방 안에 채팅내용 리스트
	public ArrayList<Long> chatContentsList(Long chatIdx) {
		return chatContentsRepository.findChatContentsListIdxByChatIdx(chatIdx);
	}

//	채팅 입력시 안읽은사람 추가
	public void addChatUnreader(Long chatContentsIdx, Long memberIdx) {
		Optional<Member> member = memberRepository.findById(memberIdx);
		Optional<ChatContents> chatContents = chatContentsRepository.findById(chatContentsIdx);
		Optional<ChatUnreader> existingUnreader = chatUnreaderRepository.findByChatContentsAndMember(chatContents,
				member);
		if (existingUnreader.isEmpty()) {
			ChatUnreader chatUnreader = new ChatUnreader();
			chatUnreader.setMember(member.get());
			chatUnreader.setChatContents(chatContents.get());

			chatUnreaderRepository.save(chatUnreader);
		}
	}
	
	@Transactional
	public void chatDeleteAll(Long chatContentsIdx) {
		chatContentsRepository.deleteAllByChatContentsIdx(chatContentsIdx);
	}

//	채팅방 입장 시 안읽은 사람에서 제거
	@Transactional
	public void deleteChatUnreaders(Long chatContentsIdx, Member member) {
		Optional<ChatContents> chatContents = chatContentsRepository.findById(chatContentsIdx);

		chatUnreaderRepository.deleteAllByChatContentsAndMember(chatContents, member);
	}

//	채팅 내용 마다 안읽은 사람 수
	public Integer ChatUnreaderCount(Long chatContentsIdx) {
		return chatUnreaderRepository.findChatUnreaderCountByChatContentsIdx(chatContentsIdx);
	}

//	채팅 나에게만 삭제
	public void chatDeleteOnlyForMe(Long chatContentsIdx, Long memberIdx) {
		Optional<ChatContents> chatContents = chatContentsRepository.findById(chatContentsIdx);
		Optional<Member> member = memberRepository.findById(memberIdx);

		ChatDeleteOnlyForMe chatDelete = new ChatDeleteOnlyForMe();
		chatDelete.setChatContents(chatContents.get());
		chatDelete.setMember(member.get());

		chatDeleteOnlyForMeRepository.save(chatDelete);
	}

//	chatContents 엔티티에 저장할 데이터들
	@Value("${file.upload-dir}")
	private String uploadDir;

	public ChatContents addChatContent(Long chatIdx, String chatContents, Member member, Long replyIdx,
			MultipartFile file) throws IOException {
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
			String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
			String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
			Path filePath = Paths.get(uploadDir, fileName);

			// Ensure the directory exists
			Files.createDirectories(filePath.getParent());

			// Copy the file to the target location (Replacing existing file with the same
			// name)
			Files.copy(file.getInputStream(), filePath);

			// Create and save the file entity
			ChatContentsHasFile chatContentsHasFile = new ChatContentsHasFile();
			chatContentsHasFile.setFileName(fileName);
			chatContentsHasFile.setFileType(file.getContentType());
			byte[] a = file.getBytes();
			System.out.println(a.length / 1024.0);
			chatContentsHasFile.setData(file.getBytes());
			chatContentsHasFileRepository.save(chatContentsHasFile);
			chatContent.setHasFile(chatContentsHasFile);
		}

		return chatContentsRepository.save(chatContent);
	}

	public void addNotice(Long chatContentsIdx, Member member) { // chatContentsIdx = "공지 설정할 메세지의 idx", memberIdx =
																	// "공지로 설정하는 사람 idx"
		Optional<ChatContents> chatContents = chatContentsRepository.findById(chatContentsIdx);
		if (chatContents.isPresent()) {
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
