<%-- 
    Document   : insurer_board
    Created on : 02-Feb-2009, 16:19:35
    Author     : Emmanuel
--%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    
    <script>
        $(document).ready(function(){  
            
            $("#supplierId").change(onSelectChange);  
            loadData(-1);
            
            new Ext.ToolTip({target: 'tip0',html: 'Description for Number of Active Users'});
            Ext.QuickTips.init();
        }); 
        
        function onSelectChange(){  
            
            var selectedValue = '-1';
            var selected = $("#supplierId option:selected");           
            if(selected.val() != ""){  
                selectedValue = selected.val();
            }           
            
            loadData(selectedValue);
            
        }  
        
        function loadData(supplierId)
        {
            $.get("showInsurerBoard.action?supplierId=" + supplierId, function(data){
                $("#resultHolder").html(data);
            });
        }

    </script>  
</head>

<div class="x-panel-bwrap chox-form-container"> 
    <fieldset class="x-fieldset">
        <legend>Insurer Admin Dashboard</legend>
        
        <div class="status-info">
            This dashboard displays a snapshot of claims in the
            system to date across a weekly, monthly and yearly period.
            Results can be viewed for an individual CHO or across the entire
            CHO book.
        </div>
        <div class="dashboard" class="form-container">
            
            <table cellpadding="0" cellspacing="0" class="dashboard" border="0">       
                <tr><th nowrap>Number of Active Users<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png" id="tip0"/></th><td colspan="2"><label class="std-data-ro"><s:property value="numberOfActiveUser"/></label></td></tr>
                <tr><th nowrap><label>Credit Hire Organisation</label></th><td colspan="2"><s:select name="supplierId" id="supplierId" list="suppliers" listKey="value" listValue="text" headerKey="-1"
                                                                                                         headerValue="--- ALL ---"
                                                                                                     emptyOption="false"></s:select></td>                         
                </tr>                       
            </table>
            <div style="height:500px" id="resultHolder"></div>
        </div>        
        
    </fieldset>
</div>
