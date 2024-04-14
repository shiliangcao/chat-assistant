    var stompClient = null;
    var orderId = null;
    var identity = null;

    function connect() {
        orderId = document.getElementById('orderId').value;
        identity = document.getElementById('identity').value;
        if(orderId && identity) {
            var socket = new SockJS('/orderChat');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                document.getElementById('login').style.display = 'none';
                document.getElementById('chat').style.display = 'block';
                document.getElementById('displayOrderId').innerText = 'Order ID: ' + orderId;
                document.getElementById('displayIdentity').innerText = 'Identity: ' + identity;
                stompClient.subscribe('/topic/orders/' + orderId, function (chatMessage) {
                    showChatMessage(JSON.parse(chatMessage.body));
                }, {'identity': identity});
            });
        } else {
            alert("Order ID and Identity are required");
        }
    }

    function sendMessageWithContent(messageContent) {
        if(messageContent && stompClient) {
            var chatMessage = {
                orderId: orderId,
                from: identity,
                text: messageContent,
            };
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            document.getElementById('message').value = '';
        }
    }

    function sendMessage() {
        var messageContent = document.getElementById('message').value;
        sendMessageWithContent(messageContent)
    }

    function showChatMessage(message) {
        var messageElement = document.createElement('li');
        var messageContent = document.createElement('div');
        messageContent.className = 'message-content';

        // 添加头像
        var avatar = document.createElement('img');
        avatar.className = 'avatar';
        avatar.src = '/img/customer.png';  // 这里使用默认头像，你可以根据不同的用户设置不同的头像
        messageContent.appendChild(avatar);

        // 消息文本
        var text = document.createElement('div');
        text.className = 'message-text';
        text.textContent = message.from + ": " + message.text;
        messageContent.appendChild(text);

        // 时间戳
        var timestamp = document.createElement('span');
        timestamp.className = 'timestamp';
        timestamp.textContent = new Date().toLocaleTimeString();  // 显示当前时间，也可以修改为服务器传回的时间
        messageContent.appendChild(timestamp);

        messageElement.appendChild(messageContent);
        document.getElementById('messageList').appendChild(messageElement);
        document.getElementById('messageList').scrollTop = document.getElementById('messageList').scrollHeight;
    }

    function disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        console.log("Disconnected");
        document.getElementById('login').style.display = 'block';
        document.getElementById('chat').style.display = 'none';
    }

    function leaveChat() {
        if(stompClient && orderId && identity) {
            var leaveMessage = { orderId: orderId, from: identity, text: identity + " has left the chat." };
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(leaveMessage));
            stompClient.disconnect(function() { console.log("Disconnected"); }, {});
            alert(identity + ' left the chat.');
        }

        document.getElementById('chat').style.display = 'none';
        document.getElementById('login').style.display = 'block';
        document.getElementById('messageList').innerHTML = '';
    }