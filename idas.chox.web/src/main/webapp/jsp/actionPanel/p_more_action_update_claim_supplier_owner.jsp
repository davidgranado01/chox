<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    function overideCombo() {
        // The 'setValue' function on the combo box doesn't work
        // as, fue to the asynchronous nature of the widget, the store may
        // not be loaded. Below is a patch to fix this problem.
        Ext.override(Ext.form.ComboBox, {
          setValue : function(v){
            //begin patch
            // Store not loaded yet? Set value when it *is* loaded.
            // Defer the setValue call until after the next load.
            if (this.store.getCount() == 0) {
                this.store.on('load',
                this.setValue.createDelegate(this, [v]), null, {single: true});
                return;
            }
            //end patch
            var text = v;
            if(this.valueField){
                var r = this.findRecord(this.valueField, v);
                if(r){
                    text = r.data[this.displayField];
                }else if(this.valueNotFoundText !== undefined){
                    text = this.valueNotFoundText;
                }
            }
            this.lastSelectionText = text;
            if(this.hiddenField){
                this.hiddenField.value = v;
            }
            Ext.form.ComboBox.superclass.setValue.call(this, text);
            this.value = v;
        }});
    }

    Ext.onReady(function(){
        overideCombo();
        var supplierId = '<s:property value="chorganisation.id"/>';
        var currentOwnerId = '<s:property value="supplierClaimOwner.id"/>';

        var supplierClaimOwnerReader = new Ext.data.JsonReader({
                            totalProperty: 'totalCount',
                            root: 'results',
                            fields:
                            [
                                {name:'id'},
                                {name:'name'}
                            ]
        });

        var supplierClaimOwnerStore = new Ext.data.Store({
                            proxy : new Ext.data.HttpProxy
                            ({url : "<%= request.getContextPath()%>/prv/p/SearchSupplierClaimOwnerDropDownAction.action", method:'GET', params : {"supplierId":supplierId}}),
                            reader : supplierClaimOwnerReader
        });

        var supplierClaimOwnerCombo = new Ext.form.ComboBox({
                            store : supplierClaimOwnerStore,
                            listWidth: 200,
                            width: 200,
                            renderTo: 'supplierClaimOwnerDropDownDiv',
                            valueField : 'id',
                            id : 'supplierClaimOwnerComboId',
                            hiddenName: 'supplierClaimOwnerId',
                            displayField :'name',
                            triggerAction: 'all',
                            selectOnFocus: true,
                            typeAhead : true,
                            forceSelection: true,
                            mode : 'local',
                            emptyText : '--- Please Select ---',
                            listeners: { 
                                blur: function () {
                                   if(this.getRawValue() == "" ) {
                                       this.clearValue(); this.reset();
                                       }
                                   }
                            }
        });
        $.validator.addMethod("claimOwnerSelection",
                            function(value) {
                                if(value === "") {
                                    return false;
                                }
                                return true;
                            }
        );
        $("form#formSupplierOwnershipAction").validate(
        {
            errorLabelContainer: "#supplierOwnershipAssignmentMessageBox",
            rules: {
                supplierClaimOwnerId: {claimOwnerSelection: document.getElementById('supplierClaimOwnerComboId')}
            },
            messages: {
                supplierClaimOwnerId: {claimOwnerSelection:"You must supply a value for 'Claim Owner'"}
            }
        });
        
        supplierClaimOwnerStore.load({
            params: {
                "supplierId":supplierId
            },
            callback: function (records, operation, success) {
                if (currentOwnerId) {
                        if(typeof Ext.StoreMgr.lookup(supplierClaimOwnerStore).getById(currentOwnerId) === 'object'){
                            supplierClaimOwnerCombo.setValue(currentOwnerId);
                        } else {
                            supplierClaimOwnerCombo.reset();
                        }
                    }
            }
        });
    });
    
    $("#formSupplierOwnershipAction").submit(function() {
        if ($("#supplierClaimOwnerComboId").val() != "--- Please Select ---") {
          $("#supplierOwnershipAssignmentMessageBox").text("").show();
          return true;
        }
        $("#supplierOwnershipAssignmentMessageBox").text("You must supply a value for 'Claim Owner'").show();
        return false;
     });
    

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/updateClaimSupplier.action" method="post" id="formSupplierOwnershipAction" name="formSupplierOwnershipAction">
        <fieldset class="x-fieldset">
            <legend>Update Claim Owner - Action Required</legend>
            <div>
                <s:hidden id="claimId" name="id" />
                <div>
                    <div class="status-info">
                        Update the Claim Owner by using the drop down menus provided below.
                    </div>
                    <div class="status-control-set">
                        <table class="status-table" width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td align="right" width="10%"><label>Claim Owner : </label></td>
                                <td width="20%"><div id="supplierClaimOwnerDropDownDiv"></div></td>
                                <td width="70%"></td>
                            </tr>
                            <tr>
                                <td></td>
                                <td colspan="2" class="choice" nowrap>
                                    <input type="submit" id="MAUCSOAssignOwnerButtonId"value="Assign Owner"/>
                                </td>
                            </tr>
                        </table>
                            <div class="action-error-msg" id="supplierOwnershipAssignmentMessageBox"></div>
                    </div>
                </div>
            </div>
        </fieldset>
       <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>