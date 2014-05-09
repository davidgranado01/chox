<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<script>

    Ext.onReady(function(){
        Ext.util.Format.gbMoney = function(v){
            v = (Math.round((v-0)*100))/100;
            v = (v == Math.floor(v)) ? v + ".00" : ((v*10 == Math.floor(v*10)) ? v + "0" : v);
            v = String(v);
            var ps = v.split('.');
            var whole = ps[0];
            var sub = ps[1] ? '.'+ ps[1] : '.00';
            var r = /(\d+)(\d{3})/;
            while (r.test(whole)) {
                whole = whole.replace(r, '$1' + ',' + '$2');
            }
            v = whole + sub;
            if(v.charAt(0) == '-'){
                return '-£' + v.substr(1);
            }
            return '£' +  v;
        }
    	
        var initpage = function(){
            Ext.namespace('Chox','Chox.billing');
            Chox.appname = '<%= request.getContextPath()%>';
//            Chox.nonce = '<%= session.getAttribute("SessionNonce")%>';
            Chox.billing.billingmode = '${billingType}';
            if ( Chox.billing.billingmode =='insurer' ){
                Chox.billing.billingPageTitle = 'Insurer Billing';
                Chox.billing.billingHeader1 = 'Insurer';
            }else{
                Chox.billing.billingPageTitle = 'CHO Billing';
                Chox.billing.billingHeader1 = 'CHO';
            }
        }();
        
        new Ext.ux.JSLoader({
            url: '/scripts/billing/billingPanel.js',
            onLoad: function(options) { startPage(); },
            onError: function(options, e) { 
                Ext.MessageBox.show({
                    title: '',
                    msg: 'Error loading script',
                    width:300,
                    buttons: Ext.MessageBox.OK,
                    icon : Ext.MessageBox.ERROR
                });
            },
            params:{par:'${billingType}'}
        });
        
    });
         function startPage(){
            Chox.billing.billingmode = '${billingType}';
           
            Chox.billing.billingFormObj = new Chox.billing.BillingForm({
                id:'refbillingform',
                url:Chox.appname + '/prv/p/addBill.action'
            });
            Chox.billing.billingWindowObj = new Chox.billing.billingWindow();
            
            var myPanel = new Ext.Panel({                
                applyTo: 'billingPanel-div',
                //title: Chox.billing.billingPageTitle,
                border: true,
                items:[
                    new Chox.billing.BillingGrid({
                        btype:'<s:property value="billingType"/>'                        
                    }),
                    new Chox.billing.BillingDetailGrid()
                ],
                autoWidth:true,
                autoHeight: true,
                autoShow:true
            });
        }

    
</script>
<div id="chox-admin-holder">
    <div id="chox-admin-col-div" style ="width:780" >
        <div id="billingPanel-div">
        </div>
    </div>
</div>