<%-- 
    Document   : insurer_board
    Created on : 02-Feb-2009, 16:19:35
    Author     : Emmanuel
--%>
<%@ taglib uri="/struts-tags" prefix="s" %>


    <script>
        $(document).ready(function(){  
            
            $("#dashboardSupplierId").change(onSelectChange);  
            loadData(-1);
            
            new Ext.ToolTip({target: 'tip0',html: 'Description for Number of Active Users'});
            Ext.QuickTips.init();
            
        }); 
        
        function onSelectChange(){  
            
            var selectedValue = '-1';
            var selected = $("#dashboardSupplierId option:selected");           
            if(selected.val() != ""){  
                selectedValue = selected.val();
            }           
            $("#resultHolder").block();
            loadData(selectedValue);
        }
        
        function loadData(supplierId)
        {
            $.get("showInsurerBoard.action?supplierId=" + supplierId, function(data){
                $("#resultHolder").html(data);
            });
        }

    </script>


<div class="x-panel-bwrap chox-form-container">
    <fieldset class="x-fieldset">
        <legend>Insurer Admin Dashboard</legend>
        
        <div class="ReportActionMsg">
            This dashboard displays a snapshot of claims in the
            system to date across a weekly, monthly and yearly period.
            Results can be viewed for an individual CHO or across the entire
            CHO book. (Please hover over a dashboard item label to see an explanation of the numbers displayed)
        </div>
        
        <div class="dashboard" class="form-container">
            <table cellpadding="0" cellspacing="0" class="dashboard" border="0">  
            <tr><th nowrap><label id="tip0">Number of Active Users</label></th><td colspan="2"><label class="std-data-ro"><s:property value="numberOfActiveUser"/></label></td></tr>
            <tr>
                <th nowrap><label>Credit Hire Organisation</label></th><td colspan="2">
                <s:select 
                name="dashboardSupplierId" 
                id="dashboardSupplierId" 
                list="suppliers" 
                listKey="id" 
                listValue="name" 
                headerKey="-1"
                headerValue="--- ALL ---"
                emptyOption="false"></s:select></td>              
            </tr>
            </table>
            <div style="height:595px; width:900px" id="resultHolder" name="resultHolder"></div>
        </div>
        
    </fieldset>
</div>
