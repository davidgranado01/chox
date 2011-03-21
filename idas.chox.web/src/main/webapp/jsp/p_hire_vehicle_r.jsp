<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Hire Vehicle Details</legend>
    <div style="display:none" class="form-container" id="hireVehicleDetailRId">
        <table class="chox-table-form">
            <s:if test="tpiClaim">
        <tr>
            <td><label class="std-label-ro">
                Courtesy Car Provided?</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="courtesyCarProvidedDesc" /></label></td></tr>

            </s:if>
        <tr>
            <td><label class="std-label-ro">
                Manufacturer</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="vehicleManufacturer" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Model</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="vehicleModel" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Registration</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="vehicleRegistration" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Replacement Vehicle Class</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="vehicleClass.name" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Hire Start</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date format="dd/MM/yyyy HH:mm" name="rentalStart" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Hire End</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:date format="dd/MM/yyyy HH:mm" name="rentalEnd" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                Reason For Collection</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="collectionReason" /></label></td></tr>
        <tr>
            <td><label class="std-label-ro">
                No. Days Hire</label></td>
            <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="days" /></label></td>
        </tr>
        </table>
        <hr width="80%"/>
        <div>
            <a  href="http://www.hpicheck.com/" target="_blank"><img align="right" src="<%= request.getContextPath()%>/images/logo-hpi.png" style="display: inline;" alt="HPI" width="80" height="60" border="0"/></a>
            <label>HPI Check</label>
            <s:if test="hpiError != null">
                <label class="std-label-small">&nbsp;&nbsp;&nbsp;(HPI Check information not available)</label>
            </s:if>
            <table class="chox-table-form">
              <tr>
                <td><label class="std-label-ro">Vehicle Manufacturer</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="hpiVehicleManufacturer" /></label></td>
              </tr>
              <tr>
                <td><label class="std-label-ro">Vehicle Model</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="hpiVehicleModel" /></label></td>
              </tr>
              <tr>
                <td><label class="std-label-ro">Year of Manufacture</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="hpiVehicleYear" /></label></td>
              </tr>
              <tr>
                <td><label class="std-label-ro">Date of Registration</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="hpiFirstRegistration" /></label></td>
              </tr>
              <tr>
                <td><label class="std-label-ro">Engine Capacity</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="hpiVehicleCapacity" /></label></td>
              </tr>
              <tr>
                <td><label class="std-label-ro">Door Plan</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="hpiVehicleDoorplan" /></label></td>
              </tr>
              <tr>
                <td><label class="std-label-ro">Transmission</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="hpiVehicleTransmission" /></label></td>
              </tr>
            </table>
        </div>
    </div>
</fieldset>
