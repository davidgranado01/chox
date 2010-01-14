<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){

        var form = $("form#routeUnacknowledgedUnroutedClaim");
        form.validate(
        {
            errorLabelContainer: "#RouteUnacknowledgedUnroutedClaimMessageBox",
            rules: {
                workgroupId:{min:0}
            },
            messages: {
                workgroupId:{min:"You must supply a value for 'Workgroup'"}
            }
        });
        
    });


</script>
<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="routeUnacknowledgedUnroutedClaim" name="routeUnacknowledgedUnroutedClaim" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST">

        <fieldset class="x-fieldset"><legend>Claim Routing - Action Required</legend>
            <div>
                <div class="status-info">
                    Please select the 'Workgroup' in order to route the claim to the relevant handling team.
                </div>
                <s:hidden name="id" />
                <s:hidden name="name" value="assignWorkgroup" />
                <div class="status-control-set">
                    <div class="status-info-submit">
                        <table>
                            <tr>
                                <td>
                                    <div class="no-format">
                                        <label>Workgroup</label>
                                        <s:select name="workgroupId" id="workgroupId"
                                                  list="workgroups" headerKey="-1"
                                                  listKey="id" listValue="name"
                                                  headerValue="-- Please Select --"></s:select>
                                        <input type="submit" value="Assign Workgroup"/>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div id="RouteUnacknowledgedUnroutedClaimMessageBox" class="action-error-msg"></div>
                </div>
            </div>
        </fieldset>
    </form>
</div>