/* 
 * FORM RELATED
 */

function DatePicker(name){

   return new Ext.form.DateField({
        name: name,
        width: 120,
        allowBlank: false,
        format: 'd/m/Y',
        value: getTodayDate(),
        showWeekNumber: true
    });
    
}