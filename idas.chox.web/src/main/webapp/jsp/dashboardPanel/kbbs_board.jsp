<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var iframeURL = 'https://dashboards-beta.idaschox.com/mydashboard/create/2f2b369e-0ea1-41c6-b070-688f4013329d';
//    var authenticationURL = 'https://dashboards-beta.idaschox.com/authentication/GenerateAccessToken';
    var iframeID = 'MyIFrame';

    function createCookie(name, value, days) {
        if (days) {
            var date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            var expires = "; expires=" + date.toGMTString();
        }
        else
            var expires = "";
//        console.log("Cookies: " + document.cookie);
//        console.log("Adding cookie '" + name + "=" + value + expires + "; path=/" + "'.");
        document.cookie = name + "=" + value + expires + "; path=/; domain=idaschox.com";
//        console.log("Cookie set in domain " + document.domain + " to " + document.cookie);
    }

    function getCookie(name) {
        var value = "; " + document.cookie;
        var parts = value.split("; " + name + "=");
        if (parts.length === 2)
            return parts.pop().split(";").shift();
    }

//    function loadIframe(iFrameWin) {
//        var params = {
//            UserName: "<s:property value="kbbsUsername" />",
//            Password: "<s:property value="kbbsPassword" />",
//            ExtraData: {
//                UniqueId: "<s:property value="kbbsUniqueId" />"
//            }
//        };
//
//        $.ajax({
//            type: "POST",
//            url: authenticationURL,
//            dataType: "json",
//            data: params,
//            timeout: 30000,
//            xhrFields: {withCredentials: true},
//            success: function (data) {
//                //setiFrame's SRC attribute
//                iFrameWin.src = iframeURL;
//            },
//            error: function () {
////                console.log("KBBS error");
//            }
//        });
//    }

    Ext.onReady(function () {

        if (document.domain !== "localhost") {
            var iFrameWin = document.getElementById(this.iframeID);
//            loadIframe(iFrameWin);
            document.domain = 'idaschox.com';
            createCookie("ASP.NET_Token", getCookie("JD.Token"), 1);
            iFrameWin.src = iframeURL;
        }
    });
</script>

<iframe id="MyIFrame" style="height:750px; width:1187px"></iframe>
