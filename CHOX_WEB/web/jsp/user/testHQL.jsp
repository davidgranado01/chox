
<%@ taglib uri="/struts-tags" prefix="s" %>     

<head>
    <title>IDAS-CHOX</title>
    
    
    <link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>        
    <link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>
    
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery-1.2.6.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.form.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/ext-jquery-adapter.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.blockUI.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.timer.js"></script>  
    
    <script src="<%= request.getContextPath()%>/scripts/ext-base.js" type="text/javascript"></script>
    <script src="<%= request.getContextPath()%>/scripts/ext-all.js" type="text/javascript"></script> 
    <script src="<%= request.getContextPath()%>/scripts/Application.js" type="text/javascript"></script> 
    <script src="<%= request.getContextPath()%>/scripts/general.js" type="text/javascript"></script> 
    
</head>

<script language="JavaScript">
    
    $(document).ready(function(){                
        $("#formTestHQL").submit(function() {
            var queryString = $('#formTestHQL').formSerialize(); 
            //var popwin = window.open("runHQL.action?" + queryString, "Result", "WIDTH=575,HEIGHT=500,RESIZABLE=No,SCROLLBARS=YES,TOOLBAR=NO,LEFT=200,TOP=100");
            $.get("runSQL.action?" + queryString, function(data){
                $("#result").text(data);                
            });

            return false;
        });
    });       

</script>
<div class="x-panel-bwrap chox-form-container">
    <form id="formTestHQL" name="formTestHQL" class="XXentity-form">
        
        <fieldset class="x-fieldset">        
            <legend>Test HQL</legend>
            <div class="form-container">
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Query</label>
                <textarea id="query" name="query" cols="160" rows="20" ><s:property value="query" /></textarea></div>        
                
                <div class="chox-form-button"><input type="submit" value="Run" methid="post"/></div>
                <br />
                <div class="chox-form-item"><textarea id="result" name="result" cols="160" rows="20" ></textarea></div>
                <div id="MessageBox" class="errorBox"></div>
                <div class="chox-form-submit-result"></div>
            </div>
        </fieldset>
    </form>
</div>