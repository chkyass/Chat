let socket;
let stompClient;
let colors = ['#e6194b', '#3cb44b', '#ffe119', '#4363d8', '#f58231', '#911eb4', '#46f0f0', '#f032e6', '#bcf60c', '#fabebe', '#008080', '#e6beff', '#9a6324', '#fffac8', '#800000', '#aaffc3', '#808000', '#ffd8b1', '#000075', '#808080', '#ffffff', '#000000'];
let index = 0;
let user_color = {}

function connect() {
    socket = new SockJS('/sockjs');
    stompClient = new Stomp.over(socket);
    stompClient.connect({}, function() {
        console.log('connected: ' + $('#username').text());
        stompClient.subscribe('/topic/messages', function(event) {
            receive(event.body);
        });
        stompClient.subscribe('/topic/numberOfUsers', function(event) {
            updateUsers(event.body);
            console.log("received new number of users: " + event.body);
        });
    });
    $.get("/online",
        function (data) {
            updateUsers(data);
        });

}
connect();


$(document).ready(function () {
    $.get( "/history",
        function (data) {
            console.log(data);
            data.slice().reverse().forEach ( element =>
                addMessage(element)
            );
            scrollDown();
    });
});

$(document).ready(function () {
        $.get( "/username",
            function (data) {
                console.log(data);
                $("#username").text(data);
        });
    });

function updateUsers(nb) {
    $('.chat-num').text(nb);
}

function sendMessage() {
    stompClient.send("/send/message", {},
        JSON.stringify(
        {
            'user' : $('#username').text(),
            'message' : $('#msg').val()
        })
    );
    $('#msg').val("");
}

function addMessage(message) {
    if (user_color[message.user] === undefined) {
        user_color[message.user] = colors[index%22];
        index++;
    }
    console.log(user_color[message.user]);
    $('.message-container').append(
        '<div class="mdui-card '+ message.user + '" style="margin: 10px 0;">' +
        '<div class="mdui-card-primary">' +
        '<div class="mdui-card-content message-content" style="word-wrap: break-word;"> <div style="color: ' + user_color[message.user]+';">' + message.user + '：</div>' + message.message + '</div>' +
        '</div></div>');
}

function receive(event) {
    console.log('WebSocket Receives：%c' + event, 'color:green');
    var message = JSON.parse(event);
    addMessage(message);
    scrollDown();
}

function close() {
    console.log("Disconnected");
    stompClient.disconnect();
}

function clearMsg() {
    $.post( "/clear", { username: $("#username").text()});
    var selector = "."+$("#username").text();
    $(selector).remove();
}

function scrollDown() {
    $(".message-container").scrollTop($(".message-container")[0].scrollHeight);
}

$(document).ready(function(){
    $('#msg').keypress(function(e){
      if(e.keyCode==13)
        sendMessage();
    });
});