<link href="<%= request.getContextPath()%>/css/chox.css" rel="stylesheet" type="text/css" media="all"/>
<link href="<%= request.getContextPath()%>/css/ext-all-3.4.1.css" rel="stylesheet" type="text/css" media="all"/>
<s:if test='brandingClaim || brandingType.equalsIgnoreCase("full")'>
    <link href="<%= request.getContextPath()%>/css/xtheme-erac.css" rel="stylesheet" type="text/css" media="all"/>
    <link href="<%= request.getContextPath()%>/css/xtheme-erac_ie6.css" rel="stylesheet" type="text/css" media="all"/>
    <link href="<%= request.getContextPath()%>/css/branding_panel_theme.css" rel="stylesheet" type="text/css" media="all"/>
</s:if>
<s:else>
    <link href="<%= request.getContextPath()%>/css/chox_panel_theme.css" rel="stylesheet" type="text/css" media="all"/>
</s:else>
<link href="<%= request.getContextPath()%>/css/ext-roweditor-3.4.0.css" rel="stylesheet" type="text/css" media="all"/>
<link href="<%= request.getContextPath()%>/css/ext-statusbar-3.4.0.css" rel="stylesheet" type="text/css" media="all"/>
<link href="<%= request.getContextPath()%>/css/superboxselect.css" rel="stylesheet" type="text/css" media="all"/>