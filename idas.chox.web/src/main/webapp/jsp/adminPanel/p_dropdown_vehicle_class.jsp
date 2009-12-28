<%@ taglib prefix="s" uri="/struts-tags"%>

<label class="chox-form-std-label">Vehicle Class</label>
<s:select
    id="vehicleClassId"
    name="vehicleClassId"
    list="vehicleClasses"
    listKey="id"
    listValue="name"
    headerKey="-1"
    headerValue="-- Please Select --"
    emptyOption="false">
</s:select>