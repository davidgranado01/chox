<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(document).ready(function(){
            var insurerId = '<s:property value="insurer.id"/>';
            var claimId = '<s:property value="id"/>';
            var wgrpJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                [
                    {name:'text'},
                    {name:'value'}
                ]
            });

            var workgroupStore = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy
                ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer2.action", method:'GET', params : {"claimId":claimId}}),
                reader: wgrpJsonReader
            });

            var workgroupCombo = new Ext.form.ComboBox({
                                store: workgroupStore,
                                width: 200,
                                renderTo: 'escalateWorkgroupDiv',
                                valueField: 'text',
                                id: 'escalateWorkgroupComboId',
                                hiddenName: 'escalateWorkgroupId',
                                displayField:'value',
                                typeAhead: true,
                                mode: 'local',
                                triggerAction: 'all',
                                emptyText: '--- Please Select ---',
                                listWidth: 200,
                                selectOnFocus: true,
                                forceSelection : true,
                                listeners: {blur: function () {
                                                if(this.getRawValue() == "") {
                                                    this.clearValue(); this.reset();
                                                }
                                            },
                                            specialkey:function (el, e) {
                                                        if(e.keyCode == e.ENTER) {
                                                            e.preventDefault();
                                                        }
                                            }
                                           }
            });
            workgroupStore.load({ params : {"claimId":claimId}});
            
            $("#formEscalateClaimAction").submit(function() {
                if ($("#escalateWorkgroupComboId").val() != "--- Please Select ---") {
                  $("#EscalateClaimMessageBox").text("").show();
                  return true;
                }
                $("#EscalateClaimMessageBox").text("You must supply a value for 'Workgroup'").show();
                return false;
             });
    });
    
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/escalatedUnassignedClaim.action" method="post" id="formEscalateClaimAction" name="formEscalateClaimAction">
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
                                <td width="200px" align="right"><label>Workgroup : </label></td>
                                <td width="100%"><div id="escalateWorkgroupDiv"/></td>
                            </tr>
                            <tr>
                                <td colspan="2" class="choice" nowrap align="center">
                                    <input id="assignOnly" type="submit" value="Re-assign Workgroup"/>
                                </td>
                            </tr>
                        </table>
                        <div class="action-error-msg" id="EscalateClaimMessageBox"></div>
                    </div>
                </div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>