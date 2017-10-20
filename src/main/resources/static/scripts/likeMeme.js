function likeMeme(memeId){
    $.ajax({
        url: "/meme/" + memeId + "/like",
        type: 'GET',
        contentType: 'application/json',
        success: function(numberOfLikes){
            var $likeButton = document.getElementById("likeMeme");
            $likeButton.setAttribute("value", "Like (" + numberOfLikes + ")");
        },
        error: function (error) {
            console.log(error)
        }
    });
}