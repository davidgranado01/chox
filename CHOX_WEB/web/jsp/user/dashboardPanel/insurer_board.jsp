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
            
        }); 
        
        function onSelectChange(){  
            
            var selectedValue = '-1';
            var selected = $("#supplierId option:selected");           
            if(selected.val() != ""){  
                selectedValue = selected.val();
            }  
                        
            $.get("showInsurerBoard.action?supplierId=" + selectedValue, function(data){
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
                Dashboard Description
            </div>
            <table cellpadding="0" cellspacing="0" class="searchForm" style="width:99%;" border="0">                    
                <tr>
                    <td nowrap><label>CH Organisation</label></td><td colspan="2"><s:select name="supplierId" id="supplierId" list="suppliers" listKey="id" listValue="name" headerKey="-1"
                                                                                                headerValue="--- ALL ---"
                                                                                            emptyOption="false" onchange="reload();"></s:select></td>                         
                </tr>                       
            </table>
            <div style="width:100%;height:480px" id="resultHolder"></div>
        </div>        
        
    </fieldset>
</div>
