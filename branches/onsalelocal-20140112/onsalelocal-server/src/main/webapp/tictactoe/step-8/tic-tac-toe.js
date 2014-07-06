var next = 'x';
var mark = new Array();
var finished = false;

function clicked(where, position) {
    if(finished) 
        return;
    
    if(where.innerHTML && where.innerHTML.length > 0 ) {
        alert("Occupied!")
        return;
    }
    
    mark[position] = next;
    if(next == 'x') {
        where.innerHTML = "<img src='http://www.gifs.net/Animation11/Alphabets/Colored_glass/x_.gif'>";
        next = 'o';
    }
    else {
        where.innerHTML = "<img src='http://www.gifs.net/Animation11/Alphabets/Colored_glass/o_.gif'>";
        next = 'x'
    }
    
    checkResult();
}

function checkResult() {
    if(mark[0] && mark[0] == mark[1] && mark[0] == mark[2]) {
       alert(mark[0] + " is the winner");
       finished = true;
       return;
    } 
    
    if(mark[3] && mark[3] == mark[4] && mark[3] == mark[5]) {
       alert(mark[3] + " is the winner");
       finished = true;
       return;
    } 
    
    if(mark[6] && mark[6] == mark[7] && mark[6] == mark[8]) {
       alert(mark[6] + " is the winner");
       finished = true;
       return;
    } 
    
    if(mark[0] && mark[0] == mark[3] && mark[0] == mark[6]) {
       alert(mark[0] + " is the winner");
       finished = true;
       return;
    } 
    
    if(mark[1] && mark[1] == mark[4] && mark[1] == mark[7]) {
       alert(mark[1] + " is the winner");
       finished = true;
       return;
    } 
    
    if(mark[2] && mark[2] == mark[5] && mark[2] == mark[8]) {
       alert(mark[2] + " is the winner");
       finished = true;
       return;
    } 
    
    if(mark[0] && mark[0] == mark[4] && mark[0] == mark[8]) {
       alert(mark[0] + " is the winner");
       finished = true;
       return;
    } 
    
    if(mark[2] && mark[2] == mark[4] && mark[2] == mark[6]) {
       alert(mark[2] + " is the winner");
       finished = true;
       return;
    } 
}
