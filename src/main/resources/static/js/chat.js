let socket;
let stompClient;
let colors = ['#e6194b', '#3cb44b', '#ffe119', '#4363d8', '#f58231', '#911eb4', '#46f0f0', '#f032e6', '#bcf60c', '#fabebe', '#008080', '#e6beff', '#9a6324', '#fffac8', '#800000', '#aaffc3', '#808000', '#ffd8b1', '#000075', '#808080', '#ffffff', '#000000'];
let index = 0;
let user_color = {};
let roomid;


$(document).ready(function() {
    socket = new SockJS('/chat/sockjs');
        stompClient = new Stomp.over(socket);
        jQuery.ajax({
            url: '/chat/roomid',
            success: function (result) {
                roomid = result;
            },
            async: false
        });
        stompClient.connect({}, function() {
            console.log('connected: ' + $('#username').text());
            stompClient.subscribe('/topic/'+roomid, function(event) {
                receive(event.body);
            });
        });
});

$(document).ready(function () {
    $.get( "/chat/history",
        function (data) {
            console.log(data);
            data.slice().reverse().forEach ( element =>
                addMessage(element)
            );
            scrollDown();
    });
});

$(document).ready(function () {
        $.get( "/chat/username",
            function (data) {
                console.log(data);
                $("#username").text(data);
        });
    });

$(document).ready(function () {
        $.get( "/chat/usersinroom",
            function (data) {
                console.log(data)
                data.slice().reverse().forEach( user =>
                    $("#user-list").append('<div style="margin-left: 10%; margin-top: 5%; color= '+ user_color[user] +';">' + user +'</div>')
                );
        });
    });


function updateUsers(nb) {
    $('.chat-num').text(nb);
}

function sendMessage() {
    stompClient.send("/send/message", {},
        JSON.stringify(
        {
            'username' : $('#username').text(),
            'message' : $('#msg').val()
        })
    );
    $('#msg').val("");
}

function addMessage(message) {
    username = message.username;
    if (user_color[username] === undefined) {
        user_color[username] = colors[index%22];
        index++;
    }
    console.log(user_color[username]);
    $('.message-container').append(
        '<div class="mdui-card '+ username + '" style="margin: 10px 0;">' +
        '<div class="mdui-card-primary" style="padding: 0px;">' +
        '<div class="mdui-card-content message-content" style="word-wrap: break-word;"> <div style="color: ' + user_color[username]+';">' + username + '：</div>' + message.message + '</div>' +
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


function scrollDown() {
    $(".message-container").scrollTop($(".message-container")[0].scrollHeight);
}

$(document).ready(function(){
    $('#msg').keypress(function(e){
      if(e.keyCode==13)
        sendMessage();
    });
});