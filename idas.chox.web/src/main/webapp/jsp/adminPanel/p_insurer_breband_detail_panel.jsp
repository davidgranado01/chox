<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<script type="text/javascript">

    var protocolVehicleClassCeiling_gridviewStore;
    var protocolVehicleClassCeiling_gridviewGrid;
    var protocolVehicleCeilingEditSelectionDlg;
    
    Ext.onReady(function(){

        new Ext.ToolTip({ target: 'help-averageLabourHoursPerHireDay', html: 'How many hours the garage should work on the car per day'});
        new Ext.ToolTip({ target: 'help-averageLabourRateStandard', html: 'Average amount charged per hour for repair for Standard Vehicles & Vans. This is based on an average amount charged for both preferred repairers and all other repairers.</br>Vehicle classes considered \'Standard Vehicles & Vans\' are: S1-S7, M-M4, F1-F3, SP1-SP3, T1-T4, B1-B3. '});
        new Ext.ToolTip({ target: 'help-averageLabourRatePrestige', html: 'Average amount charged per hour for repair for Prestige & Special Vehicles. This is based on an average amount charged for both preferred repairers and all other repairers.</br>Vehicle classes considered \'Prestige & Special Vehicles\' are: M5-M6, F4-F9, P1-P13, SP4-SP13, PV1-PV6, CV1-CV4, RV1-RV2, CP1-CP3, CS1-CS5, CM1-CM3, T5-T14 (PT9 & PT13), B4-B6. '});
        new Ext.ToolTip({ target: 'help-hireDayCeiling', html: 'Maximum allowable hire days.'});
        new Ext.ToolTip({ target: 'help-hireNetCeiling', html: 'Maximum amount allowed to be charged for hire only.'});
        new Ext.ToolTip({ target: 'help-hireRateChargeTolerance', html: 'A figure allowing small deviations to the price charged per day for the hire based on the vehicle class.'});
        new Ext.ToolTip({ target: 'help-maxRepairValue', html: 'Maximum amount allowed to be charged for repair of vehicle.'});

        var form = $("form#formUpdateInsurerBreBandDetail");

        form.validate(
        {
            errorLabelContainer: "#CDInsurerBreBandmessageBox",
            rules: {
                name:{required:true},
                isMobileDayAllowance:{required:true, number:true, min:0},
                isNotMobileDayAllowance:{required:true, number:true, min:0},
                takeVehicleToGarageDaysMobile:{required:true, number:true, min:0},
                takeVehicleToGarageDaysNonMobile:{required:true, number:true, min:0},
                weekendBufferDays:{required:true, number:true, min:0},
                engineerInspectionDelayDays:{required:true, number:true, min:0},
                offerMadeDays:{required:true, number:true, min:0},
                receiptOfFinalStatementChequeDays:{required:true, number:true, min:0},
                inspectionDelayDays:{required:true, number:true, min:0},
                hireRateChargeTolerance:{required:true, number:true, min:0},
                hireNetCeiling:{required:true, number:true, min:0},
                hireDayCeiling:{required:true, number:true, min:0},
                repairNetCeiling:{required:true, number:true, min:0},
                averageLabourRateStandard:{required:true, number:true, min:0},
                averageLabourRatePrestige:{required:true, number:true, min:0},
                averageLabourHoursPerHireDay:{required:true, number:true, min:0},
                takeVehicleOutDays:{required:true, number:true, min:0},
                maxAllowedLabourRate:{required:true, number:true, min:0},
                maxAllowedLabourStandardRate:{required:true, number:true, min:0},
                maxAllowedLabourPrestigeRate:{required:true, number:true, min:0},
                maxAllowedEngineerNetFee:{required:true, number:true, min:0},
                maxAllowedTotalLossNetFee:{required:true, number:true, min:0}
            },
            messages: {
                name: {required:"You must supply a value for 'Name'" },
                isMobileDayAllowance: {required:"You must supply a value for 'Mobile Day Allowance'", number:"'Mobile Day Allowance' must be numeric", min:"'Mobile Day Allowance' cannot be less than zero"},
                isNotMobileDayAllowance: {required:"You must supply a value for 'Not Mobile Day Allowance'", number:"'Not Mobile Day Allowance' must be numeric", min:"'>Not Mobile Day Allowance' cannot be less than zero"},
                takeVehicleToGarageDaysMobile: {required:"You must supply a value for 'Take Vehicle To Garage Days - Mobile'", number:"'Take Vehicle To Garage Days - Mobile' must be numeric", min:"'Take Vehicle To Garage Days - Mobile' cannot be less than zero"},
                takeVehicleToGarageDaysNonMobile: {required:"You must supply a value for 'Take Vehicle To Garage Days - Non Mobile'", number:"'Take Vehicle To Garage Days - Non Mobile' must be numeric", min:"'Take Vehicle To Garage Days - Non Mobile' cannot be less than zero"},
                weekendBufferDays: {required:"You must supply a value for 'Weekend Buffer Days'", number:"'Weekend Buffer Days' must be numeric", min:"'Weekend Buffer Days' cannot be less than zero"},
                engineerInspectionDelayDays: {required:"You must supply a value for 'Engineer Inspection Delay Days'", number:"'Engineer Inspection Delay Days' must be numeric", min:"'Engineer Inspection Delay Days' cannot be less than zero"},
                offerMadeDays: {required:"You must supply a value for 'Offer Made Days'", number:"'Offer Made Days' must be numeric", min:"'Offer Made Days' cannot be less than zero"},
                receiptOfFinalStatementChequeDays: {required:"You must supply a value for 'Receipt Of Final Statement Cheque Days'", number:"'Receipt Of Final Statement Cheque Days' must be numeric", min:"'Receipt Of Final Statement Cheque Days' cannot be less than zero"},
                inspectionDelayDays: {required:"You must supply a value for 'Inspection Delay Days'", number:"'Inspection Delay Days' must be numeric", min:"'Inspection Delay Days' cannot be less than zero"},
                hireRateChargeTolerance: {required:"You must supply a value for 'Hire Rate Charge Tolerance'", number:"'Hire Rate Charge Tolerance' must be numeric", min:"'Hire Rate Charge Tolerance' cannot be less than zero"},
                hireNetCeiling: {required:"You must supply a value for 'Hire Net Ceiling'", number:"'Hire Net Ceiling' must be numeric", min:"'Hire Net Ceiling' cannot be less than zero"},
                hireDayCeiling: {required:"You must supply a value for 'Hire Day Ceiling'", number:"'Hire Day Ceiling' must be numeric", min:"'Hire Day Ceiling' cannot be less than zero"},
                repairNetCeiling: {required:"You must supply a value for 'Max Repair Value'", number:"'Max Repair Value' must be numeric", min:"'Max Repair Value' cannot be less than zero"},
                averageLabourRateStandard: {required:"You must supply a value for 'Average Labour Rate For Standard Vehicles & Vans'", number:"'Average Labour Rate For Standard Vehicles & Vans' must be numeric", min:"'Average Labour Rate For Standard Vehicles & Vans' cannot be less than zero"},
                averageLabourRatePrestige: {required:"You must supply a value for 'Average Labour Rate For Prestige & Special Vehicles'", number:"'Average Labour Rate For Prestige & Special Vehicles' must be numeric", min:"'Average Labour Rate For Prestige & Special Vehicles' cannot be less than zero"},
                averageLabourHoursPerHireDay: {required:"You must supply a value for 'Average Labour Hours Per Hire Day'", number:"'Average Labour Hours Per Hire Day' must be numeric", min:"'Average Labour Hours Per Hire Day' cannot be less than zero"},
                takeVehicleOutDays: {required:"You must supply a value for 'Take Vehicle Out Days'", number:"'Take Vehicle Out Days' must be numeric", min:"'Take Vehicle Out Days' cannot be less than zero"},
                maxAllowedLabourRate: {required:"You must supply a value for 'Maximum Labour Rate'", number:"'Maximum Labour Rate' must be numeric", min:"'Maximum Labour Rate' cannot be less than zero"},
                maxAllowedLabourStandardRate: {required:"You must supply a value for 'Maximum Labour Rate Per Hour For Standard Vehicles & Vans'", number:"'Maximum Labour Rate Per Hour For Standard Vehicles & Vans'' must be numeric", min:"'Maximum Labour Rate Per Hour For Standard Vehicles & Vans'' cannot be less than zero"},
                maxAllowedLabourPrestigeRate: {required:"You must supply a value for 'Maximum Labour Rate Per Hour For Prestige & Special Vehicles'", number:"'Maximum Labour Rate Per Hour For Prestige & Special Vehicles' must be numeric", min:"'Maximum Labour Rate Per Hour For Prestige & Special Vehicles' cannot be less than zero"},
                maxAllowedEngineerNetFee: {required:"You must supply a value for 'Maximum Engineer Fee Net Ceiling'", number:"'Maximum Engineer Fee Net Ceiling' must be numeric", min:"'Maximum Engineer Fee Net Ceiling' cannot be less than zero"},
                maxAllowedTotalLossNetFee: {required:"You must supply a value for 'Maximum Total Loss Fee Net Ceiling'", number:"'Maximum Total Loss Fee Net Ceiling' must be numeric", min:"'Maximum Total Loss Fee Net Ceiling' cannot be less than zero"}
            }
        });

        ui.ajaxForm(form, doNewBreBandSaveResult);

        doRefreshCalculation();
        
        createProtocolVehicleCeilingEditWindow();

        var protocolVehicleClassCeiling_JsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'vehicleClassId'},
                {name:'vehicleClassName'},
                {name:'hireNetCeiling'},
                {name:'repairNetCeiling'},
                {name:'createdBy'},
                {name:'createdDate'},
                {name:'removed'}
            ]
        });

        protocolVehicleClassCeiling_gridviewStore = new choxDataStore({
            removedList: [],
            listeners: {
                remove: function(store, record, index) {
                    if(record.get("id")) { 
                        // add removed record to an array if this is existing record.
                        store.removedList.push(record);
                    }
                }, 
                load : function(store, records, index){
                            // if it is new bre band then mark all the PVCC recods as dirty(red flag).
                            <s:if test="id == null">
                                // Dirty flag can not be set to all fields to an existing record, so need to 
                                // create and add new array of records and remove all the old records. 
                                var addList = [];
                                Ext.each(records,function(item){
                                    item.set('id',null); 
                                    item.markDirty(); 
                                    addList.push(item);
                                 });
                                store.removeAll(true); 
                                store.add(addList);
                            </s:if>
                }
            },
            pruneModifiedRecords : true, // to avoid sending newly added and removed record.
            url: '/prv/p/getSelectedBreBandProtocolVehicleClassCeiling.action', 
            reader:protocolVehicleClassCeiling_JsonReader
        });

        protocolVehicleClassCeiling_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:protocolVehicleClass_recordOnclickRemoveVehicleClassCeiling},
            store: protocolVehicleClassCeiling_gridviewStore,
            renderTo:'protocolVehicleClassCeilingGridviewGrid',
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            loadMask : true,
            viewConfig:{forceFit:true},
            columns: [
                {header: "Vehicle Class", width: 200, dataIndex: 'vehicleClassName', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>"+value+"</a>"; }},
                {header: "Hire Net Ceiling", width: 160, dataIndex: 'hireNetCeiling', sortable: true, resizable: true, renderer: function(value,p,r) { 
                    return '£' + (parseFloat(value).toFixed(2));
                }},
                {header: "Repair Net Ceiling", width: 160, dataIndex: 'repairNetCeiling', sortable: true, resizable: true, renderer: function(value,p,r) {
                    return '£' + (parseFloat(value).toFixed(2));
                }},
                {header: "", width: 80, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Remove</a>";}}
            ],
            height:200,
            width: 670
        });

        onProtocolVehicleClassPageRefresh();


    });
    
    function createProtocolVehicleCeilingEditWindow() {
        if(!protocolVehicleCeilingEditSelectionDlg || protocolVehicleCeilingEditSelectionDlg===null){
            protocolVehicleCeilingEditSelectionDlg =  new Ext.Window({
                applyTo:'pvccSelectionDlgHolder',
                id:'protocolVehicleCeilingEditSelectionDlgId',
                width:400,
                height:200,
                layout:'fit',
                modal:true,
                closeAction:'hide',
                plain: false,
                title: 'Edit Vehicle Class Ceiling',
                resizable : false,
                items: new Ext.Panel({
                    applyTo: 'pvccSelectionPanel'
                }),
                buttons: [{
                        text:'Ok', handler: function(){
                            if (validateEditProtocolVehicleClassCeilingForm()) {
                
                                var protocolVehicleClassCeilingName = $("#editProtocolVehicleClassName").html();
                                var editProtocolHireNetCeiling = parseFloat($("#editProtocolHireNetCeiling").val()).toFixed(2);
                                var editProtocolRepairNetCeiling = parseFloat($("#editProtocolRepairNetCeiling").val()).toFixed(2);

                                var editedRec = protocolVehicleClassCeiling_gridviewStore.getAt(protocolVehicleClassCeiling_gridviewStore.find('vehicleClassName', protocolVehicleClassCeilingName));
                                if (parseFloat(editedRec.get('hireNetCeiling')).toFixed(2) !== editProtocolHireNetCeiling) {
                                    editedRec.set('hireNetCeiling', editProtocolHireNetCeiling);
                                } 
                                if (parseFloat(editedRec.get('repairNetCeiling')).toFixed(2) !== editProtocolRepairNetCeiling) {
                                    editedRec.set('repairNetCeiling', editProtocolRepairNetCeiling);
                                } 
                                protocolVehicleCeilingEditSelectionDlg.hide();
                            }
                        }
                    },{
                        text: 'Close', handler: function(){
                            protocolVehicleCeilingEditSelectionDlg.hide();
                        }
                    }]
            });

        }
    }

    function doRefreshCalculation(){

        // C09 - Take Mobile Vehicle To Garage Variable
        var iCCDTakeVehicleToGarageDaysMobile = $("#CCDTakeVehicleToGarageDaysMobile").val();
        $(".chox-ttxt-readonly-TakeVehicleToGarageDaysMobile").val(iCCDTakeVehicleToGarageDaysMobile);

        // C10 - Take Non-Mobile Vehicle To Garage Variable (Days)
        var iCCDTakeVehicleToGarageDaysNonMobile = $("#CCDTakeVehicleToGarageDaysNonMobile").val();
        $(".chox-ttxt-readonly-TakeVehicleToGarageDaysNonMobile").val(iCCDTakeVehicleToGarageDaysNonMobile);

        // C11 - Engineer Inspection Delay Variable (Days)
        var iCCDEngineerInspectionDelayDaysMobile = $("#CCDEngineerInspectionDelayDaysMobile").val();
        $(".chox-ttxt-readonly-EngineerInspectionDelayVariableMobile").val(iCCDEngineerInspectionDelayDaysMobile);

        // C11 - Engineer Inspection Delay Variable (Days)
        var iCCDEngineerInspectionDelayDaysNonMobile = $("#CCDEngineerInspectionDelayDaysNonMobile").val();
        $(".chox-ttxt-readonly-EngineerInspectionDelayVariableNonMobile").val(iCCDEngineerInspectionDelayDaysNonMobile);

        // C12 - Collection of Vehicle from garage Variable (Days)
        var iCCDTakeVehicleOutDays = $("#CCDTakeVehicleOutDays").val();
        $(".chox-ttxt-readonly-CollectionofVehiclefromGarageVariable").val(iCCDTakeVehicleOutDays);

        doTtlLossAllowableTtlDuration();
        doRepairDurationRuleforMobileVehicleWithoutECD();
        doRepairDurationRuleforNonMobileVehicleWithoutECD();
    }

    function doTtlLossAllowableTtlDuration(){

        var ttl = 0;
        var iReceiptOfFinalStatementChequeDays = $("#CCDReceiptOfFinalStatementChequeDays").val();
        var iOfferMadeDays = $("#CCDOfferMadeDays").val();
        var iCCDInspectionDelayDays = $("#CCDInspectionDelayDays").val();

        ttl = parseFloat(iReceiptOfFinalStatementChequeDays) + parseFloat(iOfferMadeDays) + parseFloat(iCCDInspectionDelayDays);
        $("#iTtlLossAllowableTtlDuration").val(ttl);

    }

    function doRepairDurationRuleforMobileVehicleWithoutECD(){

        var iLabourCostTotalDay = 0;
        var ttl = 0;
        var iWeekendBufferDays = 0;

        var iCCDTakeVehicleOutDays = $("#CCDTakeVehicleOutDays").val();
        var iCCDEngineerInspectionDelayDaysMobile = $("#CCDEngineerInspectionDelayDaysMobile").val();
        var iCCDTakeVehicleToGarageDaysMobile = $("#CCDTakeVehicleToGarageDaysMobile").val();
        var iCCDIsMobileDayAllowance = $("#CCDIsMobileDayAllowance").val();

        iLabourCostTotalDay = parseFloat(iCCDTakeVehicleOutDays)
            + parseFloat(iCCDEngineerInspectionDelayDaysMobile)
            + parseFloat(iCCDTakeVehicleToGarageDaysMobile)
            + parseFloat(iCCDIsMobileDayAllowance);

        iWeekendBufferDays = getWeekendBuffer(iLabourCostTotalDay);

        ttl = parseFloat(iLabourCostTotalDay) + parseFloat(iWeekendBufferDays);
        $("#iTotalAllowableDaysforMobileVehicleWhereNoECDIsProvidedWoEcd").val(ttl);
        $("#iWeekendBufferDays_mwoecd").val(iWeekendBufferDays);
    }

    function doRepairDurationRuleforNonMobileVehicleWithoutECD(){

        var iLabourCostTotalDay = 0;
        var ttl = 0;
        var iWeekendBufferDays = 0;

        var iCCDTakeVehicleToGarageDaysNonMobile = $("#CCDTakeVehicleToGarageDaysNonMobile").val();
        var iCCDEngineerInspectionDelayDaysNonMobile = $("#CCDEngineerInspectionDelayDaysNonMobile").val();
        var iCCDTakeVehicleOutDays = $("#CCDTakeVehicleOutDays").val();
        var iCCDIsNotMobileDayAllowance = $("#CCDIsNotMobileDayAllowance").val();

        iLabourCostTotalDay = parseFloat(iCCDTakeVehicleToGarageDaysNonMobile)
            + parseFloat(iCCDEngineerInspectionDelayDaysNonMobile)
            + parseFloat(iCCDTakeVehicleOutDays)
            + parseFloat(iCCDIsNotMobileDayAllowance);

        iWeekendBufferDays = getWeekendBuffer(iLabourCostTotalDay);
        ttl = parseFloat(iLabourCostTotalDay) + parseFloat(iWeekendBufferDays);
        $("#iTtlAllowableDaysforNonMobileVehicleWoECD").val(ttl);
        $("#iWeekendBufferDays_nmwoecd").val(iWeekendBufferDays);

    }

    function getWeekendBuffer(iLabourCostTotalDay){

        var iWeekendBufferDay = 0;

        if(iLabourCostTotalDay<5){ iWeekendBufferDay = 0;
        }else if(iLabourCostTotalDay>=5 && iLabourCostTotalDay<12){ iWeekendBufferDay = 2;
        }else if(iLabourCostTotalDay>=12 && iLabourCostTotalDay<19){ iWeekendBufferDay = 4;
        }else if(iLabourCostTotalDay>=19 && iLabourCostTotalDay<26){ iWeekendBufferDay = 6;
        }else if(iLabourCostTotalDay>=26 && iLabourCostTotalDay<33){ iWeekendBufferDay = 8;
        }else if(iLabourCostTotalDay>=40 && iLabourCostTotalDay<47){ iWeekendBufferDay = 10;
        }else if(iLabourCostTotalDay>=47 && iLabourCostTotalDay<54){ iWeekendBufferDay = 12;
        }else if(iLabourCostTotalDay>=54 && iLabourCostTotalDay<61){ iWeekendBufferDay = 14;
        }else if(iLabourCostTotalDay>=61 && iLabourCostTotalDay<68){ iWeekendBufferDay = 16;
        }else if(iLabourCostTotalDay>=68 && iLabourCostTotalDay<75){ iWeekendBufferDay = 18;
        }else if(iLabourCostTotalDay>=75 && iLabourCostTotalDay<82){ iWeekendBufferDay = 20;
        }else if(iLabourCostTotalDay>=82 && iLabourCostTotalDay<89){ iWeekendBufferDay = 22;
        }else if(iLabourCostTotalDay>=89 && iLabourCostTotalDay<96){ iWeekendBufferDay = 24;
        }else if(iLabourCostTotalDay>=96 && iLabourCostTotalDay<103){ iWeekendBufferDay = 26;
        }else if(iLabourCostTotalDay>=103 && iLabourCostTotalDay<110){ iWeekendBufferDay = 28;
        }else if(iLabourCostTotalDay>=110 && iLabourCostTotalDay<117){ iWeekendBufferDay = 30;
        }else if(iLabourCostTotalDay>=117 && iLabourCostTotalDay<124){ iWeekendBufferDay = 32;
        }else if(iLabourCostTotalDay>=124 && iLabourCostTotalDay<131){ iWeekendBufferDay = 34;
        }else if(iLabourCostTotalDay>=131 && iLabourCostTotalDay<138){ iWeekendBufferDay = 36;
        }else if(iLabourCostTotalDay>=138 && iLabourCostTotalDay<145){ iWeekendBufferDay = 38;
        }else if(iLabourCostTotalDay>=145 && iLabourCostTotalDay<152){ iWeekendBufferDay = 40;
        }else if(iLabourCostTotalDay>=152 && iLabourCostTotalDay<159){ iWeekendBufferDay = 42;
        }else if(iLabourCostTotalDay>=159 && iLabourCostTotalDay<166){ iWeekendBufferDay = 44;
        }else if(iLabourCostTotalDay>=166 && iLabourCostTotalDay<173){ iWeekendBufferDay = 46;
        }else if(iLabourCostTotalDay>=173 && iLabourCostTotalDay<180){ iWeekendBufferDay = 48;
        }else if(iLabourCostTotalDay>=180 && iLabourCostTotalDay<187){ iWeekendBufferDay = 50;
        }else if(iLabourCostTotalDay>=187 && iLabourCostTotalDay<194){ iWeekendBufferDay = 52;
        }else if(iLabourCostTotalDay>=194 && iLabourCostTotalDay<201){ iWeekendBufferDay = 54;
        }else if(iLabourCostTotalDay>=201 && iLabourCostTotalDay<208){ iWeekendBufferDay = 56;
        }else if(iLabourCostTotalDay>=208 && iLabourCostTotalDay<215){ iWeekendBufferDay = 58;
        }else if(iLabourCostTotalDay>=215 && iLabourCostTotalDay<222){
            iWeekendBufferDay = 60;
        }

        return iWeekendBufferDay;
    }

    function doInsurerBreBandBack(){
        protocolVehicleCeilingEditSelectionDlg = null;
        var tabIndex = 1;

    <s:if test="isChoxAdmin">
            tabIndex = 4;
    </s:if>

            var target = "#insurerBreDetailTab";
            var url = "/prv/p/getInsurerBreBandPage.action";
            var param = {"insurerId":<s:property value="insurerId" />,"tabIndex":tabIndex};

            ajax.loadHtml2(url,param,function(data){
                $(target).html(data);
    <s:if test="isChoxAdmin">
                insAdminTabs.activate(tabIndex); 
    </s:if><s:else >
                InsurerMainPanelTabs.activate(tabIndex);
    </s:else>
            });

        }

        function doDeleteBreBand(){
            Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete this BRE Band?',function(btn){
            if(btn==='yes'){
                var url = "/prv/p/deleteInsurerBreBandDetail.action";
                var param = {"objectId":<s:property value="objectId" />};
                ajax.loadHtml2(url, param, doDeleteBreBandResponse);
            }
            });
        }

        function doDeleteBreBandResponse(responseText, statusText){
            var response = eval('(' + responseText.trim() + ')');
            if(response) {
                if(response.isValid) {
                    doInsurerBreBandBack();
                } else {
                    Ext.Msg.show({
                        title: 'Error',
                        msg:response.errors,
                        icon:Ext.Msg.ERROR,
                        buttons:Ext.Msg.OK,
                        width : 400
                    });
                }
            } 
        }

        function doNewBreBandSaveResult(responseText, statusText){

            var response = eval('(' + responseText.trim() + ')');

            if(response)
            {
                if(response.isValid){

                    if(response.resultType && response.resultType === 'New')
                    {
                        //                    alert("Your changes have been saved");
                        var newObjectId =  parseInt(response.result);
                        //                    var target = "div#insurerBreDetailTab";
                        var url = "/prv/p/updateInsurerBreBandDetailPanel.action";
                        var param = {"objectId":newObjectId, "insurerId":<s:property value="insurerId" />};
                        ajax.loadHtml2(url, param, doInsurerBreBandBack);

                    } else { // if this is existing bre band detail then
                        onProtocolVehicleClassPageRefresh();
                    }

                } else {
                    Ext.Msg.show({
                        title: 'Error',
                        msg:response.errors,
                        icon:Ext.Msg.ERROR,
                        buttons:Ext.Msg.OK,
                        width : 400
                    });
                }

            }
        }
        
        function refereshBreBandDetailPanel() {
            var target = "div#insurerBreDetailTab";
                var url = "/prv/p/updateInsurerBreBandDetailPanel.action";
                var param = {"objectId":<s:property value="objectId" />, "insurerId":<s:property value="insurerId" />};
                ajax.loadHtml2(url,param,function(data){
                    $(target).html(data);
                });
        }

    
        function submitBreBandDetailForm(asCopy){
                var protocolVehicleClassCeilingRecords = [];
                var i = 0;
                $("#asCopy").val(asCopy);

                if (asCopy){
                    // Add each record to the protocolVehicleClassCeilingRecords
                    this.protocolVehicleClassCeiling_gridviewStore.each(function(item){
                        protocolVehicleClassCeilingRecords[i] = item.data;
                        i++;
                    });
                } else {
                    // now add the removed records to the protocolVehicleClassCeilingRecords
                    Ext.each(this.protocolVehicleClassCeiling_gridviewStore.removedList,function(item){
                        protocolVehicleClassCeilingRecords[i] = item.data;
                        i++;
                    });
                
                    // add the updated/new records to the protocolVehicleClassCeilingRecords
                    Ext.each(this.protocolVehicleClassCeiling_gridviewStore.getModifiedRecords(),function(item){
                        protocolVehicleClassCeilingRecords[i] = item.data;
                        i++;
                    });
                }
                
                //Empty the removed records list.
                this.protocolVehicleClassCeiling_gridviewStore.removedList = [];
                // add the protocol vehicle class grid records to the form dynamically. 
                if ($('input[name=protocolVehicleClassCeilingRecords]').length > 0 ) { // If the input tag already exists then just add the value. 
                   $('input[name=protocolVehicleClassCeilingRecords]').val(Ext.util.JSON.encode(protocolVehicleClassCeilingRecords));
                } else { // If the input tag does not exists then create, set the value and append the element to the form.
                    var input = $("<input>").attr("name", "protocolVehicleClassCeilingRecords").attr('type', "hidden").val(Ext.util.JSON.encode(protocolVehicleClassCeilingRecords));
                    $("form#formUpdateInsurerBreBandDetail").append($(input));
                }
                
                choxJqueryHttpSubmit($("form#formUpdateInsurerBreBandDetail"));
                $("#CDInsurerBreBandmessageBox").show().fadeOut(10000);
                $("#submitMesResult").show().fadeOut(10000);
        }
        
        function onProtocolVehicleClassPageRefresh(){
            // hide the edit form
            protocolVehicleCeilingEditSelectionDlg.hide();
            // populate the vehicle class dropdown.
            showProtocolVehicleClassDropDown();
            // load the grid.
            protocolVehicleClassCeiling_loadGridViewList();
            // reset the form.
            resetPVCCForm();
        }

        function resetPVCCForm(){
            $("#vehicleClassId").val("");
            $("#protocolHireNetCeiling").val('0.00');
            $("#protocolRepairNetCeiling").val('0.00');
        }

        function protocolVehicleClassCeiling_loadGridViewList() {
            <s:if test="id != null">  // if it is existing bre band then use breband id to get the grid records.
                protocolVehicleClassCeiling_gridviewStore.load({params: {breBandId:'<s:property value="id" />'}});
            </s:if>
            <s:else > // if it is new bre band then use insurer id to get the grid records from the insurer vehicle class ceiling.
                protocolVehicleClassCeiling_gridviewStore.load({params: {"breBandId":-1, "insurerId":'<s:property value="insurerId" />'}});
            </s:else>
            
        }

        function protocolVehicleClass_recordOnclickRemoveVehicleClassCeiling(grid, rowIndex, columnIndex, e) {

            var gridRecord = protocolVehicleClassCeiling_gridviewGrid.getStore().getAt(rowIndex);

            if (columnIndex === 3) {
                
                var protocolVehicleClassCeilingId = gridRecord.get("id");
                var vehicleClassId = gridRecord.get("vehicleClassId");
                var vehicleClassName = gridRecord.get("vehicleClassName");
                
                if (protocolVehicleClassCeilingId) {
                    gridRecord.set('removed', 'true');
                    gridRecord.markDirty();
                } 
                protocolVehicleClassCeiling_gridviewGrid.getStore().remove(gridRecord);
                // add the removed(from grid) vehicle class name to the struts dropdown(available vehicle class) list.
                $("#vehicleClassId").append('<option value="'+vehicleClassId+'">'+vehicleClassName+'</option>');

            } else if (columnIndex === 0) {
                showEditProtocolVehicleClassCeiling(gridRecord);
            }

        }

        function showEditProtocolVehicleClassCeiling(gridRecord) {
            $("#pvccMessageBox").html('');
            protocolVehicleCeilingEditSelectionDlg.show();
            $("#editProtocolVehicleClassCeilingId").val(gridRecord.get("id"));
            $("#editProtocolVehicleClassName").html(gridRecord.get("vehicleClassName"));
            $("#editProtocolHireNetCeiling").val(parseFloat(gridRecord.get("hireNetCeiling")).toFixed(2));
            $("#editProtocolRepairNetCeiling").val(parseFloat(gridRecord.get("repairNetCeiling")).toFixed(2));
        }

        function showProtocolVehicleClassDropDown() {
            var target = "#protocolVehicleClassDropDownDiv";
            var url = "/prv/p/protocolVehicleClassDropDownAction.action";
            <s:if test="id != null"> // if it is existing breband then use breband id to get the available list for this breband. 
                var param = {"breBandId":'<s:property value="id" />'};
            </s:if>
            <s:else > // if it is new breband then use insurer id to get the available list for this insurer vehicle class ceiling. 
                var param = {"breBandId":-1, "insurerId":'<s:property value="insurerId" />'};
            </s:else>
            
            ajax.loadHtml2(url, param, function(data) {
                $(target).html(data);
            });
        }
        
        function addProtocolVehicleClassCeiling() {
            if (validateProtocolVehicleClassCeilingForm()) {
                // get the values from the form.
                var vehicleClassName = $("#vehicleClassId :selected").text();
                var vehicleClassId = $("#vehicleClassId").val();
                var protocolHireNetCeiling = parseFloat($("#protocolHireNetCeiling").val()).toFixed(2);
                var protocolRepairNetCeiling = parseFloat($("#protocolRepairNetCeiling").val()).toFixed(2);
                // create new record type, mark dirty and add it to the grid store.
                var recordType = protocolVehicleClassCeiling_gridviewGrid.getStore().recordType;
                var newRecord = new recordType({'vehicleClassId':vehicleClassId, 'vehicleClassName':vehicleClassName, 'hireNetCeiling':protocolHireNetCeiling, 'repairNetCeiling':protocolRepairNetCeiling});
                newRecord.markDirty();
                newRecord.set('removed', 'false');
                protocolVehicleClassCeiling_gridviewGrid.getStore().insert(0,newRecord);
                // reset the form details.
                $("#vehicleClassId option[value='"+vehicleClassId+"']").remove();
                $("#protocolHireNetCeiling").val('0.00');
                $("#protocolRepairNetCeiling").val('0.00');
            }
        }
        
        function validateProtocolVehicleClassCeilingForm(){
            var mesBox = $("#CDProtocolVehicleClassCeilingMessageBox");
            mesBox.empty();
            var validForm = true;
            
            if ($("#vehicleClassId :selected").text() === "-- Please Select --") {
                mesBox.append("You must select a 'Vehicle Class'\n<br/>").show();
                validForm = false;
            } 
            if ($.isNumeric($("#protocolHireNetCeiling").val())) {
                if (parseFloat($("#protocolHireNetCeiling").val()).toFixed(2) < 0) {
                    mesBox.append("You must supply a value for 'Hire Net Ceiling'\n<br/>").show();
                    validForm = false;
                }
            } else {
                mesBox.append("You must supply a numeric value for 'Hire Net Ceiling'\n<br/>").show();
                validForm = false;
            }
            
            if ($.isNumeric($("#protocolRepairNetCeiling").val())) {
                if (parseFloat($("#protocolRepairNetCeiling").val()).toFixed(2) < 0) {
                    mesBox.append("You must supply a value for 'Repair Net Ceiling'\n<br/>").show();
                    validForm = false;
                }
            } else {
                mesBox.append("You must supply a numeric value for 'Repair Net Ceiling'\n<br/>").show();
                validForm = false;
            }
            
            if (validForm){
                return true;
            } else {
                return false;
            }

         }
        
        function validateEditProtocolVehicleClassCeilingForm(){
            var mesBox = $("#pvccMessageBox");
            mesBox.empty();
            var validForm = true;
            if ($.isNumeric($("#editProtocolHireNetCeiling").val())) {
                if ($("#editProtocolHireNetCeiling").val() < 0) {
                    mesBox.append("You must supply a value for 'Hire Net Ceiling'\n<br/>").show();
                    validForm = false;
                }
            } else {
                mesBox.append("You must supply a numeric value for 'Hire Net Ceiling'\n<br/>").show();
                validForm = false;
            }
            if ($.isNumeric($("#editProtocolRepairNetCeiling").val())) {
                if ($("#editProtocolRepairNetCeiling").val() < 0) {
                    mesBox.append("You must supply a value for 'Repair Net Ceiling'\n<br/>").show();
                    validForm = false;
                }
            } else {
                mesBox.append("You must supply a numeric value for 'Repair Net Ceiling'\n<br/>").show();
                validForm = false;
            }
            if (validForm){
                return true;
            } else {
                return false;
            }

         }
         
        function handleAfterSubmitProtocolVehicleClassCeilingForm(responseText, statusText){
            var response = eval('(' + responseText.trim() + ')');

            if(response){
                if(response.isValid){

                    if(response.resultType && response.resultType === 'New'){
                        onProtocolVehicleClassPageRefresh();
                    }
                }else{
                    Ext.Msg.show({
                        title: 'Error',
                        msg:response.errors,
                        icon:Ext.Msg.ERROR,
                        buttons:Ext.Msg.OK,
                        width : 400
                    });
                    onProtocolVehicleClassPageRefresh();
                }
            }
        }
        
        function handleAfterSubmitEditProtocolVehicleClassCeilingForm(responseText, statusText){
           var response = eval('(' + responseText.trim() + ')');

           if(response){
               if(response.isValid){
                   onProtocolVehicleClassPageRefresh();
               }else{
                   Ext.Msg.show({
                       title: 'Error',
                       msg:response.errors,
                       icon:Ext.Msg.ERROR,
                       buttons:Ext.Msg.OK,
                       width : 400
                   });
               }
           }
       }

