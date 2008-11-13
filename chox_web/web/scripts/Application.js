Ext.onReady(
borderLayout
);


function borderLayout(){
    var center = new Ext.Panel({
        items: [{

                contentEl: 'content',
                bodyStyle : 'padding: 4px;',
                autoHeight : true,
                autoScroll: true,
                border: false
            }]
    });

     Ext.QuickTips.init();
  
      var viewport = new Ext.Viewport({
         autoHeight: true,
        layout:'border',
        items: [{
	        region: 'north',
	        height: 100,
	        minSize: 240,
	        maxSize: 240,
	        margins: '0 0 0 0',
	        border: false,
	        items: [{
                        contentEl: 'header',
                        bodyStyle : 'background-color: #000000;',
                        autoScroll: true,
                        border: false
                    }]
	    },{
	        region:'west',
                title:'Navigation',
	        margins: '0 0 0 5',
	        width: 200,
	        minSize: 240,
	        maxSize: 300,
	        items: [{
                        contentEl: 'navigation',
                        autoScroll: true,
                        border: false
                    }]
	    },{
	        region: 'center',
                layout:'fit',                
                title:'&#160;',
	        margins: '0 0 0 5',
                padding : '0,0,0,0',
	        items: [center]
	    }]
    });
	
}