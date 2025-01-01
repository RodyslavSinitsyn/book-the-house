const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
    const chatId = document.getElementById("chatId").value;

    stompClient.subscribe(`/user/topic/chat/${chatId}`, (message) => {
        const msg = JSON.parse(message.body);
        console.log(msg)
        appendMessage(msg.senderId, msg.text);
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
    appendMessage(senderId, messageText)
});

function appendMessage(sender, messageText) {
    const chatMessages = document.getElementById("chat-messages");

    const newMessage = document.createElement("div");
    newMessage.className = "alert alert-secondary";
    newMessage.textContent = sender + ": " + messageText;

    chatMessages.appendChild(newMessage);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}