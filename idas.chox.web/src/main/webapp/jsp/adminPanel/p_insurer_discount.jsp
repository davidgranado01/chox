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

    Ext.onReady(function(){
        
        
        var discountAmountField =new Ext.form.NumberField({
            id:"InsurerDiscountAmountId",
            name:"discountAmount",
            width:70,
            allowBlank:false,
            //            fieldLabel : 'Discount Amount',
            //            value:,
            renderTo:'discountAmountId'
        });
            
            
        var discountAmountDateFrom = new Ext.form.DateField({
            id : 'InsurerDiscountDateFromId',
            name: 'dateFrom',
            renderTo: 'discountDateFromId',
            width: 100,
            allowBlank: false,
            format: 'd/m/Y',
            //            fieldLabel : 'Date From',
            //            value: '',
            showWeekNumber: true
        });
        
        var discountAmountDateTo = new Ext.form.DateField({
            id : 'InsurerDiscountDateToId',
            name: 'dateTo',
            renderTo: 'discountDateToId',
            width: 100,
            allowBlank: false,
            format: 'd/m/Y',
            //            fieldLabel : 'Date To',
            //            value: '',
            showWeekNumber: true
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

        insurerDiscountSupplierFilterCombo = new Ext.form.ComboBox({
            store : insurerDiscountSuppliersStore,
            id:'insurerDiscountSupplierId',
            width: 180,
            listWidth: 180,
            valueField : 'text',
            displayField :'value',
            typeAhead : true,
            mode : 'local',
            triggerAction : 'all',
            valueNotFoundText : '--- ALL ---',
            //            selectOnFocus : true,
            listeners: {
                select: function () {
                    if(this.getRawValue() == "" ) {
                        this.clearValue();
                        choId = -1;
                        insurerDiscount_loadGridViewList();
                    }else{
                        choId = this.getValue();
                        insurerDiscount_loadGridViewList();
                    }
                }
            }
        });
        insurerDiscountSupplierFilterCombo.render('insurerDiscountSuppliers');
        insurerDiscountSupplierFilterCombo.setValue('--- ALL ---');
        
        
        insurerDiscount_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'discountId'},
                {name:'dateFrom',type: 'date',format: 'd/m/Y'},
                {name:'dateTo',type: 'date',format: 'd/m/Y'},
                {name:'discount'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        insurerDiscount_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/listDiscountGridData.action',method:'POST'}),
            reader:insurerDiscount_gridviewJsonReader,
            listeners: {update : function(store,record,operation) {
                    $("div#CDInsurerinsurerDiscountMessageBox").html("");
                    if(choId==-1 || choId == null || choId ==  '' || choId == 0){
                        alert("Please choose 'CHO' from drop down list"); 
                    }else{
                        var url = "<%= request.getContextPath()%>/prv/p/addOrUpdateDiscount.action";
                        var param = {"insurerId":<s:property value="insurerId" />,"choId":choId,"dateFrom": record.get('dateFrom').format('d/m/Y'),"dateTo": record.get('dateTo').format('d/m/Y'),"discountAmount": record.get('discount'),"discountId": record.get('discountId')};
                        ajax.loadHtml2(url, param, function(responseText, statusText){
                
                            var response = eval('(' + responseText.trim() + ')');
                            var outputDiv = $('div#CDInsurerinsurerDiscountMessageBox');
                            if(response){
                    
                                if(response.success){
                                    alert("discount has been updated");
                                    insurerDiscount_loadGridViewList();
                                } else if(response.errors){
                                    alert(response.errors.dateTo);
                                }
                    
                            }
                
                        });
                    }
                    
                }
            }
        });
        
        insurerDiscountRowEditor = new Ext.ux.grid.RowEditor({
            saveText: 'Update'
        });

        insurerDiscount_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:insurerDiscount_recordOnclick },
            store: insurerDiscount_gridviewData,
            enableHdMenu:false,
            layout:'fit',
            plugins: [insurerDiscountRowEditor],
            viewConfig:{forceFit:true},
            columns: [
                {header: "Date From", xtype: 'datecolumn', width: 140, dataIndex: 'dateFrom', sortable: true, resizable: true,format: 'd/m/Y',editor: {xtype: 'datefield',format: 'd/m/Y',allowBlank: false, emptyText  : 'Date From is required'}},
                {header: "Date To", xtype: 'datecolumn', width: 140, dataIndex: 'dateTo', sortable: true, resizable: true,format: 'd/m/Y',editor: {xtype: 'datefield',format: 'd/m/Y',allowBlank: false, emptyText  : 'Date To is required'}},
                {header: "Discount", width: 80, dataIndex: 'discount', sortable: true, resizable: true,editor: {xtype: 'numberfield',allowBlank: false, emptyText  : 'Discount is required'}},
                {header: "Created By", width: 200, dataIndex: 'createdBy', sortable: true, resizable: true,editable : false},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true,editable : false},
                {header: "Action", width: 70, dataIndex: 'Remove', sortable: true, resizable: true,editable : false, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>Remove</a>"}},
            ],
            renderTo:'insurerDiscount_gridviewGrid',
            height:405,
            width: 770
        });

        insurerDiscount_loadGridViewList();

    });
    

    function insurerDiscount_loadGridViewList(){
        insurerDiscount_gridviewData.load({ params : { insurerId:<s:property value="insurerId" />,choId:choId } });
    }

    function insurerDiscount_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = insurerDiscount_gridviewGrid.getStore().getAt(rowIndex);
        
        if(columnIndex==5){
            if(choId==-1 || choId == null || choId ==  '' || choId == 0){
                Ext.MessageBox.show({
                    title: 'Please select CHO',
                    msg: 'In order to remove record CHO selection is must..',
                    width:300,
                    buttons: Ext.MessageBox.OK,
                    icon : Ext.MessageBox.ERROR
                });
            }else{
                insurerDiscount_triggerStatusRemoveRecord(gridView);
            }
            
        }
    }

    function insurerDiscount_triggerStatusAddRecord(){
        
        $("div#CDInsurerinsurerDiscountMessageBox").html("");
        var insurerDiscountAmount = $("#InsurerDiscountAmountId").val();
        var insurerDiscountDateFrom = $("#InsurerDiscountDateFromId").val();
        var insurerDiscountDateTo = $("#InsurerDiscountDateToId").val();

        
        if(choId==-1 || choId == null || choId ==  '' || choId == 0){
            triggerCss("div#CDInsurerinsurerDiscountMessageBox", true);
            $("div#CDInsurerinsurerDiscountMessageBox").html("Please choose 'CHO' from drop down list"); 
        }else if(insurerDiscountDateFrom==null || insurerDiscountDateFrom==""){
            triggerCss("div#CDInsurerinsurerDiscountMessageBox", true);
            $("div#CDInsurerinsurerDiscountMessageBox").html("Please enter 'Date From'");
        }else if(insurerDiscountDateTo==null || insurerDiscountDateTo==""){
            triggerCss("div#CDInsurerinsurerDiscountMessageBox", true);
            $("div#CDInsurerinsurerDiscountMessageBox").html("Please enter 'Date To'");
        }else if(insurerDiscountAmount==null || insurerDiscountAmount==""){
            triggerCss("div#CDInsurerinsurerDiscountMessageBox", true);
            $("div#CDInsurerinsurerDiscountMessageBox").html("Please enter 'Discount Amount'");
        }else{
            var url = "<%= request.getContextPath()%>/prv/p/addOrUpdateDiscount.action";
            var param = {"insurerId":<s:property value="insurerId" />,"choId":choId,"dateFrom":insurerDiscountDateFrom,"dateTo":insurerDiscountDateTo,"discountAmount":insurerDiscountAmount};
            ajax.loadHtml2(url, param, function(responseText, statusText){
                
                var response = eval('(' + responseText.trim() + ')');
                var outputDiv = $('div#CDInsurerinsurerDiscountMessageBox');
                if(response){
                    
                    if(response.success){
                        triggerCss("div#CDInsurerinsurerDiscountMessageBox", false);
                        outputDiv.html("New discount has been created");
                        insurerDiscount_loadGridViewList();
                    } else if(response.errors){
                        triggerCss("div#CDInsurerinsurerDiscountMessageBox", true);
                        outputDiv.html(response.errors.dateTo);
                    }
                    
                }
                
            });
        }

    }

    
    function insurerDiscount_triggerStatusRemoveRecord(gridView){
        
        Ext.MessageBox.show({
            title: '',
            msg: 'Are you sure you want to remove this insurerDiscount?',
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

                outputDiv.addClass("chox-form-submit-result");
                outputDiv.html("Record has been deleted.");
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

    <div class="status-info">
        The insurer discount that dictate where claims are routed to and therefore which users have access/visibility of the said claims is managed here.  Please note, it is not possible to remove a insurerDiscount where there is an open claim within the system that is assigned to the said insurerDiscount.
    </div>

    <div class="grid-view-header">

        <table >
            <tr><td>
                    <div class="label-block">
                        <p class="std-label">CHO<span class="mandatory">*</span> </p><div id="insurerDiscountSuppliers"></div>
                    </div>
                </td>
            </tr>
        </table>
        &nbsp;
        <table >
            <tr>
                <td width ="220">
                    <p class="std-label">Date From<span class="mandatory">*</span> </p><div id="discountDateFromId"></div>
                </td> 

                <td width ="220">
                    <p class="std-label">Date To<span class="mandatory">*</span> </p><div id="discountDateToId"></div>
                </td> 

                <td width ="220">
                    <p class="std-label">Discount<span class="mandatory">*</span> </p><div id="discountAmountId"></div>
                </td> 
                <td align="center">
                    <input type="button" onclick="javascript:return insurerDiscount_triggerStatusAddRecord();" value="Add"/>
                </td>
            </tr>  
        </table>

    </div>
    <div id="CDInsurerinsurerDiscountMessageBox" class="chox-form-submit-result"></div>
    <div id="insurerDiscount_gridviewGrid"></div>
</div>