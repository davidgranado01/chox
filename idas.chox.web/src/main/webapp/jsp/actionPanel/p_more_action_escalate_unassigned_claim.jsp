<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(document).ready(function(){

        $("#formEscalateClaimAction").validate(
        {
            errorLabelContainer: "#EscalateClaimMessageBox",
            rules: {
                escalateWorkgroupId:{min:1}
            },
            messages: {
                escalateWorkgroupId: {min:"You must supply a value for 'Workgroup'"}
            }
        });
    });

    
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/escalatedUnassignedClaim.action" method="post" id="formEscalateClaimAction" name="formEscalateClaimAction">
        <fieldset class="x-fieldset">
            <legend>Re-assign Workgroup - Action Required</legend>
            <div>
                <s:hidden id="claimId" name="id" />
                <div>
                    <div class="status-info">
                        If the claim has been incorrectly assigned to this workgroup, please select the correct Workgroup from the selection list below.
                    </div>
                    <div class="status-control-set">
                        <table class="status-table" width="100%">
                            <tr>
                                <td width="200px"><label>Workgroup</label></td>
                                <td width="100%">
                                    <s:select name="escalateWorkgroupId"
                                              id="escalateWorkgroupId"
                                              list="insurerWorkgroups"
                                              headerKey="-1"
                                              listKey="id"
                                              listValue="name"
                                              emptyOption="false"
                                              headerValue="-- Please Select --">
                                    </s:select>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="choice" nowrap>
                                    <input id="assignOnly" type="submit" value="Re-assign Workgroup"/>
                                </td>
                            </tr>
                        </table>
                        <div class="action-error-msg" id="EscalateClaimMessageBox"></div>
                    </div>
                </div>
            </div>
        </fieldset>
    </form>
</div>