const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
    const chatId = document.getElementById("chatId").value;

    stompClient.subscribe(`/user/topic/chat/${chatId}`, (message) => {
        const msg = JSON.parse(message.body);
        console.log('Message received: ', msg)
        fetch(`/chat/read`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                [csrfHeader()]: csrfToken()
            },
            body: `chatId=${chatId}&messageId=${msg.messageId}`
        })
            .then(result => result.text())
            .then(value => console.log(value))
        appendMessage(msg.text, false);
    });
});

document.getElementById("chat-form").addEventListener("submit", (e) => {
    e.preventDefault();

    const chatId = document.getElementById("chatId").value;
    const recipientId = document.getElementById("recipientId").value;
    const senderId = document.getElementById("senderId").value;
    const messageText = document.getElementById("message").value;

    const messageBody = {
        chatId: chatId,
        recipientId: recipientId,
        text: messageText,
        timestamp: Date.now()
    }
    stompClient.send(`/app/chat`, {}, JSON.stringify(messageBody));
    document.getElementById("message").value = '';
    appendMessage(messageText, true)
});

document.querySelector("textarea#message").addEventListener("keypress", (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
        e.preventDefault();
        document.getElementById("chat-form").dispatchEvent(new Event("submit"));
    }
});

function appendMessage(text, isSender) {
    const chatMessages = document.getElementById("chat-messages");

    // Create message element
    const messageDiv = document.createElement("div");
    messageDiv.classList.add("mb-2", isSender ? "text-end" : "text-start");

    const messageContent = document.createElement("div");
    messageContent.classList.add("alert", "d-inline-block", isSender ? "alert-primary" : "alert-secondary");
    messageContent.style.maxWidth = "70%";
    messageContent.textContent = `${text}`;

    messageDiv.appendChild(messageContent);
    chatMessages.appendChild(messageDiv);

    // Scroll to the bottom of the chat
    chatMessages.scrollTop = chatMessages.scrollHeight;
}