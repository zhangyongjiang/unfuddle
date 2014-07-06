var next = 'x';

function clicked(where) {
    if(where.innerHTML && where.innerHTML.length > 0 ) {
        alert("No Cheating!!!!!")
        return;
    }
    
    if(next == 'x') {
        where.innerHTML = "<img src='http://www.gifs.net/Animation11/Alphabets/Colored_glass/x_.gif'>";
        next = 'o';
    }
    else {
        where.innerHTML = "<img src='http://www.gifs.net/Animation11/Alphabets/Colored_glass/o_.gif'>";
        next = 'x';
    }
}


