<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){
        
        var form = $("form#formUpdateExtrasFORM");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        
        form.validate(
        {
            errorLabelContainer: "#EXTmessageBox",
            rules: {
                cdwFee:{required:true, number:true},
                cdwQty:{required:true, digits:true},
                automaticFee:{required:true, number:true},
                automaticQty:{required:true, digits:true},
                additionalDriverFee:{required:true, number:true},
                additionalDriverQty:{required:true, digits:true},
                satNavFee:{required:true, number:true},
                satNavQty:{required:true, digits:true},
                estateFee:{required:true, number:true},
                estateQty:{required:true, digits:true},
                babySeatFee:{required:true, number:true},
                babySeatQty:{required:true, digits:true},
                towBarsFee:{required:true, number:true},
                towBarsQty:{required:true, digits:true},
                nonStandardInsurancePremiumFee:{required:true, number:true},
                nonStandardInsurancePremiumQty:{required:true, digits:true},
                adminFee:{required:true, number:true},
                adminQty:{required:true, digits:true},
                roofRackFee:{required:true, number:true},
                roofRackQty:{required:true, digits:true},
                dualControlFee:{required:true, number:true},
                dualControlQty:{required:true, digits:true},
                deliveryCollectionFee:{required:true, number:true},
                deliveryCollectionQty:{required: true, digits:true}
            },
            messages: {
                cdwFee :{required:"Please supply a valid value for 'Cdw Fee'", number:"Please supply a valid value for 'Cdw Fee'"},
                cdwQty:{required:"Please supply a valid value for 'Cdw Qty'", digits:"Please supply a valid value for 'Cdw Qty'"},
                automaticFee:{required:"Please supply a valid value for 'Automatic Fee'", number:"Please supply a valid value for 'Automatic Fee'"},
                automaticQty:{required:"Please supply a valid value for 'Automatic Qty'", digits:"Please supply a valid value for 'Automatic Qty'"},
                additionalDriverFee:{required:"Please supply a valid value for 'Additional Driver Fee'", number:"Please supply a valid value for 'Additional Driver Fee'"},
                additionalDriverQty:{required:"Please supply a valid value for 'Additional Driver Qty'", digits:"Please supply a valid value for 'Additional Driver Qty'"},
                satNavFee:{required:"Please supply a valid value for 'Satnav Fee'", number:"Please supply a valid value for 'Satnav Fee'"},
                satNavQty:{required:"Please supply a valid value for 'Satnav Qty'", digits:"Please supply a valid value for 'Satnav Qty'"},
                estateFee:{required:"Please supply a valid value for 'Estate Fee'", number:"Please supply a valid value for 'Estate Fee'"},
                estateQty:{required:"Please supply a valid value for 'Estate Qty'", digits:"Please supply a valid value for 'Estate Qty'"},
                babySeatFee:{required:"Please supply a valid value for 'Baby-seat Fee'", number:"Please supply a valid value for 'Baby-seat Fee'"},
                babySeatQty:{required:"Please supply a valid value for 'Baby-seat Qty'", digits:"Please supply a valid value for 'Baby-seat Qty'"},
                towBarsFee:{required:"Please supply a valid value for 'Tow-bars Fee'", number:"Please supply a valid value for 'Tow-bars Fee'"},
                towBarsQty:{required:"Please supply a valid value for 'Tow-bars Qty'", digits:"Please supply a valid value for 'Tow-bars Qty'"},
                nonStandardInsurancePremiumFee:{required:"Please supply a valid value for 'Non-Standard Insurance Premium Fee'", number:"Please supply a valid value for 'Non-Standard Insurance Premium Fee'"},
                nonStandardInsurancePremiumQty:{required:"Please supply a valid value for 'Non-Standard Insurance Premium Qty'", digits:"Please supply a valid value for 'Non-Standard Insurance Premium Qty'"},
                adminFee:{required:"Please supply a valid value for 'Admin Fee'", number:"Please supply a valid value for 'Admin Fee'"},
                adminQty:{required:"Please supply a valid value for 'Admin Qty'", digits:"Please supply a valid value for 'Admin Qty'"},
                roofRackFee:{required:"Please supply a valid value for 'Roofrack Fee'", number:"Please supply a valid value for 'Roofrack Fee'"},
                roofRackQty:{required:"Please supply a valid value for 'Roofrack Qty'", digits:"Please supply a valid value for 'Roofrack Qty'"},
                dualControlFee:{required:"Please supply a valid value for 'Dual Control Fee'", number:"Please supply a valid value for 'Dual Control Fee'"},
                dualControlQty:{required:"Please supply a valid value for 'Dual Control Qty'", digits:"Please supply a valid value for 'Dual Control Qty'"},
                deliveryCollectionFee:{required:"Please supply a valid value for 'Delivery Collection Fee'", number:"Please supply a valid value for 'Delivery Collection Fee'"},
                deliveryCollectionQty:{required:"Please supply a valid value for 'Delivery Collection Qty'", digits:"Please supply a valid value for 'Delivery Collection Qty'"}
            }
        });

        ui.ajaxForm(form,null,'html');
        
    });

