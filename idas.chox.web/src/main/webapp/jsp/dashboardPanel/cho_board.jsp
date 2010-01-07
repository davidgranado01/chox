<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        $(document).ready(function(){  
            
            $("#dashboardInsurerId").change(onSelectChange);  
            loadData(-1);
            
            new Ext.ToolTip({target: 'tip0',html: 'Number of users registered and using CHOX'});
            new Ext.ToolTip({target: 'tipTitle',html: 'Selected Insurer(s) for dashboard data'});
            
            Ext.QuickTips.init();
            
        }); 
        
        function onSelectChange(){  
            
            var selectedValue = '-1';
            var selected = $("#dashboardInsurerId option:selected");           
            
            if(selected.val() != ""){  
                selectedValue = selected.val();
            }           
            
            $("#resultHolder").block();
            loadData(selectedValue);
        }  
        
        function loadData(insurerId)
        {
            $.get("<%= request.getContextPath()%>/prv/p/showChoBoard.action?insurerId=" + insurerId, function(data){
                $("#resultHolder").html(data);
            });
        }

    </script>  


<div class="x-panel-bwrap chox-form-container" id="dashboardId"> 
    <fieldset class="x-fieldset">
        <legend>CHO Admin Dashboard</legend>
        
        <div class="instruction-message">
               This dashboard displays a snapshot of claims in the system
                  to date across a weekly, monthly and yearly period. Results can
                  be viewed for an individual Insurer or across the entire Insurer
                  book. (Please hover over a dashboard item label to see an explanation of the numbers displayed)
        </div>
        
        <div class="form-container">
            <table cellpadding="0" cellspacing="0" class="dashboard" border="0">       
                <tr><th nowrap><label id="tip0">Number of Active Users</label></th><td colspan="2"><label class="std-data-ro"><s:property value="numberOfActiveUser"/></label></td></tr>
                <tr><th nowrap><label >Last Update Date</label></th><td colspan="2" nowrap="true"><label class="std-data-ro"><s:property value="lastProcessDate"/></label>
</td></tr>
                <tr>
                    <th nowrap><label id="tipTitle">Insurer</label></th><td>
                        <s:select 
                        name="dashboardInsurerId" 
                        id="dashboardInsurerId" 
                        list="insurers" 
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
