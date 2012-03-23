//package idas.chox.web.actions;
//
//import idas.chox.core.hpi.*;
//import idas.chox.core.model.Claim;
//import idas.chox.core.model.ClaimType;
//import java.util.Date;
//import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import idas.chox.core.model.VehicleClass;
//import idas.chox.core.model.VehicleHire;
//import idas.chox.core.services.LookupService;
//import idas.chox.core.util.DateHelper;
//import idas.chox.service.security.ApplicationAccessibility;
//import idas.chox.web.VehicleClassComparator;
//import java.util.Collections;
//
///*
// *  This action class is instantiated in InvoiceRecalculationAction (This is not really a action calss but act like bean )
// *  When you use any service classes it needed to be set first in InvoiceRecalculationAction prepare() method before any services accessed. eg. vehicleHireAction.setClaimService(claimService); 
// *  the above line applies to super class of this class as well.
// */
//
//public class VehicleHireAction extends ClaimModelAction<VehicleHire> {
//
//    private static final Logger LOG = LoggerFactory.getLogger(VehicleHireAction.class);
//    private LookupService lookupService;
//    private String oldVRN;
//
//    public String getOldVRN() {
//        return oldVRN;
//    }
//
//    public void setLookupService(LookupService lookupService) {
//        this.lookupService = lookupService;
//    }
//
//    @Override
//    public VehicleHire loadModel() {
//
//        LOG.debug("VehicleHire loadModel is called");
//        VehicleHire vehicleHire = claim.getVehicleHire();
//        if (vehicleHire != null) {
//            LOG.debug("VehicleHire loadModel is not null ");
//            oldVRN = vehicleHire.getVehicleRegistration();
//            LOG.debug("VehicleHire loadModel is not null and old vrn is set up");
//            return vehicleHire;
//        }
//
//        oldVRN = "";
//        LOG.debug("VehicleHire loadModel is null ");
//        return new VehicleHire();
//
//    }
//
//    public boolean isTpiClaim() {
//        return ClaimType.isTPI(claim.getClaimType());
//    }
//
////    @Override
//    public String updateModel(Claim claim) {
//
//        if (!oldVRN.equalsIgnoreCase(model.getVehicleRegistration())) {
//            try {
//                LOG.debug("VRN has changed - performing HPI check/retrieval");
//                HpiResponse response = Hpi.getHpiInfo(model.getVehicleRegistration());
//                model.setHpiVehicleManufacturer(response.getManufacturer());
//                model.setHpiVehicleModel(response.getModel());
//                model.setHpiVehicleYear(response.getYear());
//                model.setHpiVehicleCapacity(response.getCapacity());
//                model.setHpiVehicleDoorplan(response.getDoorPlan());
//                model.setHpiVehicleTransmission(response.getTransmission());
//                model.setHpiFirstRegistration(response.getFirstRegistration());
//                model.setHpiError(null);
//            } catch (HpiException ex) {
//                LOG.warn("Error getting HPI info for vrn '{}': {}", claim.getCustomer().getVehicleRegistration(), ex.getMessage());
//                model.setHpiError(ex.getMessage());
//                model.setHpiVehicleManufacturer(null);
//                model.setHpiVehicleModel(null);
//                model.setHpiVehicleYear(null);
//                model.setHpiVehicleCapacity(null);
//                model.setHpiVehicleDoorplan(null);
//                model.setHpiVehicleTransmission(null);
//                model.setHpiFirstRegistration(null);
//            }
//        }
//        claim.setVehicleHire(model);
//        LOG.debug("vehicleHire set in the claim ");
//        return SUCCESS;
//    }
//
//    public void setVehicleClassId(int vehicleClassId) {
//    	if(model != null){
//	    	setVehicleClassIdOriginal(getVehicleClassId());
//	        if (model.getVehicleClass().getId() != vehicleClassId) {
//	            List<VehicleClass> vehicleClasses = this.lookupService.getVehicleClasses();
//	            for (VehicleClass vClass : vehicleClasses) {
//	                if (vClass.getId() == vehicleClassId) {
//	                    model.setVehicleClass(vClass);
//	                    break;
//	                }
//	            }
//	        }
//    	}
//    }
//
//    public int getVehicleClassId() {
//    	if(this.model != null && this.model.getVehicleClass() != null)
//    		return this.model.getVehicleClass().getId();
//    	return 0;
//    }
//
//    public List<VehicleClass> getVehicleClasses() {
//        List<VehicleClass> vehicleClasses = this.lookupService.getVehicleClasses();
//        Collections.sort(vehicleClasses, new VehicleClassComparator());
//
//        return vehicleClasses;
//    }
//
//    public int getVehicleClassIdOriginal() {
//    	if(this.model != null && this.model.getVehicleClassOriginal() != null)
//    		return this.model.getVehicleClassOriginal().getId();
//    	return 0;
//    }
//
//    public String getVehicleClassNameOriginal() {
//        if (getVehicleClassIdOriginal() != 0) {
//            return lookupService.getVehicleClassName(getVehicleClassIdOriginal());
//        } else {
//            return null;
//        }
//
//    }
//
//    public String getVehicleClassName() {
//        if (getVehicleClassId() != 0) {
//            return lookupService.getVehicleClassName(getVehicleClassId());
//        } else {
//            return null;
//        }
//
//    }
//
//    public void setVehicleClassIdOriginal(int vehicleClassId) {
//
//        if (vehicleClassId != getVehicleClassIdOriginal() && getVehicleClassIdOriginal() == 0) {
//
//            List<VehicleClass> vehicleClasses = this.lookupService.getVehicleClasses();
//            for (VehicleClass vClass : vehicleClasses) {
//                if (vClass.getId() == vehicleClassId) {
//                    model.setVehicleClassOriginal(vClass);
//                    break;
//                }
//            }
//        }
//
//    }
//
//    @Override
//    String getTabName() {
//        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
//    }
//
//    public String getRentalStartTime() {
//    	if(model == null)
//    		return null;
//        return DateHelper.getTimeFormat().format(model.getHireStart());
//    }
//
//    public void setRentalStartTime(String time) {
//        if (model != null) {
//            try {
//                Date a = model.getHireStart();
//                Date b = DateHelper.getTimeFormat().parse(time);
//                model.setHireStart(DateHelper.mergeTimeToDate(a, b));
//            } catch (Exception ex) {
//                LOG.error("Error setting Rental Start-time to '{}': {}", time, ex.getMessage());
//            }
//        }
//    }
//
//    public String getRentalEndTime() {
//        return DateHelper.getTimeFormat().format(model.getHireEnd());
//    }
//
//    public void setRentalEndTime(String time) {
//        if (model != null) {
//            try {
//                Date a = model.getHireEnd();
//                Date b = DateHelper.getTimeFormat().parse(time);
//                model.setHireEnd(DateHelper.mergeTimeToDate(a, b));
//            } catch (Exception ex) {
//                LOG.error("Error setting Rental End-time to '{}': {}", time, ex.getMessage());
//            }
//        }
//
//    }
//
//    public String getRentalStartTimeOriginal() {
//        return DateHelper.getTimeFormat().format(model.getHireStart_original());
//    }
//
//    public void setRentalStartTimeOriginal(String time) {
//        if (model != null) {
//            try {
//                Date a = model.getHireStart_original();
//                Date b = DateHelper.getTimeFormat().parse(time);
//                model.setHireStart_original(DateHelper.mergeTimeToDate(a, b));
//            } catch (Exception ex) {
//                LOG.error("Error setting Rental Start-time-original to '{}': {}", time, ex.getMessage());
//            }
//        }
//    }
//
//    public String getRentalEndTimeOriginal() {
//        return DateHelper.getTimeFormat().format(model.getHireEnd_original());
//        
//    }
//
//    public void setRentalEndTimeOriginal(String time) {
//        if (model != null) {
//            try {
//                Date a = model.getHireEnd_original();
//                Date b = DateHelper.getTimeFormat().parse(time);
//                model.setHireEnd_original(DateHelper.mergeTimeToDate(a, b));
//            } catch (Exception ex) {
//                LOG.error("Error setting Rental End-time-original to '{}': {}", time, ex.getMessage());
//            }
//        }
//
//    }
//}
