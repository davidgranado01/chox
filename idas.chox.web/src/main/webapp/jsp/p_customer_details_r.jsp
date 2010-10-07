<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="x-fieldset">
    <legend>Customer Details</legend>
    <div style="display:none" class="form-container" id="customerDetailsRId">
        <table class="chox-table-form">
        <tr>
            <td><label class="std-label-ro">
                Title</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="title" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                First Name(s)</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="firstName" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Surname</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="lastName" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Address 1</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address1" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Address 2</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address2" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Address 3</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address3" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Address 4</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address4" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Address 5</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="address5" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Postcode</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="postcode" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Telephone Day</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="telephoneDay" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Telephone Evening</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="telephoneEvening" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Email</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="email" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Age</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="age" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Occupation</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="occupation" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Policy Usage</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="policyUsage" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Insurer</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="insurerName" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Policy Number</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="policyNumber" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Claim Number</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="claimReference" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Comprehensive</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="isComprehensiveDesc" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Vehicle Manufacturer</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="vehicleManufacturer" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Vehicle Model</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="vehicleModel" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Vehicle Class</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="vehicleClass.Name" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Vehicle Registration Number</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="vehicleRegistration" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Vehicle Year of Manufacture</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="vehicleYear" /></label></td>
        </tr>
        <tr>
            <td><label class="std-label-ro">
                Vehicle Location</label></td>
             <td>&nbsp;</td>
            <td><label class="std-data-ro"><s:property value="location" /></label></td>
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


