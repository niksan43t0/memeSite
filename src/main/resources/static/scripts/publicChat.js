$(window).resize(function()
{
    var winNewHeight = $(window).height();
    $("#messages").height(winNewHeight - 175);
});


var stompClient = null;

window.onload = function connect() {
    var socket = new SockJS('/meme-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        //console.log('Connected: ' + frame);
        stompClient.subscribe('/publicChat/receiveMessages', function (message) {
            showMessage(JSON.parse(message.body).content);
        });
    });
};

window.onclose = function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
};

function sendMessage() {
    if (!$("#message").val().replace(/\s/g, '').length) {
        $("#message").val("");
        return;
    }
    stompClient.send("/app/sendMessages", {}, JSON.stringify({'userId': parseInt($('#dataCarrier').attr('data-userId')) , 'content': $("#message").val()}));
    $("#message").val("");
}

function showMessage(message) {
    $("#messages").append("<tr><td>" + message + "</td></tr>");
    $("#messages").animate({ scrollTop: Number.MAX_SAFE_INTEGER }, "fast");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#send" ).click(function() { sendMessage(); });
});