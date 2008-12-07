
<%@ taglib uri="/struts-tags" prefix="s" %>



<script language="JavaScript">

        $(document).ready(function(){        
        
        
            $("#formUpdateExtrasFORM").validate(
            {
                errorLabelContainer: "#EXTmessageBox",                
                rules: {
                    
                    cdwFee:{required:true, number:true},
                    cdwQty:{required:true, digits:true},
                    automaticFee:{required:true, number:true},
                    automaticQty:{required:true, digits:true},
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
                    
                    cdwFee :{required:"Please supply a valid value for Cdw Fee", number:"Please supply a valid value for Cdw Fee"},
                    cdwQty:{required:"Please supply a valid value for Cdw Qty", digits:"Please supply a valid value for Cdw Qty"},
                    automaticFee:{required:"Please supply a valid value for Automatic Fee", number:"Please supply a valid value for Automatic Fee"},
                    automaticQty:{required:"Please supply a valid value for Automatic Qty", digits:"Please supply a valid value for Automatic Qty"},
                    satNavFee:{required:"Please supply a valid value for Satnav Fee", number:"Please supply a valid value for Satnav Fee"},
                    satNavQty:{required:"Please supply a valid value for Satnav Qty", digits:"Please supply a valid value for Satnav Qty"},
                    estateFee:{required:"Please supply a valid value for Estate Fee", number:"Please supply a valid value for Estate Fee"},
                    estateQty:{required:"Please supply a valid value for Estate Qty", digits:"Please supply a valid value for Estate Qty"},
                    babySeatFee:{required:"Please supply a valid value for Baby-seat Fee", number:"Please supply a valid value for Baby-seat Fee"},
                    babySeatQty:{required:"Please supply a valid value for Baby-seat Qty", digits:"Please supply a valid value for Baby-seat Qty"},
                    towBarsFee:{required:"Please supply a valid value for Tow-bars Fee", number:"Please supply a valid value for Tow-bars Fee"},
                    towBarsQty:{required:"Please supply a valid value for Tow-bars Qty", digits:"Please supply a valid value for Tow-bars Qty"},
                    nonStandardInsurancePremiumFee:{required:"Please supply a valid value for Non-Standard Insurance Premium Fee", number:"Please supply a valid value for Non-Standard Insurance Premium Fee"},
                    nonStandardInsurancePremiumQty:{required:"Please supply a valid value for Non-Standard Insurance Premium Qty", digits:"Please supply a valid value for Non-Standard Insurance Premium Qty"},
                    adminFee:{required:"Please supply a valid value for Admin Fee", number:"Please supply a valid value for Admin Fee"},
                    adminQty:{required:"Please supply a valid value for  Admin Qty", digits:"Please supply a valid value for Admin Qty"},
                    roofRackFee:{required:"Please supply a valid value for Roofrack Fee", number:"Please supply a valid value for Roofrack Fee"},
                    roofRackQty:{required:"Please supply a valid value for Roofrack Qty", digits:"Please supply a valid value for Roofrack Qty"},
                    dualControlFee:{required:"Please supply a valid value for Dual Control Fee", number:"Please supply a valid value for Dual Control Fee"},
                    dualControlQty:{required:"Please supply a valid value for Dual Control Qty", digits:"Please supply a valid value for Dual Control Qty"},
                    deliveryCollectionFee:{required:"Please supply a valid value for Delivery Collection Fee", number:"Please supply a valid value for Delivery Collection Fee"},
                    deliveryCollectionQty:{required:"Please supply a valid value for Delivery Collection Qty", digits:"Please supply a valid value for Delivery Collection Qty"}                    
                    
                },
                
                submitHandler: function(form) {
                    $(form).ajaxSubmit(globalEntityFormOptions);
                }                

                
            }); 
            
        });  




</script>




<form id="formUpdateExtrasFORM" name="formUpdateExtrasFORM" action="user/updateExtra.action" class="XXentity-form">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
    <fieldset class="x-fieldset">
        <legend>Extras</legend>
        <div style="display:none" class="form-container">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                CDW Fee</label>
            <input type="text" class="chox-ttxt"  name="cdwFee" value='<s:property value="cdwFee" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                CDW Quantity</label>
            <input type="text" class="chox-ttxt"  name="cdwQty" value='<s:property value="cdwQty" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Automatic Fee</label>
            <input type="text" class="chox-ttxt"  name="automaticFee" value='<s:property value="automaticFee" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Automatic Quantity</label>
            <input type="text" class="chox-ttxt"  name="automaticQty" value='<s:property value="automaticQty" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Sat Nav Fee</label>
            <input type="text" class="chox-ttxt"  name="satNavFee" value='<s:property value="satNavFee" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Sat Nav Quantity</label>
            <input type="text" class="chox-ttxt"  name="satNavQty" value='<s:property value="satNavQty" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Estate Fee</label>
            <input type="text" class="chox-ttxt"  name="estateFee" value='<s:property value="estateFee" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Estate Quantity</label>
            <input type="text" class="chox-ttxt"  name="estateQty" value='<s:property value="estateQty" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Baby Seat Fee</label>
            <input type="text" class="chox-ttxt"  name="babySeatFee" value='<s:property value="babySeatFee" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Baby Seat Quantity
                </label>
            <input type="text" class="chox-ttxt"  name="babySeatQty" value='<s:property value="babySeatQty" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Tow Bars Fee</label>
            <input type="text" class="chox-ttxt"  name="towBarsFee" value='<s:property value="towBarsFee" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Tow Bars Quantity</label>
            <input type="text" class="chox-ttxt"  name="towBarsQty" value='<s:property value="towBarsQty" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Non-standard Risk Ins. Premium Fee</label>
            <input type="text" class="chox-ttxt"  name="nonStandardInsurancePremiumFee" value='<s:property value="nonStandardInsurancePremiumFee" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Non-standard Risk Ins. Premium Qty</label>
            <input type="text" class="chox-ttxt"  name="nonStandardInsurancePremiumQty" value='<s:property value="nonStandardInsurancePremiumQty" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Admin Fee</label>
            <input type="text" class="chox-ttxt"  name="adminFee" value='<s:property value="adminFee" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Admin Quantity</label>
            <input type="text" class="chox-ttxt"  name="adminQty" value='<s:property value="adminQty" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Roof Rack Fee</label>
            <input type="text" class="chox-ttxt"  name="roofRackFee" value='<s:property value="roofRackFee" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Roof Rack Quantity</label>
            <input type="text" class="chox-ttxt"  name="roofRackQty" value='<s:property value="roofRackQty" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Dual Control Fee</label>
            <input type="text" class="chox-ttxt"  name="dualControlFee" value='<s:property value="dualControlFee" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Dual Control Quantity</label>
            <input type="text" class="chox-ttxt"  name="dualControlQty" value='<s:property value="dualControlQty" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Delivery Collection Fee</label>
            <input type="text" class="chox-ttxt"  name="deliveryCollectionFee" value='<s:property value="deliveryCollectionFee" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Delivery Collection Fee Quantity</label>
            <input type="text" class="chox-ttxt"  name="deliveryCollectionQty" value='<s:property value="deliveryCollectionQty" />'/></div>
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>   
            <div id="EXTmessageBox" style="text-align:center"></div>   
            <div class="chox-form-submit-result">&nbsp;</div>   
        </div>
    </fieldset>
</form>