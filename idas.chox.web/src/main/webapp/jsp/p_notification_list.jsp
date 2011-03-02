<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    /***********************************************************************************
     * ACKNOWLEDGE NOTIFICATION
     ***********************************************************************************/
    function acknowledgeNotification(notificationId)
    {
        var url = "<%= request.getContextPath()%>/prv/p/acknowledgeNotification.action";
        var param = {"notificationId" : notificationId,"id": <s:property value="id" />};
        ajax.loadHtml2(url,param,pageRefresh,function(data){
            $("div#noteAcknowledgeDiv").html(data);
        });
    }
    
</script>

<s:if test="hasNotifications">
    <div class="chox-claim-header x-panel-bwrap chox-form-container">
        <fieldset class="x-fieldset">
            <s:if test="!isCHO">
                <legend>Hire Update Notification/Warning</legend>


                <s:if test="isAllNotationStatus" >
                    <div style="display:none">
                        <div id="noteAcknowledgeDiv ">
                        <div class="status-info">
                            Acknowledging all notes will remove the claim form the ‘Hire Update Notifications/Warnings’ queue, acknowledging a note will keep a record of the hire update/warning, removing the note will remove the note permanently.
                        </div>
                        <div  class="status-warning listContainer">

                            <ul>
                                <s:iterator value="filteredNotifications">
                                    <li><s:property value="message"/> - <s:date name="createdDate" format="dd/MM/yyyy HH:mm:ss"  /> <s:if test="isClaimNotificationEditable">(<a href='javascript:removeNotification(<s:property value="id"/>);'>Remove note from list</a>)</s:if>
                                        <s:if test="isClaimNotificationEditable && !isacknowledged ">(<a href='javascript:acknowledgeNotification(<s:property value="id"/>);'>Acknowledge note</a>)</s:if>
                                        <s:else>(Note acknowledged)</s:else>

                                    </li>
                                </s:iterator>
                            </ul>
                        </div>
                        </div>
                         <div>
                            <div>
                                <s:if test="isClaimNotificationEditable">
                                    <input type="button" value="Remove All" onclick='javascript:removeNotification(-1);' />
                                </s:if>
                                <s:if test="!isCHO">
                                    <input type="button" value="Acknolwedge All" onclick='javascript:acknowledgeNotification(-1);' />
                                </s:if>
                            </div>
                        </div>

                    
                   </div>
                </s:if>
                <s:else>
                    <div>
                        <div class="status-info">
                            Acknowledging all notes will remove the claim form the ‘Hire Update Notifications/Warnings’ queue, acknowledging a note will keep a record of the hire update/warning, removing the note will remove the note permanently.
                        </div>
                        <div id="noteAcknowledgeDiv " class="status-warning listContainer">

                            <ul>
                                <s:iterator value="filteredNotifications">
                                    <li><s:property value="message"/> - <s:date name="createdDate" format="dd/MM/yyyy HH:mm:ss"  /> <s:if test="isClaimNotificationEditable">(<a href='javascript:removeNotification(<s:property value="id"/>);'>Remove note from list</a>)</s:if>
                                        <s:if test="isClaimNotificationEditable && !isacknowledged ">(<a href='javascript:acknowledgeNotification(<s:property value="id"/>);'>Acknowledge note</a>)</s:if>
                                        <s:else>(Note acknowledged)</s:else>

                                    </li>
                                </s:iterator>
                            </ul>
                        </div>
                        <div>
                            <div>
                                <s:if test="isClaimNotificationEditable">
                                    <input type="button" value="Remove All" onclick='javascript:removeNotification(-1);' />
                                </s:if>
                                <s:if test="!isCHO">
                                    <input type="button" value="Acknolwedge All" onclick='javascript:acknowledgeNotification(-1);' />
                                </s:if>
                            </div>
                        </div>
                    </div>

                </s:else>

            </s:if>

            <s:else>
                <legend>Liability Update Notification</legend>
                <div class="status-warning listContainer">
                    <ul>
                        <s:iterator value="filteredNotifications">
                            <li><s:property value="message"/> <s:if test="isClaimNotificationEditable">(<a href='javascript:removeNotification(<s:property value="id"/>);'>Remove note from list</a>)</s:if></li>
                        </s:iterator>
                    </ul>
                </div>

                <div>

                    <div>
                        <s:if test="isClaimNotificationEditable">
                            <input type="button" value="Remove All" onclick='javascript:removeNotification(-1);' />
                        </s:if>
                        <s:if test="!isCHO">
                            <input type="button" value="Acknolwedge All" onclick='javascript:acknowledgeNotification(-1);' />
                        </s:if>
                    </div>
                </div>
            </s:else>

        </fieldset>
    </div>
</s:if>