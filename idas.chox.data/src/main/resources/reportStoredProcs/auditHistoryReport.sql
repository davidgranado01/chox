DROP FUNCTION auditHistoryReport(IN insId integer, IN choIds  integer[], IN worgroupGroupings integer[][], IN worgroupGroupingLabels character varying[], IN numberOfWeeks integer, IN claimTypes integer[]);

CREATE OR REPLACE FUNCTION auditHistoryReport(
    IN insId integer,
    IN choIds  integer[],
    IN worgroupGroupings integer[][],
    IN worgroupGroupingLabels varchar[],
    IN numberOfWeeks integer,
    IN claimTypes integer[])

RETURNS TABLE(
    "Week No." varchar,
    "Workgroup Grouping" varchar,
--    "Workgroups" integer[],
--    "Week Start" date,
--    "Week End" date)
    "Volume of Audits Completed" integer,
    "Total No. of audits (rolling total)" integer,
    "Hire Volume" integer,
    "Average Hire Days" numeric(4,0),
    "Average Hire Cost" numeric(10,2),
    "No. of cases with Penalties Paid" bigint,
    "Amount of Penalties Paid" numeric(10,2),
    "Number of cases with Hire Leakage" bigint,
    "Amount of Hire Leakage" numeric(10,2),
    "Repair Volume" bigint,
    "Average Repair Cost" numeric(10,2),
    "No. of cases exceeding Engineers Recommendations" bigint,
    "Number of cases with labour above ABP" bigint,
    "Total Loss Volume" bigint,
    "No. of cases with Recovery Charged" bigint,
    "No. of cases with Recovery Incorrectly Charged" bigint,
    "No. of cases with Storage Charged" bigint,
    "No. of cases with Storage Incorrectly Charged" bigint)
AS

$BODY$
    DECLARE
        weeklyBreakDownDatesRecord RECORD;
        workgroups integer[];
        workgroupsLabel varchar;
    BEGIN
        FOR weeklyBreakDownDatesRecord IN SELECT * FROM breakDownDatesByWeek(numberOfWeeks) LOOP -- loop through each row
            FOR i IN 0 .. array_upper(worgroupGroupings, 1) LOOP
                workgroups = case when i=0 then null::integer[] else worgroupGroupings[i:i] end;
                -- unnest the workgroups
                workgroups = array(select unnest(workgroups[1:1]));
                workgroupsLabel = case when i=0 then 'All' else worgroupGroupingLabels[i] end;

                RETURN QUERY
--                    select weeklyBreakDownDatesRecord.week_label, workgroupsLabel, workgroups, weeklyBreakDownDatesRecord.week_start, weeklyBreakDownDatesRecord.week_end;
                    select weeklyBreakDownDatesRecord.week_label as "Week No.",
                           workgroupsLabel as "Workgroup Grouping",
                           (select count(*)::integer
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "Volume of Audits Completed",
                           (select count(*)::integer
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and  ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "Total No. of audits (rolling total)",
                           (select count(*)::integer
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.total_hire_cost > 0
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "Hire Volume",
                           (select avg(ar.hire_duration)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.total_hire_cost > 0
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "Average Hire Days",
                           (select avg(ar.total_hire_cost)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.total_hire_cost > 0
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "Average Hire Cost",
                           (select count(*)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.penalty_charges_paid > 0
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "No. of cases with Penalties Paid",
                           (select sum(penalty_charges_paid)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.penalty_charges_paid > 0
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "Amount of Penalties Paid",
                           (select count(*)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.hire_leakage = true
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "Number of cases with Hire Leakage",
                           (select sum(hire_leakage_cost)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.hire_leakage = true
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "Amount of Hire Leakage",
                           (select count(*)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.total_repair_cost > 0
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "Repair Volume",
                           (select avg(total_repair_cost)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.total_repair_cost > 0
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "Average Repair Cost",
                           (select count(*)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.repair_cost_exceeds_eng_rec = true
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "No. of cases exceeding Engineers Recommendations",
                           (select count(*)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.within_abp_guidelines = false
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "Number of cases with labour above ABP",
                           (select count(*)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.total_loss = true
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "Total Loss Volume",
                           (select count(*)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.recovery_claimed = true
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "No. of cases with Recovery Charged",
                           (select count(*)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.recovery_claimed_correctly = false
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "No. of cases with Recovery Incorrectly Charged",
                           (select count(*)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.storage_claimed = true
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "No. of cases with Storage Charged",
                           (select count(*)
                            from claim c, claim_audit_review ar
                            where c.audit_review_id = ar.id and ar.storage_claimed_correctly = false
                              and c.insurer_id = insId
                              and ar.claim_audit_review_completed = true
                              and (choIds is null or c.chorganisation_id = ANY(choIds))
                              and (array_length(workgroups, 1) < 1 or (c.workgroup_id is not null and c.workgroup_id = ANY(workgroups)))
                              and ar.audit_completed_date >= weeklyBreakDownDatesRecord.week_start and ar.audit_completed_date <= weeklyBreakDownDatesRecord.week_end
                              and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))) as "No. of cases with Storage Incorrectly Charged";
            END LOOP;
        END LOOP;
    END;

$BODY$
    LANGUAGE plpgsql VOLATILE COST 100;

GRANT EXECUTE ON FUNCTION auditHistoryReport(IN insId integer, IN choIds  integer[], IN worgroupGroupings integer[][], IN worgroupGroupingLabels varchar[], IN numberOfWeeks integer, IN claimTypes integer[]) TO chox_user;
GRANT EXECUTE ON FUNCTION auditHistoryReport(IN insId integer, IN choIds  integer[], IN worgroupGroupings integer[][], IN worgroupGroupingLabels varchar[], IN numberOfWeeks integer, IN claimTypes integer[]) TO chox_mi;

-- select * from auditHistoryReport(6, array[1123], array[array[112,113,118,119,125], array[120,121,122,null,null]]::integer[][], array['Personal','Commercial'], 12, null::integer[]);
select * from auditHistoryReport(6, null, array[array[112,113,118,119,125], array[120,121,122,null,null]]::integer[][], array['Personal','Commercial'], 12, null::integer[]);
