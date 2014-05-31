<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fieldset class="x-fieldset">
    <legend>Estimate complete Date (ECD)</legend>
    <s:iterator value="ecdItems" >
        <div style="display:none" class="form-container">
            <div class="chox-form-item">
                <label class="std-label-ro">
                    Estimate complete Date</label>
                <label class="std-data-ro"><s:property value="ecdDate" /></label></div>
            <div class="chox-form-item">
                <label class="std-label-ro">
                    Reason</label>
                <label class="std-data-ro"><s:property value="reason" /></label></div>
            <div class="chox-form-item">
                <label class="std-label-ro">
                    Supporting Note</label>
                <label class="std-data-ro"><c:out value='supportingNote' /></label></div>
        </div>

    </s:iterator>

</fieldset>
