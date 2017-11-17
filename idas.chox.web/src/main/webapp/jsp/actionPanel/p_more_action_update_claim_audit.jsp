<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function () {

        Ext.override(Ext.form.NumberField, {
            setValue: function (v) {
                var dp = this.decimalPrecision;
                if (dp < 0 || !this.allowDecimals) {
                    dp = 0;
                }
                v = this.fixPrecision(v);
                v = Ext.isNumber(v) ? v : parseFloat(String(v).replace(this.decimalSeparator, "."));
                v = isNaN(v) ? '' : String(v.toFixed(dp)).replace(".", this.decimalSeparator);
                return Ext.form.NumberField.superclass.setValue.call(this, v);
            }
        });

//        var claimAuditReviewId = '<s:property value="claimAuditReview.id"/>';
//        var isAuditReviewAlreadyExists = (claimAuditReviewId !== null && claimAuditReviewId !== '') ? true : false;
        var isAuditReviewAlreadyExists = '<s:property value="claimAuditReviewSaved"/>' === 'true' ? true : false;
        
        var whoManagedRepairData = ['Insurer', 'CHO', 'TPI'];
        var yesNoData = [['Yes', 1], ['No', 2]];
        var reasonHDNotAcceptableData = ['Delay In Off-hire', 'Delay In Inspection', 'Delay In Authorisation', 'TP Not Mitigated'];

        var yesNoDataStore = new Ext.data.ArrayStore({
            fields: [
                {name: 'text', type: 'string'},
                {name: 'value', type: 'string'}
            ]
        });

        yesNoDataStore.loadData(yesNoData);

        var getYesNoDataStoreValue = function (modelValue) {

            if (modelValue !== null && modelValue !== '') {
                return modelValue === 'true' ? 1 : 2;
            }
        };

        var claimTypeJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                    [
                        {name: 'text'},
                        {name: 'value'}
                    ]
        });

        var claimTypeStore = new choxDataStore({
            url: "/prv/p/getAuditReviewHireTypes.action",
            reader: claimTypeJsonReader
            , listeners: {load: function () {
                    if (isAuditReviewAlreadyExists) {
                        if ('<s:property value="claimAuditReview.claimType"/>' !== null) {
                            claimTypesCombo.setValue('<s:property value="auditReviewClaimTypeFromModel"/>');
                        }
                    } else {
                        claimTypesCombo.setValue('<s:property value="auditReviewClaimType"/>');
                    }
                }}
        });

        claimTypeStore.load();

        var vehicleClassJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                    [
                        {name: 'text'},
                        {name: 'value'}
                    ]
        });

        var vehicleClassStore = new choxDataStore({
            url: "/prv/p/getAvailableVehicleClasses.action",
            reader: vehicleClassJsonReader
            , listeners: {load: function () {
                    if (isAuditReviewAlreadyExists) {
                        if ('<s:property value="claimAuditReview.customerVehicleClass"/>' !== null) {
                            customerVehicleClassCombo.setValue('<s:property value="claimAuditReview.customerVehicleClass.id"/>');
                        }
                        if ('<s:property value="claimAuditReview.hireVehicleClass"/>' !== null) {
                            hireVehicleClassCombo.setValue('<s:property value="claimAuditReview.hireVehicleClass.id"/>');
                        }
                    } else {
                        customerVehicleClassCombo.setValue('<s:property value="customer.vehicleClass.id"/>');
                        hireVehicleClassCombo.setValue('<s:property value="vehicleHire.vehicleClass.id"/>');
                    }

                }}
        });

        vehicleClassStore.load();

        $("form#formClaimAuditReview").validate(
                {
                    ignore: [],
                    errorLabelContainer: "#auditReviewMessageBox",
                    rules: {
                        claimTypeId: {required: true},
                        whoManagedRepair: {required: true},
                        totalLossId: {required: true},
                        customerVehicleClassId: {required: true},
                        hireVehicleClassId: {required: true},
                        hireDuration: {required: true, number:true},
                        hireDurationAcceptableId: {required: true},
                        totalHireCost: {required: true, number:true},
                        hireLeakageId: {required: true},
                        totalRepairCost: {required: true, number:true},
//                        repairCostExceedsEngRecId: {required: true},
                        penaltyChargesPaid: {required: true, number:true},
//                        withinABPGuidelinesId: {required: true},
                        storageClaimedId: {required: true},
                        recoveryClaimedId: {required: true}
                    },
                    messages: {
                        claimTypeId: {required: "You must make a selection for 'Claim Type'"},
                        whoManagedRepair: {required: "You must make a selection for 'Who managed repair?'"},
                        totalLossId: {required: "You must make a selection for 'Total Loss?'"},
                        customerVehicleClassId: {required: "You must make a selection for 'Customers Vehicle Class'"},
                        hireVehicleClassId: {required: "You must make a selection for 'Hire Vehicle Class'"},
                        hireDuration: {required: "You must supply a value for 'Hire Duration'", number:"'Hire Duration' must be numeric"},
                        hireDurationAcceptableId: {required: "You must make a selection for 'Hire Duration Acceptable?'"},
                        totalHireCost: {required: "You must supply a value for 'Total Hire Costs'", number:"'Total Hire Costs' must be numeric"},
                        hireLeakageId: {required: "You must make a selection for 'Hire Leakage?'"},
                        totalRepairCost: {required: "You must supply a value for 'Total Repair Cost'", number:"'Total Repair Costs' must be numeric"},
//                        repairCostExceedsEngRecId: {required: "You must make a selection for 'Repair Cost Exceeds Engineers Recommendations?'"},
                        penaltyChargesPaid: {required: "You must supply a value for 'Penalty Charges paid'", number:"'Penalty Charges paid' must be numeric"},
//                        withinABPGuidelinesId: {required: "You must make a selection for 'Repair labour rate within ABP guidelines?'"},
                        storageClaimedId: {required: "You must make a selection for 'Storage Claimed?'"},
                        recoveryClaimedId: {required: "You must make a selection for 'Recovery Claimed?'"}
                    }
                }
        );

        var addHireDurationNotAcceptableReasonValidation = function () {
            $("form#formClaimAuditReview [name='hireDurationNotAcceptableReason']").rules("add", {
                required: true,
                messages: {required: "You must make a selection for 'Reason for Hire Duration Not Acceptable'"}
            });
        };
        var removeHireDurationNotAcceptableReasonValidation = function () {
            $("form#formClaimAuditReview [name='hireDurationNotAcceptableReason']").rules("remove");
        };

        var addHireLeakageCostValidation = function () {
            $("form#formClaimAuditReview #hireLeakageCostId").rules("add", {
                required: true,
                min: 0.01,
                number:true,
                messages: {
                    required: "You must supply a value for 'If Yes, by how much?' for Hire Leakage",
                    min:"'If Yes, by how much?' for Hire Leakage Must Be Larger Than 0",
                    number:"'If Yes, by how much?' for Hire Leakage Must be numeric"
                }
            });
        };
        var removeHireLeakageCostValidation = function () {
            $("form#formClaimAuditReview #hireLeakageCostId").rules("remove");
        };

        var addExceededRepairCostValidation = function () {
            $("form#formClaimAuditReview #exceededRepairCostId").rules("add", {
                required: true,
                min: 0.01,
                number:true,
                messages: {
                    required: "You must supply a value for 'If Yes, by how much?' for Repair Cost Exceeds Engineers Recommendations",
                    min:"'If Yes, by how much?' for Repair Cost Exceeds Engineers Recommendations Must Be Larger Than 0",
                    number:"'If Yes, by how much?' for Repair Cost Exceeds Engineers Recommendations Must be numeric"
                }
            });
        };
        var removeExceededRepairCostValidation = function () {
            $("form#formClaimAuditReview #exceededRepairCostId").rules("remove");
        };

        var addPenaltyChargeAvoidableValidation = function () {
//            $("form#formClaimAuditReview [name='penaltyChargeAvoidableId']").rules("add", {
//                required: true,
//                messages: {required: "You must make a selection for 'Were penalty charges avoidable?'"}
//            });
        };
        var removePenaltyChargeAvoidableValidation = function () {
//            $("form#formClaimAuditReview [name='penaltyChargeAvoidableId']").rules("remove");
        };

        var addPenaltyChargeAvoidableNoteValidation = function () {
            $("form#formClaimAuditReview #penaltyChrgAvoidableReasonId").rules("add", {
                required: true,
                messages: {required: "You must supply a value for 'How were the penalty charges avoidable?'"}
            });
        };
        var removePenaltyChargeAvoidableNoteValidation = function () {
            $("form#formClaimAuditReview #penaltyChrgAvoidableReasonId").rules("remove");
        };

        var addNonABPGuidelineRepairLabourRateValidation = function () {
            $("form#formClaimAuditReview #nonABPGuidelineRepairLabourRateId").rules("add", {
                required: true,
                min: 0.01,
                number:true,
                messages: {
                    required: "You must supply a value for 'If No how much was charged (hourly rate)' for Repair labour rate",
                    min:"'If No how much was charged (hourly rate)' for Repair labour rate Must Be Larger Than 0",
                    number:"'If No how much was charged (hourly rate)' for Repair labour rate Must be numeric"
                }
            });
        };
        var removeNonABPGuidelineRepairLabourRateValidation = function () {
            $("form#formClaimAuditReview #nonABPGuidelineRepairLabourRateId").rules("remove");
        };

        var addStorageClaimedCorrectlyValidation = function () {
            $("form#formClaimAuditReview [name='storageClaimedCorrectlyId']").rules("add", {
                required: true,
                messages: {required: "You must make a selection for 'If Yes, correctly so?' for Storage Claimed"}
            });
        };
        var removeStorageClaimedCorrectlyValidation = function () {
            $("form#formClaimAuditReview [name='storageClaimedCorrectlyId']").rules("remove");
        };

        var addRecoveryClaimedCorrectlyValidation = function () {
            $("form#formClaimAuditReview [name='recoveryClaimedCorrectlyId']").rules("add", {
                required: true,
                messages: {required: "You must make a selection for 'If Yes, correctly so?' for Recovery Claimed"}
            });
        };
        var removeRecoveryClaimedCorrectlyValidation = function () {
            $("form#formClaimAuditReview [name='recoveryClaimedCorrectlyId']").rules("remove");
        };

        var claimTypesCombo = new Ext.form.ComboBox({
            store: claimTypeStore,
            renderTo: 'claimTypeDropDownDiv',
            width: 175,
            valueField: 'text',
            id: 'claimTypesComboId',
            hiddenName: 'claimTypeId',
            displayField: 'value',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        this.clearValue();
                        this.reset();
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

        var whoManagedRepairCombo = new Ext.form.ComboBox({
            store: whoManagedRepairData,
            renderTo: 'whoManagedRepairDropDownDiv',
            width: 175,
            valueField: 'value',
            id: 'whoManagedRepairComboId',
            hiddenName: 'whoManagedRepair',
            displayField: 'text',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        this.clearValue();
                        this.reset();
                    }
                },
                afterrender: function () {
                    if (isAuditReviewAlreadyExists) {
                        this.setValue('<s:property value="claimAuditReview.whoManagedRepair" />');
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

        var totalLossCombo = new Ext.form.ComboBox({
            store: yesNoDataStore,
            renderTo: 'totalLossDropDownDiv',
            width: 175,
            valueField: 'value',
            id: 'totalLossComboId',
            hiddenName: 'totalLossId',
            displayField: 'text',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        this.clearValue();
                        this.reset();
                    }
                },
                afterrender: function () {
                    if (isAuditReviewAlreadyExists) {
                        this.setValue(getYesNoDataStoreValue('<s:property value="claimAuditReview.totalLoss" />'));
                    } else {
                        this.setValue('<s:property value="hireMonitoringDetail.isTotalLossDesc" />' === 'Yes' ? 1 : 2);
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

        var customerVehicleClassCombo = new Ext.form.ComboBox({
            store: vehicleClassStore,
            renderTo: 'customerVehicleClassDropDownDiv',
            width: 175,
            valueField: 'text',
            id: 'customerVehicleClassComboId',
            hiddenName: 'customerVehicleClassId',
            displayField: 'value',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        this.clearValue();
                        this.reset();
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

        var hireVehicleClassCombo = new Ext.form.ComboBox({
            store: vehicleClassStore,
            renderTo: 'hireVehicleClassDropDownDiv',
            width: 175,
            valueField: 'text',
            id: 'hireVehicleClassComboId',
            hiddenName: 'hireVehicleClassId',
            displayField: 'value',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        this.clearValue();
                        this.reset();
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

        var hireDuration = new Ext.form.NumberField({
            id: "hireDurationId",
            name: "hireDuration",
            width: 175,
            allowNegative: false,
            decimalPrecision: 0,
            renderTo: 'hireDurationDiv',
            value: isAuditReviewAlreadyExists ? '<s:property value="claimAuditReview.hireDuration" />' : '<s:property value="vehicleHire.days"/>'
        });

        var hireDurationAcceptableCombo = new Ext.form.ComboBox({
            store: yesNoDataStore,
            renderTo: 'hireDurationAcceptableDropDownDiv',
            width: 175,
            valueField: 'value',
            id: 'hireDurationAcceptableComboId',
            hiddenName: 'hireDurationAcceptableId',
            displayField: 'text',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        removeHireDurationNotAcceptableReasonValidation();
                        reasonHDNotAcceptableCombo.clearValue();
                        $("#reasonHDNotAcceptableDropDownDivId").hide();
                        this.clearValue();
                        this.reset();
                    }
                },
                afterrender: function () {
                    if (isAuditReviewAlreadyExists) {
                        this.setValue(getYesNoDataStoreValue('<s:property value="claimAuditReview.hireDurationAcceptable" />'));
                    }
                },
                select: function () {
                    if (this.getValue() == 2) {
                        $("#reasonHDNotAcceptableDropDownDivId").show();
                        addHireDurationNotAcceptableReasonValidation();
                        if (isAuditReviewAlreadyExists) {
                            reasonHDNotAcceptableCombo.setValue('<s:property value="claimAuditReview.hireDurationNotAcceptableReason" />');
                        }
                    } else {
                        removeHireDurationNotAcceptableReasonValidation();
                        reasonHDNotAcceptableCombo.clearValue();
                        $("#reasonHDNotAcceptableDropDownDivId").hide();
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

        var reasonHDNotAcceptableCombo = new Ext.form.ComboBox({
            store: reasonHDNotAcceptableData,
            renderTo: 'reasonHDNotAcceptableDropDownDiv',
            width: 175,
            valueField: 'value',
            id: 'reasonHDNotAcceptableComboId',
            hiddenName: 'hireDurationNotAcceptableReason',
            displayField: 'text',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        this.clearValue();
                        this.reset();
                    }
                },
                afterrender: function () {
                    if (hireDurationAcceptableCombo.getValue() == 2) {
                        $("#reasonHDNotAcceptableDropDownDivId").show();
                        addHireDurationNotAcceptableReasonValidation();
                        if (isAuditReviewAlreadyExists) {
                            this.setValue('<s:property value="claimAuditReview.hireDurationNotAcceptableReason" />');
                        }
                    } else {
                        removeHireDurationNotAcceptableReasonValidation();
                        $("#reasonHDNotAcceptableDropDownDivId").hide();
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

        var totalHireCost = new Ext.form.NumberField({
            id: "totalHireCostId",
            name: "totalHireCost",
            width: 175,
            allowNegative: false,
            renderTo: 'totalHireCostDiv',
            value: isAuditReviewAlreadyExists ? '<s:property value="claimAuditReview.totalHireCost" />' : '<s:property value="invoice.hireNet"/>'
        });

        var hireLeakageCombo = new Ext.form.ComboBox({
            store: yesNoDataStore,
            renderTo: 'hireLeakageDropDownDiv',
            width: 175,
            valueField: 'value',
            id: 'hireLeakageComboId',
            hiddenName: 'hireLeakageId',
            displayField: 'text',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        removeHireLeakageCostValidation();
                        hireLeakageCost.reset();
                        $("#hireLeakageCostDivId").hide();
                        this.clearValue();
                        this.reset();
                    }
                },
                afterrender: function () {
                    if (isAuditReviewAlreadyExists) {
                        this.setValue(getYesNoDataStoreValue('<s:property value="claimAuditReview.hireLeakage" />'));
                    }
                },
                select: function () {
                    if (this.getValue() == 1) {
                        addHireLeakageCostValidation();
                        $("#hireLeakageCostDivId").show();
                        if (isAuditReviewAlreadyExists) {
                            hireLeakageCost.setValue('<s:property value="claimAuditReview.hireLeakageCost" />');
                        }
                    } else {
                        removeHireLeakageCostValidation();
                        hireLeakageCost.reset();
                        $("#hireLeakageCostDivId").hide();
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

        var hireLeakageCost = new Ext.form.NumberField({
            id: "hireLeakageCostId",
            name: "hireLeakageCost",
            width: 175,
            allowNegative: false,
            renderTo: 'hireLeakageCostDiv',
            listeners: {
                afterrender: function () {
                    if (hireLeakageCombo.getValue() == 1) {
                        addHireLeakageCostValidation();
                        $("#hireLeakageCostDivId").show();
                        if (isAuditReviewAlreadyExists) {
                            this.setValue('<s:property value="claimAuditReview.hireLeakageCost" />');
                        }
                    } else {
                        removeHireLeakageCostValidation();
                        $("#hireLeakageCostDivId").hide();
                    }
                }
            }
        });

        var totalRepairCost = new Ext.form.NumberField({
            id: "totalRepairCostId",
            name: "totalRepairCost",
            width: 175,
            allowNegative: false,
            renderTo: 'totalRepairCostDiv',
            value: isAuditReviewAlreadyExists ? '<s:property value="claimAuditReview.totalRepairCost" />' : '<s:property value="invoice.repairNet"/>'
        });

        var repairCostExceedEngRecomCombo = new Ext.form.ComboBox({
            store: yesNoDataStore,
            renderTo: 'RepairCostExceedEngRecomDropDownDiv',
            width: 175,
            valueField: 'value',
            id: 'RepairCostExceedEngRecomComboId',
            hiddenName: 'repairCostExceedsEngRecId',
            displayField: 'text',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        $("#exceededRepairCostDivId").hide();
                        exceededRepairCost.reset();
                        removeExceededRepairCostValidation();
                        this.clearValue();
                        this.reset();
                    }
                },
                afterrender: function () {
                    if (isAuditReviewAlreadyExists) {
                        this.setValue(getYesNoDataStoreValue('<s:property value="claimAuditReview.repairCostExceedsEngRec" />'));
                    }
                },
                select: function () {
                    if (this.getValue() == 1) {
                        $("#exceededRepairCostDivId").show();
                        addExceededRepairCostValidation();
                        if (isAuditReviewAlreadyExists) {
                            exceededRepairCost.setValue('<s:property value="claimAuditReview.exceededRepairCost" />');
                        }
                    } else {
                        $("#exceededRepairCostDivId").hide();
                        exceededRepairCost.reset();
                        removeExceededRepairCostValidation();
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

        var exceededRepairCost = new Ext.form.NumberField({
            id: "exceededRepairCostId",
            name: "exceededRepairCost",
            width: 175,
            allowNegative: false,
            renderTo: 'exceededRepairCostDiv',
            listeners: {
                afterrender: function () {
                    if (repairCostExceedEngRecomCombo.getValue() == 1) {
                        $("#exceededRepairCostDivId").show();
                        addExceededRepairCostValidation();
                        if (isAuditReviewAlreadyExists) {
                            this.setValue('<s:property value="claimAuditReview.exceededRepairCost" />');
                        }
                    } else {
                        $("#exceededRepairCostDivId").hide();
                    }
                }
            }
        });

        var penaltyChargesPaid = new Ext.form.NumberField({
            id: "penaltyChargesPaidId",
            name: "penaltyChargesPaid",
            width: 175,
            allowNegative: false,
            renderTo: 'penaltyChargesPaidDiv',
            listeners: {
                afterrender: function () {
                    if (isAuditReviewAlreadyExists) {
                        this.setValue('<s:property value="claimAuditReview.penaltyChargesPaid" />');
                    } else {
                        this.setValue('<s:property value="totalPenaltyChargePaid"/>');
                    }
                },
                change: function () {
                    if (this.getValue() > 0) {
                        $("#penaltyChrgAvoidableDropDownDivId").show();
//                        addPenaltyChargeAvoidableValidation();
                        if (isAuditReviewAlreadyExists) {
                            penaltyChrgAvoidableCombo.setValue(getYesNoDataStoreValue('<s:property value="claimAuditReview.penaltyChargeAvoidable" />'));
                        }
                    } else {
                        $("#penaltyChrgAvoidableDropDownDivId").hide();
                        penaltyChrgAvoidableCombo.clearValue();
//                        removePenaltyChargeAvoidableValidation();

                        $("#penaltyChrgAvoidableReasonDivId").hide();
                        penaltyChrgAvoidableReason.reset();
                        removePenaltyChargeAvoidableNoteValidation();
                    }
                }
            }
        });

        var penaltyChrgAvoidableCombo = new Ext.form.ComboBox({
            store: yesNoDataStore,
            renderTo: 'penaltyChrgAvoidableDropDownDiv',
            width: 175,
            valueField: 'value',
            id: 'penaltyChrgAvoidableComboId',
            hiddenName: 'penaltyChargeAvoidableId',
            displayField: 'text',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        $("#penaltyChrgAvoidableReasonDivId").hide();
                        penaltyChrgAvoidableReason.reset();
                        removePenaltyChargeAvoidableNoteValidation();
                        this.clearValue();
                        this.reset();
                    }
                },
                select: function () {
                    if (this.getValue() == 1) {
                        $("#penaltyChrgAvoidableReasonDivId").show();
                        addPenaltyChargeAvoidableNoteValidation();
                        if (isAuditReviewAlreadyExists) {
                            penaltyChrgAvoidableReason.setValue('<s:property value="claimAuditReview.penaltyChargeAvoidableNote" />');
                        }
                    } else {
                        $("#penaltyChrgAvoidableReasonDivId").hide();
                        penaltyChrgAvoidableReason.reset();
                        removePenaltyChargeAvoidableNoteValidation();
                    }
                },
                afterrender: function () {
                    if (penaltyChargesPaid.getValue() > 0) {
                        $("#penaltyChrgAvoidableDropDownDivId").show();
                        if (isAuditReviewAlreadyExists) {
                            this.setValue(getYesNoDataStoreValue('<s:property value="claimAuditReview.penaltyChargeAvoidable" />'));
                        }
                    } else {
                        $("#penaltyChrgAvoidableDropDownDivId").hide();
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

        var penaltyChrgAvoidableReason = new Ext.form.TextArea({
            name: 'penaltyChargeAvoidableNote',
            id: 'penaltyChrgAvoidableReasonId',
            width: 350,
            height: 80,
            renderTo: 'penaltyChrgAvoidableReasonDiv',
            listeners: {
                afterrender: function () {
                    if (penaltyChrgAvoidableCombo.getValue() == 1) {
                        $("#penaltyChrgAvoidableReasonDivId").show();
                        addPenaltyChargeAvoidableNoteValidation();
                        if (isAuditReviewAlreadyExists) {
                            this.setValue('<s:property value="claimAuditReview.penaltyChargeAvoidableNote" />');
                        }
                    } else {
                        $("#penaltyChrgAvoidableReasonDivId").hide();
                    }
                }
            }
        });

        var withinABPGuidelineCombo = new Ext.form.ComboBox({
            store: yesNoDataStore,
            renderTo: 'withinABPGuidelineDropDownDiv',
            width: 175,
            valueField: 'value',
            id: 'withinABPGuidelineComboId',
            hiddenName: 'withinABPGuidelinesId',
            displayField: 'text',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        $("#nonABPGuidelineRepairRateDivId").hide();
                        removeNonABPGuidelineRepairLabourRateValidation();
                        nonABPGuidelineRepairRate.reset();
                        this.clearValue();
                        this.reset();
                    }
                },
                afterrender: function () {
                    if (isAuditReviewAlreadyExists) {
                        this.setValue(getYesNoDataStoreValue('<s:property value="claimAuditReview.withinABPGuidelines" />'));
                    }
                },
                select: function () {
                    if (this.getValue() == 2) {
                        $("#nonABPGuidelineRepairRateDivId").show();
                        addNonABPGuidelineRepairLabourRateValidation();
                        if (isAuditReviewAlreadyExists) {
                            nonABPGuidelineRepairRate.setValue('<s:property value="claimAuditReview.nonABPGuidelineRepairLabourRate" />');
                        }
                    } else {
                        $("#nonABPGuidelineRepairRateDivId").hide();
                        nonABPGuidelineRepairRate.reset();
                        removeNonABPGuidelineRepairLabourRateValidation();
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

        var nonABPGuidelineRepairRate = new Ext.form.NumberField({
            id: "nonABPGuidelineRepairLabourRateId",
            name: "nonABPGuidelineRepairLabourRate",
            width: 175,
            allowNegative: false,
            renderTo: 'nonABPGuidelineRepairRateDiv',
            listeners: {
                afterrender: function () {
                    if (withinABPGuidelineCombo.getValue() == 2) {
                        $("#nonABPGuidelineRepairRateDivId").show();
                        addNonABPGuidelineRepairLabourRateValidation();
                        if (isAuditReviewAlreadyExists) {
                            this.setValue('<s:property value="claimAuditReview.nonABPGuidelineRepairLabourRate" />');
                        }
                    } else {
                        $("#nonABPGuidelineRepairRateDivId").hide();
                    }
                }
            }
        });

        var storageClaimedCombo = new Ext.form.ComboBox({
            store: yesNoDataStore,
            renderTo: 'storageClaimedDropDownDiv',
            width: 175,
            valueField: 'value',
            id: 'storageClaimedComboId',
            hiddenName: 'storageClaimedId',
            displayField: 'text',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        $("#storageClaimedCorrectlyDropDownDivId").hide();
                        removeStorageClaimedCorrectlyValidation();
                        storageClaimedCorrectlyCombo.clearValue();
                        this.clearValue();
                        this.reset();
                    }
                },
                afterrender: function () {
                    if (isAuditReviewAlreadyExists) {
                        this.setValue(getYesNoDataStoreValue('<s:property value="claimAuditReview.storageClaimed" />'));
                    }
                },
                select: function () {
                    if (this.getValue() == 1) {
                        $("#storageClaimedCorrectlyDropDownDivId").show();
                        addStorageClaimedCorrectlyValidation();
                        if (isAuditReviewAlreadyExists) {
                            storageClaimedCorrectlyCombo.setValue(getYesNoDataStoreValue('<s:property value="claimAuditReview.storageClaimedCorrectly" />'));
                        }
                    } else {
                        $("#storageClaimedCorrectlyDropDownDivId").hide();
                        storageClaimedCorrectlyCombo.clearValue();
                        removeStorageClaimedCorrectlyValidation();
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

        var storageClaimedCorrectlyCombo = new Ext.form.ComboBox({
            store: yesNoDataStore,
            renderTo: 'storageClaimedCorrectlyDropDownDiv',
            width: 175,
            valueField: 'value',
            id: 'storageClaimedCorrectlyComboId',
            hiddenName: 'storageClaimedCorrectlyId',
            displayField: 'text',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        this.clearValue();
                        this.reset();
                    }
                },
                afterrender: function () {
                    if (storageClaimedCombo.getValue() == 1) {
                        $("#storageClaimedCorrectlyDropDownDivId").show();
                        addStorageClaimedCorrectlyValidation();
                        if (isAuditReviewAlreadyExists) {
                            this.setValue(getYesNoDataStoreValue('<s:property value="claimAuditReview.storageClaimedCorrectly" />'));
                        }
                    } else {
                        $("#storageClaimedCorrectlyDropDownDivId").hide();
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

        var recoveryClaimedCombo = new Ext.form.ComboBox({
            store: yesNoDataStore,
            renderTo: 'recoveryClaimedDropDownDiv',
            width: 175,
            valueField: 'value',
            id: 'recoveryClaimedComboId',
            hiddenName: 'recoveryClaimedId',
            displayField: 'text',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        $("#recoveryClaimedCorrectlyDropDownDivId").hide();
                        removeRecoveryClaimedCorrectlyValidation();
                        recoveryClaimedCorrectlyCombo.clearValue();
                        this.clearValue();
                        this.reset();
                    }
                },
                afterrender: function () {
                    if (isAuditReviewAlreadyExists) {
                        this.setValue(getYesNoDataStoreValue('<s:property value="claimAuditReview.recoveryClaimed" />'));
                    }
                },
                select: function () {
                    if (this.getValue() == 1) {
                        $("#recoveryClaimedCorrectlyDropDownDivId").show();
                        addRecoveryClaimedCorrectlyValidation();
                        if (isAuditReviewAlreadyExists) {
                            recoveryClaimedCorrectlyCombo.setValue(getYesNoDataStoreValue('<s:property value="claimAuditReview.recoveryClaimedCorrectly" />'));
                        }
                    } else {
                        $("#recoveryClaimedCorrectlyDropDownDivId").hide();
                        recoveryClaimedCorrectlyCombo.clearValue();
                        removeRecoveryClaimedCorrectlyValidation();
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

        var recoveryClaimedCorrectlyCombo = new Ext.form.ComboBox({
            store: yesNoDataStore,
            renderTo: 'recoveryClaimedCorrectlyDropDownDiv',
            width: 175,
            valueField: 'value',
            id: 'recoveryClaimedCorrectlyComboId',
            hiddenName: 'recoveryClaimedCorrectlyId',
            displayField: 'text',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '-- Please Select --',
            selectOnFocus: true,
            forceSelection: true,
            listeners: {
                blur: function () {
                    if (this.getRawValue() === "") {
                        this.clearValue();
                        this.reset();
                    }
                },
                afterrender: function () {
                    if (recoveryClaimedCombo.getValue() == 1) {
                        $("#recoveryClaimedCorrectlyDropDownDivId").show();
                        addRecoveryClaimedCorrectlyValidation();
                        if (isAuditReviewAlreadyExists) {
                            this.setValue(getYesNoDataStoreValue('<s:property value="claimAuditReview.recoveryClaimedCorrectly" />'));
                        }
                    } else {
                        $("#recoveryClaimedCorrectlyDropDownDivId").hide();
                    }
                },
                specialkey: function (el, e) {
                    if (e.keyCode === e.ENTER) {
                        e.preventDefault();
                    }
                }
            }
        });

    });

function saveAuditReview() {
        $("#nameOfActivity").val('saveClaimAuditReview');
        var settings = $('form#formClaimAuditReview').validate().settings;
        // deep copy the rules so that the deleted rules can be re-applied if the form is not valid.
        var deletedRules = $.extend(true, {}, settings.rules);
        for (var rule in settings.rules) {
            delete settings.rules[rule].required;
            delete settings.rules[rule].min;
        }
        if ($("form#formClaimAuditReview").valid()) {
             Ext.get('claimDetailScreenDiv').mask("Saving Claim Audit...");
            choxJqueryHttpSubmit($("form#formClaimAuditReview"));
        } else {
            // Need to put back the deleted rules so that when the 'Save and Complete' button pressed, all the rules are validated again.
            for (var rule in settings.rules) {
                settings.rules[rule] = deletedRules[rule];
            }
        }
        return false;
    }

    function submitAuditReview() {
        $("#nameOfActivity").val('submitClaimAuditReview');
        if ($("form#formClaimAuditReview").valid()) {
            Ext.get('claimDetailScreenDiv').mask("Submitting Claim Audit...");
            choxJqueryHttpSubmit($("form#formClaimAuditReview"));
        }
        return false;
    }


</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formClaimAuditReview" name="formClaimAuditReview">
        <fieldset class="x-fieldset">
            <legend>Claim Audit</legend>
            <s:hidden id="name" name="name" value="saveOrSubmitClaimAuditReview"/>
            <s:hidden id="nameOfActivity" name="nameOfActivity" />
            <div class="status-info">
                <s:if test="claimAuditReview == null || !claimAuditReview.claimAuditReviewCompleted">
                    This claim has been selected for a random audit on some of the claim/invoice details. Please complete the details below. You can either 'Save' this form to come back at a later date or 'Save and Complete' when you have entered in all the relevant details.
                </s:if>
                <s:else>
                    This claim has been through a Manual Audit Process with the details visible below. If required adjust any of the answers below and select 'Save' to confirm the changes and close this panel.
                </s:else>
            </div>
            <div class="status-control-set">
                <table class="status-table">
                    <tr>
                        <td align="right"><label>Claim Type<span class="mandatory">*</span> : </label></td>
                        <td><div id="claimTypeDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td align="right"><label>Who managed repair?<span class="mandatory">*</span> : </label></td>
                        <td><div id="whoManagedRepairDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td align="right"><label>Total Loss?<span class="mandatory">*</span> : </label></td>
                        <td ><div id="totalLossDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td align="right" ><label>Customers Vehicle Class<span class="mandatory">*</span> : </label></td>
                        <td ><div id="customerVehicleClassDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td align="right" ><label>Hire Vehicle Class<span class="mandatory">*</span> : </label></td>
                        <td ><div id="hireVehicleClassDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td align="right" ><label>Hire Duration<span class="mandatory">*</span> : </label></td>
                        <td ><div id="hireDurationDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td align="right" ><label>Hire Duration Acceptable?<span class="mandatory">*</span> : </label></td>
                        <td ><div id="hireDurationAcceptableDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr id="reasonHDNotAcceptableDropDownDivId">
                        <td align="right" ><label>Reason for Hire Duration Not Acceptable<span class="mandatory">*</span> : </label></td>
                        <td ><div id="reasonHDNotAcceptableDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td align="right">
                            <label>Total Hire Costs<span class="mandatory">*</span> :</label>
                        </td>
                        <td ><div id="totalHireCostDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td align="right" ><label>Hire Leakage?<span class="mandatory">*</span> : </label></td>
                        <td ><div id="hireLeakageDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr id="hireLeakageCostDivId">
                        <td align="right">
                            <label>If Yes, by how much?<span class="mandatory">*</span> :</label>
                        </td>
                        <td ><div id="hireLeakageCostDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td align="right">
                            <label>Total Repair Costs<span class="mandatory">*</span> :</label>
                        </td>
                        <td ><div id="totalRepairCostDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td align="right" ><label style="line-height: 14px;">Repair Cost Exceeds Engineers Recommendations? : </label></td>
                        <td ><div id="RepairCostExceedEngRecomDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr id="exceededRepairCostDivId">
                        <td align="right">
                            <label>If Yes, by how much?<span class="mandatory">*</span> :</label>
                        </td>
                        <td ><div id="exceededRepairCostDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td align="right">
                            <label>Penalty Charges paid<span class="mandatory">*</span> :</label>
                        </td>
                        <td ><div id="penaltyChargesPaidDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr id="penaltyChrgAvoidableDropDownDivId">
                        <td align="right" ><label style="line-height: 14px;">Were penalty charges avoidable? : </label></td>
                        <td ><div id="penaltyChrgAvoidableDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr id="penaltyChrgAvoidableReasonDivId">
                        <td align="right">
                            <label>How were the penalty charges avoidable?<span class="mandatory">*</span> :</label>
                        </td>
                        <td colspan="3">
                            <div id="penaltyChrgAvoidableReasonDiv"></div>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" ><label style="line-height: 14px;">Repair labour rate within ABP guidelines? : </label></td>
                        <td ><div id="withinABPGuidelineDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr id="nonABPGuidelineRepairRateDivId">
                        <td align="right">
                            <label>If No how much was charged (hourly rate)<span class="mandatory">*</span> :</label>
                        </td>
                        <td ><div id="nonABPGuidelineRepairRateDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td align="right" ><label style="line-height: 14px;">Storage Claimed?<span class="mandatory">*</span> : </label></td>
                        <td ><div id="storageClaimedDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr id="storageClaimedCorrectlyDropDownDivId">
                        <td align="right" ><label style="line-height: 14px;">If Yes, correctly so?<span class="mandatory">*</span> : </label></td>
                        <td ><div id="storageClaimedCorrectlyDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td align="right" ><label style="line-height: 14px;">Recovery Claimed?<span class="mandatory">*</span> : </label></td>
                        <td ><div id="recoveryClaimedDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr id="recoveryClaimedCorrectlyDropDownDivId">
                        <td align="right" ><label style="line-height: 14px;">If Yes, correctly so?<span class="mandatory">*</span> : </label></td>
                        <td ><div id="recoveryClaimedCorrectlyDropDownDiv"></div></td>
                        <td colspan="2"></td>
                    </tr>

                    <tr>
                        <td colspan="4">
                            <div class="action-error-msg" id="auditReviewMessageBox"></div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" class="choice" nowrap>
                            <s:if test="claimAuditReview == null || !claimAuditReview.claimAuditReviewCompleted">
                                <input type="button" id="saveAuditReviewButtonId" value="Save" onclick="event.preventDefault(); saveAuditReview();" />
                                <input type="button" id="submitAuditReviewButtonId" value="Save and Complete" onclick="event.preventDefault(); submitAuditReview();" />
                            </s:if>
                            <s:else>
                                <input type="button" id="submitAuditReviewButtonId" value="Save" onclick="event.preventDefault(); submitAuditReview();" />
                            </s:else>
                        </td>
                    </tr>
                </table>
            </div>
        </fieldset>
    </form>
</div>