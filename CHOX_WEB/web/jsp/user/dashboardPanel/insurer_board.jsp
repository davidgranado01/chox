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
        
        <div class="form-container">
            <div class="status-info">
                This dashboard displays a snapshot of claims in the
                  system to date across a weekly, monthly and yearly period.
                  Results can be viewed for an individual CHO or across the entire
                  CHO book.
            </div>
            <table cellpadding="0" cellspacing="0" class="searchForm" style="width:99%;" border="0">       
                <tr><td>Number of Active User </td><td colspan="2"><label class="std-data-ro"><s:property value="numberOfActiveUser"/></label></td></tr>
                <tr>
                    <td nowrap><label>CH Organisation</label></td><td colspan="2"><s:select name="supplierId" id="supplierId" list="suppliers" listKey="id" listValue="name" headerKey="-1"
                                                                                                headerValue="--- ALL ---"
                                                                                            emptyOption="false"></s:select></td>                         
                </tr>                       
            </table>
            <div style="width:100%;height:480px" id="resultHolder"></div>
        </div>        
        
    </fieldset>
</div>
