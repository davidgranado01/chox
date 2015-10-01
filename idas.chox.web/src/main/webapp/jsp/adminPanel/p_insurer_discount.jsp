<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var insurerDiscount_gridviewJsonReader;
    var insurerDiscount_gridviewDataStore;
    var insurerDiscount_gridviewGrid;
    var insurerDiscount_gridviewData;
    var insurerDiscountSuppliersJsonReader;
    var insurerDiscountMysuppliers;
    var insurerDiscountSuppliersStore;
    var insurerDiscountSupplierFilterCombo;
    var insurerDiscountTypeCombo;
    var insurerDiscountRowEditor;
    var choId = -1;
    var discountTypeId = -1;
    var insdiscountFromDateEditor;
    var insdiscountToDateEditor;
    var insdiscountTypeEditor;
    var applyToPenaltiesChecked = false;

    Ext.onReady(function(){
        Ext.QuickTips.init();
        
        if (Ext.getCmp('newInsurerDiscountWindow')) {
            Ext.getCmp('newInsurerDiscountWindow').destroy();
        }
        
        Ext.apply(Ext.form.VTypes, {
            daterange : function(val, field) {
                var date = field.parseDate(val);
 
                if(!date){
                    return;
                }
                if (field.startDateField && (!this.dateRangeMax || (date.getTime() !== this.dateRangeMax.getTime()))) {
                    var start = Ext.getCmp(field.startDateField);
                    start.setMaxValue(date);
                    start.validate();
                    this.dateRangeMax = date;
                } 
                else if (field.endDateField && (!this.dateRangeMin || (date.getTime() !== this.dateRangeMin.getTime()))) {
                    var end = Ext.getCmp(field.endDateField);
                    end.setMinValue(date);
                    end.validate();
                    this.dateRangeMin = date;
                }
                /*
                 * Always return true since we're only using this vtype to set the
                 * min/max allowed values (these are tested for after the vtype test)
                 */
                return true;
            }
        });
        
        insurerDiscountTypeJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'text'},
                {name:'value'}
            ]
        });

        insurerDiscountType = Ext.util.JSON.decode('<s:property value="insurerDiscountTypeJsonString" escape="false"/>');
        insurerDiscountTypeStore = new Ext.data.Store({
            data : insurerDiscountType,
            reader : insurerDiscountTypeJsonReader
        });
       
            
        insurerDiscountSuppliersJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'text'},
                {name:'value'}
            ]
        });

        insurerDiscountMysuppliers = Ext.util.JSON.decode('<s:property value="suppliersJsonString" escape="false"/>');
        insurerDiscountSuppliersStore = new Ext.data.Store({
            data : insurerDiscountMysuppliers,
            reader : insurerDiscountSuppliersJsonReader
        });
        
        
        var discountPercentageField =new Ext.form.NumberField({
            id:"insurerDiscountPercentageId",
            name:"discountPercentage",
            width:40,
            allowNegative : false,
            maxValue : 100,
            renderTo:'discountPercentageId'
        });
            
            
        var discountPercentageDateFrom = new Ext.form.DateField({
            id : 'InsurerDiscountDateFromId',
            name: 'dateFrom',
            renderTo: 'discountDateFromId',
            width: 95,
            format: 'd/m/Y',
            showWeekNumber: true,
            vtype: 'daterange',
            endDateField: 'InsurerDiscountDateToId'
        });
        
        var discountPercentageDateTo = new Ext.form.DateField({
            id : 'InsurerDiscountDateToId',
            name: 'dateTo',
            renderTo: 'discountDateToId',
            width: 95,
            format: 'd/m/Y',
            showWeekNumber: true,
            vtype: 'daterange',
            startDateField: 'InsurerDiscountDateFromId'
        });
            
        var appliedToPenalties = new Ext.form.Checkbox ({
            id : 'applyPenaltiesToInsurerTypeId',
            name : 'applyPenalties',
            renderTo : 'appliedToPenaltiesInsurerDiscountId'
        });

        insurerDiscountSupplierFilterCombo = new Ext.form.ComboBox({
            store : insurerDiscountSuppliersStore,
            name : 'insurerDiscountSupplierId',
            width: 150,
            listWidth: 150,
            valueField : 'text',
            displayField :'value',
            typeAhead : true,
            mode : 'local',
            triggerAction : 'all',
            renderTo : 'insurerDiscountSuppliers',
            listeners: {
                select: function () {
                    if(this.getRawValue() === "" ) {
                        this.clearValue();
                        choId = -1;
                        insurerDiscount_loadGridViewList();
                    }else{
                        choId = this.getValue();
                        insurerDiscount_loadGridViewList(choId);
                    }
                }
            }
        });
        
        
        insurerDiscountTypeCombo = new Ext.form.ComboBox({
                store : insurerDiscountTypeStore,
                name : 'insurerDiscountTypeComboId',
                width: 95,
                listWidth: 95,
                valueField : 'text',
                displayField :'text',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
//                allowBlank : false,
                renderTo : 'discountTypeComboId',
                listeners: {
                select: function () {
                    if(this.getRawValue() === "" ) {
                        this.clearValue();
                        discountTypeId = -1;
                    }else{
                        discountTypeId = this.getValue();
                    }
                }
            }
        }); 
        
        
        insdiscountFromDateEditor = new Ext.form.DateField({
            id : 'insdisfromdateId',
            name: 'insdateFrom',
            format: 'd/m/Y',
            showWeekNumber: true
            ,listeners: {
                change : function(){
                    var end = Ext.getCmp('insdistodateId');
                    end.setMinValue(this.getValue());
                },
                focus : function(){
                    var end = Ext.getCmp('insdistodateId');
                    this.setMaxValue(end.getValue());
                }
            }

        });
        
        insdiscountToDateEditor = new Ext.form.DateField({
            id : 'insdistodateId',
            name: 'insdateTo',
            format: 'd/m/Y',
            showWeekNumber: true
            ,listeners: {change : function(){
                    var start = Ext.getCmp('insdisfromdateId');
                    start.setMaxValue(this.getValue());
                },
                focus : function(){
                    var start = Ext.getCmp('insdisfromdateId');
                    this.setMinValue(start.getValue());
                }
            }
        });
        
        insdiscountTypeEditor = new Ext.form.ComboBox({
                store : insurerDiscountTypeStore,
                width: 120,
                listWidth: 120,
                valueField : 'text',
                displayField :'text',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                fieldLabel : 'Type',
                name : ''
        });
        
        insurerDiscount_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'discountId'},
                {name:'dateFrom'},
                {name:'dateTo'},
                {name:'discount'},
                {name:'createdBy'},
                {name:'createdDate'},
                {name:'choName'},
                {name:'choId'},
                {name:'insurerDiscountType'},
                {name:'appliedToPenalties'}
            ]
        });

        insurerDiscount_gridviewData = new choxDataStore({
            url: '/prv/p/listDiscountGridData.action',
            reader:insurerDiscount_gridviewJsonReader,
            listeners: {update : function(store,record,operation) {
                    clearValidation();
                    $("div#CDInsurerinsurerDiscountMessageBox").html("");
                    var url = "/prv/p/addOrUpdateDiscount.action";
                    var param = {"insurerId":<s:property value="insurerId" />,"choId": record.get('choId'),"dateFrom": record.get('dateFrom').format('d/m/Y'),"dateTo": record.get('dateTo').format('d/m/Y'),"discountPercentage": record.get('discount'),"discountId": record.get('discountId'),"insurerDiscountType": record.get('insurerDiscountType'),"appliedToPenalties": record.get('appliedToPenalties')};
                    ajax.loadHtml2(url, param, function(responseText, statusText){
                
                        var response = eval('(' + responseText.trim() + ')');
                        var outputDiv = $('div#CDInsurerinsurerDiscountMessageBox');
                        if(response){
                    
                            if(response.success){
                                insurerDiscount_loadGridViewList(choId);
                            } else if(response.errors){
                                Ext.MessageBox.show({
                                    title: 'ERROR',
                                    msg: response.errors.dateTo,
                                    width:300,
                                    buttons: Ext.MessageBox.OK,
                                    icon : Ext.MessageBox.ERROR
                                });
                            }
                        }
                    });
                }
            }
        });
        insurerDiscount_gridviewData.setDefaultSort('choName', 'asc');
        insurerDiscountRowEditor = new Ext.ux.grid.RowEditor({
            saveText: 'Update'
            ,clicksToEdit: 2
            ,listeners: {
                canceledit : function () {
                    clearValidation();
                }
            }
        });

        insurerDiscount_gridviewGrid = new Ext.grid.GridPanel({
        
            listeners:  {cellclick:insurerDiscount_recordOnclick },
            store: insurerDiscount_gridviewData,
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            plugins: [insurerDiscountRowEditor],
            viewConfig:{forceFit:true},
            columns: [
                {header: "Date From",  width: 90, dataIndex: 'dateFrom', sortable: false, resizable: true, editor: insdiscountFromDateEditor},
                {header: "Date To",  width: 90, dataIndex: 'dateTo', sortable: false, resizable: true,editor: insdiscountToDateEditor},
                {header: "Discount", width: 60, dataIndex: 'discount',  sortable: false, resizable: true,xtype: 'numbercolumn',format: '0,0.00%',editor: {xtype: 'numberfield',allowBlank: false, maxValue : 100, allowNegative : false, emptyText  : 'Discount is required'}},
                {header: "Discount Type", width: 60, dataIndex: 'insurerDiscountType', sortable: false, resizable: true, editor: insdiscountTypeEditor},
                {header: "Apply To Penalties", width: 60, dataIndex: 'appliedToPenalties',  sortable: false, resizable: true,editor: {xtype: 'checkbox', listeners:{beforeshow:function(){this.setValue(applyToPenaltiesChecked);}}}},
                {header: "CHO Name", width: 170, dataIndex: 'choName', sortable: false, resizable: true,editable : false},
                {header: "Created By", width: 150, dataIndex: 'createdBy', sortable: false, resizable: true,editable : false},
                {header: "Created Date", width: 130, dataIndex: 'createdDate', sortable: false, resizable: true,editable : false},
                {header: "Action", width: 70, dataIndex: 'Remove', sortable: false, resizable: true,editable : false, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>Remove</a>";}}
            ],
            renderTo:'insurerDiscount_gridviewGridPanel',
            height:300,
            width: 760
        });

        insurerDiscount_loadGridViewList();
        
        $.validator.addMethod(
        	    "checkCHOId",
        	    function(value, element) {
//                        var choId = insurerDiscountSupplierFilterCombo.getValue();
                        if (choId===-1 || choId === null || choId ===  '' || choId === 0) {
                            return false;
                        }
                    return true;
                }
        );
         
        $.validator.addMethod(
        	    "checkDiscountType",
        	    function(value, element) {
                        if (discountTypeId===-1 || discountTypeId === null || discountTypeId ===  '' || discountTypeId === 0){
                            return false;
                        }
                    return true;
                }
        );
            
            
        $("#insurerDiscountForm").validate(
        {
            errorLabelContainer: "#CDInsurerinsurerDiscountMessageBox",
            rules: {
            	
                insurerDiscountTypeComboId : {checkDiscountType:true},
                insurerDiscountSupplierId : { checkCHOId:true },
                dateFrom :{required:true, dateITA:true, max:function(){
                    var sd = Ext.get('InsurerDiscountDateFromId').getValue().split("/");
                    var ed = Ext.get('InsurerDiscountDateToId').getValue().split("/");
                    var time = new Date(sd[2],sd[1] - 1 ,sd[0]).getTime() - new Date(ed[2],ed[1] - 1 ,ed[0]).getTime();
	            if(time > 0)return true;
	        }},
                dateTo :{required:true, dateITA:true},
                discountPercentage : {required:true,number:true, max:100}
                
            },
            messages: {
                dateFrom : {required:"A value must be supplied for 'Date From'", dateITA:"You must supply valid date format for 'Date From'", max:"'Date To' can't be before 'Date From'"},
                dateTo : {required:"A value must be supplied for 'Date To'", dateITA:"You must supply valid date format for 'Date To'"},
                discountPercentage : {required:"A value must be supplied for 'Discount Percentage'", max :"Maximum allowed discount is 100%"},
                insurerDiscountTypeComboId : {checkDiscountType : "Please select a Discount Type from the drop-down list"},
                insurerDiscountSupplierId : {checkCHOId : "Please select a CHO from the drop-down list"}
            }
        });
        
    });
    
    function clearValidation(){
        var from = Ext.getCmp('InsurerDiscountDateFromId');
        var to = Ext.getCmp('InsurerDiscountDateToId');
        var end = Ext.getCmp('insdistodateId');
        var start = Ext.getCmp('insdisfromdateId');
        from.setMaxValue(null);
        to.setMinValue(null);
        start.setMaxValue(null);
        end.setMinValue(null);
        
    }

    function insurerDiscount_loadGridViewList(choId){
        if(choId > 0) {
            insurerDiscount_gridviewData.load({ params : { insurerId:<s:property value="insurerId" />, choId:choId } });
        } else {
            insurerDiscount_gridviewData.load({ params : { insurerId:<s:property value="insurerId" />, choId:-1 } });
        }
    }

    function insurerDiscount_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = insurerDiscount_gridviewGrid.getStore().getAt(rowIndex);
        if (gridView.get("appliedToPenalties")=== 'Yes') {
            applyToPenaltiesChecked = true;
        } else {
            applyToPenaltiesChecked = false;
        }
        if(columnIndex === 8){
            insurerDiscount_triggerStatusRemoveRecord(gridView);
        }
    }
    
    function insurerDiscount_triggerStatusRemoveRecord(gridView){
        
        Ext.MessageBox.show({
            title: '',
            msg: 'Are you sure you want to remove this discount?',
            width:300,
            buttons: Ext.MessageBox.OKCANCEL,
            icon : Ext.MessageBox.QUESTION,
            fn: function removeInsurerDiscount(btn){
                if(btn==='ok'){
                    var insurerDiscountId = gridView.get("discountId");
                    var url = "/prv/p/deleteInsurerDiscount.action";
                    var param = {"discountId": insurerDiscountId};
                    ajax.loadHtml2(url, param, insurerDiscount_onSubmitResponseReceived);
                } 
            }
        });

    }

    function insurerDiscount_onSubmitResponseReceived(responseText, statusText)  {

        var response = eval('(' + responseText.trim() + ')');
        var outputDiv = $('div#CDInsurerinsurerDiscountMessageBox');

        triggerCss("div#CDInsurerinsurerDiscountMessageBox", true);

        if(response)
        {
            triggerCss("div#CDInsurerinsurerDiscountMessageBox", false);

            if(response.success){
                insurerDiscount_loadGridViewList(choId);
            }
            else
            {
                triggerCss("div#CDInsurerinsurerDiscountMessageBox", true);
                $.each(response.errors, function() {
                    outputDiv.append(this.toString());
                });
            }
        }
        else
        {
            outputDiv.append("Unknown Error Encountered, please try again.");
            outputDiv.addClass("submit-error");
        }

    }
    
    function insurerDiscount_triggerStatusAddRecord(){
        
        $("div#CDInsurerinsurerDiscountMessageBox").html("");
        var insurerDiscountPercentage = $("#insurerDiscountPercentageId").val();
        var insurerDiscountDateFrom = $("#InsurerDiscountDateFromId").val();
        var insurerDiscountDateTo = $("#InsurerDiscountDateToId").val();
        var insurerDiscountapplyPenalties = Ext.getCmp('applyPenaltiesToInsurerTypeId').getValue();

        if ($("form#insurerDiscountForm").valid()) {

            var url = "/prv/p/addOrUpdateDiscount.action";
            var param = {"insurerId":<s:property value="insurerId" />,"choId":choId,"dateFrom":insurerDiscountDateFrom,"dateTo":insurerDiscountDateTo,"discountPercentage":insurerDiscountPercentage, "insurerDiscountType" : discountTypeId, "appliedToPenalties": insurerDiscountapplyPenalties};
            ajax.loadHtml2(url, param, function(responseText, statusText){
                
                var response = eval('(' + responseText.trim() + ')');
                if(response){
                    
                    if(response.success){
                        Ext.getCmp('InsurerDiscountDateFromId').reset();
                        Ext.getCmp('InsurerDiscountDateToId').reset();
                        Ext.getCmp('insurerDiscountPercentageId').reset();
//                        insurerDiscountTypeCombo.reset();
//                        Ext.getCmp('insurerDiscountTypeComboIdId').reset();
                        Ext.getCmp('applyPenaltiesToInsurerTypeId').reset();
//                        Ext.getCmp('insurerDiscountSupplierId').reset();
                        clearValidation();
                        insurerDiscount_loadGridViewList(choId);
                    } else if(response.errors){
                        Ext.MessageBox.show({
                          title: 'Error',
                          msg: formErrorMessage(response.errors),
                          width:300,
                          buttons: Ext.MessageBox.OK,
                          icon : Ext.MessageBox.ERROR
                      });
                    }
                    
                }
                
            });
        }

    }

    