</script>

<div class="sub-admin-tab-css">

    <form id="formUpdateInsurerBreBandDetail" name="formUpdateInsurerBreBandDetail"
          action="<%= request.getContextPath()%>/prv/p/updateInsurerBreBandDetail.action" method="POST" class="XXentity-form">
       <s:hidden id="asCopy" name="asCopy" />

        <div class="form-container">

            <div class="grid-view-header">
                <table width="100%">
                    <tr>
                        <td>
                            <div class="label-block">
                                <label class="chox-form-std-label-longer">Name<span class="mandatory">*</span></label>
                                <input type="text" class="chox-ttxt" id="CCDName" name="name" value="<s:property value="name" />"/>
                                <input type="button" value="Save" onclick="javascript: submitBreBandDetailForm(false);"/>
                                <s:if test="!isNew">
                                    <input type="button" value="Copy" onclick="javascript: submitBreBandDetailForm(true);"/>
                                    <input type="button" value="Delete" onclick="javascript: doDeleteBreBand();"/>
                                </s:if>
                                <input type="button" value="Cancel" class="cancel" onclick="javascript: doInsurerBreBandBack();" />
                            </div>
                             <div id="CDInsurerBreBandmessageBox" class="action-error-msg"></div>
                             <div id="submitMesResult" class="chox-form-submit-result"></div>
                        </td>
                    </tr>
                </table>
            </div>

            <!--div -->
                <input type="hidden" name="objectId" id="objectId" value='<s:property value="objectId"/>'/>
                <input type="hidden" name="insurerId" id="insurerId" value='<s:property value="insurerId"/>'/>

                <div class="admin-bre-band-detail-holder">
                   
                    

                  <div class="admin-bre-band-detail-section">                 
                    <div class="section-heading">Insurer/CHO Configuration Parameters</div>
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Claim Upload Note</div>
                        <div class="status-info">
                            The note below will appear as a Public Note when a new claim is uploaded by this CHO.
                        </div>
                        <div>
                            <label class="chox-form-std-label-longer">Note</label>
                            <input type="text" size="255" style="width:600px" class="chox-ttxt" id="CCDClaimUploadNote" name="claimUploadNote" value="<s:property value='claimUploadNote' />"/>
                        </div>
                    </div>

                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Supplier Rates</div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="useSupplierRates" value="useSupplierRates" /></div><label class="chox-form-std-label"><b>Use Supplier Rates</b></label>
                            <div class="chox-form-std-label-longer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Use supplier vehicle class hire rates for this CHO and not the standard ABI GTA rates.</div>
                        </div>
                    </div>
