document.addEventListener('DOMContentLoaded', function() {
	// 이벤트 리스너를 초기화하는 함수
	function addEventListenersToButtons() {
		const modalMoreBtns = document.querySelectorAll('.modal-more-btn');
		const modalMores = document.querySelectorAll('.modal-more');
		const replyOpenBtns = document.querySelectorAll('.reply-open-btn');
		const chatContainer = document.querySelector('.chat-container');
		const replyIdxInput = document.getElementById('reply-idx');
		const chatReplyAlert = document.querySelector('.chat-reply-alert');
		const replyChatTxt = document.getElementById('reply-chat-txt');
		const replyFrom = document.getElementById('reply-from');
		const delOnlyForMeBtns = document.querySelectorAll('.delete-only-for-me');
		const delAllBtns = document.querySelectorAll('.delete-all');
		const delChatContentIdxStorage = document.getElementById('del-chat-content-idx-storage');
		const delAllChatContentIdxStorage = document.getElementById('del-all-chat-content-idx-storage');

		modalMoreBtns.forEach((btn, index) => {
			const container = modalMores[index];
			btn.addEventListener('click', () => {
				container.style.display = 'block';
			});
			container.addEventListener('mouseleave', () => {
				container.style.display = 'none';
			});
		});

		replyOpenBtns.forEach((btn) => {
			btn.addEventListener('click', (e) => {
				const chatContainerLastChild = chatContainer.lastElementChild;
				chatContainerLastChild.style.paddingBottom = '70px';
				scrollToBottom()
				const chatTxtWr = e.target.closest('.chat-txt-wr');
				if (chatTxtWr) {
					const userSection = e.target.closest('.user-section');
					const chatTxt = chatTxtWr.querySelector('.chat-txt');
					const fileTit = chatTxtWr.querySelector('.file-tit');
					const chatContentIdx = chatTxtWr.closest('li').querySelector('input').value;
					replyIdxInput.value = chatContentIdx;
					replyChatContentIdx = chatContentIdx;

					chatReplyAlert.style.display = 'block';
					chatReplyAlert.classList.add('on');
					if (chatTxt) {
						replyChatTxt.innerText = chatTxt.innerText;
					} else if (fileTit) {
						replyChatTxt.innerText = fileTit.innerText;
					}
					if (userSection) {
						const userName = userSection.querySelector('.user-title span strong');
						if (userName) {
							replyFrom.innerText = `${userName.innerText}에게 답장`;
						}
					} else {
						replyFrom.innerText = '나에게 답장';
					}
				}
			});
		});


		delOnlyForMeBtns.forEach((btn) => {

			btn.addEventListener('click', (e) => {
				const chatTxtWr = e.target.closest('.chat-txt-wr');
				const delChatContentIdx = chatTxtWr.closest('li').querySelector('input').value;
				delChatContentIdxStorage.innerText = delChatContentIdx;
				const chatLiParentNode = chatTxtWr.closest('.chat_li_parent_node');
				chatLiParentNode.style.display = 'none';
				send();
			});
		});
		delAllBtns.forEach((btn) => {
			btn.addEventListener('click', (e) => {
				const chatTxtWr = e.target.closest('.chat-txt-wr');
				const delAllChatContentIdx = chatTxtWr.closest('li').querySelector('input').value;
				delAllChatContentIdxStorage.innerText = delAllChatContentIdx;
				const chatLiParentNode = chatTxtWr.closest('.chat_li_parent_node');
				chatLiParentNode.style.display = 'none';
				send();
			});
		});

		// 이벤트 위임을 통해 동적 요소에도 이벤트 리스너 적용
		document.body.addEventListener('click', function(e) {
			if (e.target.classList.contains('copy')) {
				const chatTxtWr = e.target.closest('.chat-txt-wr');
				if (chatTxtWr) {
					const chatTxt = chatTxtWr.querySelector('.chat-txt');
					if (chatTxt) {
						const text = chatTxt.innerText;
						navigator.clipboard.writeText(text).then(() => {
							console.log('텍스트가 복사되었습니다.');
						}).catch((err) => {
							console.error('텍스트 복사 실패:', err);
						});
					} else {
						console.error('chat-txt 요소를 찾을 수 없습니다.');
					}
				} else {
					console.error('chat-txt-wr 요소를 찾을 수 없습니다.');
				}
			}
		});



		const btnCloseReply = document.querySelector('.btn-close-reply');
		btnCloseReply.addEventListener('click', () => {
			const lastChild = chatContainer.lastElementChild;
			lastChild.style.paddingBottom = '20px';
			replyReset();
		});
	}

	// DOM이 로드된 후 초기 이벤트 리스너 설정
	// 웹소켓 실행시 이벤트 핸들러가 사라져서 재 호출을 위해 달아 놓음
	addEventListenersToButtons();

	const noticebtn = document.querySelector('.small-notice-button');
	const noticeBox = document.querySelector('.notice-box');
	const noticeCloseBtns = document.querySelectorAll('.notice-box-close-btn');
	const noticeButtonQuit = document.querySelector('.button-quit');
	const modalMoreBtns = document.querySelectorAll('.modal-more-btn');
	const modalMores = document.querySelectorAll('.modal-more');
	const copyBtn = document.querySelectorAll('.copy');
	const menuOpenBtn = document.querySelector('.menu-open-btn');
	const chatMenu = document.querySelector('.chat-menu');
	const menuContainer = document.querySelector('.menu-container');
	const btnCloseReply = document.querySelector('.btn-close-reply');
	const chatReplyAlert = document.querySelector('.chat-reply-alert');
	const replyOpenBtn = document.querySelectorAll('.reply-open-btn');
	const replyChatTxt = document.getElementById('reply-chat-txt');
	const replyFrom = document.getElementById('reply-from');
	const chatContainer = document.querySelector('.chat-container');
	const iconFile = document.querySelectorAll('.icon-file');
	const replyIdxInput = document.getElementById('reply-idx');
	const delOnlyForMeBtns = document.querySelectorAll('.delete-only-for-me');
	const delAllBtns = document.querySelectorAll('.delete-all');
	const delChatContentIdxStorage = document.getElementById('del-chat-content-idx-storage');
	const delAllChatContentIdxStorage = document.getElementById('del-all-chat-content-idx-storage');
	const changeName = document.querySelector('.change-name');
	const changeNameSubmit = document.getElementById('change-name-submit');
	const changeNameInput = document.querySelector('.change-name-input');
	const inviteBtns = document.querySelectorAll('button[id^="invite-btn-"]');

	changeName.addEventListener('click', () => {
		const changeNameWrab = document.querySelector('.change-name-wrab');
		changeNameWrab.style.display = 'flex';
	});

	changeNameSubmit.addEventListener('click', (event) => {
		const inputValue = changeNameInput.value.trim();
		if (inputValue === "" || changeNameInput.value.startsWith(" ")) {
			event.preventDefault(); // 폼 제출을 막음
			alert("유효한 채팅방 이름을 입력해주세요."); // 사용자에게 알림
		}
	});


	let replyChatContentIdx;

	scrollToBottom();

	function replyReset() {
		chatReplyAlert.style.display = 'none';
		chatReplyAlert.classList.remove('on');
		replyIdxInput.value = ''; // 답장 인덱스 값 초기화
		replyChatContentIdx = undefined;
		const chatContainerLastChild = chatContainer.lastElementChild;
		if (chatContainerLastChild) {
			chatContainerLastChild.style.paddingBottom = '20px';
		} else {
			console.warn('chatContainerLastChild element is null');
		}
	}

	noticebtn.addEventListener('click', () => {
		noticeBox.style.display = 'block';
	});

	noticeCloseBtns.forEach((btn) => {
		btn.addEventListener('click', () => {
			noticeBox.style.display = 'none';
		});
	});

	noticeButtonQuit.addEventListener('click', () => {
		noticeBox.style.display = 'none';
		noticebtn.style.display = 'none';
	});

	modalMoreBtns.forEach((btn, index) => {
		const container = modalMores[index];
		btn.addEventListener('click', () => {
			container.style.display = 'block';
		});
		container.addEventListener('mouseleave', () => {
			container.style.display = 'none';
		});
	});

	menuOpenBtn.addEventListener('click', () => {
		chatMenu.style.display = 'block';
	});

	chatMenu.addEventListener('click', function(e) {
		const isClickInside = menuContainer.contains(e.target);
		if (!isClickInside) {
			chatMenu.style.display = 'none';
		}
	});

	document.querySelectorAll('.menu-btns li').forEach(function(item) {
		item.addEventListener('click', function() {
			window.open(
				'http://teamtayo.co.kr/chatCollection',
				'_blank',
				'width=640,height=800'
			);
		});
	});

	inviteBtns.forEach(btn => {
		btn.addEventListener('click', function() {
			const chatIdx = this.id.split('-')[2];
			const form = document.getElementById(`chat-invite-form-${chatIdx}`);
			const newWindow = window.open(
				'',
				`chatInviteWindow-${chatIdx}`,
				'width=650,height=680'
			);
			form.submit();
		});
	});

	iconFile.forEach((icon) => {
		const fileType = icon.innerText;
		if (fileType === 'application/haansofthwp') {
			icon.classList.add('icon-hwp');
		} else if (
			fileType ===
			'application/vnd.openxmlformats-officedocument.presentationml.presentation'
		) {
			icon.classList.add('icon-ppt');
		} else if (
			fileType ===
			'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
		) {
			icon.classList.add('icon-excel');
		} else if (fileType === 'application/pdf') {
			icon.classList.add('icon-pdf');
		} else if (fileType === 'application/x-zip-compressed') {
			icon.classList.add('icon-zip');
		} else {
			icon.classList.add('icon-etc');
		}
	});

	// chatContent에 대해 form 태그 생성 및 전송
	const chatContentsForm = document.getElementById('chat-contents-form');
	const createPostContent = document.querySelector('.create-post-content');
	const contentInputBtn = document.getElementById('content-input');
	const chatIdx = chatContentsForm.querySelector('input').value;
	// 웹소켓 설정
	const loginMember = document.getElementById('login-member').value;
	const userName = document.getElementById('senderName').innerText;
	const userIdx = document.getElementById('senderIdx').innerText;
	const chatMsg = document.querySelector('.chat-container');
	let replyIdx;
	let chatContent;

	const websocket = new WebSocket('ws://teamtayo.co.kr/ws/chat');

	// 주기적으로 상태 확인
	setInterval(() => {
		const displayStyle = window.getComputedStyle(chatReplyAlert).display;

		if (displayStyle === 'block') {
			replyIdx = replyChatContentIdx;
		} else {
			replyIdx = '//~//null//~//';
		}
	}, 100);

	function send() {

		const msg = document.querySelector('.create-post-content');
		chatContent = msg.innerText;

		var delChatContentIdx = delChatContentIdxStorage.innerText;
		var delAllChatContentIdx = delAllChatContentIdxStorage.innerText;

		// 채팅 나에게만 삭제 시 
		if (delChatContentIdx === '') {
			delChatContentIdx = '//~//null//~//';
		} else {
			chatContent = '//~//null//~//';
		}
		// 채팅 전체삭제 시
		if (delAllChatContentIdx === '') {
			delAllChatContentIdx = '//~//null//~//';
		} else {
			chatContent = '//~//null//~//';
		}
		const message = {
			userName: userName,
			chatIdx: chatIdx,
			content: chatContent,
			userIdx: userIdx,
			replyIdx: replyIdx,
			delChatContentIdx: delChatContentIdx,
			delAllChatContentIdx: delAllChatContentIdx
		};
		console.log(message);
		console.log(userName + ':' + msg.innerText);
		websocket.send(JSON.stringify(message));
		msg.innerText = '';
		delChatContentIdxStorage.innerText = '';
		delAllChatContentIdxStorage.innerText = '';
	}

	function onClose(evt) {
		console.log("websocket close")
	}

	function onOpen(evt) {

		console.log("websocket open")
		chatContent = "//~//null//~//";
		replyIdx = "//~//null//~//";
		var delChatContentIdx = '//~//null//~//';
		var delAllChatContentIdx = '//~//null//~//';
		const message = {
			userName: userName,
			chatIdx: chatIdx,
			content: chatContent,
			userIdx: userIdx,
			replyIdx: replyIdx,
			delChatContentIdx: delChatContentIdx,
			delAllChatContentIdx: delAllChatContentIdx
		};
		console.log(message);
		websocket.send(JSON.stringify(message));
	}

	function onMessage(evt) {
		const requestData = JSON.parse(evt.data);
		const { curUserIdx, curChatContent, chatContentTime, maxChatContentIdx, replyChatContent, replyMemberName, chatUnreaders } = requestData;
		console.log(curUserIdx, curChatContent, maxChatContentIdx, replyChatContent, replyMemberName);

		let replyContent;
		let replyUserName;

		if (replyChatContent !== null) {
			replyContent = replyChatContent;
			replyUserName = replyMemberName;
		}
		console.log(replyUserName, replyContent);
		try {
			const cur_session = loginMember;
			const sessionId = curUserIdx;
			const message = curChatContent;

			if (sessionId == cur_session && message !== '//~//null//~//'
				&& (replyChatContent === null)) {
				// 내가 보낸경우
				const str = `<div class='chat_li_parent_node'>
					          <li class='right-section'>
								<input type="hidden" value='${maxChatContentIdx}'>
					            <div>
					              <div class='chat-right clearfix'>
					                <div class='chat-txt-wr'>
					                  <div class='chat-btns'>
					                    <div>
					                      <div class='chat-btn-wrap'>
					                        <button class='reply-open-btn'>
					                          <img src='/img/chat-btn-left.png' alt='' />
					                        </button>
					                        <div class='medal-more-wrap'>
					                          <button class='modal-more-btn'>
					                            <img src='/img/chat-btn-right.png' alt='' />
					                          </button>
					                          <ul class='modal-more'>
					                            <li><button class='copy'>복사</button></li>
					                            <li><button class='notice'>공지설정</button></li>
					                            <li class='delete-only-for-me'><button class='del'>나에게서만 삭제</button></li>
					                            <li class='delete-all'><button class='del'>전체삭제</button></li>
					                          </ul>
					                        </div>
					                      </div>
					                      <div class='user-time'>${chatContentTime}</div>
					                    </div>
					                    <div class='chat-read' ${chatUnreaders === 0 ? "style='display:none;'" : ""}>${chatUnreaders}</div>
					                  </div>
					                  <div class='chat-txt'>
					                    <div>${message}</div>
					                  </div>
					                </div>
					              </div>
					            </div>
					          </li>
					          </div>`;
				chatMsg.innerHTML += str;
				scrollToBottom();

			} else if (sessionId == cur_session && message !== '//~//null//~//' && replyChatContent !== null) {
				// 내가 보낸경우 + 답장인경우
				str = `						
						<li class='right-section'>
							<input type="hidden" value='${maxChatContentIdx}'>
						  <div>
						    <div class='chat-right clearfix'>
						      <div class='chat-txt-wr'>
						        <div class='chat-btns'>
						          <div>
						            <div class='chat-btn-wrap'>
						              <button class='reply-open-btn'>
						                <img src='/img/chat-btn-left.png' alt='' />
						              </button>
						              <div class='medal-more-wrap'>
						                <button class='modal-more-btn'>
						                  <img src='/img/chat-btn-right.png' alt='' />
						                </button>
						                <ul class='modal-more'>
						                  <li>
						                    <button class='copy'>복사</button>
						                  </li>
						                  <li class='notice'>
						                    <button>공지설정</button>
						                  </li>
						                  <li class='delete-only-for-me'>
						                    <button class='del'>나에게서만 삭제</button>
						                  </li>
						                  <li class='delete-all'>
						                    <button class='del'>전체삭제</button>
						                  </li>
						                </ul>
						              </div>
						            </div>
						            <div class='user-time'>${chatContentTime}</div>
						          </div>
						          <div class='chat-read' ${chatUnreaders === 0 ? "style='display:none;'" : ""}>${chatUnreaders}</div>
						          <div class='chat-read-none'></div>
						        </div>
						        <div class='reply-box'>
						          <div class='chat-txt-reply'>
						            <strong>${replyUserName}님에게 답장</strong>
						            <p>${replyContent}</p>
						          </div>
						          <div class='chat-txt'> 
						            <div>${message}</div>
						          </div>
						        </div>
						      </div>
						    </div>
						  </div>
						</li>`
				chatMsg.innerHTML += str;
				scrollToBottom();
			} else if (sessionId != cur_session && message !== '//~//null//~//'
				&& (replyChatContent === null)) {
				// 상대가 보낸경우
				const str = `<li class='left-section'>
							<input type="hidden" value='${maxChatContentIdx}'>
						  <div class='user-picture'>
						    <img src='/img/chat_profile.png' alt='' />
						  </div>
						  <div class='user-section'>
						    <div class='user-title'>
						      <span>
						        <strong>${userName}</strong>
						      </span>
						    </div>
						    <div class='chat-left clearfix'>
						      <div class='chat-txt-wr'>
						        <div class='chat-txt'>
						          <div>${message}</div>
						        </div>
						        <div class='chat-btns'>
						          <div class='chat-read' ${chatUnreaders === 0 ? "style='display:none;'" : ""}>${chatUnreaders}</div>
						          <div class='chat-read-none'></div>
						          <div>
						            <div class='chat-btn-wrap'>
						              <button class='reply-open-btn'>
						                <img src='/img/chat-btn-left.png' alt='' />
						              </button>
						              <div class='medal-more-wrap'>
						                <button class='modal-more-btn'>
						                  <img src='/img/chat-btn-right.png' alt='' />
						                </button>
						                <ul class='modal-more'>
						                  <li>
						                    <button class='copy'>복사</button>
						                  </li>
						                  <li class='notice'>
						                    <button type='submit'>공지설정</button>
						                  </li>
						                  <li class='delete-only-for-me'>
						                    <button class='del'>나에게서만 삭제</button>
						                  </li>
						                  <li class='delete-all'>
						                    <button class='del'>전체삭제</button>
						                  </li>
						                </ul>
						              </div>
						            </div>
						            <div class='user-time'>${chatContentTime}</div>
						          </div>
						        </div>
						      </div>
						    </div>
						  </div>
						</li>`;
				chatMsg.innerHTML += str;
				scrollToBottom();
			} else if (sessionId != cur_session && message !== '//~//null//~//' && replyChatContent !== null) {
				// 상대가 보낸경우 + 답장인 경우
				const str = `<li class='left-section'>
							<input type="hidden" value='${maxChatContentIdx}'>
						  <div class='user-picture'>
						    <img src='/img/chat_profile.png' alt='' />
						  </div>
						  <div class='user-section'>
						    <div class='user-title'>
						      <span>
						        <strong>${userName}</strong>
						      </span>
						    </div>
						    <div class='chat-left clearfix'>
						      <div class='chat-txt-wr'>
						        <div class='reply-box'>
						          <div class='chat-txt-reply'>
									<strong>${replyUserName}님에게 답장</strong>
						            <p>${replyContent}</p>
						          </div>
						          <div class='chat-txt'>
						            <div>${message}</div>
						          </div>
						        </div>
						        <div class='chat-btns'>
						          <div class='chat-read' ${chatUnreaders === 0 ? "style='display:none;'" : ""}>${chatUnreaders}</div>
						          <div class='chat-read-none'></div>

						          <div>
						            <div class='chat-btn-wrap'>
						              <button class='reply-open-btn'>
						                <img src='/img/chat-btn-left.png' alt='' />
						              </button>
						              <div class='medal-more-wrap'>
						                <button class='modal-more-btn'>
						                  <img src='/img/chat-btn-right.png' alt='' />
						                </button>
						                <ul class='modal-more'>
						                  <li>
						                    <button class='copy'>복사</button>
						                  </li>
						                  <li class='notice'>
						                    <button type='submit'>공지설정</button>
						                  </li>
						                  <li class='delete-only-for-me'>
						                    <button class='del'>나에게서만 삭제</button>
						                  </li>
						                  <li class='delete-all'>
						                    <button class='del'>전체삭제</button>
						                  </li>
						                </ul>
						              </div>
						            </div>
						            <div class='user-time'>${chatContentTime}</div>
						          </div>
						        </div>
						      </div>
						    </div>
						  </div>
						</li>`;
				chatMsg.innerHTML += str;
				scrollToBottom();
			}
			addEventListenersToButtons();
		} catch (error) {
			console.error('onMessage error:', error);
		}
	}


	websocket.onmessage = onMessage;
	websocket.onopen = onOpen;
	websocket.onclose = onClose;

	// 새로운 form 요소 생성 및 설정
	/*			const form = document.createElement('form');
				form.method = 'post';
				form.action = '/chatRoom/' + chatIdx;
	
				const hiddenInput = document.createElement('input');
				hiddenInput.type = 'hidden';
				hiddenInput.name = 'chatContents';
				hiddenInput.id = 'hidden-content-input';
	
				while (chatContentsForm.firstChild) {
					form.appendChild(chatContentsForm.firstChild);
				}
	
				form.appendChild(hiddenInput);
				chatContentsForm.parentNode.replaceChild(form, chatContentsForm);
				form.classList.add('write-wr');*/
	function scrollToBottom() {
		chatContainer.scrollTop = chatContainer.scrollHeight;
	}


	contentInputBtn.addEventListener('click', function(event) {
		event.preventDefault();
		let chatContentMsg = createPostContent.innerText.trim();
		if (chatContentMsg === '') {
			alert('메시지를 입력하세요.');
			return;
		}
		replyReset()
		send();
	});


	createPostContent.addEventListener('keydown', function(event) {
		if (event.key === 'Enter' && !event.shiftKey) {
			event.preventDefault();
			let chatContentMsg = createPostContent.innerText.trim();
			if (chatContentMsg === '') {
				alert('메시지를 입력하세요.');
				return;
			}
			replyReset()
			send();
		}
	});

	const fileInput = document.getElementById('file-input');
	const fileUploadForm = document.getElementById('file-upload-form');
	const fileButton = document.querySelector('.btn-files');

	fileButton.addEventListener('click', function() {
		fileInput.click();
	});

	fileInput.addEventListener('change', function() {
		fileUploadForm.submit();
		scrollToBottom();
	});

});