</script>

<form id="formUpdateExtrasFORM" name="formUpdateExtrasFORM" action="<%=request.getContextPath()%>/prv/p/updateExtra.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <input name="currentVersion" type="hidden" value="<s:property value="version" />" />
    <fieldset class="x-fieldset partial">
        <legend>Extras</legend>
        <div class="form-container" id="extrasWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">CDW Fee<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="cdwFee" value="<s:property value="cdwFee" />"/>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">CDW Quantity<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="cdwQty" value="<s:property value="cdwQty" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Automatic Fee<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="automaticFee" value="<s:property value="automaticFee" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Automatic Quantity<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="automaticQty" value="<s:property value="automaticQty" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Additional Driver Fee<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="additionalDriverFee" value="<s:property value="additionalDriverFee" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Additional Driver Quantity<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="additionalDriverQty" value="<s:property value="additionalDriverQty" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Sat Nav Fee<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="satNavFee" value="<s:property value="satNavFee" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Sat Nav Quantity<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="satNavQty" value="<s:property value="satNavQty" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Estate Fee<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="estateFee" value="<s:property value="estateFee" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Estate Quantity<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="estateQty" value="<s:property value="estateQty" />"/>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Baby Seat Fee<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="babySeatFee" value="<s:property value="babySeatFee" />"/>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Baby Seat Quantity<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="babySeatQty" value="<s:property value="babySeatQty" />"/>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Tow Bars Fee<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="towBarsFee" value="<s:property value="towBarsFee" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Tow Bars Quantity<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="towBarsQty" value="<s:property value="towBarsQty" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Non-standard Risk Ins. Premium Fee<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="nonStandardInsurancePremiumFee" value="<s:property value="nonStandardInsurancePremiumFee" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Non-standard Risk Ins. Premium Qty<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="nonStandardInsurancePremiumQty" value="<s:property value="nonStandardInsurancePremiumQty" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Cover Note Required For<br/>Customer's Own Insurance Policy?</label>
            <s:checkbox name="coverNoteRequired" /></div>
            <div class="chox-form-item"><label class="chox-form-std-label">&nbsp;</label></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Admin Fee<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="adminFee" value="<s:property value="adminFee" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Admin Quantity<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="adminQty" value="<s:property value="adminQty" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Roof Rack Fee<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="roofRackFee" value="<s:property value="roofRackFee" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Roof Rack Quantity<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="roofRackQty" value="<s:property value="roofRackQty" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Dual Control Fee<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="dualControlFee" value="<s:property value="dualControlFee" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Dual Control Quantity<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="dualControlQty" value="<s:property value="dualControlQty" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Delivery Collection Fee<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="deliveryCollectionFee" value="<s:property value="deliveryCollectionFee" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Delivery Collection Fee Quantity<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="deliveryCollectionQty" value="<s:property value="deliveryCollectionQty" />"/></div>
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>

            <div id="EXTmessageBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    <!--s:token/-->
</form>