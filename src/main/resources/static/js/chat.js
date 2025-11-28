let stompClient = null;
let typingTimeout = null;

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        console.log("Connected to WebSocket!");

        // Receive chat messages
        stompClient.subscribe('/topic/public', function (msg) {
            const chat = JSON.parse(msg.body);
            if (!chat) return;
            displayMessage(chat);
        });

        // Receive typing indicator
        stompClient.subscribe('/topic/typing', function (typingMsg) {
            const typing = JSON.parse(typingMsg.body);
            updateTypingIndicator(typing);
        });
    });
}

// Display received messages
function displayMessage(chat) {
    const chatBox = document.getElementById('chatBox');

    const li = document.createElement('li');
    li.innerText = `[${chat.timeStamp}] ${chat.sender}: ${chat.content}`;
    chatBox.appendChild(li);

    // Auto-scroll down
    chatBox.scrollTop = chatBox.scrollHeight;
}

// Send actual chat message
function sendMessage() {
    const name = document.getElementById('name').value.trim();
    const content = document.getElementById('message').value.trim();

    if (!name || !content) return;

    stompClient.send("/app/chat.send", {}, JSON.stringify({
        sender: name,
        content: content
    }));

    document.getElementById('message').value = '';
}

// Send typing event
function sendTyping() {
    const name = document.getElementById('name').value.trim();
    if (!name || !stompClient || !stompClient.connected) return;

    // user is typing
    stompClient.send("/app/chat.typing", {}, JSON.stringify({
        sender: name,
        typing: true
    }));

    // Stop typing after 1.5 seconds of no key presses
    if (typingTimeout) clearTimeout(typingTimeout);
    typingTimeout = setTimeout(() => {
        stompClient.send("/app/chat.typing", {}, JSON.stringify({
            sender: name,
            typing: false
        }));
    }, 1500);
}

// Update UI for typing indicator
function updateTypingIndicator(typing) {
    const indicator = document.getElementById('typingIndicator');

    if (typing.typing) {
        indicator.innerText = `${typing.sender} is typing...`;
    } else {
        indicator.innerText = "";
    }
}

// Setup events after loading
window.addEventListener('load', () => {
    connect();

    document.getElementById('sendBtn').addEventListener('click', sendMessage);

    document.getElementById('message').addEventListener('keyup', (event) => {
        if (event.key === 'Enter') {
            sendMessage();
        }
        sendTyping();
    });
});