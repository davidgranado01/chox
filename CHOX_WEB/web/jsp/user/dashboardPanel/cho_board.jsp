<%-- 
    Document   : cho_board
    Created on : 02-Feb-2009, 16:19:46
    Author     : Emmanuel
--%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <script>
        $(document).ready(function(){  
            
            $("#insurerId").change(onSelectChange);  
            loadData(-1);
            
            new Ext.ToolTip({target: 'tip0',html: 'Description for Number of Active Users'});
            Ext.QuickTips.init();
        }); 
        
        function onSelectChange(){  
            
            var selectedValue = '-1';
            var selected = $("#insurerId option:selected");           
            if(selected.val() != ""){  
                selectedValue = selected.val();
            }           
            
            loadData(selectedValue);
            
        }  
        
        function loadData(insurerId)
        {
            $.get("showChoBoard.action?insurerId=" + insurerId, function(data){
                $("#resultHolder").html(data);
            });
        }

    </script>  
</head>

<div class="x-panel-bwrap chox-form-container"> 
    <fieldset class="x-fieldset">
        <legend>CHO Admin Dashboard</legend>
        
        <div class="ReportActionMsg">
               This dashboard displays a snapshot of claims in the system
                  to date across a weekly, monthly and yearly period. Results can
                  be viewed for an individual Insurer or across the entire Insurer
                  book.
            </div>
        
        <div class="form-container">
            
            <table cellpadding="0" cellspacing="0" class="dashboard" border="0">       
                <tr><th nowrap><label id="tip0">Number of Active Users</label></th><td colspan="2"><label class="std-data-ro"><s:property value="numberOfActiveUser"/></label></td></tr>
                <tr>
                    <th nowrap><label>Insurer</label></th><td><s:select name="insurerId" id="insurerId" list="insurers" listKey="value" listValue="text" headerKey="-1"
                                                                                                headerValue="--- ALL ---"
                                                                                            emptyOption="false"></s:select></td>                         
                </tr>                       
            </table>
            <div style="height:500px; width:900px" id="resultHolder"></div>
        </div>        
        
    </fieldset>
</div>
