<!-- This file is generated with yui-aggregator so don't forget to change the version of the file inside of pom.xml -->
<link href="<%= request.getContextPath()%>/css/libs-<s:property value="getText('js.aggregator.libs')" />-min.css" rel="stylesheet" type="text/css" media="all"/>
<s:if test='brandingClaim || brandingType.equalsIgnoreCase("full")'>
    <link id="theme" href="<%= request.getContextPath()%>/css/branding-libs-<s:property value="getText('js.aggregator.libs')" />-min.css" rel="stylesheet" type="text/css" media="all"/>
    <link href="<%= request.getContextPath()%>/css/branding_panel_theme-<s:property value="getText('js.aggregator.choxall')" />-min.css" rel="stylesheet" type="text/css" media="all"/>
</s:if>
<s:else>
    <link href="<%= request.getContextPath()%>/css/chox_panel_theme-<s:property value="getText('js.aggregator.choxall')" />-min.css" rel="stylesheet" type="text/css" media="all"/>
</s:else>
<link href="<%= request.getContextPath()%>/css/chox-<s:property value="getText('js.aggregator.choxall')" />-min.css" rel="stylesheet" type="text/css" media="all"/>

