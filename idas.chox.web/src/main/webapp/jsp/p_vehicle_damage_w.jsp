<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<form id="f6" action="dummyAction">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>  
    <fieldset class="x-fieldset partial">
        <legend>Customer Vehicle Damage</legend>

        <div class="form-container">
            <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Description</label>
                <textarea class="chox-tta" id="CVDDescription" cols="20" rows="5"></textarea></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Usable?</label>
                <input type="checkbox" class="chox-tcb" id="CVDUsable" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Initial ECD</label>
                <input type="text" class="chox-ttxt" id="CVDECD" /></div>
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>
            <div class="chox-form-submit-result">&nbsp;</div>                                            
        </div>
    </fieldset>
</form>