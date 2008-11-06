package chox.message;

import chox.data.CHOXUsageFee;
import chox.data.Claim;
import chox.data.Driver;
import chox.data.EngineerCost;
import chox.data.EngineerReport;
import chox.data.Extra;
import chox.data.HandlingFee;
import chox.data.Injured;
import chox.data.InsurerCountry;
import chox.data.Invoice;
import chox.data.Message;
import chox.data.Rental;
import chox.data.RentalAuthorisation;
import chox.data.RentalCost;
import chox.data.RentalExtra;
import chox.data.RentalMessage;
import chox.data.RentalNote;
import chox.data.RentalVehicle;
import chox.data.RentalVehicleExtra;
import chox.data.RepairCost;
import chox.data.StorageRecoveryCost;
import chox.data.Supplier;
import chox.data.VehicleClass;
import chox.data.Witness;
import chox.util.FeeGenerator;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import com.filesystemsoftware.utils.Logger;
import com.filesystemsoftware.utils.XMLUtils;
import idas.alert.Alert;
import idas.configuration.DatabaseConnectionFactory;
import idas.web.security.*;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SortMessages {

    private String configFile;
    public static final int MAX_RUNTIME_SECONDS = 240;
    public static final int MAX_PER_BATCH = 100000;
    public static final long startTime = System.currentTimeMillis();

    public static void main(String[] args) {

        args = new String[]{"/Users/stu/Projects/CHOX/config.xml"};

        if (args.length != 1) {
            System.err.println("Usage: chox.message.SortMessages <config.xml>");
            System.exit(1);
        }

        try {
            SortMessages a = new SortMessages(args[0]);
            a.process();

        } catch (Exception e) {
            Alert.message("idas.message.SortMessages Exception: " + e.getMessage());
            e.printStackTrace(Logger.err);
        }
    }

    public SortMessages() {
    }

    private SortMessages(String configFile) {
        this.configFile = configFile;
        Alert.init(configFile);
    }

    private void process() throws Exception {
        DBConnectionWrapper connection = DatabaseConnectionFactory.newConnection(configFile);

        ArrayList<Long> messages = new ArrayList<Long>();
        PreparedStatement s = connection.prepareStatement("select id from message where status_id in (3,4) order by status_id,received,id limit " + MAX_PER_BATCH);
        ResultSet rs = s.executeQuery();
        while (rs.next()) {
            messages.add(new Long(rs.getLong(1)));
        }
        rs.close();
        s.close();

        for (Long mID : messages) {
            Message m = Message.instantiate(connection, mID.longValue());
            if (System.currentTimeMillis() > startTime + (1000 * MAX_RUNTIME_SECONDS)) {
                break;
            }
            boolean success = false;
            try {

                success = process(connection, m);

                if (!success) {
                    if (m.getStatusID() != 4) {
                        m.setStatusID(4);
                        m.save(connection);
                        success = true;
                    }
                }

            } catch (Exception e) {
                Logger.err.println(e.getMessage());
                e.printStackTrace(Logger.err);
                //Alert.message("CHOX Error handling messageID=" + m.getID());
                //Alert.message("chox.message.SortMessages Exception: " + e.getMessage());
                connection.rollback();
                m.setStatusID(2);
                m.save(connection);
                connection.commit();
            }
            if (success) {
                connection.commit();
            } else {
                connection.rollback();
            }
        }

        connection.commit();
        connection.close();
    }

    public boolean process(DBConnectionWrapper connection, Message m) throws Exception {
        Document doc = XMLUtils.toDocument(m.getMessage());

        Element root = doc.getDocumentElement();
        if (root != null && root.getTagName().equals("chox")) {
            ArrayList<Element> rentalElements = XMLUtils.getElements(doc, root, "rental");
            int count = 0;
            for (Element re : rentalElements) {
                try {
                    count++;
                    Rental r=handleRental(connection, doc, re, m);
                    if(r!=null) {
                        r.updateInsurerLocationProduct(connection);
                        r.updateSearchData(connection);
                    }
                } catch (Exception e) {
                    Logger.err.println("Error loading record " + count);
                    throw e;
                }
            }
        }

        return true;
    }

    private Rental handleRental(DBConnectionWrapper connection, Document doc, Element root, Message message) throws Exception {
        Element supplierElement = XMLUtils.getElement(root, "supplier");
        if (supplierElement == null) {
            throw new Exception("Cannot find <supplier> element");
        }
        String supplierName = XMLUtils.getElementValue(supplierElement, "supplier-name");
        if (supplierName == null || supplierName.trim().length() == 0) {
            throw new Exception("Cannot find or invalid value for <supplier-name>");
        }
        Supplier supplier = Supplier.getByName(connection, supplierName);
        if (supplier == null) {
            throw new Exception("Cannot find supplier [" + supplierName + "]");
        }
        if (message.getSessionID() != -1 && !iDASSecurityManager.hasSupplierPermission(connection, message.getSessionID(), supplier.getID())) {
            throw new Exception("No permission to process this message!");
        }
        String supplierReference = XMLUtils.getElementValue(supplierElement, "supplier-reference");
        if (supplierReference == null || supplierReference.trim().length() == 0) {
            throw new Exception("Cannot find or invalid value for <supplier-reference>");
        }
        Rental rental = Rental.getByReference(connection, supplier, supplierReference);

        boolean isNew = rental == null;
        String newRentalStatus = XMLUtils.getElementValue(root, "rental-status");

        RentalAuthorisation ra = null;

        if (isNew) {
            if (!newRentalStatus.equals(Rental.IN_PROGRESS)) {
                throw new Exception("New rentals must always have a status of " + Rental.IN_PROGRESS + ", supplierRef=" + supplierReference);
            }

            rental = new Rental();
            rental.setSupplierID(supplier.getID());
            rental.setSupplierReference(supplierReference);
            rental.setFirstContact(new Timestamp(System.currentTimeMillis()));
            rental.setRentalStatus(Rental.PENDING);
            rental.save(connection);

            ra = new RentalAuthorisation();
            ra.setRentalID(rental.getID());
            ra.setSessionID(message.getSessionID());
            ra.setStatus(RentalAuthorisation.RESET);
            ra.save(connection);
        } else { // not new

            ra = RentalAuthorisation.getLatestAuthorisation(connection, rental);

            if (ra.getStatus().equals(RentalAuthorisation.INVOICE_AUTHORISED)) {
                return rental; // just ignore new stuff after it comes through after payment
            }

            if ((ra.getStatus().equals(RentalAuthorisation.INVOICED) || ra.getStatus().equals(RentalAuthorisation.INVOICE_DISPUTED)) && newRentalStatus.equals(Rental.IN_PROGRESS)) {
                return rental;  // ignore new stuff if the invoice has already been issued... but allow Complete changes
            }

            if (newRentalStatus.equals(Rental.CANCELLED)) {
                rental.setRentalStatus(Rental.CANCELLED);
                rental.save(connection);

                ra = new RentalAuthorisation();
                ra.setRentalID(rental.getID());
                ra.setSessionID(message.getSessionID());
                ra.setStatus(RentalAuthorisation.CANCELLED);
                ra.save(connection);

                return rental;
            }

            if (ra.getStatus().equals(RentalAuthorisation.AUTHORISED) || ra.getStatus().equals(RentalAuthorisation.SELF_AUTHORISED) || ra.getStatus().equals(RentalAuthorisation.IN_PROGRESS)) {
                if (newRentalStatus.equals(Rental.IN_PROGRESS)) {
                    ra = new RentalAuthorisation();
                    ra.setRentalID(rental.getID());
                    ra.setSessionID(message.getSessionID());
                    ra.setStatus(RentalAuthorisation.RESET);
                    ra.save(connection);

                    RentalNote rn = new RentalNote();
                    rn.setRentalID(rental.getID());
                    rn.setSessionID(message.getSessionID());
                    rn.setNote("WARNING: Rental reset after being changed after authorisation had happened.");
                    rn.save(connection);
                }
            }

            if (ra.getStatus().equals(RentalAuthorisation.CANCELLED)) {
                ra = new RentalAuthorisation();
                ra.setRentalID(rental.getID());
                ra.setSessionID(message.getSessionID());
                ra.setStatus(RentalAuthorisation.RESET);
                ra.save(connection);

                RentalNote rn = new RentalNote();
                rn.setRentalID(rental.getID());
                rn.setSessionID(message.getSessionID());
                rn.setNote("WARNING: Rental reset after being changed after being previously cancelled.");
                rn.save(connection);
            }

            if (ra.getStatus().equals(RentalAuthorisation.AWAITING_AUTHORISATION)) {
                ra = new RentalAuthorisation();
                ra.setRentalID(rental.getID());
                ra.setSessionID(message.getSessionID());
                ra.setStatus(RentalAuthorisation.RESET);
                ra.save(connection);
            }
        }

        boolean ok = handleRentalDetails(connection, doc, root, message, rental);

        ra = RentalAuthorisation.getLatestAuthorisation(connection, rental);

        if (ra == null) {
            ra = new RentalAuthorisation();
            ra.setRentalID(rental.getID());
            ra.setSessionID(message.getSessionID());
            ra.setStatus(RentalAuthorisation.RESET);
            ra.save(connection);
        }


        RentalMessage rm = new RentalMessage();
        rm.setMessageID(message.getID());
        rm.setRentalID(rental.getID());
        rm.save(connection);

        iDASSession session = iDASSession.instantiate(connection, message.getSessionID());
        FeeGenerator.chargeFee(connection, session, rental, CHOXUsageFee.UPLOAD_RENTAL);

        return rental;
    }

    private boolean handleRentalDetails(DBConnectionWrapper connection, Document doc, Element root, Message message, Rental rental) throws Exception {
        String firstContactString = XMLUtils.getElementValue(root, "first-contact");
        if (firstContactString == null || firstContactString.trim().length() == 0) {
            throw new Exception("Cannot find or invalid value for <first-contact>");
        }
        Timestamp firstContact = parseDate(firstContactString);
        rental.setFirstContact(firstContact);
        rental.setRentalStatus(XMLUtils.getElementValue(root, "rental-status"));
        rental.save(connection);

        handleDrivers(connection, doc, root, message, rental);
        handleRentalVehicles(connection, doc, root, message, rental);
        handleClaim(connection, doc, root, message, rental);
        handleRepair(connection, doc, root, message, rental);
        handleInvoice(connection, doc, root, message, rental);
        handleNotes(connection, doc, root, message, rental);

        return true;
    }

    private void handleNotes(DBConnectionWrapper connection, Document doc, Element root, Message message, Rental rental) throws Exception {
        Element notesElement = XMLUtils.getElement(root, "notes");
        if (notesElement == null) {
            return;
        }
        ArrayList<Element> noteElements = XMLUtils.getElements(doc, notesElement, "note");
        for (Element ne : noteElements) {
            String noteText = XMLUtils.getElementText(ne);
            if (noteText != null && noteText.trim().length() > 0) {
                RentalNote rn = new RentalNote();
                rn.setRentalID(rental.getID());
                rn.setNote(noteText);
                rn.setSessionID(message.getSessionID());
                rn.save(connection);
            }
        }
    }

    private void handleInvoice(DBConnectionWrapper connection, Document doc, Element root, Message message, Rental rental) throws Exception {
        Element invoiceElement = XMLUtils.getElement(root, "invoice");
        if (invoiceElement == null) {
            return;
        }
        Invoice.removeForRental(connection, rental);

        String netString = XMLUtils.getElementValue(invoiceElement, "net");
        if (netString != null && netString.trim().length() == 0) {
            netString = null;
        }
        String vatString = XMLUtils.getElementValue(invoiceElement, "vat");
        if (vatString != null && vatString.trim().length() == 0) {
            vatString = null;
        }
        String grossString = XMLUtils.getElementValue(invoiceElement, "gross");
        if (grossString != null && grossString.trim().length() == 0) {
            grossString = null;
        }

        if (netString != null && vatString != null && grossString != null) {

            BigDecimal net = new BigDecimal(XMLUtils.getElementValue(invoiceElement, "net"));
            BigDecimal vat = new BigDecimal(XMLUtils.getElementValue(invoiceElement, "vat"));
            BigDecimal gross = new BigDecimal(XMLUtils.getElementValue(invoiceElement, "gross"));

            Invoice invoice = new Invoice();
            invoice.setRentalID(rental.getID());
            invoice.setGross(gross);
            invoice.setNet(net);
            invoice.setVAT(vat);
            invoice.save(connection);
        }

        handleInvoiceVehicles(connection, doc, invoiceElement, message, rental);
        handleInvoiceExtras(connection, doc, invoiceElement, message, rental);
        handleInvoiceRepair(connection, doc, invoiceElement, message, rental);
        handleInvoiceStorageRecovery(connection, doc, invoiceElement, message, rental);
        handleInvoiceHandlingFee(connection, doc, invoiceElement, message, rental);
    }

    private void handleInvoiceVehicles(DBConnectionWrapper connection, Document doc, Element root, Message message, Rental rental) throws Exception {
        Element vehiclesElement = XMLUtils.getElement(root, "vehicles");
        if (vehiclesElement == null) {
            return;
        }
        RentalCost.removeForRental(connection, rental);

        String netString = XMLUtils.getElementValue(vehiclesElement, "net");
        if (netString != null && netString.trim().length() == 0) {
            netString = null;
        }
        String vatString = XMLUtils.getElementValue(vehiclesElement, "vat");
        if (vatString != null && vatString.trim().length() == 0) {
            vatString = null;
        }
        String grossString = XMLUtils.getElementValue(vehiclesElement, "gross");
        if (grossString != null && grossString.trim().length() == 0) {
            grossString = null;
        }

        if (netString != null && vatString != null && grossString != null) {

            BigDecimal net = new BigDecimal(XMLUtils.getElementValue(vehiclesElement, "net"));
            BigDecimal vat = new BigDecimal(XMLUtils.getElementValue(vehiclesElement, "vat"));
            BigDecimal gross = new BigDecimal(XMLUtils.getElementValue(vehiclesElement, "gross"));

            RentalCost rc = new RentalCost();
            rc.setRentalID(rental.getID());
            rc.setGrossRental(gross);
            rc.setNetRental(net);
            rc.setRentalVAT(net);
            rc.save(connection);
        }
    }

    private void handleInvoiceExtras(DBConnectionWrapper connection, Document doc, Element root, Message message, Rental rental) throws Exception {
        Element extrasElement = XMLUtils.getElement(root, "extras");
        if (extrasElement == null) {
            return;
        }

        RentalExtra.removeForRental(connection, rental);
        ArrayList<Element> extraElements = XMLUtils.getElements(doc, extrasElement, "extra");
        for (Element ee : extraElements) {
            String extraName = XMLUtils.getElementValue(ee, "name");
            BigDecimal cost = new BigDecimal(XMLUtils.getElementValue(ee, "item-cost"));
            BigDecimal quantity = new BigDecimal(XMLUtils.getElementValue(ee, "quantity"));

            Extra extra = Extra.getByName(connection, extraName);
            if (extra == null) {
                throw new Exception("Unrecognised extra name [" + extraName + "]");
            }
            RentalExtra re = new RentalExtra();
            re.setRentalID(rental.getID());
            re.setExtraID(extra.getID());
            re.setQuantity(quantity);
            re.setItemAmount(cost);
            re.save(connection);
        }
    }

    private void handleInvoiceRepair(DBConnectionWrapper connection, Document doc, Element root, Message message, Rental rental) throws Exception {
        Element repairElement = XMLUtils.getElement(root, "repair");
        if (repairElement == null) {
            return;
        }

        Claim claim = Claim.getForRental(connection, rental);
        if (claim == null) {
            throw new Exception("Cannot find claim for rentalID [" + rental.getID() + "]");
        }
        RepairCost.removeForClaim(connection, claim);


        String netString = XMLUtils.getElementValue(repairElement, "net");
        if (netString != null && netString.trim().length() == 0) {
            netString = null;
        }
        String vatString = XMLUtils.getElementValue(repairElement, "vat");
        if (vatString != null && vatString.trim().length() == 0) {
            vatString = null;
        }
        String grossString = XMLUtils.getElementValue(repairElement, "gross");
        if (grossString != null && grossString.trim().length() == 0) {
            grossString = null;
        }

        if (netString != null && vatString != null && grossString != null) {

            BigDecimal net = new BigDecimal(XMLUtils.getElementValue(repairElement, "net"));
            BigDecimal vat = new BigDecimal(XMLUtils.getElementValue(repairElement, "vat"));
            BigDecimal gross = new BigDecimal(XMLUtils.getElementValue(repairElement, "gross"));

            RepairCost rc = new RepairCost();
            rc.setClaimID(claim.getID());
            rc.setGrossRepair(gross);
            rc.setNetRepair(net);
            rc.setRepairVAT(gross);
            rc.save(connection);
        }
    }

    private void handleInvoiceStorageRecovery(DBConnectionWrapper connection, Document doc, Element root, Message message, Rental rental) throws Exception {
        Element storageRecoveryElement = XMLUtils.getElement(root, "storage-recovery");
        if (storageRecoveryElement == null) {
            return;
        }

        Claim claim = Claim.getForRental(connection, rental);
        if (claim == null) {
            throw new Exception("Cannot find claim for rentalID [" + rental.getID() + "]");
        }

        StorageRecoveryCost.removeForClaim(connection, claim);

        String netString = XMLUtils.getElementValue(storageRecoveryElement, "net");
        if (netString != null && netString.trim().length() == 0) {
            netString = null;
        }
        String vatString = XMLUtils.getElementValue(storageRecoveryElement, "vat");
        if (vatString != null && vatString.trim().length() == 0) {
            vatString = null;
        }
        String grossString = XMLUtils.getElementValue(storageRecoveryElement, "gross");
        if (grossString != null && grossString.trim().length() == 0) {
            grossString = null;
        }

        if (netString != null && vatString != null && grossString != null) {


            BigDecimal net = new BigDecimal(XMLUtils.getElementValue(storageRecoveryElement, "net"));
            BigDecimal vat = new BigDecimal(XMLUtils.getElementValue(storageRecoveryElement, "vat"));
            BigDecimal gross = new BigDecimal(XMLUtils.getElementValue(storageRecoveryElement, "gross"));

            StorageRecoveryCost sc = new StorageRecoveryCost();
            sc.setClaimID(claim.getID());
            sc.setGrossStorageRecovery(gross);
            sc.setNetStorageRecovery(net);
            sc.setStorageRecoveryVAT(gross);
            sc.save(connection);

        }
    }

    private void handleInvoiceEngineerFee(DBConnectionWrapper connection, Document doc, Element root, Message message, Rental rental) throws Exception {
        Element engineerFeeElement = XMLUtils.getElement(root, "engineer-fee");
        if (engineerFeeElement == null) {
            return;
        }

        Claim claim = Claim.getForRental(connection, rental);
        if (claim == null) {
            throw new Exception("Cannot find claim for rentalID [" + rental.getID() + "]");
        }

        EngineerCost.removeForClaim(connection, claim);

        String netString = XMLUtils.getElementValue(engineerFeeElement, "net");
        if (netString != null && netString.trim().length() == 0) {
            netString = null;
        }
        String vatString = XMLUtils.getElementValue(engineerFeeElement, "vat");
        if (vatString != null && vatString.trim().length() == 0) {
            vatString = null;
        }
        String grossString = XMLUtils.getElementValue(engineerFeeElement, "gross");
        if (grossString != null && grossString.trim().length() == 0) {
            grossString = null;
        }

        if (netString != null && vatString != null && grossString != null) {

            BigDecimal net = new BigDecimal(XMLUtils.getElementValue(engineerFeeElement, "net"));
            BigDecimal vat = new BigDecimal(XMLUtils.getElementValue(engineerFeeElement, "vat"));
            BigDecimal gross = new BigDecimal(XMLUtils.getElementValue(engineerFeeElement, "gross"));

            EngineerCost hf = new EngineerCost();
            hf.setClaimID(claim.getID());
            hf.setFeeVAT(vat);
            hf.setGrossFee(gross);
            hf.setNetFee(net);
            hf.save(connection);
        }
    }

    private void handleInvoiceHandlingFee(DBConnectionWrapper connection, Document doc, Element root, Message message, Rental rental) throws Exception {
        Element handlingFeeElement = XMLUtils.getElement(root, "claim-handling-fee");
        if (handlingFeeElement == null) {
            return;
        }

        HandlingFee.removeForRental(connection, rental);

        String netString = XMLUtils.getElementValue(handlingFeeElement, "net");
        if (netString != null && netString.trim().length() == 0) {
            netString = null;
        }
        String vatString = XMLUtils.getElementValue(handlingFeeElement, "vat");
        if (vatString != null && vatString.trim().length() == 0) {
            vatString = null;
        }
        String grossString = XMLUtils.getElementValue(handlingFeeElement, "gross");
        if (grossString != null && grossString.trim().length() == 0) {
            grossString = null;
        }

        if (netString != null && vatString != null && grossString != null) {

            BigDecimal net = new BigDecimal(XMLUtils.getElementValue(handlingFeeElement, "net"));
            BigDecimal vat = new BigDecimal(XMLUtils.getElementValue(handlingFeeElement, "vat"));
            BigDecimal gross = new BigDecimal(XMLUtils.getElementValue(handlingFeeElement, "gross"));

            HandlingFee hf = new HandlingFee();
            hf.setRentalID(rental.getID());
            hf.setFeeVAT(vat);
            hf.setGrossFee(gross);
            hf.setNetFee(net);
            hf.save(connection);
        }
    }

    private void handleRentalVehicles(DBConnectionWrapper connection, Document doc, Element root, Message message, Rental rental) throws Exception {
        Element rentalVehiclesElement = XMLUtils.getElement(root, "rental-vehicles");
        if (rentalVehiclesElement == null) {
            return;
        }
        ArrayList<Element> rentalVehicleElements = XMLUtils.getElements(doc, rentalVehiclesElement, "rental-vehicle");
        RentalVehicle.removeForRental(connection, rental);
        for (Element rve : rentalVehicleElements) {
            String reg = XMLUtils.getElementValue(rve, "vehicle-registration");
            if (reg != null && reg.trim().length() == 0) {
                reg = null;
            }
            String manu = XMLUtils.getElementValue(rve, "vehicle-manufacturer");
            if (manu != null && manu.trim().length() == 0) {
                manu = null;
            }
            String model = XMLUtils.getElementValue(rve, "vehicle-model");
            if (model != null && model.trim().length() == 0) {
                model = null;
            }
            String cls = XMLUtils.getElementValue(rve, "vehicle-class");
            if (cls != null && cls.trim().length() == 0) {
                cls = null;
            }
            String rentalStart = XMLUtils.getElementValue(rve, "rental-start");
            if (rentalStart != null && rentalStart.trim().length() == 0) {
                rentalStart = null;
            }
            String rentalEnd = XMLUtils.getElementValue(rve, "rental-end");
            String rentalDays = XMLUtils.getElementValue(rve, "rental-days");

            if (reg == null || manu == null || model == null || cls == null || rentalStart == null) {
                continue;
            }

            VehicleClass vc = VehicleClass.getByName(connection, cls);
            if (vc == null) {
                throw new Exception("Invalid vehicle class [" + cls + "] specified, supplierRef=" + rental.getSupplierReference());
            }
            RentalVehicle rv = new RentalVehicle();
            rv.setRentalID(rental.getID());
            rv.setVehicleRegistration(reg);
            rv.setVehicleManufacturer(manu);
            rv.setVehicleModel(model);
            rv.setVehicleClassID(vc.getID());
            rv.setRentalStart(parseDate(rentalStart));
            Timestamp re = null;
            try {
                re = parseDate(rentalEnd);
                rv.setRentalEnd(re);
            } catch (Exception e) {
                rv.setRentalEnd(null);
            }
            BigDecimal d = null;
            try {
                d = new BigDecimal(rentalDays);
                rv.setDays(d);
            } catch (Exception e) {
                rv.setDays(null);
            }
            rv.save(connection);

            Element extrasElement = XMLUtils.getElement(rve, "extras");
            if (extrasElement != null) {
                ArrayList<Element> extraElements = XMLUtils.getElements(doc, extrasElement, "extra");
                for (Element ee : extraElements) {
                    String c = XMLUtils.getElementText(ee);
                    Extra eo = Extra.getByName(connection, c);
                    if (eo == null) {
                        throw new Exception("Cannot find extra [" + c + "]");
                    }
                    RentalVehicleExtra e = new RentalVehicleExtra();
                    e.setExtraID(eo.getID());
                    e.setRentalVehicleID(rv.getID());
                    e.save(connection);
                }
            }
        }
    }

    private void handleDrivers(DBConnectionWrapper connection, Document doc, Element root, Message message, Rental rental) throws Exception {
        Element driversElement = XMLUtils.getElement(root, "drivers");
        if (driversElement == null) {
            return;
        }
        ArrayList<Element> driverElements = XMLUtils.getElements(doc, driversElement, "driver");

        Driver.removeForRental(connection, rental);
        for (Element de : driverElements) {

            String title = XMLUtils.getElementValue(de, "title");
            if (title != null && title.trim().length() == 0) {
                title = null;
            }
            String firstnames = XMLUtils.getElementValue(de, "firstnames");
            if (firstnames != null && firstnames.trim().length() == 0) {
                firstnames = null;
            }
            String lastname = XMLUtils.getElementValue(de, "lastname");
            if (lastname != null && lastname.trim().length() == 0) {
                lastname = null;
            }
            if (title == null || firstnames == null || lastname == null) {
                continue;
            }
            Driver d = new Driver();
            d.setRentalID(rental.getID());
            d.setTitle(title);
            d.setFirstnames(firstnames);
            d.setLastname(lastname);
            d.setAddress1(XMLUtils.getElementValue(root, "address1"));
            d.setAddress2(XMLUtils.getElementValue(root, "address2"));
            d.setAddress3(XMLUtils.getElementValue(root, "address3"));
            d.setAddress4(XMLUtils.getElementValue(root, "address4"));
            d.setAddress5(XMLUtils.getElementValue(root, "address5"));
            d.setPostcode(XMLUtils.getElementValue(root, "postcode"));
            d.setTelephoneDay(XMLUtils.getElementValue(root, "telephone-day"));
            d.setTelephoneEvening(XMLUtils.getElementValue(root, "telephone-evening"));
            d.setEmail(XMLUtils.getElementValue(root, "email"));

            String isPrimary = XMLUtils.getElementValue(de, "primary-driver");
            if (isPrimary == null || isPrimary.trim().length() == 0) {
                isPrimary = "n";
            }
            isPrimary = new String(isPrimary.toLowerCase().substring(0, 1));
            d.setPrimaryDriver(isPrimary);

            d.save(connection);
        }
    }

    private void handleClaim(DBConnectionWrapper connection, Document doc, Element root, Message message, Rental rental) throws Exception {
        Element claimElement = XMLUtils.getElement(root, "claim");
        if (claimElement == null) {
            return;
        }
        Claim claim = Claim.getForRental(connection, rental);
        if (claim == null) {
            claim = new Claim();
            claim.setRentalID(rental.getID());
            claim.setClaimStatus("Awaiting Authorization");
        }

        Element replacementVehicleElement = XMLUtils.getElement(claimElement, "replacement-vehicle");
        if (replacementVehicleElement != null) {
            String vc = XMLUtils.getElementValue(replacementVehicleElement, "vehicle-class");
            VehicleClass c = VehicleClass.getByName(connection, vc);
            if (c != null) {
                claim.setProposedRentalClassID(c.getID());
            } else {
                throw new Exception("Cannot find specified proposed vehicle-class: " + vc + ", supplierRef=" + rental.getSupplierReference());
            }
        }

        ArrayList<Driver> drivers = Driver.getDrivers(connection, rental);
        boolean foundPrimaryDriver = false;
        for (Driver d : drivers) {
            if (d.getPrimaryDriver().equals("y")) {
                claim.setPolicyHolderName(d.getTitle() + " " + d.getFirstnames() + " " + d.getLastname());
                foundPrimaryDriver = true;
            }
        }
        if (!foundPrimaryDriver && drivers.size() > 0) {
            Driver d = drivers.get(0);
            claim.setPolicyHolderName(d.getTitle() + " " + d.getFirstnames() + " " + d.getLastname());
        }

        Element insurerElement = XMLUtils.getElement(claimElement, "insurer");
        if (insurerElement != null) {
            String insurerName = XMLUtils.getElementValue(insurerElement, "name");
            InsurerCountry insurer = InsurerCountry.getByName(connection, insurerName);
            if (insurer == null) {
                claim.setInsurerCountryID(-1);
            } else {
                claim.setInsurerCountryID(insurer.getID());
            }
            String policyNumber = XMLUtils.getElementValue(insurerElement, "policy-number");
            if (policyNumber != null && policyNumber.trim().length() == 0) {
                policyNumber = null;
            }
            claim.setPolicyNumber(policyNumber);

            String claimReference = XMLUtils.getElementValue(insurerElement, "claim-reference");
            if (claimReference != null && claimReference.trim().length() == 0) {
                claimReference = null;
            }

            claim.setClaimReference(claimReference);


            String comprehensive = XMLUtils.getElementValue(insurerElement, "comprehensive");
            if (comprehensive == null || comprehensive.trim().length() == 0) {
                comprehensive = "n";
            }
            comprehensive = new String(comprehensive.toLowerCase().substring(0, 1));
            claim.setComprehensive(comprehensive);

        }

        Element vehicleElement = XMLUtils.getElement(claimElement, "vehicle");
        if (vehicleElement == null) {
            throw new Exception("Missing <claim><customer><vehicle> section");
        }
        String vehicleRegistration = XMLUtils.getElementValue(vehicleElement, "vehicle-registration");
        if (vehicleRegistration != null && vehicleRegistration.trim().length() == 0) {
            vehicleRegistration = null;
        }
        claim.setVehicleRegistration(vehicleRegistration);

        String vehicleManufacturer = XMLUtils.getElementValue(vehicleElement, "vehicle-manufacturer");
        if (vehicleManufacturer != null && vehicleManufacturer.trim().length() == 0) {
            vehicleManufacturer = null;
        }
        claim.setVehicleManufacturer(vehicleManufacturer);

        String vehicleModel = XMLUtils.getElementValue(vehicleElement, "vehicle-model");
        if (vehicleModel != null && vehicleModel.trim().length() == 0) {
            vehicleModel = null;
        }
        claim.setVehicleModel(vehicleModel);

        String vc = XMLUtils.getElementValue(vehicleElement, "vehicle-class");
        VehicleClass c = VehicleClass.getByName(connection, vc);
        if (c == null) {
            throw new Exception("Cannot find vehicle class [" + vc + "]");
        }
        claim.setVehicleClassID(c.getID());

        String usable = XMLUtils.getElementValue(vehicleElement, "usable");
        if (usable == null || usable.trim().length() == 0) {
            usable = "n";
        }
        usable = new String(usable.toLowerCase().substring(0, 1));
        claim.setUsable(usable);

        Element thirdPartyElement = XMLUtils.getElement(claimElement, "third-party");
        if (thirdPartyElement == null) {
            throw new Exception("Cannot find <claim><third-party> element");
        }
        Element tpInsurerElement = XMLUtils.getElement(thirdPartyElement, "insurer");
        if (tpInsurerElement == null) {
            throw new Exception("Cannot find <claim><third-party><insurer> element");
        }
        claim.setDamageDescription(XMLUtils.getElementValue(vehicleElement, "damage-description"));

        String tpInsurerName = XMLUtils.getElementValue(tpInsurerElement, "name");
        if (tpInsurerName == null) {
            throw new Exception("Cannot find <claim><third-party><insurer><name> element");
        }
        InsurerCountry tpInsurerCountry = InsurerCountry.getByName(connection, tpInsurerName);
        if (tpInsurerCountry == null) {
            throw new Exception("Cannot find Insurer [" + tpInsurerName + "]");
        }
        claim.setTpInsurerCountryID(tpInsurerCountry.getID());

        String tpPolicyNumber = XMLUtils.getElementValue(tpInsurerElement, "policy-number");
        if (tpPolicyNumber != null && tpPolicyNumber.trim().length() == 0) {
            tpPolicyNumber = null;
        }
        claim.setTpPolicyNumber(tpPolicyNumber);

        String tpClaimReference = XMLUtils.getElementValue(tpInsurerElement, "claim-reference");
        if (tpClaimReference != null && tpClaimReference.trim().length() == 0) {
            tpClaimReference = null;
        }
        if (claim.getTpClaimReference() == null || claim.getTpClaimReference().trim().length() == 0) {
            claim.setTpClaimReference(tpClaimReference);
        }
        Element tpVehicleElement = XMLUtils.getElement(thirdPartyElement, "vehicle");
        if (tpVehicleElement != null) {

            claim.setTpVehicleRegistration(XMLUtils.getElementValue(tpVehicleElement, "vehicle-registration"));
            claim.setTpVehicleManufacturer(XMLUtils.getElementValue(tpVehicleElement, "vehicle-manufacturer"));
            claim.setTpVehicleModel(XMLUtils.getElementValue(tpVehicleElement, "vehicle-model"));

            VehicleClass tpVc = null;
            try {
                tpVc = VehicleClass.getByName(connection, XMLUtils.getElementValue(tpVehicleElement, "vehicle-class"));
            } catch (Exception e) {
            }
            claim.setTpVehicleClassID(tpVc == null ? -1 : tpVc.getID());
        }

        Element tpDriverElement = XMLUtils.getElement(thirdPartyElement, "driver");
        if (tpDriverElement != null) {
            String tpTitle = XMLUtils.getElementValue(tpDriverElement, "title");
            String tpFirstnames = XMLUtils.getElementValue(tpDriverElement, "firstnames");
            String tpLastname = XMLUtils.getElementValue(tpDriverElement, "lastname");

            String tpName = ((tpTitle == null ? "" : tpTitle + " ") + (tpFirstnames == null ? "" : tpFirstnames + " ") + (tpLastname == null ? "" : tpLastname)).trim();
            claim.setTpName(tpName);
            claim.setTpAddress1(XMLUtils.getElementValue(tpDriverElement, "address1"));
            claim.setTpAddress2(XMLUtils.getElementValue(tpDriverElement, "address2"));
            claim.setTpAddress3(XMLUtils.getElementValue(tpDriverElement, "address3"));
            claim.setTpAddress4(XMLUtils.getElementValue(tpDriverElement, "address4"));
            claim.setTpAddress5(XMLUtils.getElementValue(tpDriverElement, "address5"));
            claim.setTpPostcode(XMLUtils.getElementValue(tpDriverElement, "postcode"));
            claim.setTpTelephoneDay(XMLUtils.getElementValue(tpDriverElement, "telephone-day"));
            claim.setTpTelephoneEvening(XMLUtils.getElementValue(tpDriverElement, "telephone-evening"));
            claim.setTpEmail(XMLUtils.getElementValue(tpDriverElement, "email"));
        }

        Element incidentElement = XMLUtils.getElement(claimElement, "incident");
        if (incidentElement != null) {

            claim.setIncidentDate(parseDate(XMLUtils.getElementValue(incidentElement, "date")));
            claim.setLocation(XMLUtils.getElementValue(incidentElement, "location"));

            String policeInvolved = XMLUtils.getElementValue(incidentElement, "police-involved");
            if (policeInvolved == null || policeInvolved.trim().length() == 0) {
                policeInvolved = "n";
            }
            policeInvolved = new String(policeInvolved.toLowerCase().substring(0, 1));
            claim.setPoliceInvolved(policeInvolved);

            claim.setIncidentDescription(XMLUtils.getElementValue(incidentElement, "description"));

            claim.save(connection);


            Witness.removeForClaim(connection, claim);
            Element witnessesElement = XMLUtils.getElement(incidentElement, "witnesses");
            if (witnessesElement != null) {
                ArrayList<Element> witnessElements = XMLUtils.getElements(doc, witnessesElement, "witness");
                for (Element we : witnessElements) {
                    Witness w = new Witness();
                    w.setClaimID(claim.getID());
                    String witnessName=XMLUtils.getElementValue(we, "name");
                    if(witnessName!=null && witnessName.trim().length()==0)
                        witnessName=null;
                    if(witnessName==null) continue;
                    w.setName(witnessName);
                    w.setAddress1(XMLUtils.getElementValue(we, "address1"));
                    w.setAddress2(XMLUtils.getElementValue(we, "address2"));
                    w.setAddress3(XMLUtils.getElementValue(we, "address3"));
                    w.setAddress4(XMLUtils.getElementValue(we, "address4"));
                    w.setAddress5(XMLUtils.getElementValue(we, "address5"));
                    w.setPostcode(XMLUtils.getElementValue(we, "postcode"));
                    w.setTelephoneDay(XMLUtils.getElementValue(we, "telephone-day"));
                    w.setTelephoneEvening(XMLUtils.getElementValue(we, "telephone-evening"));
                    w.setEmail(XMLUtils.getElementValue(we, "email"));
                    w.save(connection);
                }
            }

            Injured.removeForClaim(connection, claim);
            Element injuriesElement = XMLUtils.getElement(incidentElement, "injuries");
            if (injuriesElement != null) {
                ArrayList<Element> injuryElements = XMLUtils.getElements(doc, injuriesElement, "injury");
                for (Element ie : injuryElements) {
                    Injured i = new Injured();
                    i.setClaimID(claim.getID());
                    String injuredName=XMLUtils.getElementValue(ie, "name");
                    if(injuredName!=null && injuredName.trim().length()==0)
                        injuredName=null;
                    if(injuredName==null)
                        continue;
                    i.setName(injuredName);
                    i.setAddress1(XMLUtils.getElementValue(ie, "address1"));
                    i.setAddress2(XMLUtils.getElementValue(ie, "address2"));
                    i.setAddress3(XMLUtils.getElementValue(ie, "address3"));
                    i.setAddress4(XMLUtils.getElementValue(ie, "address4"));
                    i.setAddress5(XMLUtils.getElementValue(ie, "address5"));
                    i.setPostcode(XMLUtils.getElementValue(ie, "postcode"));
                    i.setTelephoneDay(XMLUtils.getElementValue(ie, "telephone-day"));
                    i.setTelephoneEvening(XMLUtils.getElementValue(ie, "telephone-evening"));
                    i.setEmail(XMLUtils.getElementValue(ie, "email"));

                    Element solicitorElement = XMLUtils.getElement(ie, "solicitor");
                    if (solicitorElement == null) {
                        i.setSolicitorAppointed("n");
                    } else {
                        i.setSolicitorAppointed("y");
                        i.setSolicitorName(XMLUtils.getElementValue(solicitorElement, "name"));
                        i.setSolicitorAddress1(XMLUtils.getElementValue(solicitorElement, "address1"));
                        i.setSolicitorAddress2(XMLUtils.getElementValue(solicitorElement, "address2"));
                        i.setSolicitorAddress3(XMLUtils.getElementValue(solicitorElement, "address3"));
                        i.setSolicitorAddress4(XMLUtils.getElementValue(solicitorElement, "address4"));
                        i.setSolicitorAddress5(XMLUtils.getElementValue(solicitorElement, "address5"));
                        i.setSolicitorPostcode(XMLUtils.getElementValue(solicitorElement, "postcode"));
                        i.setSolicitorTelephone(XMLUtils.getElementValue(solicitorElement, "telephone"));
                        i.setSolicitorEmail(XMLUtils.getElementValue(solicitorElement, "email"));
                    }
                    i.save(connection);
                }
            }
        }
    }

    private void handleRepair(DBConnectionWrapper connection, Document doc, Element root, Message message, Rental rental) throws Exception {
        /*Element repairElement = XMLUtils.getElement(root, "repair");
        if (repairElement == null) {
        return;
        }*/
        Element engineerReportElement = XMLUtils.getElement(root, "engineer-report");
        if (engineerReportElement == null) {
            return;
        }
        Claim claim = Claim.getForRental(connection, rental);
        if (claim == null) {
            return;
        }
        EngineerReport.removeForClaim(connection, claim);
        EngineerReport er = new EngineerReport();
        er.setClaimID(claim.getID());
        
        String days=XMLUtils.getElementValue(engineerReportElement, "days");
        if(days==null || days.trim().length()==0)
            return;        
        er.setDays(new BigDecimal(days));
        
        String labourAmount=XMLUtils.getElementValue(engineerReportElement, "labour-amount");
        if(labourAmount==null || labourAmount.trim().length()==0)
            return;
        er.setLabourAmount(new BigDecimal(labourAmount));
        
        String totalAmount=XMLUtils.getElementValue(engineerReportElement, "total-amount");
        if(totalAmount==null || totalAmount.trim().length()==0)
            return;
        er.setTotalAmount(new BigDecimal(totalAmount));

        String name = XMLUtils.getElementValue(engineerReportElement, "name");
        if (name != null && name.trim().length() == 0) {
            name = null;
        }
        er.setName(name);

        String address1 = XMLUtils.getElementValue(engineerReportElement, "address1");
        if (address1 != null && address1.trim().length() == 0) {
            address1 = null;
        }
        er.setAddress1(address1);

        String address2 = XMLUtils.getElementValue(engineerReportElement, "address2");
        if (address2 != null && address2.trim().length() == 0) {
            address2 = null;
        }
        er.setAddress2(address2);

        String address3 = XMLUtils.getElementValue(engineerReportElement, "address3");
        if (address3 != null && address3.trim().length() == 0) {
            address3 = null;
        }
        er.setAddress3(address3);

        String address4 = XMLUtils.getElementValue(engineerReportElement, "address4");
        if (address4 != null && address4.trim().length() == 0) {
            address4 = null;
        }
        er.setAddress4(address4);

        String address5 = XMLUtils.getElementValue(engineerReportElement, "address5");
        if (address5 != null && address5.trim().length() == 0) {
            address5 = null;
        }
        er.setAddress5(address5);

        String postcode = XMLUtils.getElementValue(engineerReportElement, "postcode");
        if (postcode != null && postcode.trim().length() == 0) {
            postcode = null;
        }
        er.setPostcode(postcode);

        String telephone = XMLUtils.getElementValue(engineerReportElement, "telephone");
        if (telephone != null && telephone.trim().length() == 0) {
            telephone = null;
        }
        er.setTelephone(telephone);

        String email = XMLUtils.getElementValue(engineerReportElement, "email");
        if (email != null && email.trim().length() == 0) {
            email = null;
        }
        er.setEmail(email);

        String usable = XMLUtils.getElementValue(engineerReportElement, "usable");
        if (usable == null || usable.trim().length() == 0) {
            usable = "n";
        }
        usable = new String(usable.toLowerCase().substring(0, 1));
        er.setUsable(usable);

        er.save(connection);
    }

    private Timestamp parseDate(String t) // date in CCYY-MM-DDTHH:MN:SS
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.YEAR, Integer.parseInt(t.substring(0, 4)));
        c.set(Calendar.MONTH, Integer.parseInt(t.substring(5, 7)) - 1);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(t.substring(8, 10)));
        c.set(Calendar.HOUR, Integer.parseInt(t.substring(11, 13)));
        c.set(Calendar.MINUTE, Integer.parseInt(t.substring(14, 16)));
        c.set(Calendar.SECOND, Integer.parseInt(t.substring(17)));

        return new Timestamp(c.getTimeInMillis());
    }
}
