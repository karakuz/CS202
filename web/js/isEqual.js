function isEqual() {
    if(document.querySelectorAll('p > input')[0].value == document.querySelectorAll('p > input')[1].value){
        document.querySelectorAll('p > input')[0].value = document.querySelectorAll('p > input')[1].value = "";
        alert("Start and End time can not be same");
    }
}

function isEqual2() {
    if(document.querySelectorAll('p > input')[2].value == document.querySelectorAll('p > input')[3].value){
        document.querySelectorAll('p > input')[2].value = document.querySelectorAll('p > input')[3].value = "";
        alert("Start and End time can not be same");
    }
}

function isEqualLoop() {
    //console.log("hit");
    for(let i=0; i<document.querySelectorAll('p > input').length; i=i+2){
        //console.log(i);
        if((document.querySelectorAll('p > input')[i].value == document.querySelectorAll('p > input')[i+1].value)
            && document.querySelectorAll('p > input')[i].value != ""
            && document.querySelectorAll('p > input')[i+1].value != ""){
            document.querySelectorAll('p > input')[i].value = document.querySelectorAll('p > input')[i+1].value = "";
            alert("Start and End Time can not be same");
        }
    }
}