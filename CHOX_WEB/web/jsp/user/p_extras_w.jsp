
<%@ taglib uri="/struts-tags" prefix="s" %>
<form id="formUpdateIncident" action="user/updateExtra.action" class="entity-form">
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
                <input type="submit" value="Save Changes" /><div class="chox-update-result"></div>
            </div>                                            
        </div>
    </fieldset>
</form>