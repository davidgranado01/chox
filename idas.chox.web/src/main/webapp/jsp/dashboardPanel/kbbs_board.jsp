<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var iframeURL = 'https://dashboards.idaschox.com/mydashboard/create/2f2b369e-0ea1-41c6-b070-688f4013329d';
    var iframeID = 'choxKBBSDashboardId';

    Ext.onReady(function () {

        if (document.domain !== "localhost") {
            var iFrameWin = document.getElementById(this.iframeID);
            document.domain = 'idaschox.com';
            iFrameWin.src = iframeURL;
        }
    });
</script>

<iframe id="choxKBBSDashboardId" style="height:750px; width:1187px"></iframe>
