<%-- 
    Document   : insurer_board
    Created on : 02-Feb-2009, 16:19:35
    Author     : Emmanuel
--%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <script>
        function reload()
        {
            $.get("showInsurerBoard.action?supplierId=1", function(data){
                $("#resultHolder").html(data);
            });

        }
    </script>  
</head>

<fieldset class="x-fieldset">
    <legend>Insurer Admin Dashboard</legend>
    <div class="x-panel-bwrap chox-form-container">      
        <div class="form-container">
            <div class="status-info">
                Dashboard Description
            </div>
            <table cellpadding="0" cellspacing="0" class="searchForm" style="width:99%;" border="0">                    
                <tr>
                    <td nowrap><label>CH Organisation</label></td><td colspan="2"><s:select name="supplierId" list="suppliers" listKey="id" listValue="name" headerKey="-1"
                                                                                                headerValue="--- ALL ---"
                                                                                            emptyOption="false" onchange="reload();"></s:select></td>                         
                </tr>                       
            </table>
            <div id="resultHolder"></div>
        </div>        
    </div>
</fieldset>
