<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formUpdateCaseWithSolicitor" name="formUpdateCaseWithSolicitor" class="XXentity-form">
        <fieldset class="x-fieldset">
            <legend>Case with Clients Solicitor?</legend>
            <div>
                <!--s:hidden id="claimId" name="id" /-->
                <s:hidden id="name" name="name" value="updateCaseWithSolicitor" />
                <div class="status-info">
                    Please use the check box below to mark if this case is with your Clients Solicitor. Once checked and updated the case
                    will remain at status Contested Invoice Referred To CHO but move into the Inbox Queue Case With Clients Solicitor.
                </div>
                <div class="status-control-set">
                    <table class="status-table" width="100%">
                        <tr>
                            <td align="right" width="10%"><label>Confirm Case Is With Clients Solicitor</label></td> 
                            <td width="20%"><s:checkbox id="caseWithSolicitorId" name="caseWithSolicitor" value="caseWithClientsSolicitor" /></td>
                            <td width="70%"></td>
                        </tr>
                        <tr>
                            <td>
                                <input id="assign" type="submit" value="Update"/>
                            </td>
                        </tr>
                    </table>
                    <div class="action-error-msg" id="CaseWithSolicitorMessageBox"></div>
                </div>
            </div>
        </fieldset>
    </form>
</div>