</script>
<div class="sub-admin-tab-css">
    
    <div class="status-info">
        This tab allows you to setup discounts for CHOs. The discount can be off the hire, repair and/or total as submitted by the CHO and you can also select the period the discount should be applied from and to, the period will be based on the date the invoice was submitted into CHOX by the CHO. If the invoice is amended by the CHO then the discount will be applied to the revised amount(s).
    </div>

    <div class="grid-view-header">
        <div class="admin-bre-band-detail-section">
            <div class="section-name">Insurer Discount</div>
            <form id="insurerDiscountForm" class="XXentity-form" name="insurerDiscountForm" action="POST">
            <table width="100%">
                <tr>
                    <td width ="16%">
                        <p class="std-label-insdiscount">CHO<span class="mandatory">*</span> </p>
                    </td>
                    <td width ="16%">
                        <div id="insurerDiscountSuppliers"></div>
                    </td>
                    <td width ="16%">
                        <p class="std-label-insdiscount">Discount Type<span class="mandatory">*</span> </p>
                    </td>
                    <td width ="16%">
                        <div id="discountTypeComboId"></div>
                    </td>
                    <td width ="16%">
                        <p class="std-label-insdiscount">Apply To Penalties? </p>
                    </td>
                    <td width ="16%">
                        <div id="appliedToPenaltiesInsurerDiscountId"></div>
                    </td>
                </tr>
                <tr>
                    <td width ="16%">
                        <p class="std-label-insdiscount">Date From<span class="mandatory">*</span> </p>
                    </td> 
                    <td width ="16%">
                        <div id="discountDateFromId"></div>
                    </td>
                    <td width ="16%">
                        <p class="std-label-insdiscount">Date To<span class="mandatory">*</span> </p>
                    </td> 
                    <td width ="16%">
                        <div id="discountDateToId"></div>
                    </td>
                    <td width ="16%">
                        <p class="std-label-insdiscount">Discount %<span class="mandatory">*</span> </p>
                    </td> 
                    <td width ="16%">
                        <div id="discountPercentageId"></div> 
                    </td>
                </tr>
            </table>
            </form>
            &nbsp;
            <table width="100%">
                <tr>
                    <td width ="100%" align="center">
                        <input type="button" onclick="javascript:return insurerDiscount_triggerStatusAddRecord();" value="Add"/>
                    </td>
                </tr>  
            </table>
            <div id="CDInsurerinsurerDiscountMessageBox" class="chox-form-submit-result"></div>
        </div>
    </div>
    
    <div id="insurerDiscount_gridviewGridPanel"></div>
</div>