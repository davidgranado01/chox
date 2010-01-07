<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div class="UploadStatusContainer">
    <s:if test="results.size()==0">
        <div class="UploadStatusMessage">
            <div class="status-info">
                Couldn't find any claims in the input file! Please try again.
            </div>
        </div>
    </s:if>
    <s:else>


        <div class="UploadStatusMessage">

            <div class="status-info">
                Please carefully review the infomation provided below, as it contains important information regarding the claims you have uploaded.
                <br/><br/>
                If the XML file that you have supplied contains errors, please correct any errors in accordance with the information given beneath the specifc claim.

                <p style="text-align:center">
                    <s:url id="goBackToInbox" action="inbox" />
                    <s:url id="reUpload" action="uploadClaims" /></p>
                <s:a href="%{goBackToInbox}" >Proceed to CHOX Inbox</s:a>
                <br /> <br />
                <s:a href="%{reUpload}" >Re-Upload XML file</s:a>


            </div>

        </div>

        <div class="bordereauResultHolder">

            <table class="BordereauResultTable" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td width="15%" class="titleLabel">Created Date</td><td width="35%"><s:property value="bordereauResult.CreatedDate" /></td>
                    <td width="15%" class="titleLabel">Created By</td><td width="35%"><s:property value="bordereauResult.CreatedBy.DisplayName" /></td>
                </tr>
                <tr>
                    <td width="15%" class="titleLabel">Status</td><td width="35%"><s:property value="bordereauStatus" /></td>
                    <td width="15%" class="titleLabel">Total Claims</td><td width="35%"><s:property value="totalClaim" /></td>
                </tr>
                <tr>
                    <td width="15%" class="titleLabel">Description</td>
                    <td colspan="3"><s:property value="bordereauStatusDesc" /></td>
                </tr>
                <tr>
                    <td width="15%" class="titleLabel">Messages</td>
                    <td colspan="3">
                        <ul class="bordereauErrorMessage">
                            <s:iterator id="bordereauMsg" value="bordereauResult.Message" status="stat">
                                <li><s:property value="#bordereauMsg" /></li>
                            </s:iterator>
                        </ul>
                    </td>
                </tr>
            </table>

        </div>
        <div class="bordereauResultHolder">

            <table class="UploadStatusTable" cellpadding="0" cellspacing="0" border="0">

                <tr>
                    <th width="30px"></th>
                    <th width="1%" nowrap="true">Supplier Reference&nbsp;&nbsp;</th>
                    <th>Claim Status</th>
                    <th>Process Status</th>
                    <th>Remark</th>
                </tr>

                <s:iterator id="next" value="bordereauResult.ClaimResult" status="stat">

                    <s:if test="%{#next.ClaimParseStatus.toString()=='newInvoice'}">
                        <tr class="<s:property value="#next.claim.status"/>" valign="top">
                        </s:if>
                        <s:elseif test="%{#next.ClaimParseStatus.toString()=='newClaim'}">
                        <tr class="<s:property value="#next.claim.status"/>" valign="top">
                        </s:elseif>
                        <s:elseif test="%{#next.ClaimParseStatus=='ClaimNotEditable'}">
                        <tr class="<s:property value="#next.claim.status"/>" valign="top">
                        </s:elseif>
                        <s:elseif test="%{#next.ClaimParseStatus.toString()=='existClaim'}">
                        <tr class="<s:property value="#next.claim.status"/>" valign="top">
                        </s:elseif>
                        <s:elseif test="%{#next.ClaimParseStatus.toString()=='existInvoice'}">
                        <tr class="ErrorRow" valign="top">
                        </s:elseif>
                        <s:elseif test="%{#next.ClaimParseStatus.toString()=='invalidSchema'}">
                        <tr class="ErrorRow" valign="top">
                        </s:elseif>
                        <s:else>
                        <tr valign="top">
                        </s:else>

                        <td><s:property value="{#stat.index + 1}" /></td>
                        <td><s:property value="#next.claim.choReference" /><span>&nbsp;</span></td>
                        <td><s:property value="#next.ClaimStatus" /><span>&nbsp;</span></td>
                        <td><s:property value="#next.ProcessStatus" /><span>&nbsp;</span></td>
                        <td><s:property value="#next.UploadedStatus" /><span>&nbsp;</span></td>

                    </tr>

                    <s:if test="#next.Message.size() > 0">
                        <tr>
                            <td colspan="5">
                                <ul>
                                    <s:iterator id="remark" value="#next.Message">
                                        <s:if test="#remark.length() > 0">
                                            <li><s:property value="#remark" /></li>
                                        </s:if>
                                    </s:iterator>
                                </ul>
                            </td>
                        </tr>
                    </s:if>

                </s:iterator>
            </table>
        </div>

    </s:else>
</div>
