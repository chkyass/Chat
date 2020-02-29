let socket;
let stompClient;
let colors = ['#e6194b', '#3cb44b', '#ffe119', '#4363d8', '#f58231', '#911eb4', '#46f0f0', '#f032e6', '#bcf60c', '#fabebe', '#008080', '#e6beff', '#9a6324', '#fffac8', '#800000', '#aaffc3', '#808000', '#ffd8b1', '#000075', '#808080', '#ffffff', '#000000'];
let index = 0;
let user_color = {};
let subscription;
let roomid;

$(document).ready(function () {
    socket = new SockJS('/chat/sockjs');
    stompClient = new Stomp.over(socket);
    stompClient.connect({}, function() {
        stompClient.subscribe('/topic/numberOfUsers', function(event) {
            updateUsers(event.body);
            console.log("received new number of users: " + event.body);
        });
    });
    $.get("/chat/online",
        function (data) {
            updateUsers(data);
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
   $("#rooms").empty();
    $.get( "/chat/rooms",
        function (data) {
            console.log(data);
            Object.keys(data).forEach ( key =>
                $("#rooms").append('<li><input type="button" value="'+ key +'" onclick="selectRoom(this.value)"></input>'+ data[key].join('|') +'</li>')
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
        '<div class="mdui-card-primary">' +
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

function clearMsg() {
    $.post( "/chat/clear", { username: $("#username").text()});
    var selector = "."+$("#username").text();
    $(selector).remove();
}

function scrollDown() {
    $(".message-container").scrollTop($(".message-container")[0].scrollHeight);
}

function selectRoom(id) {
    roomid  = id;
    console.log("select room" + roomid);
    if(typeof subscription !== 'undefined'){
       console.log("unsubscibe")
       subscription.unsubscribe();

    }

    subscription = stompClient.subscribe('/topic/' + roomid, function(event) {
        console.log("message received on room "+ roomid)
        receive(event.body);
    });


    $(".message-container").empty();
    $.post( "/chat/selectroom", { room_id: roomid},
            function (data) {
                console.log(data);
                data.slice().reverse().forEach ( element =>
                    addMessage(element)
                );
                scrollDown();
    });
}

$(document).ready(function(){
    $('#msg').keypress(function(e){
      if(e.keyCode==13)
        sendMessage();
    });
});

