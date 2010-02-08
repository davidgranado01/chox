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
            Chox.billing.billingmode = '${billingType}';
            console.log( 'initpage');
            console.log( Chox.billing.billingmode);
            if ( Chox.billing.billingmode =='insurer' ){
                Chox.billing.billingPageTitle = 'Insurer Billing';
                Chox.billing.billingHeader1 = 'Insurer';
            }else{
                Chox.billing.billingPageTitle = 'Cho Billing';
                Chox.billing.billingHeader1 = 'Cho';
            }
        }();
        
        new Ext.ux.JSLoader({
            url: '<%= request.getContextPath()%>/scripts/billing/billingPanel.js',
            onLoad: function(options) { startPage(); },
            onError: function(options, e) { alert('Error loading script'); },
            params:{par:'${billingType}'}
        });
        

         function startPage(){
            console.log('startpage '+ Chox.billing.billingmode);
            Chox.billing.billingmode = '${billingType}';
           
            cb.billingFormObj = new Chox.billing.BillingForm({
                id:'refbillingform',
                url:Chox.appname + '/prv/p/addBill.action'
            });
            cb.billingWindowObj = new Chox.billing.billingWindow();
            
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
                autoShow:true,
                listeners:{
                    render:function(){
                        console.log('rendered');
                    },
                    show:function(myarg){
                        console.log('show' +myarg);
                    },
                    enable:function(){
                        console.log('enable');
                    },
                    savestate:function(){
                        console.log('savestate');
                    }
                }
            });
        }
    });
    
</script>
<div id="chox-admin-holder">
    <div id="chox-admin-col-div">
        <div id="billingPanel-div">
        </div>
    </div>
</div>