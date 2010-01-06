function getRandomNumber() {
    var min = 10000000;
    var max = 99999999;
    return (Math.round((max-min) * Math.random() + min));
}

function doSectionLoad(location, action, pamareter){
    $(location).load(action+"?"+pamareter+"&urdn="+getRandomNumber());
}