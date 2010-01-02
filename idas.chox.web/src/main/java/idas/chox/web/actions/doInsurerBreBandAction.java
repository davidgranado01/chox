package idas.chox.web.actions;

public class doInsurerBreBandAction{

/*

    public String deleteBreBand() throws Exception {

        try {




//             <<BRE NAME>> 
            if (service.isBreBandOccupied(model)) {

                // actionResult = "F: You cannot delete '" + model.getName() + "' because it is currently being used by one or more Credit Hire Organisations. Please remove the Credit Hire Organisations from this BRE and try again";
                return SUCCESS;
            } else {
                this.service.deleteObject(model);
                //actionResult = "D:'" + model.getName() + "' has deleted";

            }



        } catch (Exception ex) {
            throw ex;
        }

        return SUCCESS;
    }

    public String updateModel() throws Exception {

        try {

            if (this.isNew) {
                model.setInsurer(insurerService.getInsurer(insurerId));
            }

            if (service.isBreBandNameExist(model)) {
                //actionResult = "Selected Band Name already exists";
                return SUCCESS;
            }

            //actionResult = "";

            this.service.updateObject(model);

            if (this.isNew) {
                //actionResult = "objectId:" + model.getId();
                this.isNew = false;
            }

        } catch (Exception ex) {
            throw ex;
        }

        return SUCCESS;
    }

    public void prepare() throws Exception {
        if (Integer.valueOf(objectId) <= 0) {
            model = new BreBand();
            this.isNew = true;
        } else {
            model = service.getObject(Integer.valueOf(objectId));
            this.isNew = false;
        }
    }
 * */
}
