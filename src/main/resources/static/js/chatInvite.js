document.addEventListener('DOMContentLoaded', function() {
    const nothingMemberButton = document.querySelector('.nothing_btn');
    const inviteModal = document.getElementById("invite-participants-modal");
    const closeInviteButton = document.querySelector('.invite-modal-header .close');
    const inviteModalBtn = document.querySelector('.invite-modal-footer .invite_submit_btn');

    document.querySelectorAll('#employeeList li').forEach(item => {
        item.addEventListener('click', event => {
            const id = item.getAttribute('data-id');
            const name = item.getAttribute('data-name');
            toggleInviteSelectedList(id, name);
        });
    });

    function toggleInviteSelectedList(id, name) {
        try {
            const selectedList = document.getElementById('selectedList');
            const existingItem = document.querySelector(`#selectedList li[data-id="${id}"]`);
            const itemToUpdate = document.querySelector(`#employeeList li[data-id="${id}"] .check_invite img`);

            if (!selectedList) {
                throw new Error("selectedList 요소를 찾을 수 없습니다.");
            }

            if (!itemToUpdate) {
                throw new Error(`itemToUpdate 요소를 찾을 수 없습니다. data-id=${id}`);
            }

            if (existingItem) {
                existingItem.remove();
                itemToUpdate.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_check.png?v=7f39425e224a53bff0043caff9f6446b14c0f667';
            } else {
                const li = document.createElement('li');
                li.setAttribute('data-id', id);
                li.classList.add('selected_member');
                li.innerHTML = `<img src="https://flow.team/flow-renewal/assets/images/profile-default.png"/>
                                <strong style="margin-left:2px;">${name}</strong>
                                <button class="remove-btn" data-id="${id}" style="position:absolute; right:10px; color:#000;">X</button>`;

                selectedList.appendChild(li);

                li.querySelector('.remove-btn').addEventListener('click', event => {
                    removeFromSelectedList(id);
                });

                itemToUpdate.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_check_on.png?v=8a10086b9d33ff65ead56b67a69de154fcbe2c4a';
            }
        } catch (error) {
            console.error(error.message);
        }
    }

    function removeFromSelectedList(id) {
        const item = document.querySelector(`#selectedList li[data-id="${id}"]`);
        if (item) {
            item.remove();
        }

        const itemToUpdate = document.querySelector(`#employeeList li[data-id="${id}"] .check_invite img`);
        if (itemToUpdate) {
            itemToUpdate.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_check.png?v=7f39425e224a53bff0043caff9f6446b14c0f667';
        }
    }

    document.querySelectorAll('#employeeList li').forEach(item => {
        item.addEventListener('mouseover', event => {
            const img = item.querySelector('.check_invite img');
            const id = item.getAttribute('data-id');
            const selectedItem = document.querySelector(`#selectedList li[data-id="${id}"]`);
            if (img && !selectedItem) {
                img.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_check_hover.png?v=5244c8bcd5a25963296eb4e51fc2a65bcc3dc376';
            }
        });
        item.addEventListener('mouseout', event => {
            const img = item.querySelector('.check_invite img');
            const id = item.getAttribute('data-id');
            const selectedItem = document.querySelector(`#selectedList li[data-id="${id}"]`);
            if (img && !selectedItem) {
                img.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_check.png?v=7f39425e224a53bff0043caff9f6446b14c0f667';
            }
        });
    });

    function inviteParticipants(event) {
        event.preventDefault();

        const employeeList = document.getElementById('employeeList');
        const pushMyId = employeeList.getAttribute('push-my-id');
        const selectedList = document.getElementById('selectedList');
        if (!selectedList) {
            console.error('selectedList 요소를 찾을 수 없습니다.');
            return;
        }

        const members = [pushMyId];
        selectedList.querySelectorAll('li').forEach(item => {
            members.push(Number(item.getAttribute('data-id')));
        });

        const chatIdx = Number(window.location.pathname.split('/').pop());

        const requestData = {
            chatIdx: chatIdx,
            members: members
        };

        console.log('Sending data:', requestData);

        fetch('/addChatMember', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestData) // JSON 객체로 보냄
        })
        .then(response => {
            if (response.ok) {
                return response.text(); // JSON 대신 텍스트로 응답 받기
            }
            throw new Error('Network response was not ok.');
        })
        .then(data => {
            console.log('Success:', data);
            window.opener.location.reload(); // 기존 창 리프레시
            window.close(); // 현재 창 닫기
        })
        .catch((error) => {
            console.error('Error:', error);
        });
    }

    document.querySelector('.invite_submit_btn').addEventListener('click', inviteParticipants);
});
