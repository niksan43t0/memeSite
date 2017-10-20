function openPublicChat(){
    // open a blank "target" window
    // or get the reference to the existing "target" window
    var winref = window.open('', 'publicChat', 'width=550,height=350');

    // if the "target" window was just opened, change its url
    if(winref.location.href === 'about:blank'){
        winref.location.href = '/chat/public-chat';
    }
    return false;
}