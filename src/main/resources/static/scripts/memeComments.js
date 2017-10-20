var currentPage = 0;

window.onload = function() {
    $("#postCommentForm").submit(function (event) {
        event.preventDefault();
        postComment();
    });

    getComments(0);
};

function getComments(page) {

    $.ajax({
        url: window.location.pathname + "/comments/" + page,
        type: 'GET',
        contentType: 'application/json',
        success: function(comments){
            if(loadComments(comments) &&
                loadCommentPageButtons((comments.commentPage.number + 1), comments.commentPage.last, comments.commentPage.first) &&
                    loadFindCommentPageButtons()){

                document.getElementById('commentSection').style.display = 'block';
                currentPage = comments.commentPage.number + 1;
            }
        },
        error: function (error) {
            console.log(error)
        }
    });
}

function loadComments(comments) {
    if(comments.commentPage.numberOfElements === 0){
        return false;
    }
    $("#commentSection").html("");

    comments.commentPage.content.forEach(function (comment) {

        var $paragraph = $('<p align="center" ></p>');
        $paragraph.text(comment.content);

        var $author = $('<span class="author pull-right"></span>');
        $author.text("by " + comment.author.fullName);

        var $divTag = $('<form class="form-horizontal" style="overflow:hidden; border:2px solid #999999;" ></form>');
        $divTag.append($paragraph);
        $divTag.append($author);

        if(comments.loggedUserAdminOrMemeAuthor || (comments.loggedUserId === comment.author.id)) {
            var $deleteButton = $('<button style="cursor: pointer" type="submit" class="btn btn-danger btn-xs"></button>');
            $deleteButton.text("Delete");
            $divTag.submit(function (event) {
                event.preventDefault();
                $deleteButton.prop("disabled", true);
                deleteComment(comment.id);
                $deleteButton.prop("disabled", false);
            });
            $divTag.append($deleteButton);
        }
        $("#commentSection").append($divTag);
    });
    return true;
}

function loadCommentPageButtons(commentPage, isLastPage, isFirstPage){

    var $divTag = $('<div align="center" ></div>');

    if(!isFirstPage){
        var $previousPageButton = $('<a style="cursor: pointer" class="myButton myButtonSmall"></a>');
        $previousPageButton.text(commentPage - 1);
        $previousPageButton.click(function () {
            getComments(commentPage - 1);
        });
        $divTag.append($previousPageButton);
    }

    var $currentPageButton = $('<a style="cursor: pointer" class="myButton" ></a>');
    $currentPageButton.text(commentPage);
    $currentPageButton.click(function () {
        getComments(commentPage);
    });
    $divTag.append($currentPageButton);

    if(!isLastPage){
        var $nextPageButton = $('<a style="cursor: pointer" class="myButton myButtonSmall"></a>');
        $nextPageButton.text(commentPage + 1);
        $nextPageButton.click(function () {
            getComments(commentPage + 1);
        });
        $divTag.append($nextPageButton);
    }
    $("#commentSection").append('</br>');
    $("#commentSection").append($divTag);

    return true;
}

function loadFindCommentPageButtons(){
    var $divTag = $('<div align="center" ></div>');

    var $searchBar = $(' <input style="width: 40px" id="commentPageTextBox" placeholder="goTo" required = "required"/>')
    $divTag.append($searchBar);

    var $searchButton = $('<input style="cursor: pointer" class="searchButton" />')
    $searchButton.click(function () {
        goToCommentPage = parseInt(document.getElementById('commentPageTextBox').value);
        getComments(isNaN(goToCommentPage) ? 0 : goToCommentPage);
    });
    $divTag.append($searchButton);

    $("#commentSection").append('</br>');
    $("#commentSection").append($divTag);
    return true;
}

function deleteComment(commentId){

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/meme/deleteComment",
        data: JSON.stringify(commentId),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            getComments(currentPage);
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });


}

function postComment(){
    var data = {};
    data["memeId"] = parseInt(window.location.pathname.substring(6));
    data["commentContent"] = $("#commentTextArea").val();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/meme/postComment",
        data: JSON.stringify(data),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            getComments(0);
            $("#commentTextArea").val("");
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });

}