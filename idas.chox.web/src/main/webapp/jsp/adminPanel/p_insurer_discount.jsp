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
    var insurerDiscountRowEditor;
    var insurerDiscountDefaultDropdownValue={'value':'--- ALL ---','text':-1};
    var choId = <s:property value="choId" />;
    var insdiscountFromDateEditor;
    var insdiscountToDateEditor;
    var insdiscountTypeEditor;

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
                if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
                    var start = Ext.getCmp(field.startDateField);
                    start.setMaxValue(date);
                    start.validate();
                    this.dateRangeMax = date;
                } 
                else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
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
        
                    
            
            
        insdiscountFromDateEditor = new Ext.form.DateField({
            id : 'insdisfromdateId',
            name: 'insdateFrom',
            allowBlank: false,
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
            allowBlank: false,
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
            reader : insurerDiscountSuppliersJsonReader,
            listeners: {load: function() {this.insert(0, new Ext.data.Record(insurerDiscountDefaultDropdownValue));}}
        });

//        insurerDiscountSupplierFilterCombo = new Ext.form.ComboBox({
//            store : insurerDiscountSuppliersStore,
//            id:'insurerDiscountSupplierId',
//            width: 180,
//            listWidth: 180,
//            valueField : 'text',
//            displayField :'value',
//            typeAhead : true,
//            mode : 'local',
//            triggerAction : 'all',
//            valueNotFoundText : '--- ALL ---',
//            //            selectOnFocus : true,
//            listeners: {
//                select: function () {
//                    if(this.getRawValue() == "" ) {
//                        this.clearValue();
//                        choId = -1;
//                        insurerDiscount_loadGridViewList();
//                    }else{
//                        choId = this.getValue();
//                        insurerDiscount_loadGridViewList(choId);
//                    }
//                }
//            }
//        });
//        insurerDiscountSupplierFilterCombo.render('insurerDiscountSuppliers');
//        insurerDiscountSupplierFilterCombo.setValue('--- ALL ---');
        
        
        var tbar = new Ext.Toolbar({
        items:['-','',{
                text:'New ',
                handler : function() {
                    
                        Ext.getCmp('newInsurerDiscountFormId').getForm().reset();
                        newInsurerDiscountWindow.show();
//                        Ext.getCmp('newInsurerDiscountFormId').getForm().reset();
//                        Ext.getCmp('newInsurerDiscountFormId').show();
                   
                }
            },'-','']
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
                allowBlank : false,
                fieldLabel : 'Type',
                name : ''
        });
        
        var insurerDiscountForm = new Ext.FormPanel({
        xtype       : 'form',
        id : 'newInsurerDiscountFormId',
        frame:true,
        title: 'Multi Column, Nested Layouts and Anchoring',
        bodyStyle : 'padding:10px',
        defaults     : {
            msgTarget : 'side'
        },
        width: 400,
        items : [{
                xtype : 'combo',
                store : insurerDiscountSuppliersStore,
                id:'newInsurerDiscountSupplierId',
                width: 180,
                listWidth: 180,
                valueField : 'text',
                displayField :'value',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                allowBlank: false,
                emptyText  : 'Please select CHO',
                fieldLabel : 'CHO',
                listeners: {
                select: function () {
                    if(this.getRawValue() == "" ) {
                        this.clearValue();
                        choId = -1;
                    }else{
                        choId = this.getValue();
                    }
                }
            }
            },{
                xtype : 'datefield',
                id : 'InsurerDiscountDateFromId',
                name: 'dateFrom',
//                renderTo: 'discountDateFromId',
                width: 95,
                allowBlank: false,
                format: 'd/m/Y',
                fieldLabel : 'Date From',
                //            value: '',
                showWeekNumber: true,
                vtype: 'daterange',
                endDateField: 'InsurerDiscountDateToId'
            }, {
                xtype : 'datefield',
                id : 'InsurerDiscountDateToId',
                name: 'dateTo',
//                renderTo: 'discountDateToId',
                width: 95,
                allowBlank: false,
                format: 'd/m/Y',
                fieldLabel : 'Date To',
                //            value: '',
                showWeekNumber: true,
                vtype: 'daterange',
                startDateField: 'InsurerDiscountDateFromId'
            }, {
                xtype : 'textfield',
                id:"newInsurerDiscountPercentageId",
                fieldLabel : 'Discount %',
                name:"discountPercentage",
                width:95,
                allowBlank : false,
                maskRe : /[0-9.]+/,
//                minValue : 0.1,
                allowNegative : false,
                maxValue : 100
                

            }, {
                xtype : 'combo',
                store : insurerDiscountTypeStore,
                id:'insurerDiscountTypeComboId',
                width: 95,
                listWidth: 95,
                valueField : 'value',
                displayField :'text',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                allowBlank : false,
                fieldLabel : 'Type',
                name : 'insurerDiscountType'
            },{
                xtype : 'checkbox',
                name : 'applyPenalties',
                fieldLabel : 'Apply To Penalties?'
            },{
                xtype : 'hidden',
                id : 'insurerDiscountInsurerId',
                name : 'insurerId'
            },{
                xtype : 'hidden',
                id : 'insurerDiscountCHOId',
                name : 'choId'
            },{
                xtype : 'hidden',
                id : 'insurerDiscountNonceId',
                name : 'nonce'
            }],

            buttons : [ {
                text : 'Save',
                handler : function() {
                    Ext.getCmp('insurerDiscountNonceId').setValue('<%= session.getAttribute("SessionNonce")%>');
                    Ext.getCmp('insurerDiscountInsurerId').setValue('<s:property value="insurerId" />');
                    Ext.getCmp('insurerDiscountCHOId').setValue(choId);
//                    if (Ext.getCmp('newInsurerDiscountFormId').validate()) {
                        insurerDiscountForm.getForm().submit({
                        url:"<%= request.getContextPath()%>/prv/p/addOrUpdateDiscount.action",
//                        param : {"insurerId":<s:property value="insurerId" />},
                        clientValidation: true,
                        waitTitle :'Please wait',
                        waitMsg :'Creating Discount...',
                        success : function(f, a) {

                            if ( a.result.success ){
                                newInsurerDiscountWindow.hide();
                                insurerDiscount_loadGridViewList();
                            }
                            clearValidation();
                        },
                        failure: function(form, action) {
                            switch (action.failureType) {
                            case Ext.form.Action.CLIENT_INVALID:
//                                    Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
                                    break;
                            case Ext.form.Action.CONNECT_FAILURE:
                                    Ext.MessageBox.show({
                                    title: 'Failure',
                                    msg: 'Ajax communication failed',
                                    width:300,
                                    buttons: Ext.MessageBox.OK,
                                    icon : Ext.MessageBox.ERROR
                                    });
                                    break;
                            case Ext.form.Action.SERVER_INVALID:
                                    Ext.MessageBox.show({
                                    title: 'Error',
                                    msg: formErrorMessage(action.result.errors),
                                    width:300,
                                    buttons: Ext.MessageBox.OK,
                                    icon : Ext.MessageBox.ERROR
                                });
                            }
                        }
                    });
                    }
                        
//                }

            }, {
                text : 'Cancel',
                handler : function(){
                    newInsurerDiscountWindow.hide();
                }
            } ]

        });
        
        var newInsurerDiscountWindow = new Ext.Window({
        id     : 'newInsurerDiscountWindow',
        closeAction : 'hide',
        autoHeight: true, 
//        height : 600,
        autoWidth : true,
        resizable : false,
        items  : [insurerDiscountForm]
//        listners : {beforeclose : function() {
//                alert("before close called")
//                this.hide();
//        }} 
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
                {name:'insurerDiscountType'},
                {name:'appliedToPenalties'}
            ]
        });

        insurerDiscount_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/listDiscountGridData.action',method:'POST'}),
            reader:insurerDiscount_gridviewJsonReader,
            listeners: {update : function(store,record,operation) {
                    clearValidation();
                    $("div#CDInsurerinsurerDiscountMessageBox").html("");
                    var url = "<%= request.getContextPath()%>/prv/p/addOrUpdateDiscount.action";
                    var param = {"insurerId":<s:property value="insurerId" />,"choId":choId,"dateFrom": record.get('dateFrom').format('d/m/Y'),"dateTo": record.get('dateTo').format('d/m/Y'),"discountPercentage": record.get('discount'),"discountId": record.get('discountId'),"insurerDiscountType": record.get('insurerDiscountType')};
                    ajax.loadHtml2(url, param, function(responseText, statusText){
                
                        var response = eval('(' + responseText.trim() + ')');
                        var outputDiv = $('div#CDInsurerinsurerDiscountMessageBox');
                        if(response){
                    
                            if(response.success){
                                insurerDiscount_loadGridViewList();
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
        
        
            tbar:tbar,
            listeners:  {cellclick:insurerDiscount_recordOnclick },
            store: insurerDiscount_gridviewData,
            enableHdMenu:false,
            layout:'fit',
            plugins: [insurerDiscountRowEditor],
            viewConfig:{forceFit:true},
            columns: [
                {header: "Date From",  width: 90, dataIndex: 'dateFrom', sortable: true, resizable: true,editor: insdiscountFromDateEditor},
                {header: "Date To",  width: 90, dataIndex: 'dateTo', sortable: true, resizable: true,editor: insdiscountToDateEditor},
                {header: "Discount", width: 60, dataIndex: 'discount',  sortable: true, resizable: true,xtype: 'numbercolumn',format: '0,0.00%',editor: {xtype: 'numberfield',allowBlank: false, maxValue : 100, allowNegative : false, emptyText  : 'Discount is required'}},
                {header: "Discount Type", width: 60, dataIndex: 'insurerDiscountType',  sortable: true, resizable: true, editor: insdiscountTypeEditor},
                {header: "Apply To Penalties", width: 60, dataIndex: 'appliedToPenalties',  sortable: true, resizable: true},
                {header: "CHO Name", width: 170, dataIndex: 'choName', sortable: true, resizable: true,editable : false},
                {header: "Created By", width: 150, dataIndex: 'createdBy', sortable: true, resizable: true,editable : false},
                {header: "Created Date", width: 130, dataIndex: 'createdDate', sortable: true, resizable: true,editable : false},
                {header: "Action", width: 70, dataIndex: 'Remove', sortable: true, resizable: true,editable : false, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>Remove</a>"}}
            ],
            renderTo:'insurerDiscount_gridviewGridPanel',
            height:450,
            width: 760
        });

        insurerDiscount_loadGridViewList();

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
        
//        insurerDiscount_gridviewData.load({ params : { insurerId:<s:property value="insurerId" />,choId:choId } });
    }

    function insurerDiscount_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = insurerDiscount_gridviewGrid.getStore().getAt(rowIndex);
        
        if(columnIndex == 8){
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
                if(btn=='ok'){
                    var insurerDiscountId = gridView.get("discountId");
                    var url = "<%= request.getContextPath()%>/prv/p/deleteInsurerDiscount.action";
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

//                outputDiv.addClass("chox-form-submit-result");
//                outputDiv.html("Record has been deleted.");
                insurerDiscount_loadGridViewList();

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

    
</script>
<div class="sub-admin-tab-css">
<!--    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>-->
    <div class="status-info">
        This tab allows you to setup discounts for CHOs. The discount is off the total amount submitted by the CHO and you can also select the period the discount should be applied.  The ‘Total To Pay’ will be automatically updated when the invoice is uploaded or when the invoice is amended by the CHO.
    </div>

    <div class="grid-view-header">

<!--        <table >
            <tr><td>
                    <div class="label-block">
                        <p class="std-label-insdiscount">CHO</p><div id="insurerDiscountSuppliers"></div>
                    </div>
                </td>
            </tr>
        </table>-->
        &nbsp;

    </div>
    <div id="CDInsurerinsurerDiscountMessageBox" class="chox-form-submit-result"></div>
    <div id="insurerDiscount_gridviewGridPanel"></div>
</div>