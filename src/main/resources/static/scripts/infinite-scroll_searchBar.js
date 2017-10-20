var pageIndex;

window.onload = function () {
    $("#searchForm").submit(function (event) {
        event.preventDefault();
        searchMemes();
    });

    loadFewArticles(3);
};

function loadFewArticles(pagesToLoad){
    for(pageIndex = 0; pageIndex < pagesToLoad; pageIndex++){
        loadMemes(pageIndex);
    }
}

function scrollHandler(){
    var container = document.getElementById('memes');
    //gets page content height
    var contentHeight = container.offsetHeight;
    //gets vertical scroll position
    var yOffset = window.pageYOffset;
    var y = yOffset + window.innerHeight;

    if(window.location.pathname === "/") //1400px - other stuff on main page
        contentHeight+=1400;

    if(y >= contentHeight){

        loadMemes(pageIndex);
        pageIndex++;
    }
}
window.onscroll = scrollHandler;

function loadMemes(page) {

    var url;
    if(window.location.pathname === "/")
        url = "/memes/page/" + page;
    else if(window.location.pathname.substring(0,5) === "/tag/")
        url = "/memes" +  window.location.pathname + "/page/" + page;
    else
        url = "/memes/category" + window.location.pathname + "/page/" + page;
    $.ajax({
        url: url,
        type: 'GET',
        contentType: 'application/json',
        success: function(memes){
            if(memes.length === 0)
                return;

            memes.forEach(function (meme) {
                var $category = $('<div class="project-category text-faded"></div>');
                $category.text("Category: " + meme['category']['name']);

                var $author = $('<div class="project-name"></div>');
                $author.text("Author: " + meme['author']['fullName']);

                var $divContainer1 =$('<div class="portfolio-box-caption-content"></div>')
                $divContainer1.append($category);
                $divContainer1.append($author);

                var $divContainer2 =$('<div class="portfolio-box-caption"></div>');
                $divContainer2.append($divContainer1);

                var $img = $('<img class="img-fluid" alt=""/>');
                $img.attr('src', '/img/memeImages/' + meme['id'] + '.jpg');

                var $a = $('<a class="portfolio-box"></a>');
                $a.attr('href', '/meme/' + meme['id']);
                $a.attr('target', '_blank');
                $a.append($img);
                $a.append($divContainer2);

                var $divContainer3 = $('<div class="grid-item"></div>');
                $divContainer3.append($a);
                $('#memes').append($divContainer3);
            })
            var grid = document.querySelector('.grid');
            var msnry = new Masonry( grid, {
                // options
                itemSelector: '.grid-item',
                gutter: 10,
                isAnimated: true,
                isFitWidth: true
            });
            imagesLoaded( grid ).on( 'progress', function() {
                // layout Masonry after each image loads
                msnry.layout();
            });
        },
        error: function (error) {
            console.log(error)
        }
    });
}

//Navigation Script for the Search Bar
//-------------------------------------------------

function searchMemes(){

    $.ajax({
        url: '/memes/getAll',
        type: 'GET',
        contentType: 'application/json',
        success: function (data) {
            var options = {
                shouldSort: true,
                threshold: 0.3,
                location: 0,
                distance: 100,
                maxPatternLength: 32,
                minMatchCharLength: 1,
                keys: ['title', 'author.fullName', 'tags.name']
            };
            var fuse = new Fuse(data, options);
            var result = fuse.search(document.getElementById('searchInput').value);

            //still looking for a good way to search and show resultsd
            /*result.forEach(function (entry) {

                var $a = $('<a></a>');
                $a.attr('href', '/meme/' + entry['id']);
                $a.attr('target', "_blank");
                var $h = $('<h3></h3>');
                $h.text(entry['title']);
                $a.append($h);

                var $div = $('<div></div>');
                $div.addClass('newData');
                $('#container').append($div);
                $div.append($a);

                console.log(entry['title'])
            })*/

        },
        error: function (error) {
            console.log(error)
        }
    })

}