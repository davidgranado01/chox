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
    var insurerDiscountDefaultDropdownValue={'value':'--- ALL ---','text':-1};
    var choId = -1;

    Ext.onReady(function(){
        
        
        var customerVrnField=new Ext.form.NumberField({
            id:"customerVrnId",
            name:"discountAmount",
            width:70,
            allowBlank:false,
//            fieldLabel : 'Discount Amount',
//            value:,
            renderTo:'discountAmountId'
        });
            
            
        var claimUploadDateToPicker = new Ext.form.DateField({
            name: 'dateFrom',
            renderTo: 'discountDateFromId',
            width: 100,
            allowBlank: false,
            format: 'd/m/Y',
//            fieldLabel : 'Date From',
//            value: '',
            showWeekNumber: true
        });
        
        
        var claimUploadDateToPicker = new Ext.form.DateField({
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
                        choId = this.getRawValue();
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
                {name:'dateFrom'},
                {name:'dateTo'},
                {name:'discount'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        insurerDiscount_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/listDiscountGridData.action',method:'POST'}),
            reader:insurerDiscount_gridviewJsonReader
        });

        insurerDiscount_gridviewGrid = new Ext.grid.GridPanel({
            //            listeners:  {cellclick:insurerDiscount_recordOnclick },
            store: insurerDiscount_gridviewData,
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Date From", width: 100, dataIndex: 'insurerName', sortable: true, resizable: true},
                {header: "Date To", width: 180, dataIndex: 'name', sortable: true, resizable: true},
                {header: "Discount", width: 80, dataIndex: 'site', sortable: true, resizable: true},
                {header: "Created By", width: 80, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 120, dataIndex: 'createdDate', sortable: true, resizable: true},
                {header: "Action", width: 60, dataIndex: 'Remove', sortable: true, resizable: true, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>Remove</a>"}},
            ],
            renderTo:'insurerDiscount_gridviewGrid',
            height:410,
            width: 770
        });

        insurerDiscount_loadGridViewList();

    });
    

    function insurerDiscount_loadGridViewList(){
        insurerDiscount_gridviewData.load({ params : { insurerId:<s:property value="insurerId" />,choId:choId } });
    }

    function insurerDiscount_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = insurerDiscount_gridviewGrid.getStore().getAt(rowIndex);
        if(columnIndex==4){
            insurerDiscount_triggerStatusUpdateRecord(gridView);
        }else if(columnIndex==5){
            insurerDiscount_triggerStatusRemoveRecord(gridView);
        }
    }

    function insurerDiscount_triggerStatusAddRecord(){

        var insurerDiscountName = $("#insurerDiscountName").val();
        var insurerDiscountTeam = $("#insurerDiscountTeam").val();
        var insurerDiscountSite = $("#insurerDiscountSite").val();

        if(insurerDiscountName==null || insurerDiscountName==""){
            triggerCss("div#CDInsurerinsurerDiscountMessageBox", true);
            $("div#CDInsurerinsurerDiscountMessageBox").html("Please enter 'insurerDiscount Name'");
        }else if(insurerDiscountTeam==null || insurerDiscountTeam==""){
            triggerCss("div#CDInsurerinsurerDiscountMessageBox", true);
            $("div#CDInsurerinsurerDiscountMessageBox").html("Please enter 'insurerDiscount Team'");
        }else if(insurerDiscountSite==null || insurerDiscountSite==""){
            triggerCss("div#CDInsurerinsurerDiscountMessageBox", true);
            $("div#CDInsurerinsurerDiscountMessageBox").html("Please enter 'insurerDiscount Site'");
        }else{
            var url = "<%= request.getContextPath()%>/prv/p/addNewInsurerinsurerDiscount.action";
            var param = {"insurerId":<s:property value="insurerId" />,"insurerDiscountName":insurerDiscountName,"insurerDiscountSite":insurerDiscountSite,"insurerDiscountTeam":insurerDiscountTeam};
            ajax.loadHtml2(url, param, insurerDiscount_onSubmitResponseReceived);
        }

    }

    function insurerDiscount_triggerStatusUpdateRecord(gridView){

        var insurerDiscountId = gridView.get("id");
        var url = "<%= request.getContextPath()%>/prv/p/triggerInsurerinsurerDiscountStatus.action";
        var param = {"insurerId":<s:property value="insurerId" />,"insurerDiscountId":insurerDiscountId};
        ajax.loadHtml2(url, param, insurerDiscount_onSubmitResponseReceived);

    }

    function insurerDiscount_triggerStatusRemoveRecord(gridView){

        if(confirm("Are you sure you want to remove this insurerDiscount?")){

            var insurerDiscountId = gridView.get("id");

            var url = "<%= request.getContextPath()%>/prv/p/removeInsurerinsurerDiscount.action";
            var param = {"insurerId":<s:property value="insurerId" />,"insurerDiscountId":insurerDiscountId};
            ajax.loadHtml2(url, param, insurerDiscount_onSubmitResponseReceived);
        }

    }

    function insurerDiscount_onSubmitResponseReceived(responseText, statusText)  {

        var response = eval('(' + responseText.trim() + ')');
        var outputDiv = $('div#CDInsurerinsurerDiscountMessageBox');

        triggerCss("div#CDInsurerinsurerDiscountMessageBox", true);

        if(response)
        {
            triggerCss("div#CDInsurerinsurerDiscountMessageBox", false);

            if(response.isValid){

                outputDiv.addClass("chox-form-submit-result");

                if(response.resultType && response.resultType == 'Message')
                {
                    alert(response.result);
                    insurerinsurerDiscount_doRefreshPage();
                }
                else
                {
                    //                    alert("Your Changes Have Been Saved");
                    insurerinsurerDiscount_doRefreshPage();
                }

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

    function insurerinsurerDiscount_doRefreshPage(){

        var tabIndex = 0;
        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/loadAdminPanel.action";
        var param = {"adminPanelName":"InsurerPanelMgmt","tabIndex":tabIndex};

    <s:if test="isChoxAdmin">
            tabIndex = 2;
            url = "<%= request.getContextPath()%>/prv/p/updateInsurerDetailPanel.action";
            var param = {"objectId":<s:property value="insurerId" />,"tabIndex":tabIndex};
    </s:if>

            ajax.loadHtml2(url,param,function(data){
                $(target).html(data);
    <s:if test="isChoxAdmin">
                adminTabs.activate(tabIndex); 
    </s:if><s:else >
                InsurerMainPanelTabs.activate(tabIndex);
    </s:else>
            });

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