<s:if test="insurerPaymentsTeamEnabled">                         
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Payments Team</div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="paymentTeamActive" value="paymentTeamActive" /></div><label class="chox-form-std-label"><b>Use Payments Team</b></label>
                            <div class="chox-form-std-label-longer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Enable use of offshore 'Payments Team'</div>
                        </div>
                    </div>
</s:if>     
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Penalty Charges</div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="allowGTAPenaltyCharges" value="allowGTAPenaltyCharges" /></div><label class="chox-form-std-label"><b>Allow Penalty Charges on GTA Invoices</b></label>
                            <div class="chox-form-std-label-longer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Check to allow the CHO to apply penalty charges to overdue GTA invoices.</div>
                            <s:if test="subscriberClaimsEnabled">
                                <div class="chox-form-checkbox"><s:checkbox name="allowSubscriberPenaltyCharges" value="allowSubscriberPenaltyCharges" /></div><label class="chox-form-std-label"><b>Allow Penalty Charges on Subscriber Invoices</b></label>
                                <div class="chox-form-std-label-longer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Check to allow the CHO to apply penalty charges to overdue Subscriber invoices.</div>
                            </s:if>
                            <s:if test="fixedFeeClaimsEnabled">
                                <div class="chox-form-checkbox"><s:checkbox name="allowFixedFeePenaltyCharges" value="allowFixedFeePenaltyCharges" /></div><label class="chox-form-std-label"><b>Allow Penalty Charges on Fixed Fee Invoices</b></label>
                                <div class="chox-form-std-label-longer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Check to allow the CHO to apply penalty charges to overdue Fixed Fee invoices.</div>
                            </s:if>
                            <s:if test="collaborationProtocolClaimsEnabled">
                                <div class="chox-form-checkbox"><s:checkbox name="allowCollaborationProtocolPenaltyCharges" value="allowCollaborationProtocolPenaltyCharges" /></div><label class="chox-form-std-label"><b>Allow Penalty Charges on Collaboration Protocol Invoices</b></label>
                                <div class="chox-form-std-label-longer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Check to allow the CHO to apply penalty charges to overdue Collaboration Protocol invoices.</div>
                            </s:if>
                            <s:if test="tpiClaimsEnabled">
                                <div class="chox-form-checkbox"><s:checkbox name="allowTPIPenaltyCharges" value="allowTPIPenaltyCharges" /></div><label class="chox-form-std-label"><b>Allow Penalty Charges on TPI Invoices (Automatic Penalty Charges Do Not Apply)</b></label>
                                <div class="chox-form-std-label-longer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Check to allow the CHO to apply penalty charges to overdue TPI invoices.</div>
                            </s:if>
                            <div class="chox-form-checkbox"><s:checkbox name="allowInsurervsInsurerPenaltyCharges" value="allowInsurervsInsurerPenaltyCharges" /></div><label class="chox-form-std-label"><b>Allow Penalty Charges on Insurer vs Insurer Invoices (Automatic Penalty Charges Do Not Apply)</b></label>
                            <div class="chox-form-std-label-longer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Check to allow the CHO to apply penalty charges to overdue Insurer vs Insurer invoices.</div>
                            <s:if test="insurerUploadEnabled">
                                <div class="chox-form-checkbox"><s:checkbox name="allowManualInvoicePenaltyCharges" value="allowManualInvoicePenaltyCharges" /></div><label class="chox-form-std-label"><b>Allow Penalty Charges on Insurer Upload Invoices (Automatic Penalty Charges Do Not Apply)</b></label>
                                <div class="chox-form-std-label-longer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Check to allow the CHO to apply penalty charges to overdue Insurer Upload invoices.</div>
                            </s:if>
                        </div>
                    </div>
                            
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Automated Tasks</div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="allowNotManagingRepairAutomatedTasks" value="allowNotManagingRepairAutomatedTasks" /></div><label class="chox-form-std-label"><b>Set Automated Tasks For The CHO When Credit Repair Costs Are Submitted And Not Managing Repair</b></label>
                            <div class="chox-form-std-label-longer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;If the CHO uploads an invoice with a charge for credit repair costs and they did not manage the repair then an automated task will be raised advising the CHO to upload documentation to support the repair costs.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="allowManagingRepairAutomatedTasks" value="allowManagingRepairAutomatedTasks" /></div><label class="chox-form-std-label"><b>Set Automated Tasks For The CHO When Credit Repair Costs Are Submitted And Managing Repair</b></label>
                            <div class="chox-form-std-label-longer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;If the CHO uploads an invoice with a charge for credit repair costs and they managed the repair then an automated task will be raised advising the CHO to upload documentation to support the repair costs.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="allowMissingECDAutomatedTasks" value="allowMissingECDAutomatedTasks" /></div><label class="chox-form-std-label"><b>Set Automated Task For The Insurer When No ECD Is Present And Six Days Have Passed Since The Hire Start Date</b></label>
                            <div class="chox-form-std-label-longer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;If a claim is at the hire monitoring stage with a Hire Start Date 6 days in the past an automated task will be generated to the Insurer to contact the CHO when no ECD is present on the claim.</div>
                        </div>
                    </div>
                  </div>
                  <div class="admin-bre-band-detail-section">                 
                    <div class="section-heading">Business Rules</div>
                            
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Total Loss Duration Rule</div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Receipt of Final Settlement Cheque Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDReceiptOfFinalStatementChequeDays" name="receiptOfFinalStatementChequeDays" value="<s:property value="receiptOfFinalStatementChequeDays" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Ttl. Loss Settlement Offer Process Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDOfferMadeDays" name="offerMadeDays" value="<s:property value="offerMadeDays" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Ttl. Loss Engineer Inspection Delay Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDInspectionDelayDays" name="inspectionDelayDays" value="<s:property value="inspectionDelayDays" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer"><b>Ttl. Loss Allowable Total Duration (Days)</b></label>
                            <input type="text" class="chox-ttxt-readonly" readonly="true" value="0" id="iTtlLossAllowableTtlDuration" name="iTtlLossAllowableTtlDuration"/>
                        </div>
                    </div>

                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Labour Cost/hours</div>
                        <div class="status-info">
                            <b>Labour Cost Calculation:</b><br/>
                            (((Labour Cost/Current Average Labour Rate Per Hour)/Productive Labour Hours in Garage Per Hire Day) + Take Mobile Vehicle To Garage Variable or Take Non-Mobile Vehicle to Garage Variable (Depending on Mobile/Non-Mobile Vehicle) + Mobile or Non-Mobile Engineer Inspection Delay Variable + Collection of Vehicle from Garage Variable + Weekend Buffer (Automatically Calculated for Expected Number of Weekends))
                            <br/><br/>
                            <b>Labour Hours Calculation:</b><br/>
                            ((Labour Hours/Productive Labour Hours in Garage Per Hire Day) + Take Mobile Vehicle to Garage or Take Non-Mobile Vehicle to Garage (Depending on Mobile/Non Mobile Vehicle) + Mobile or Non-Mobile Engineer Inspection Delay Variable + Collection of Vehicle from Garage Variable + Weekend Buffer (Automatically Calculated for Expected Number of Weekends))
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Take Mobile Vehicle To Garage Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDTakeVehicleToGarageDaysMobile" name="takeVehicleToGarageDaysMobile" value="<s:property value="takeVehicleToGarageDaysMobile" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Take Non-Mobile Vehicle To Garage Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDTakeVehicleToGarageDaysNonMobile" name="takeVehicleToGarageDaysNonMobile" value="<s:property value="takeVehicleToGarageDaysNonMobile" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Mobile Vehicle Engineer Inspection Delay Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDEngineerInspectionDelayDaysMobile" name="engineerInspectionDelayDaysMobile" value="<s:property value="engineerInspectionDelayDaysMobile" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Non-Mobile Vehicle Engineer Inspection Delay Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDEngineerInspectionDelayDaysNonMobile" name="engineerInspectionDelayDaysNonMobile" value="<s:property value="engineerInspectionDelayDaysNonMobile" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Collection of Vehicle from Garage Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDTakeVehicleOutDays" name="takeVehicleOutDays" value="<s:property value="takeVehicleOutDays" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Current Average Labour Rate Per Hour <span class="mandatory">*</span> </br>For Standard Vehicles & Vans (£)</label>
                            <input type="text" class="chox-ttxt" id="CCDAverageLabourRateStandard" name="averageLabourRateStandard" value="<s:property value="averageLabourRateStandard" />" onchange="javascript:doRefreshCalculation();"/><img id="help-averageLabourRateStandard" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt=""/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Current Average Labour Rate Per Hour <span class="mandatory">*</span> </br>For Prestige & Special Vehicles (£)</label>
                            <input type="text" class="chox-ttxt" id="CCDAverageLabourRatePrestige" name="averageLabourRatePrestige" value="<s:property value="averageLabourRatePrestige" />" onchange="javascript:doRefreshCalculation();"/><img id="help-averageLabourRatePrestige" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt=""/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Productive Labour hours Per Hire Day (Hours)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDAverageLabourHoursPerHireDay" name="averageLabourHoursPerHireDay" value="<s:property value="averageLabourHoursPerHireDay" />" onchange="javascript:doRefreshCalculation();"/><img id="help-averageLabourHoursPerHireDay" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt=""/>
                        </div>
                    </div>
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Repair Duration Rule for Mobile Vehicle With ECD</div>

                        <div class="status-info">
                            <b>Repair Duration Calculation:</b><br/>
                            ECD + Take Mobile Vehicle To Garage Variable + Mobile Engineer Inspection Delay Variable + Collection of Vehicle from Garage Variable + Weekend Buffer (Automatically Calculated for Expected Number of Weekends)
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Take Mobile Vehicle To Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-TakeVehicleToGarageDaysMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Mobile Engineer Inspection Delay Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-EngineerInspectionDelayVariableMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Collection of Vehicle from Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-CollectionofVehiclefromGarageVariable" readonly="true"/>
                        </div>
                    </div>
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Repair Duration Rule for Mobile Vehicle Without ECD</div>

                        <div class="status-info">
                            <b>ECD Calculation:</b><br/>
                            Mobile Vehicle ECD Variable + Take Mobile Vehicle To Garage Variable + Mobile Engineer Inspection Delay Variable + Collection of Vehicle from Garage Variable + Weekend Buffer (Automatically Calculated for Expected Number of Weekends)
                            <br/><br/>
                            Where No ECD is provided by the CHO the ECD variable is used, acting as an artificial ECD.
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Mobile Vehicle ECD Variable (Days)<span class="mandatory">*</span></label>
                            <input size="5" maxlength="5" type="text" class="chox-ttxt" id="CCDIsMobileDayAllowance" name="isMobileDayAllowance" value="<s:property value="isMobileDayAllowance" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Take Mobile Vehicle To Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-TakeVehicleToGarageDaysMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Mobile Engineer Inspection Delay Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-EngineerInspectionDelayVariableMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Collection of Vehicle from Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-CollectionofVehiclefromGarageVariable" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Weekend Buffer (Days)</label>
                            <input type="text" class="chox-ttxt-readonly" id="iWeekendBufferDays_mwoecd" name="iWeekendBufferDays_mwoecd" value="4" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer"><b>Ttl Allowable Days for Mobile Vehicle Without ECD</b></label>
                            <input type="text" class="chox-ttxt-readonly" readonly="true" id="iTotalAllowableDaysforMobileVehicleWhereNoECDIsProvidedWoEcd" name="iTotalAllowableDaysforMobileVehicleWhereNoECDIsProvidedWoEcd"/>
                        </div>
                    </div>
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Repair Duration Rule for Non-Mobile Vehicle with ECD</div>
                        <div class="status-info">
                            <b>Repair Duration Calculation:</b><br/>
                            ECD + Take Non-Mobile Vehicle To Garage Variable + Non-Mobile Engineer Inspection Delay Variable + Collection of Vehicle from Garage Variable + Weekend Buffer (Automatically Calculated for Expected Number of Weekends)
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Non-Mobile Engineer Inspection Delay Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-EngineerInspectionDelayVariableNonMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Take Non-Mobile Vehicle To Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-TakeVehicleToGarageDaysNonMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Collection of Vehicle from Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-CollectionofVehiclefromGarageVariable" readonly="true"/>
                        </div>
                    </div>
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Repair Duration Rule for Non-Mobile Vehicle without ECD</div>

                        <div class="status-info">
                            <b>ECD Calculation:</b><br/>
                            Non-Mobile Vehicle ECD Variable + Take Non-Mobile Vehicle To Garage Variable + Non-Mobile Engineer Inspection Delay Variable + Collection of Vehicle from Garage Variable + Weekend Buffer (Automatically Calculated for Expected Number of Weekends)
                            <br/><br/>
                            Where No ECD is provided by the CHO the ECD variable is used, acting as an artificial ECD.
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Non-Mobile Vehicle ECD Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDIsNotMobileDayAllowance" name="isNotMobileDayAllowance" value="<s:property value="isNotMobileDayAllowance" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Non-Mobile Engineer Inspection Delay Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-EngineerInspectionDelayVariableNonMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Take Non-Mobile Vehicle To Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-TakeVehicleToGarageDaysNonMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Collection of Vehicle from Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-CollectionofVehiclefromGarageVariable" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Weekend Buffer (Days)</label>
                            <input type="text" class="chox-ttxt-readonly" id="iWeekendBufferDays_nmwoecd" name="iWeekendBufferDays_nmwoecd" value="4" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer"><b>Ttl Allowable Days for Non-Mobile Vehicle Without ECD</b></label>
                            <input type="text" class="chox-ttxt-readonly" readonly="true" id="iTtlAllowableDaysforNonMobileVehicleWoECD" name="iTtlAllowableDaysforNonMobileVehicleWoECD"/>
                        </div>
                    </div>
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Engineer Estimation Rule</div>

                        <div class="status-info">
                            <b>Engineer Estimation Rule:</b><br/>
                            Engineer's Estimated Days Under Repair + Take Mobile Vehicle To Garage Variable or Take Non-Mobile Vehicle To Garage Variable (Depending on Mobile/Non-Mobile Vehicle) + Mobile or Non-Mobile Engineer Inspection Delay Variable + Collection of Vehicle from garage Variable + Weekend Buffer (Automatically Calculated for Expected Number of Weekends)
                        </div>

                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Take Mobile Vehicle To Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-TakeVehicleToGarageDaysMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Take Non-Mobile Vehicle To Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-TakeVehicleToGarageDaysNonMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Mobile Engineer Inspection Delay Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-EngineerInspectionDelayVariableMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Non-Mobile Engineer Inspection Delay Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-EngineerInspectionDelayVariableNonMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Collection of Vehicle from Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-CollectionofVehiclefromGarageVariable" readonly="true"/>
                        </div>
                    </div>
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Hire Tolerances</div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Hire Day Ceiling (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDHireDayCeiling" name="hireDayCeiling" value="<s:property value="hireDayCeiling" />" onchange="javascript:doRefreshCalculation();"/><img id="help-hireDayCeiling" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt=""/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Hire Net Ceiling (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDHireNetCeiling" name="hireNetCeiling" value="<s:property value="hireNetCeiling" />" onchange="javascript:doRefreshCalculation();"/><img id="help-hireNetCeiling" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt=""/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Hire Rate Charge Per Day Tolerance (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDHireRateChargeTolerance" name="hireRateChargeTolerance" value="<s:property value="hireRateChargeTolerance" />" onchange="javascript:doRefreshCalculation();"/><img id="help-hireRateChargeTolerance" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt=""/>
                        </div>

                        <div class="chox-form-item">

                            <label class="chox-form-std-label-longer">Allowable Hire Days Prior To Date Repairs Commenced<span class="mandatory">*</span>  <br> For Non-Mobile Vehicles &nbsp;&nbsp;&nbsp;</label>
                            <input type="text" class="chox-ttxt" id="hireDaysPriorToDateRepairCommencedId" name="hireDaysPriorToDateRepairCommenced" value="<s:property value="hireDaysPriorToDateRepairCommenced" />"/>
                        </div>
                        <br>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Allowable Hire Days Prior To Repair Book In Date<span class="mandatory">*</span> <br> For Mobile Vehicles &nbsp;&nbsp;&nbsp;</label>
                            <input type="text" class="chox-ttxt" id="hireDaysPriorToDateRepairBookInDateMobileVehiclesId" name="hireDaysPriorToDateRepairBookInDateMobileVehicles" value="<s:property value="hireDaysPriorToDateRepairBookInDateMobileVehicles" />"/>
                        </div>
                        <br>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Allowable Hire Days Prior To Repair Book In Date<span class="mandatory">*</span> <br> For Non-Mobile Vehicles &nbsp;&nbsp;&nbsp;</label>
                            <input type="text" class="chox-ttxt" id="hireDaysPriorToDateRepairBookInDateNonMobileVehiclesId" name="hireDaysPriorToDateRepairBookInDateNonMobileVehicles" value="<s:property value="hireDaysPriorToDateRepairBookInDateNonMobileVehicles" />"/>
                        </div>
                    </div>
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Repair Tolerances</div>

                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Repair Net Ceiling (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDRepairNetCeiling" name="repairNetCeiling" value="<s:property value="repairNetCeiling" />" onchange="javascript:doRefreshCalculation();"/><img id="help-maxRepairValue" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt=""/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Labour Rate Per Hour All Vehicles (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDMaxLabourRate" name="maxAllowedLabourRate" value="<s:property value="maxAllowedLabourRate" />"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Labour Rate Per Hour For<span class="mandatory">*</span>  <br> Standard Vehicles & Vans (£) &nbsp;&nbsp;&nbsp;</label>
                            <input type="text" class="chox-ttxt" id="CCDMaxLabourStandardRate" name="maxAllowedLabourStandardRate" value="<s:property value="maxAllowedLabourStandardRate" />"/>
                        </div>
                        <br>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Labour Rate Per Hour For<span class="mandatory">*</span>  <br> Prestige & Special Vehicles (£) &nbsp;&nbsp;&nbsp;</label>
                            <input type="text" class="chox-ttxt" id="CCDMaxLabourPrestigeRate" name="maxAllowedLabourPrestigeRate" value="<s:property value="maxAllowedLabourPrestigeRate" />"/>
                        </div>
                        <br>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Engineer Fee Net Ceiling(£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDMaxAllowedEngineerNetFee" name="maxAllowedEngineerNetFee" value="<s:property value="maxAllowedEngineerNetFee" />" />
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Total Loss Fee Net Ceiling(£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDMaxAllowedTotalLossNetFee" name="maxAllowedTotalLossNetFee" value="<s:property value="maxAllowedTotalLossNetFee" />" />
                        </div>
                    </div>
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Supplier Admin Fee Tolerances</div>

                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Supplier Admin Fee Ceiling Not Managing Repair (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="adminFeeCeilingId" name="adminFeeCeiling" value="<s:property value="adminFeeCeiling" />"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Supplier Admin Fee Ceiling Managing Repair (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="adminFeeCeilingManagingRepairId" name="adminFeeCeilingManagingRepair" value="<s:property value="adminFeeCeilingManagingRepair" />"/>
                        </div>
                    </div>
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Subscriber Admin Fee Tolerances</div>

                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Subscriber Admin Fee Ceiling Not Managing Repair (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="adminFeeCeilingSubscriberId" name="adminFeeCeilingSubscriber" value="<s:property value="adminFeeCeilingSubscriber" />"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Subscriber Admin Fee Ceiling Managing Repair (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="adminFeeCeilingSubscriberManagingRepairId" name="adminFeeCeilingSubscriberManagingRepair" value="<s:property value="adminFeeCeilingSubscriberManagingRepair" />"/>
                        </div>
                    </div>

                        <div class="admin-bre-band-detail-section">
                        <div class="section-name">Fixed-Fee Admin Fee Tolerances</div>

                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Fixed-Fee Admin Fee Ceiling Not Managing Repair (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="adminFeeFixedFeeCeilingId" name="adminFeeCeilingFixedFee" value="<s:property value="adminFeeCeilingFixedFee" />"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Fixed-Fee Admin Fee Ceiling Managing Repair (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="adminFeeFixedFeeCeilingManagingRepairId" name="adminFeeCeilingFixedFeeManagingRepair" value="<s:property value="adminFeeCeilingFixedFeeManagingRepair" />"/>
                        </div>
                    </div>


                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Tax Check Tolerances</div>

                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Standard Risk Insurance Premium Tax Ceiling (Per Day) (£)<span class="mandatory">*</span></label>

                            <input type="text" class="chox-ttxt" id="standardInsurancePremiumId" name="standardInsurancePremium" value="<s:property value="standardInsurancePremium" />" />
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Non-Standard Risk Insurance Premium Tax Ceiling (Per Day) (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="nonStandardInsurancePremiumId" name="nonStandardInsurancePremium" value="<s:property value="nonStandardInsurancePremium" />"/>
                        </div>

                    </div>


                    <div class="admin-bre-band-detail-section">
                        <div class="section-name"> Repairer Fixed Hire Days Tolerances</div>

                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Name Of Repairer <span class="mandatory">*</span></label>

                            <input type="text" class="chox-ttxt" id="nameOfRepairerId" name="nameOfRepairer" value="<s:property value="nameOfRepairer" />" />
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Number Of Hire Days For Above Repairer <span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="numberOfDaysId" name="numberOfDays" value="<s:property value="numberOfDays" />" />
                        </div>

                    </div>

                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">VAT Tolerances</div>
                        <div class="status-info">
                            <b>VAT Tolerances:</b><br/>
                            Due to the different methods some CHOs use to calculate their VAT the below offers tolerances on the VAT amounts submitted to account for any deviations.  The tolerances allow a specified discrepancy above the expected VAT amount for the three fields listed below.
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Hire VAT Tolerance (£)</label>
                            <input type="text" class="chox-ttxt" id="hireVatToleranceId" name="hireVatTolerance" value="<s:property value="hireVatTolerance" />" />
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Repair VAT Tolerance (£)</label>
                            <input type="text" class="chox-ttxt" id="repairVatToleranceId" name="repairVatTolerance" value="<s:property value="repairVatTolerance" />" />
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Total VAT Tolerance (£)</label>
                            <input type="text" class="chox-ttxt" id="totalVatToleranceId" name="totalVatTolerance" value="<s:property value="totalVatTolerance" />" />
                        </div>
                    </div>

                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Extras Tolerances</div>
                        <div class="status-info">
                            <b>Extras Tolerances:</b><br/>
                            Below are the maximum allowed total charges for the relevant Invoice extras.
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Non Standard Risk Insurance Premium Ceiling (£)</label>
                            <input type="text" class="chox-ttxt" id="nonStandardInsurancePremiumCeilingToleranceId" name="nonStandardInsurancePremiumCeilingTolerance" value="<s:property value="nonStandardInsurancePremiumCeilingTolerance" />" />
                        </div>
                    </div>
                    <div class="admin-bre-band-detail-section">
                        <div class="section-name">Protocol Vehicle Class Ceilings</div>
                        <div class="status-info">
                            The maximum protocol ceiling limits for both the Hire Net and Repair Net for the specific vehicle classes is managed here. If a CHO submits an invoice where the Hire Net or Repair Net value(s) exceed the values held in the below table for the specific vehicle class in question, (non-fault vehicle's vehicle class) then the rule will fail.
                        </div>
                        <div id="protocolVehicleClassCeilingorganisationGird">
                            <div class="grid-view-header">
                                <div class="admin-bre-band-detail-section">
                                    <table width="672px">
                                        <tr>
                                            <td  align="center">
                                                <label id="protocolVehicleClassDropDownDiv"></label> </td>
                                            <td  align="center">
                                                <label class="chox-form-std-label">Hire Net Ceiling<span class="mandatory">*</span></label>
                                                <input id="protocolHireNetCeiling" style="width:60px"/>
                                            </td>
                                            <td  align="center">
                                                <label class="chox-form-std-label">Repair Net Ceiling<span class="mandatory">*</span></label>
                                                <input id="protocolRepairNetCeiling" style="width:60px"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan ="3" align="center">
                                                <input type="button" value="Add New Vehicle Class" onclick="addProtocolVehicleClassCeiling();"/>
                                            </td>
                                        </tr>
                                    </table>
                                    <div id="CDProtocolVehicleClassCeilingMessageBox" class="action-error-msg"></div>
                                    <div id="protocolVehicleClassCeilingGridviewGrid"></div>
                                </div>
                            </div>
                        </div>
                        <div id="pvccSelectionDlgHolder" class="x-hidden">
                            <div id="pvccSelectionPanel">
                                <div class="form-container" style="height:300px; padding-bottom:30px">
                                    <input id="editProtocolVehicleClassCeilingId" type="hidden"/>
                                    <div class="chox-form-item">
                                        <label class="chox-form-pop">Vehicle Class</label>
                                        <div id="editProtocolVehicleClassName"></div>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-pop">Hire Net Ceiling</label>
                                        <input type="text" id="editProtocolHireNetCeiling"/>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-pop">Repair Net Ceiling</label>
                                        <input type="text" id="editProtocolRepairNetCeiling"/>
                                    </div>
                                    <div id="pvccMessageBox" class="action-error-msg"></div>
                                </div>
                            </div>
                        </div>
                    </div>

                  <div class="admin-bre-band-detail-section">
                      <div class="section-name">Additional Invoice Validations</div>

                      <div class="chox-form-checkboxitem">
                          <div class="chox-form-checkbox"><s:checkbox name="hasAllowedVehicleClass" value="hasAllowedVehicleClass" /></div>
                          <label class="chox-form-check-label">Like for like vehicle class hire provision Check</label>
                          <div class="chox-form-check-description">Check to ensure that the replacement hire vehicle is a like for like match with the non-fault driver's vehicle.</div>
                      </div>
                      <div class="chox-form-checkboxitem">
                          <div class="chox-form-checkbox"><s:checkbox name="vehicleClassHireProvisionLikeForLike6To8" value="vehicleClassHireProvisionLikeForLike6To8" /></div>
                          <label class="chox-form-check-label">Like For Like Prestige Vehicle Class Hire Provision 6-8 Year Check</label>
                          <div class="chox-form-check-description">Any Prestige vehicle class where the age of the CHOs customer's vehicle is over 6 years old and under 8 years old, the replacement vehicle class should be one vehicle class less than the CHO's customer's vehicle class.</div>
                        </div>
                      <div class="chox-form-checkboxitem">
                          <div class="chox-form-checkbox"><s:checkbox name="vehicleClassHireProvisionLikeForLike6To8SP" value="vehicleClassHireProvisionLikeForLike6To8SP" /></div>
                          <label class="chox-form-check-label">Like For Like Sports Performance Vehicle Class Hire Provision 6-8 Year Check</label>
                          <div class="chox-form-check-description">Any Sports Performance vehicle class where the age of the CHOs customer's vehicle is over 6 years old and under 8 years old, the replacement vehicle class should be one vehicle class less than the CHO's customer's vehicle class.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="vehicleClassHireProvisionLikeForLike8To9" value="vehicleClassHireProvisionLikeForLike8To9" /></div>
                            <label class="chox-form-check-label">Like For Like Prestige Vehicle Class Hire Provision 8-9 Year Check</label>
                            <div class="chox-form-check-description">Any Prestige vehicle class where the age of the CHOs customer's vehicle is over 8 years old and under 9 years old, the replacement vehicle class should be two vehicle classes less than the CHO's customer's vehicle class.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="vehicleClassHireProvisionLikeForLike8To9SP" value="vehicleClassHireProvisionLikeForLike8To9SP" /></div>
                            <label class="chox-form-check-label">Like For Like Sports Performance Vehicle Class Hire Provision 8-9 Year Check</label>
                            <div class="chox-form-check-description">Any Sports Performance vehicle class where the age of the CHOs customer's vehicle is over 8 years old and under 9 years old, the replacement vehicle class should be two vehicle classes less than the CHO's customer's vehicle class.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="vehicleClassHireProvisionLikeForLikeOver9" value="vehicleClassHireProvisionLikeForLikeOver9" /></div>
                            <label class="chox-form-check-label">Like For Like Prestige Vehicle Class Hire Provision Over 9 Year Check</label>
                            <div class="chox-form-check-description">Any Prestige vehicle class where the age of the CHOs customer's vehicle is over 9 years old, the replacement vehicle class provided should be reviewed on an individual basis.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="vehicleClassHireProvisionLikeForLikeOver9SP" value="vehicleClassHireProvisionLikeForLikeOver9SP" /></div>
                            <label class="chox-form-check-label">Like For Like Sports Performance Vehicle Class Hire Provision Over 9 Year Check</label>
                            <div class="chox-form-check-description">Any Sports Performance vehicle class where the age of the CHOs customer's vehicle is over 9 years old, the replacement vehicle class provided should be reviewed on an individual basis.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasCalculatedCorrectDailyRate" value="hasCalculatedCorrectDailyRate" /></div>
                            <label class="chox-form-check-label">Vehicle class daily rate charge Check</label>
                            <div class="chox-form-check-description">Check against the allowed daily rate for the vehicle class of the replacement hire vehicle (according to the GTA or specific CHO agreement) and the daily rate billed by the CHO.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hireNetDoesNotExceedBandHireNetCeiling" value="hireNetDoesNotExceedBandHireNetCeiling" /></div>
                            <label class="chox-form-check-label">Hire Net Ceiling Check</label>
                            <div class="chox-form-check-description">Check to ensure the Hire Net billed by the CHO does not exceed the CHO's specified Hire Net ceiling (this Hire Net ceiling is enforced regardless of vehicle class of replacement hire vehicle).</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hireNetDoesNotExceedVehicleClassHireNetCeiling" value="hireNetDoesNotExceedVehicleClassHireNetCeiling" /></div>
                            <label class="chox-form-check-label">Vehicle Class Hire Net Ceiling Check</label>
                            <div class="chox-form-check-description">Check to ensure the Hire Net billed by the CHO does not exceed the specified Hire Net ceiling for the replacement hire vehicle’s vehicle class.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="repairNetDoesNotExceedBandRepairNetCeiling" value="repairNetDoesNotExceedBandRepairNetCeiling" /></div>
                            <label class="chox-form-check-label">Repair Net Ceiling Check</label>
                            <div class="chox-form-check-description">Check to ensure the Repair Net billed by the CHO does not exceed the CHO's specified Repair Net ceiling (this Repair Net ceiling is enforced regardless of vehicle class of replacement hire vehicle).</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="repairNetDoesNotExceedVehicleClassRepairNetCeiling" value="repairNetDoesNotExceedVehicleClassRepairNetCeiling" /></div>
                            <label class="chox-form-check-label">Vehicle Class Repair Net Ceiling Check</label>
                            <div class="chox-form-check-description">Check to ensure the Repair Net billed by the CHO does not exceed the specified Repair Net ceiling for the replacement hire vehicle's vehicle class.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hireDayCountDoesNotExceedBandHireDayCeiling" value="hireDayCountDoesNotExceedBandHireDayCeiling" /></div>
                            <label class="chox-form-check-label">Hire Day Ceiling Check</label>
                            <div class="chox-form-check-description">Check to ensure the number of hire days billed by the CHO does not exceed the CHO's specified hire days ceiling.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="actualHireDaysDoesNotExceedAllowableHireDays" value="actualHireDaysDoesNotExceedAllowableHireDays" /></div>
                            <label class="chox-form-check-label">Repair Duration Rules</label>
                            <div class="chox-form-check-description">This is the maximum number of days the CHO can bill for a non Total Loss hire type, please review the series of 'Repair Duration Rules' further up this screen for details on the variables that contribute to the maximum number of days.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="actualHireDaysDoesNotExceedTotalLossInspection" value="actualHireDaysDoesNotExceedTotalLossInspection" /></div>
                            <label class="chox-form-check-label">Total Loss Duration Rule</label>
                            <div class="chox-form-check-description">This is the maximum number of days the CHO can bill for a Total Loss hire type, please review the 'Total Loss Duration Rule' further up this screen for details on the variables that contribute to the maximum number of days.</div>
                        </div>

                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="mobileVehicleTotalLossCheck" value="mobileVehicleTotalLossCheck" /></div>
                            <label class="chox-form-check-label">Mobile Vehicle Total Loss Check</label>
                            <div class="chox-form-check-description">If the CHO's Customer's vehicle has been deemed driveable/usable/mobile and the claim has been flagged as a Total Loss the invoice will be flagged for review.</div>
                        </div>


                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="repairGrossIsLessThanEstimatedTotalRepairAmount" value="repairGrossIsLessThanEstimatedTotalRepairAmount" /></div>
                            <label class="chox-form-check-label">Engineer's Estimated Total Repair Amount Check</label>
                            <div class="chox-form-check-description">Check the Engineer's estimated Total Repair Amount against the Repair Gross amount billed by the CHO. If the Repair Gross billed amount is higher than the Engineer's estimated amount then the invoice will be flagged.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="estimatedRepairDaysPlusBandDaysDoNotExceedHireDays" value="estimatedRepairDaysPlusBandDaysDoNotExceedHireDays" /></div>
                            <label class="chox-form-check-label">Engineer's Estimated Repair Days Check</label>
                            <div class="chox-form-check-description">This checks the Engineer's estimated number of repair days (with the addition of several variables) against the number of hire days billed by the CHO, if the number of hire days billed is greater than the estimation then the invoice will be flagged. Please review the 'Engineer Estimation Rule' further up this screen for more information.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="labourCostBusinessRule" value="labourCostBusinessRule" /></div>
                            <label class="chox-form-check-label">Labour Cost/Hours Check</label>
                            <div class="chox-form-check-description">This check looks at either the labour cost for the repair or the number of labour hours exerted by the repairer during the repair process.  Using the formula as detailed in the 'Labour Cost/hours' rule further up this screen, an acceptable/expected number of hire days based on the labour information provided is calculated.  This calculated number of days is compared against the number of hire days billed by the CHO, if the billed days are greater than the acceptable/expected number of hire days the invoice will be flagged.</div>
                        </div>

                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="totalLabourCostBusinessRule" value="totalLabourCostBusinessRule" /></div>
                            <label class="chox-form-check-label">Total Labour Cost Validation Check</label>
                            <div class="chox-form-check-description">This check ensures that the CHO is not entering the Repair Gross as the Total Labour Cost.</div>
                        </div>

                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="repairBookedInDateOnThursday" value="repairBookedInDateOnThursday" /></div>
                            <label class="chox-form-check-label">Thursday Repair Booked In Date Check For Mobile Vehicles</label>
                            <div class="chox-form-check-description">If the repair has been booked into a garage on a Thursday and the Customer's vehicle is driveable/usable/mobile then the invoice will be flagged for review. Excludes commercial, private hire and taxi vehicles.</div>
                        </div>

                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="repairBookedInDateOnFriday" value="repairBookedInDateOnFriday" /></div>
                            <label class="chox-form-check-label">Friday Repair Booked In Date Check For Mobile Vehicles</label>
                            <div class="chox-form-check-description">If the repair has been booked into a garage on a Friday and the Customer's vehicle is driveable/usable/mobile then the invoice will be flagged for review. Excludes commercial, private hire and taxi vehicles.</div>
                        </div>

                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="repairBookedInDateOnSaturday" value="repairBookedInDateOnSaturday" /></div>
                            <label class="chox-form-check-label">Saturday Repair Booked In Date Check For Mobile Vehicles</label>
                            <div class="chox-form-check-description">If the repair has been booked into a garage on a Saturday and the Customer's vehicle is driveable/usable/mobile then the invoice will be flagged for review. Excludes commercial, private hire and taxi vehicles.</div>
                        </div>

                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="repairBookedInDateOnSunday" value="repairBookedInDateOnSunday" /></div>
                            <label class="chox-form-check-label">Sunday Repair Booked In Date Check For Mobile Vehicles</label>
                            <div class="chox-form-check-description">If the repair has been booked into a garage on a Sunday and the Customer's vehicle is driveable/usable/mobile then the invoice will be flagged for review. Excludes commercial, private hire and taxi vehicles.</div>
                        </div>

                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="validateUniqueVehicleRegistrationNumber" value="validateUniqueVehicleRegistrationNumber" /></div>
                            <label class="chox-form-check-label">CHO's Client's Vehicle Registration Duplication Check</label>
                            <div class="chox-form-check-description">Check on CHO's Client's vehicle registration number, if a claim already exists in CHOX against the same vehicle registration the invoice will be flagged.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="flaggedForManualInvoiceReview" value="flaggedForManualInvoiceReview" /></div>
                            <label class="chox-form-check-label">Invoiced Flagged For Manual Invoice Review</label>
                            <div class="chox-form-check-description">If the claim has been manually flagged at the front of the claim cycle for review, the invoice will be flagged for review upon invoice upload.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasCorrectDiscountForNonDA" value="hasCorrectDiscountForNonDA" /></div>
                            <label class="chox-form-check-label">CHO not participating in the Delegated Authority scheme discount Check</label>
                            <div class="chox-form-check-description">Check on CHOs not participating in the Delegated Authority scheme apply correct VAT discount off the Full Total Requested.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="handlingAmountAndDeductionBothEqualZeroForNonDA" value="handlingAmountAndDeductionBothEqualZeroForNonDA" /></div>
                            <label class="chox-form-check-label">CHO not participating in the Delegated Authority scheme Claims Handling charge check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO who is not participating in the Delegated Authority scheme is not trying to charge for Claims Handling services twice.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="claimHasZeroDiscountForDA" value="claimHasZeroDiscountForDA" /></div>
                            <label class="chox-form-check-label">CHO participating in the Delegated Authority scheme discount Check</label>
                            <div class="chox-form-check-description">Check on CHOs participating in the Delegated Authority scheme do not include a flat VAT discount off the Full Total Requested.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="handlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero" value="handlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero" /></div>
                            <label class="chox-form-check-label">CHO participating in the Delegated Authority scheme Claims Handling charge reconciliation Check</label>
                            <div class="chox-form-check-description">Check on CHOs participating in the Delegated Authority scheme deduct the correct amount off the hire invoice for Claims Handling services.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="numberOfHireDaysReconcile" value="numberOfHireDaysReconcile" /></div>
                            <label class="chox-form-check-label">Hire days billed Reconciliation Check</label>
                            <div class="chox-form-check-description">Check to ensure that the number of hire days billed matches the hire start and hire end data provided.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasCorrectHireVatCalculation" value="hasCorrectHireVatCalculation" /></div>
                            <label class="chox-form-check-label">Hire VAT Calculation Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO has applied the correct VAT charge against the Hire Net.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hireVatLimitCheck" value="hireVatLimitCheck" /></div>
                            <label class="chox-form-check-label">Hire VAT Limit Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is not charging more than the current VAT rate for the Hire.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hireVatHireEndCheck" value="hireVatHireEndCheck" /></div>
                            <label class="chox-form-check-label">Hire VAT Check Using Hire End Date</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is not charging more than the allowed VAT rate for the Hire based on the Hire End Date and in relation to the date of the VAT change.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hireVatInvoicedDateCheck" value="hireVatInvoicedDateCheck" /></div>
                            <label class="chox-form-check-label">Hire VAT Check Using Invoiced Date</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is not charging more than the allowed VAT rate for the Hire based on the Invoiced Date (date hire invoice raised on CHO's internal claim system) and in relation to the date of the VAT change.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasCorrectHireGrossCalculation" value="hasCorrectHireGrossCalculation" /></div>
                            <label class="chox-form-check-label">Hire Gross Calculation Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is charging the correct Hire Gross amount.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasHireGrossSumCheck" value="hasHireGrossSumCheck" /></div>
                            <label class="chox-form-check-label">Hire Gross Sum Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is summing the Hire Net and Hire VAT amounts correctly.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasCorrectRepairVatCalculation" value="hasCorrectRepairVatCalculation" /></div>
                            <label class="chox-form-check-label">Repair VAT Calculation Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO has applied the correct VAT charge against the Repair Net. </div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="repairVatLimitCheck" value="repairVatLimitCheck" /></div>
                            <label class="chox-form-check-label">Repair VAT Limit Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is not charging more than the current VAT rate for the Repair.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="repairVatCompletionDateCheck" value="repairVatCompletionDateCheck" /></div>
                            <label class="chox-form-check-label">Repair VAT Check Using Repair Completion Date</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is not charging more than the allowed VAT rate for the Repair based on the Repair Completion Date and in relation to the date of the VAT change.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasCorrectRepairGrossCalculation" value="hasCorrectRepairGrossCalculation" /></div>
                            <label class="chox-form-check-label">Repair Gross Calculation Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is charging the correct Repair Gross amount.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasRepairGrossSumCheck" value="hasRepairGrossSumCheck" /></div>
                            <label class="chox-form-check-label">Repair Gross Sum Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is summing the Repair Net and Repair VAT amounts correctly.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasCorrectTotalLossVatCalculation" value="hasCorrectTotalLossVatCalculation" /></div>
                            <label class="chox-form-check-label">Total Loss Fee VAT Calculation Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO has applied the correct VAT charge against the Total Loss Fee Net.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="totalLossFeeVatLimitCheck" value="totalLossFeeVatLimitCheck" /></div>
                            <label class="chox-form-check-label">Total Loss Fee VAT Limit Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO has not charged more than the current VAT rate for the Total Loss Fee.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasCorrectTotalLossGrossCalculation" value="hasCorrectTotalLossGrossCalculation" /></div>
                            <label class="chox-form-check-label">Total Loss Fee Gross Calculation Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is charging the correct Total Loss Fee Gross amount.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasTotalLossFeeGrossSumCheck" value="hasTotalLossFeeGrossSumCheck" /></div>
                            <label class="chox-form-check-label">Total Loss Fee Gross Sum Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is summing the Total Loss Fee Net and Total Loss Fee VAT amounts correctly.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="engineerFeeVatLimitCheck" value="engineerFeeVatLimitCheck" /></div>
                            <label class="chox-form-check-label">Engineer Fee VAT Limit Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is not charging more than the current VAT rate for the Engineer Fee. </div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="storageRecoveryVatLimitCheck" value="storageRecoveryVatLimitCheck" /></div>
                            <label class="chox-form-check-label">Storage Recovery VAT Limit Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is not charging more than the current VAT rate for the Storage Recovery. </div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasCorrectTotalNet" value="hasCorrectTotalNet" /></div>
                            <label class="chox-form-check-label">Total Net Calculation Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is charging the correct Total Net amount.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasCorrectTotalVat" value="hasCorrectTotalVat" /></div>
                            <label class="chox-form-check-label">Total VAT Calculation Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO has applied the correct VAT charge against the Total Net.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="totalVatLimitCheck" value="totalVatLimitCheck" /></div>
                            <label class="chox-form-check-label">Total VAT Limit Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO has not charged more than the current VAT rate for the Total.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasCalculatedTotalGrossEqualSuppliedTotalGross" value="hasCalculatedTotalGrossEqualSuppliedTotalGross" /></div>
                            <label class="chox-form-check-label">Total Gross Calculation Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is charging the correct Total Gross amount.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasTotalGrossSumCheck" value="hasTotalGrossSumCheck" /></div>
                            <label class="chox-form-check-label">Total Gross Sum Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is summing the Total Gross Net and Total Gross VAT amounts correctly.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hasSuppliedCorrectTotalToPay" value="hasSuppliedCorrectTotalToPay" /></div>
                            <label class="chox-form-check-label">Total To Pay Calculation Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is charging the correct Total To Pay amount.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="automaticChargeCheck" value="automaticChargeCheck" /></div>
                            <label class="chox-form-check-label">Automatic Charge Check Regardless Of HPI Lookup Result</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for this extra, regardless of whether the HPI lookup identified the hire vehicle to be an automatic.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="automaticChargeCheckHpiLookup" value="automaticChargeCheckHpiLookup" /></div>
                            <label class="chox-form-check-label">Automatic Charge Check With HPI Lookup</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for this extra and the HPI lookup did not identify the Hire Vehicle to be an automatic.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="additionalDriverChargeCheck" value="additionalDriverChargeCheck" /></div>
                            <label class="chox-form-check-label">Additional Driver Charge Check</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for this extra.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="estateChargeCheck" value="estateChargeCheck" /></div>
                            <label class="chox-form-check-label">Estate Charge Check Regardless Of HPI Lookup Result</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for this extra, regardless of whether the HPI lookup identified the hire vehicle to be an estate.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="estateChargeCheckHpi" value="estateChargeCheckHpi" /></div>
                            <label class="chox-form-check-label">Estate Charge Check With HPI Lookup</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for this extra and the HPI lookup did not identify the Hire Vehicle to be an estate.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="nonStandardRiskInsurancePremiumCheck" value="nonStandardRiskInsurancePremiumCheck" /></div>
                            <label class="chox-form-check-label">Non Standard Risk Insurance Premium Check</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging more than the specified limit/ceiling for this extra.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="miscellaneousChargeCheck" value="miscellaneousChargeCheck" /></div>
                            <label class="chox-form-check-label">Miscellaneous Charge Check</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for this extra.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="satelliteNavigationChargeCheck" value="satelliteNavigationChargeCheck" /></div>
                            <label class="chox-form-check-label">Satellite Navigation Charge Check</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for this extra.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="babySeatChargeCheck" value="babySeatChargeCheck" /></div>
                            <label class="chox-form-check-label">Baby Seat Charge Check</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for this extra</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="towBarsChargeCheck" value="towBarsChargeCheck" /></div>
                            <label class="chox-form-check-label">Tow Bars Charge Check</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for this extra</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="roofRackChargeCheck" value="roofRackChargeCheck" /></div>
                            <label class="chox-form-check-label">Roof Rack Charge Check</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for this extra</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="deliveryOrCollectionChargeCheck" value="deliveryOrCollectionChargeCheck" /></div>
                            <label class="chox-form-check-label">Delivery / Collection Charge Check</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for this extra</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="dualControlChargeCheck" value="dualControlChargeCheck" /></div>
                            <label class="chox-form-check-label">Dual Control Charge Check</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for this extra</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="dateRepairCommencedChkForNonMobileVehicle" value="dateRepairCommencedChkForNonMobileVehicle" /></div>
                            <label class="chox-form-check-label">Date Repair Commenced Following Hire Start Check For Non-Mobile Vehicles</label>
                            <div class="chox-form-check-description">This check looks at the maximum number of days the hire can commence prior to the date repairs commenced for un-driveable/non-usable/non-mobile vehicles.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="dateRepairBookInDateChkForMobileVehicle" value="dateRepairBookInDateChkForMobileVehicle" /></div>
                            <label class="chox-form-check-label">Repair Book In Date Following Hire Start Check For Mobile Vehicles</label>
                            <div class="chox-form-check-description">This check looks at the maximum number of days the hire can commence prior to the repair book in date for driveable/usable/mobile vehicles.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="dateRepairBookInDateChkForNonMobileVehicle" value="dateRepairBookInDateChkForNonMobileVehicle" /></div>
                            <label class="chox-form-check-label">Repair Book In Date Following Hire Start Check For Non-Mobile Vehicles</label>
                            <div class="chox-form-check-description">This check looks at the maximum number of days the hire can commence prior to the repair book in date for un-driveable/non-usable/non-mobile vehicles.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hireTerminatedAfterRepairCompletionCheck" value="hireTerminatedAfterRepairCompletionCheck" /></div>
                            <label class="chox-form-check-label">Hire Terminated 1 Day After Repairs Were Complete Check</label>
                            <div class="chox-form-check-description">
                                Claim will be flagged for review if the hire was terminated more than 1 day after the repairs were completed.
                            </div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="correntAdminFee" value="correntAdminFee" /></div>
                            <label class="chox-form-check-label">Correct Administration Fee Check</label>
                            <div class="chox-form-check-description">Check to ensure that the correct administration fee is being charged based on the nature of the service provided, either management of the repair or hire only. Subscriber claims omitted.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="subscriberAdminFeeCheck" value="subscriberAdminFeeCheck" /></div>
                            <label class="chox-form-check-label">Subscriber Administration Fee Check</label>
                            <div class="chox-form-check-description">
                                Check to ensure that the CHO are not billing an administration fee when the Subscriber rejection was accepted by the CHO. Also checks that the correct Admin Fee is being charged by the CHO for Subscriber claims (that have not been rejected) as per the 'Subscriber Admin Fee' tolerance detailed above.
                            </div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="fixedFeeAdminFeeCheck" value="fixedFeeAdminFeeCheck" /></div>
                            <label class="chox-form-check-label">Fixed-Fee Administration Fee Check</label>
                            <div class="chox-form-check-description">
                                Check to ensure that the correct Admin Fee is being charged by the CHO for Fixed Fee claims as per the ‘Fixed Fee Admin Fee’ tolerance detailed above.
                            </div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="supplierAdminstrationFee" value="supplierAdminstrationFee" /></div>
                            <label class="chox-form-check-label">Correct Supplier Administration Fee Check</label>
                            <div class="chox-form-check-description">Check to ensure that the correct administration fee is being charged by the supplier.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="autoRestoreOneDayRepairCheck" value="autoRestoreOneDayRepairCheck" /></div>
                            <label class="chox-form-check-label">Repairer Fixed Hire Days Check</label>
                            <div class="chox-form-check-description">Check on the number of hire days when the vehicle is driveable/usable/mobile, check should only allow the given fixed number of hire days for the specified repairer.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="insurancePremiumTaxCheck" value="insurancePremiumTaxCheck" /></div>
                            <label class="chox-form-check-label">Insurance Premium Tax Check</label>
                            <div class="chox-form-check-description">Check to ensure that the supplier is charging the correct fee for the Insurance Premium Tax or Non Standard Risk Insurance Premium Tax.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="subscriberCheckRejectedClaims" value="subscriberCheckRejectedClaims" /></div>
                            <label class="chox-form-check-label">Subscriber Check For Rejected Claims</label>
                            <div class="chox-form-check-description">
                                Check to ensure that the CHO is not billing for hire days over the allowed amount.
                                This rule is for Subscriber claims to ensure that rejections made by 3pm are recorded
                                and are counted as 1 days hire and that a maximum of 5 days hire is billed if the
                                Subscriber rejection was accepted.
                            </div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="subscriberAcquisitionFeeCheck" value="subscriberAcquisitionFeeCheck" /></div>
                            <label class="chox-form-check-label">Subscriber No Acquisition Fee Check For Rejected Claims</label>
                            <div class="chox-form-check-description">
                                Check to ensure that the CHO are not billing an Acquisition Fee when the Subscriber rejection was accepted by the CHO.
                            </div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="overlappingHireCheck" value="overlappingHireCheck" /></div>
                            <label class="chox-form-check-label">Overlapping Hire Check</label>
                            <div class="chox-form-check-description">
                                Check to ensure that the replacement hire vehicle was not on hire simultaneously across multiple claims.
                            </div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="maximumLabourRateCheck" value="maximumLabourRateCheck" /></div>
                            <label class="chox-form-check-label">Maximum Labour Rate All Vehicles Check</label>
                            <div class="chox-form-check-description">
                                Check to ensure that the CHO is not billing more than the maximum labour rate per hour for all vehicle types (standard, vans, prestige and special).
                            </div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="maximumLabourRateStandardCheck" value="maximumLabourRateStandardCheck" /></div>
                            <label class="chox-form-check-label">Maximum Labour Rate Standard Vehicles & Vans Check</label>
                            <div class="chox-form-check-description">
                                Check to ensure that the CHO is not charging more than the maximum labour rate per hour for standard vehicles and vans.
                            </div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="maximumLabourRatePrestigeCheck" value="maximumLabourRatePrestigeCheck" /></div>
                            <label class="chox-form-check-label">Maximum Labour Rate Prestige & Special Vehicles Check</label>
                            <div class="chox-form-check-description">
                                Check to ensure that the CHO is not charging more than the maximum labour rate per hour for prestige & special vehicles.
                            </div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="hireNetDoesNotExceedProtocolVehicleClassHireNetCeiling" value="hireNetDoesNotExceedProtocolVehicleClassHireNetCeiling" /></div>
                            <label class="chox-form-check-label">Protocol Vehicle Class Hire Net Ceiling Check</label>
                            <div class="chox-form-check-description">Check to ensure that the Hire Net billed by the CHO does not exceed the agreed protocol cost for the specific customer vehicle class.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="repairNetDoesNotExceedProtocolVehicleClassRepairNetCeiling" value="repairNetDoesNotExceedProtocolVehicleClassRepairNetCeiling" /></div>
                            <label class="chox-form-check-label">Protocol Vehicle Class Repair Net Ceiling Check</label>
                            <div class="chox-form-check-description">Check to ensure that the Repair Net billed by the CHO does not exceed the agreed protocol cost for the specific customer vehicle class.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="compoundAutomaticChargeCheckHpiLookup" value="compoundAutomaticChargeCheckHpiLookup" /></div>
                            <label class="chox-form-check-label">Compound Vehicle Class Automatic Charge Check With HPI Lookup</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for this extra and the HPI lookup did not identify the Hire Vehicle to be an automatic.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="compoundEstateChargeCheckHpiLookup" value="compoundEstateChargeCheckHpiLookup" /></div>
                            <label class="chox-form-check-label">Compound Vehicle Class Estate Charge Check With HPI Lookup</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for this extra and the HPI lookup did not identify the Hire Vehicle to be an estate.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="compoundAutomaticEstateChargeCheckHpiLookup" value="compoundAutomaticEstateChargeCheckHpiLookup" /></div>
                            <label class="chox-form-check-label">Compound Vehicle Class Automatic/Estate Charge Check With HPI Lookup</label>
                            <div class="chox-form-check-description">Invoice will be flagged if the CHO is charging for these extras and the HPI lookup did not identify the Hire Vehicle to be an automatic and/or an estate.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="clientVatRegisteredCheck" value="clientVatRegisteredCheck" /></div>
                            <label class="chox-form-check-label">CHO's Client VAT Registered Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is not charging for the hire, repair or storage recovery VAT when the CHO's customer is VAT registered.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="engineerNetFeeCheck" value="engineerNetFeeCheck" /></div>
                            <label class="chox-form-check-label">Engineers Fee Net Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is not charging more than the specified maximum Engineers Fee charge as detailed above.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="totalLossFeeNetCeilingCheck" value="totalLossFeeNetCeilingCheck" /></div>
                            <label class="chox-form-check-label">Total Loss Fee Net Check</label>
                            <div class="chox-form-check-description">Check to ensure that the CHO is not charging more than the specified maximum Total Loss Fee charge as detailed above.</div>
                        </div>
                        <div class="chox-form-checkboxitem">
                            <div class="chox-form-checkbox"><s:checkbox name="totalLossAndStorageFeeCheck" value="totalLossAndStorageFeeCheck" /></div>
                            <label class="chox-form-check-label">Total Loss / Storage & Recovery Fee Net Check</label>
                            <div class="chox-form-check-description">Flag a claim when the CHO is charging a Total Loss Fee and a Storage & Recovery Fee on the same invoice.</div>
                        </div>
                    </div>
                    <input type="hidden" class="chox-ttxt" id="CCDisActive" name="isActive" value="true"/>
                    </div>
            </div>
        </div>
    </form>
</div>
