<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var iframeURL = 'https://dashboards-beta.idaschox.com/mydashboard/create/2f2b369e-0ea1-41c6-b070-688f4013329d';
    var iframeID = 'MyIFrame';

    function createCookie(name, value, days) {
        if (days) {
            var date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            var expires = "; expires=" + date.toGMTString();
        }
        else
            var expires = "";
        document.cookie = name + "=" + value + expires + "; path=/; domain=idaschox.com";
    }


    function getCookie(name) {
        var value = "; " + document.cookie;
        var parts = value.split("; " + name + "=");
        if (parts.length === 2)
            return parts.pop().split(";").shift();
    }

    Ext.onReady(function () {

        if (document.domain !== "localhost") {
            var iFrameWin = document.getElementById(this.iframeID);
            document.domain = 'idaschox.com';
            createCookie("ASP.NET_Token", getCookie("JD.Token"), 1);
            iFrameWin.src = iframeURL;
        }
    });
</script>

<iframe id="MyIFrame" style="height:750px; width:1187px"></iframe>
