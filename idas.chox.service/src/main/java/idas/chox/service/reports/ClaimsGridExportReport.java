package idas.chox.service.reports;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.services.ReportDataService;
import idas.chox.data.*;
import idas.chox.data.services.SecureDataService;

/**
 *
 * @author seeni
 */
public class ClaimsGridExportReport {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClaimsGridExportReport.class);
    private SecureDataService dataService;
    private ReportDataService reportDataService;

    public void setDataService(SecureDataService dataService) {
        this.dataService = dataService;
    }

    public void setReportDataService(ReportDataService reportDataService) {
        this.reportDataService = reportDataService;
    }
    
    public List<ExcelClaimCycle> getExcelClaimCycle(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ")
            .append(" c.cho_reference as choreference, a.created_date as modifieddate,")
            .append(" case when ins.name is not null then wu.first_name || ' ' || wu.last_name || ' (' || ins.name || ')'")
            .append("       else case when cho.name is not null then wu.first_name || ' ' || wu.last_name || ' (' || cho.name || ')'")
            .append("            else wu.first_name || ' ' || wu.last_name end end as modifiedby,")
            .append(" a.new_status as status, a.reverted as reverted")
            .append(" from claim c")
            .append(" join audit_trail a on (c.id = a.claim_id)")
            .append(" left outer join web_user wu on (a.created_by = wu.id)")
            .append(" left outer join insurer ins on (wu.insurer_id = ins.id)")
            .append(" left outer join chorganisation cho on (wu.chorganisation_id = cho.id)")
            .append(" where c.id in (");

        boolean first = true;
        for (Integer id : ids) {
            if (!first) {
                sb.append(", ").append(id.toString());
            }
            else {
                sb.append(id.toString());
                first = false;
            }
        }            
        sb.append(") ");
        sb.append(" order by  choreference, modifieddate");

        LOG.debug("Querying for claim cycle details...\n{}", sb.toString());
        List result = reportDataService.getReportData(sb.toString());
        LOG.debug("Got claim cycle details - building data objects");

        List<ExcelClaimCycle> results = new ArrayList<ExcelClaimCycle>(result.size());
        for(Object obj : result) {
            results.add(new ExcelClaimCycle((Map)obj));
        }

        return results;
    }
    
    public List<ExcelComment> getExcelComments(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ")
            .append("     c.cho_reference as choreference, n.created_date as createddate,")
            .append(" case when ins.name is not null then wu.last_name || ', ' || wu.first_name || ' (' || ins.name || ')'")
            .append("       else case when cho.name is not null then wu.last_name || ', ' || wu.first_name || ' (' || cho.name || ')'")
            .append("            else wu.last_name || ', ' || wu.first_name end end as createdby,")
            .append("     n.comment as comment, n.visibility_type as visibilitytype")
            .append(" from claim c")
            .append(" join comment n on (c.id = n.claim_id)")
            .append(" left outer join web_user wu on (n.created_by = wu.id)")
            .append(" left outer join insurer ins on (wu.insurer_id = ins.id)")
            .append(" left outer join chorganisation cho on (wu.chorganisation_id = cho.id)")
            .append(" where n.reverted = false")

//            .append(" and c.id in ( :claimIds )");
            .append(" and c.id in (");

        boolean first = true;
        for (Integer id : ids) {
            if (!first) {
                sb.append(", ").append(id.toString());
            }
            else {
                sb.append(id.toString());
                first = false;
            }
        }            
        sb.append(") ");
        sb.append(" order by  choreference, createddate");


        LOG.debug("Querying for BRE history details...\n{}", sb.toString());
        List result = reportDataService.getReportData(sb.toString());
        LOG.debug("Got BRE history details - building data objects");

        List<ExcelComment> results = new ArrayList<ExcelComment>(result.size());
        for(Object obj : result) {
            ExcelComment comment = new ExcelComment((Map)obj);
            if ((dataService.getCurrentUser().isCHO() && comment.getVisibilityType() == 1)
                    || (dataService.getCurrentUser().isAnInsurer() && comment.getVisibilityType() == 2)) {
                continue;
            }

            results.add(new ExcelComment((Map)obj));
        }

        
        return results;
    }
    
    public List<ExcelHistory> getExcelHistory(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ")
            .append(" c.cho_reference as choreference, h.process_date as processdate,")
            .append(" h.rule_id as ruleid, h.type as type, h.narrative as narrative,")
            .append(" h.is_public as ispublic")
            .append(" from claim c")
            .append(" join invoice i on (c.invoice_id = i.id)")
            .append(" join history h on (c.id = h.claim_id)")
            .append(" where h.type != 'INFO'")

//            .append(" and c.id in ( :claimIds )");
            .append(" and c.id in (");

        boolean first = true;
        for (Integer id : ids) {
            if (!first) {
                sb.append(", ").append(id.toString());
            }
            else {
                sb.append(id.toString());
                first = false;
            }
        }            
        sb.append(") ");
        sb.append(" order by choreference, processdate, ruleid");


        LOG.debug("Querying for BRE history details...\n{}", sb.toString());
        List result = reportDataService.getReportData(sb.toString());
        LOG.debug("Got BRE history details - building data objects");

        List<ExcelHistory> results = new ArrayList<ExcelHistory>(result.size());
        for(Object obj : result) {
            ExcelHistory history = new ExcelHistory((Map)obj);
            if (!dataService.getCurrentUser().isCHO() ||  history.isVisibleToCHO()) {
                results.add(history);
            }
        }

        return results;
    }
    
    public List<ExcelInvoice> getExcelInvoices(List<Integer> ids) {
        List<ExcelInvoice> results = new ArrayList<ExcelInvoice>(ids.size());
        StringBuilder sb = new StringBuilder();
        sb.append("select ")
            .append(" c.status as claimstatus, c.cho_reference as choreference, c.claim_number as claimnumber,")
            .append(" i.created_date as createddate, i.auto_penalty_start as autopenaltystart, i.collaboration_fee as collaborationfee, i.collaboration_qty as collaborationqty, i.miscellaneous_fee as miscellaneousfee,")
            .append(" i.automatic_fee as automaticfee, i.automatic_qty as automaticqty, i.additional_driver_fee as additionaldriverfee,")
            .append(" i.additional_driver_qty as additionaldriverqty, i.sat_nav_fee as satnavfee, i.sat_nav_qty as satnavqty,")
            .append(" i.estate_fee as estatefee, i.estate_qty as estateqty, i.baby_seat_fee as babyseatfee, i.baby_seat_qty as babyseatqty,")
            .append(" i.tow_bars_fee as towbarsfee, i.tow_bars_qty as towbarsqty, i.non_standard_insurance_premium_fee as nonstandardinsurancepremiumfee,")
            .append(" i.non_standard_insurance_premium_qty as nonstandardinsurancepremiumqty, i.cover_note_required as covernoterequired,")
            .append(" i.admin_fee as adminfee, i.admin_qty as adminqty, i.roof_rack_fee as roofrackfee, i.roof_rack_qty as roofrackqty,")
            .append(" i.dual_control_fee as dualcontrolfee, i.dual_control_qty as dualcontrolqty, i.delivery_collection_fee as deliverycollectionfee,")
            .append(" i.delivery_collection_qty as deliverycollectionqty, i.excess_amount_collected as excessamountcollected,")
            .append(" i.vat_amount_collected as vatamountcollected, i.handling_invoice_no as handlinginvoiceno,")
            .append(" i.claims_handling_invoice_amount as  claimshandlinginvoiceamount, i.claim_invoice_no as claiminvoiceno,")
            .append(" i.hire_rate_charged_per_day as hireratechargedperday, i.hire_net as hirenet, i.hire_vat as hirevat,")
            .append(" i.hire_gross as hiregross, i.repair_net as repairnet, i.repair_vat as repairvat, i.repair_gross as repairgross,")
            .append(" i.engineer_fee_net as engineerfeenet, i.engineer_fee_vat as engineerfeevat, i.engineer_fee_gross as engineerfeegross,")
            .append(" i.total_loss_net as totallossfeenet, i.total_loss_vat as totallossfeevat, i.total_loss_gross as totallossfeegross,")
            .append(" i.storage_recovery_net as storagerecoverynet, i.storage_recovery_vat as storagerecoveryvat, i.storage_recovery_gross as storagerecoverygross,")
            .append(" i.deduction_for_claims_handling_fee as deductionforclaimshandlingfee, i.hire_penalty_charge as hirepenaltycharge,")
            .append(" i.hire_penalty_percentage as hirepenaltypercentage, i.repair_penalty_charge as repairpenaltycharge,")
            .append(" i.repair_penalty_percentage as repairpenaltypercentage, i.total_penalty_charge as totalpenaltycharge,")
            .append(" i.total_net as totalnet, i.total_vat as totalvat, i.total_gross as totalgross, i.discount as discount,")
            .append(" i.insurer_discount as insurerdiscount, i.full_total_to_pay as fulltotaltopay, io.full_total_to_pay as original_fulltotaltopay,")
            .append(" i.total_to_pay as totaltopay, io.total_to_pay as original_totaltopay, i.interim_payment_made as interimpaymentmade,")
            .append(" i.interim_payment_received as interimpaymentreceived, i.date_invoiced as dateinvoiced, i.hire_gross_paid as hiregrosspaid,")
            .append(" i.repair_gross_paid as repairgrosspaid, i.engineer_fee_gross_paid as engineerfeegrosspaid, i.total_loss_fee_gross_paid as totallossfeegrosspaid,")
            .append(" i.storage_recovery_gross_paid as storagerecoverygrosspaid, i.hire_penalty_charge_paid as hirepenaltychargepaid,")
            .append(" i.repair_penalty_charge_paid as repairpenaltychargepaid, i.claim_handler_charge_paid as claimhandlerchargepaid,")
            .append(" i.deduction_claim_handler_fee_paid as deductionclaimhandlerfeepaid, i.cho_discount_fee_paid as chodiscountfeepaid,")
            .append(" i.insurer_discount_fee_paid as insurerdiscountfeepaid, i.final_payment as finalpayment,")
            .append(" i.repair_admin_fee as repairadminfee, i.repair_acquisition_fee as repairacquisitionfee")
            .append(" from claim c")
            .append(" left outer join third_party tp on (c.third_party_id = tp.id)")
            .append(" join invoice i on (c.invoice_id = i.id)")
            .append(" join invoice_original io on (i.invoice_original_id = io.id)")
            .append(" where c.id in (");

        boolean first = true;
        for (Integer id : ids) {
            if (!first) {
                sb.append(", ").append(id.toString());
            } else {
                sb.append(id.toString());
                first = false;
            }
        }
        sb.append(") ");
        sb.append(" order by createddate");


        LOG.debug("Querying for invoice details...\n{}", sb.toString());
        List result = reportDataService.getReportData(sb.toString());
        LOG.debug("Got invoice details - building data objects");

        for (Object obj : result) {
            results.add(new ExcelInvoice((Map) obj, dataService.getCurrentUser().isCHO()));
        }

        LOG.debug("Returning results.");

        return results;
    }
    
    public List<ExcelClaim> getExcelClaims(List<Integer> ids, Boolean isIns) {
        List<ExcelClaim> results = new ArrayList<ExcelClaim>(ids.size());
        StringBuilder sb = new StringBuilder();
        sb.append("select")
            .append(" c.status, c.claim_type, c.cho_reference, cho.name as chorg_name, w.name as workgroup_name, c.status_modified_date, c.indeminty_amount,")
            .append(" c.liability_status, c.percentage_liability_accepted, c.percentage_liability_cho, c.managing_repair, c.policy_holder_contact_date,")
            .append(" c.credit_agreement_date, c.gta_notice_date, c.claim_number, wu.last_name || ' ' || wu.first_name as claim_owner, cust.title as customer_title,")
            .append(" c.final_review_cho, c.final_review_ins, wuc.last_name || ' ' || wuc.first_name as claim_supplier_owner,")
            .append(" cust.first_name as customer_first_name, cust.last_name as customer_last_name, cust.address1 as customer_address1, cust.address2 as customer_address2,")
            .append(" cust.address3 as customer_address3, cust.address4 as customer_address4, cust.address5 as customer_address5, cust.postcode as customer_postcode,")
            .append(" cust.telephone_day as customer_telephone_day, cust.telephone_evening as customer_telephone_evening, cust.email as customer_email,")
            .append(" cust.age as customer_age, cust.occupation as customer_occupation, cust.policy_usage as customer_policy_usage,")
            .append(" cust.insurer_name as customer_insurer_name, cust.policy_number as customer_policy_number, cust.claim_reference as customer_claim_reference,")
            .append(" cust.comprehensive as customer_comprehensive, cust.vehicle_manufacturer as customer_vehicle_manufacturer, cust.vehicle_model as customer_vehicle_model,")
            .append(" cust.vehicle_registration as customer_vehicle_registration, cust.vehicle_year as customer_vehicle_year, cust_vc.name as customer_vehicle_class,")
            .append(" cust.location as customer_location, cust.hpi_vehicle_manufacturer as customer_hpi_vehicle_manufacturer, cust.hpi_vehicle_model as customer_hpi_vehicle_model,")
            .append(" cust.hpi_vehicle_year as customer_hpi_vehicle_year, cust.hpi_first_registration as customer_hpi_vehicle_first_registration,")
            .append(" cust.hpi_vehicle_capacity as customer_hpi_vehicle_capacity, cust.hpi_vehicle_doorplan as customer_hpi_vehicle_doorplan,")
            .append(" cust.hpi_vehicle_transmission as customer_hpi_vehicle_transmission, cust.access_other_vehicle as customer_access_other_vehicle,")
            .append(" cust.other_vehicle_used as customer_other_vehicle_used, cust.other_vehicle as customer_other_vehicle, cust.courtesy_car as customer_courtesy_car,")
            .append(" cust.specific_vehicle as customer_specific_vehicle, cust.specific_vehicle_reason as customer_specific_vehicle_reason,")
            .append(" cust.vehicle_type_required as customer_vehicle_type_required, cust.special_requirements as customer_special_requirements,")
            .append(" cust.average_daily_mileage as customer_average_daily_mileage, cust.damage as customer_damage, cust.is_usable as customer_is_usable,")
            .append(" cust.is_total_loss as customer_is_total_loss, cust.initial_ecd as customer_initial_ecd,")
            .append(" tp.title as tp_title, tp.first_name as tp_first_name, tp.last_name as tp_last_name,")
            .append(" tp.address1 as tp_address1, tp.address2 as tp_address2, tp.address3 as tp_address3, tp.address4 as tp_address4, tp.address5 as tp_address5, ")
            .append(" tp.postcode as tp_postcode, tp.telephone_day as tp_telephone_day, tp.telephone_evening as tp_telephone_evening, tp.email as tp_email,")
            .append(" tp_insurer.name as tp_insurer_name, tp.policy_number as tp_policy_number, ")
            .append(" tp.vehicle_manufacturer as tp_vehicle_manufacturer, tp.vehicle_model as tp_vehicle_model,")
            .append(" tp.vehicle_registration as tp_vehicle_registration, tp_vc.name as tp_vehicle_class,")
            .append(" inc. date as incident_date, inc.location as incident_location, inc.is_police_involved as incident_is_police_involved, inc.incident_description as incident_description,")
            .append(" wit.name as witness_name, wit.address1 as witness_address1, wit.address2 as witness_address2, wit.address3 as witness_address3,")
            .append(" wit.address4 as witness_address4, wit.address5 as witness_address5, wit.postcode as witness_postcode, wit.telephone_evening as witness_telephone_evening,")
            .append(" wit.telephone_day as witness_telephone_day, wit.email as witness_email,")
            .append(" inj.name as injury_name, inj.address1 as injury_address1, inj.address2 as injury_address2, inj.address3 as injury_address3, inj.address4 as injury_address4,")
            .append(" inj.address5 as injury_address5, inj.postcode as injury_postcode, inj.email as injury_email, inj.telephone_day as injury_telephone_day, ")
            .append(" inj.telephone_evening as injury_telephone_evening, inj.solicitor_name as injury_solicitor_name, inj.solicitor_address1 as injury_solicitor_address1,")
            .append(" inj.solicitor_address2 as injury_solicitor_address2, inj.solicitor_address3 as injury_solicitor_address3, inj.solicitor_address4 as injury_solicitor_address4,")
            .append(" inj.solicitor_address5 as injury_solicitor_address5, inj.solicitor_postcode as injury_solicitor_postcode, inj.solicitor_telephone as injury_solicitor_telephone,")
            .append(" inj.solicitor_email as injury_solicitor_email, er.labour_amount as er_labour_amount, er.total_amount as er_repair_amount, er.days as er_days,")
            .append(" er.is_usable as er_is_usable, er.name as er_name, er.company as er_company, er.address1 as er_address1, er.address2 as er_address2, er.address3 as er_address3,")
            .append(" er.address4 as er_address4, er.address5 as er_address5, er.postcode as er_postcode, er.telephone as er_telephone, er.email as er_email,")
            .append(" vh.vehicle_manufacturer as vh_vehicle_manufacturer, vh.vehicle_model as vh_vehicle_model, vh.vehicle_registration as vh_vehicle_registration,")
            .append(" vh_vc.name as vh_vehicle_class_name, vh.rental_start as vh_rental_start, vh.rental_end as vh_rental_end, vh.days as vh_days,")
            .append(" vh.collection_reason as vh_collection_reason, vh.hpi_vehicle_manufacturer as vh_hpi_vehicle_manufacturer, vh.hpi_vehicle_model as vh_hpi_vehicle_model,")
            .append(" vh.hpi_vehicle_year as vh_hpi_vehicle_year, vh.hpi_first_registration as vh_hpi_vehicle_first_registration,")
            .append(" vh.hpi_vehicle_capacity as vh_hpi_vehicle_capacity, vh.hpi_vehicle_doorplan as vh_hpi_vehicle_doorplan,")
            .append(" vh.hpi_vehicle_transmission as vh_hpi_vehicle_transmission, hmd.name_of_repairer as hmd_name_of_repairer, hmd.repair_book_in_date as hmd_repair_book_in_date,")
            .append(" hmd.repair_authorised_date as hmd_repair_authorised_date, hmd.repair_commenced_date as hmd_repair_commenced_date,")
            .append(" hmd.inspection_booked_date as hmd_inspection_booked_date, hmd.inspection_date as hmd_inspection_date, hmd.name_of_ime as hmd_name_of_ime,")
            .append(" hmd.repair_completion_date as hmd_repair_completion_date, hmd.is_total_lost_check as hmd_is_total_lost_check, hmd.total_loss_offer_made as hmd_total_loss_offer_made,")
            .append(" hmd.total_loss_offer_accepted as hmd_total_loss_offer_accepted, hmd.total_loss_check_issued as hmd_total_loss_check_issued,")
            .append(" hmd.total_loss_check_received as hmd_total_loss_check_received, hmd.labour_rate as hmd_labour_rate, hmd.labour_hour as hmd_labour_hour,")
            .append(" hmd.is_repair_only_check as claim_repair_only_check, hmd.is_non_fault_insurer_managing_repair as claim_non_fault_insurer_repair,")
            .append(" hmd.client_vat_registered as claim_client_vat_registered,")
            .append(" hmd.labour_cost as hmd_labour_cost, hmd.non_provision_reason as hmd_non_provision_reason, hmd.next_review_date as hmd_next_review_date")
            .append(" from claim c")
            .append("     join chorganisation cho on (c.chorganisation_id = cho.id)")
            .append("     left outer join workgroup w on (c.workgroup_id = w.id)")
            .append("     left outer join web_user wu on (c.claim_owner_id = wu.id)")
            .append("     left outer join web_user wuc on (c.cho_claim_owner_id = wuc.id)")
            .append("     left outer join customer cust on (c.customer_id = cust.id)")
            .append("     left outer join vehicle_class cust_vc on (cust.vehicle_class_id = cust_vc.id)")
            .append("     left outer join third_party tp on (c.third_party_id = tp.id)")
            .append("     left outer join vehicle_class tp_vc on (cust.vehicle_class_id = tp_vc.id)")
            .append("     left outer join insurer tp_insurer on (tp.insurer_id = tp_insurer.id)")
            .append("     left outer join incident inc on (c.incident_id = inc.id)")
            .append("     left outer join witness wit on (inc.id = wit.incident_id)")
            .append("     left outer join injury inj on (inc.id = inj.incident_id)")
            .append("     left outer join engineer_report er on (c.engineer_report_id = er.id)")
            .append("     left outer join vehicle_hire vh on (c.vehicle_hire_id = vh.id)")
            .append("     left outer join vehicle_class vh_vc on (vh.vehicle_class_id = vh_vc.id)")
            .append("     left outer join hire_monitoring_detail hmd on (c.hire_monitoring_detail_id = hmd.id)")
            .append(" where c.id in (");

        boolean first = true;
        for (Integer id : ids) {
            if (!first) {
                sb.append(", ").append(id.toString());
            }
            else {
                sb.append(id.toString());
                first = false;
            }
        }            
        sb.append(") ");

        LOG.debug("Querying for claim details...\n{}", sb.toString());
        List result = reportDataService.getReportData(sb.toString());
        LOG.debug("Got details - building data objects");

        for(Object obj : result) {
            results.add(new ExcelClaim((Map)obj, isIns));
        }

        LOG.debug("Returning results.");
        return results;
    }
}
