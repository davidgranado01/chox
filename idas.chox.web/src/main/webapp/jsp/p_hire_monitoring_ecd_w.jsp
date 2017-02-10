<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var ecdDateDateField;
    Ext.onReady(function() {

        ecdDateDateField = ui.unvalidatedDateField('ecdDate','<s:date format="dd/MM/yyyy" name="date" />','ecdDatePH');

        var form = $("#formAddNewHireMonitoringEcd");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        form.validate(
        {
            errorLabelContainer: "#ECDMessageBox",                
            rules: {
                reasonOfDelayId:{required:true},
                ecdDate:{required:true, dateITA:true},
                supportingNote:{required:true}
            },
            messages: {
                reasonOfDelayId: {
                    required:"You must supply a value for 'Reason for Delay'"
                },
                ecdDate: {
                    required:"You must supply a value for 'New ECD'",
                    dateITA:"You must supply valid date format for 'New ECD'"
                },
                supportingNote:{
                    required:"You must supply a value for 'Supporting Note'"
                }
            }
        });
        
        $("#hireMonitorModalClose").click(function(){ $("#hireMonitoringDetails").unblock();});

    });

    function onAfterEcdSubmit(responseText, statusText)  {  
        loadEcds();
        doResetForm();
    }
    
    function doResetForm(){
        document.formAddNewHireMonitoringEcd.ecdDate.value = "";
        document.formAddNewHireMonitoringEcd.reasonOfDelayId.value = "";
        document.formAddNewHireMonitoringEcd.supportingNote.value = "";
        document.formAddNewHireMonitoringEcd.updateInsurer.checked = false;
    }
    
    function doPopulateNote(){
        var reasonOfDelayId = $('#reasonOfDelayId :selected').val();
        $("#ECDSupportingNote").val(getReasonDescription(reasonOfDelayId));
    }
    
    function getReasonDescription(id){

    <s:iterator value="reasonOfDelay">
            if(id=="<s:property value="id"/>"){
                return "<s:property value="description"/>";
            }
    </s:iterator>

        }
    
    function addNewHireMonitoringEcd(){

        if($("#formAddNewHireMonitoringEcd").valid()){

            choxExtAjaxRequest({
                url:'/prv/p/addNewHireMonitoringEcd.action',
                params: {
                            ecdDate : ecdDateDateField.getRawValue(),
                            reasonOfDelayId : $('#reasonOfDelayId :selected').val(),
                            supportingNote : $("#ECDSupportingNote").val(),
                            name : $("#ecdActivityNameId").val(),
                            updateInsurer : $('form #hireMonitoringEcdisUpdateInsurerId').is(':checked') 
                        },
                callback : function(options,success,response  ){
                    var response = Ext.util.JSON.decode(response.responseText);
                    if (!response.success) {
                        Ext.MessageBox.show({
                            title: 'Error',
                            msg: response.errors,
                            width:300,
                            closable : false,
                            buttons: Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                    onAfterEcdSubmit();
                }
            });
        
        }
    }
</script>

<form id="formAddNewHireMonitoringEcd" name="formAddNewHireMonitoringEcd" class="XXentity-form">
    <s:hidden id="ecdActivityNameId" name="name" value="ecdUpdate"/>
    <fieldset class="x-fieldset partial">
        <legend>New/Revised ECD</legend>
        <div class="form-container" id="newRevisedECDWId">
            <s:if test="isECDFormVisible">
                <div class="chox-form-item">
                    <label class="chox-form-std-label" style="width:150px;">New ECD<span class="mandatory">*</span></label>
                    <span id="ecdDatePH"></span>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label" style="width:150px;">Reason for Delay<span class="mandatory">*</span></label>
                    <s:select 
                        name="reasonOfDelayId" id="reasonOfDelayId" list="reasonOfDelay"
                        listKey="id" listValue="name" headerKey=""
                        headerValue="-- Please Select --"
                        emptyOption="false" onchange="doPopulateNote();" cssClass="hm-reason-drop-down">
                    </s:select>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label" style="width:150px;">Supporting Note<span class="mandatory">*</span></label>
                    <textarea class="chox-tta" id="ECDSupportingNote" cols="30" rows="5" name="supportingNote"><s:property value="supportingNote" /></textarea>
                </div>
                <div class="chox-form-item-button">
                    <input type="button" id="hireMonitoringEcdSubmitButtonId" value="Save Changes" onclick="return addNewHireMonitoringEcd()"/>&nbsp;&nbsp;&nbsp;
                    <s:if test="isInsurer">
                        <s:checkbox disabled='true' id="hireMonitoringEcdisUpdateInsurerId" name="updateInsurer" /><label class="chox-form-std-label2">Update Insurer</label>
                    </s:if>
                    <s:elseif test="allowUpdateInsurer">
                        <s:checkbox id="hireMonitoringEcdisUpdateInsurerId" name="updateInsurer" /><label class="chox-form-std-label2">Update Insurer</label>
                    </s:elseif>
                </div>
                <div id="ECDMessageBox" class="action-error-msg"></div>
            </s:if>
            <s:else>
                <span id="ecdDatePH" style="visibility:hidden;"></span>
                <input type="hidden" name="reason"/>
                <input id="ECDSupportingNote" name="supportingNote" type="hidden"/>
            </s:else>
            <div id="ecdGridHolder"></div>
        </div>
    </fieldset>
</form>