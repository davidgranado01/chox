var dateLib = function(){

    function parseDate(sdate){
        var date;
        if(sdate!=null && sdate!=""){
            var sDay = sdate.substring(0,2);
            var sMonth = sdate.substring(3,5);
            var sYear = sdate.substring(6,10);
            var sDate = sMonth+"/"+sDay+"/"+sYear;
            date = new Date(sDate);
        }
        return date;
    }

    function today(){
        var now = new Date();
        return now.format(dateFormat);
    }

    return {
        today : today,
        parseDate : parseDate
    }
}