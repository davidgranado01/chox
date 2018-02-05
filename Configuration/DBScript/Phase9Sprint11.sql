--
-- CHOX-483: Copley Question
--
ALTER TABLE insurer ADD COLUMN copley_question boolean NOT NULL DEFAULT false;
ALTER TABLE claim ADD COLUMN copley_offer_made boolean;
ALTER TABLE claim ADD COLUMN copley_offer_made_date timestamp without time zone;

UPDATE claim set copley_offer_made = hmd.copley_offer_made,
                 copley_offer_made_date = hmd.copley_offer_made_date
FROM insurer_hire_monitoring_detail hmd
WHERE claim.insurer_hire_monitoring_detail_id=hmd.id;

ALTER TABLE insurer_hire_monitoring_detail DROP COLUMN copley_offer_made;
ALTER TABLE insurer_hire_monitoring_detail DROP COLUMN copley_offer_made_date;
--
-- End of CHOX-483
--

--
-- CHOX-484: New BRE Rule: Copley Offer Made Check
--
ALTER TABLE bre_band ADD COLUMN copley_offer_made_check boolean NOT NULL DEFAULT false;
--
-- End of CHOX-484
--

--
-- CHOX-481: New Export Role to control access to grid export
--
INSERT INTO web_user_role (name, created_by, last_modified_by, description,type_id, is_workgroup_related, is_ownership_related, version )
    values ('ROLE_CHO_GRIDEXPORT',999, 999, 'Grid Export', 3, false, false, 0);
INSERT INTO web_user_role (name, created_by, last_modified_by, description,type_id, is_workgroup_related, is_ownership_related, version )
    values ('ROLE_INS_GRIDEXPORT',999, 999, 'Grid Export', 2, false, false, 0);
--
-- End of CHOX-481
--

--
-- CHOX-427: Merge and Remove Manual CHOs
--
update claim set cho_reference='110010-duplicate' where id=209179;
update claim set chorganisation_id=1151 where chorganisation_id in (1351, 1342);
update chorganisation set name='Manual Progress Vehicle Management Ltd' where id=1151;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1151, 'ManualProgressVehicleManagementLtd', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1151, 'ManualProgressVehicleManagementLimited', 999, 999, now(), now(), 0;

delete from chorganisation_alias where chorganisation_id in (1351, 1342);
delete from insurer_chorganisation where chorganisation_id in (1351, 1342);
delete from bre_band_organisation where chorganisation_id in (1351, 1342);
delete from chorganisation where id in (1351, 1342);


update claim set cho_reference='AHA117684-duplicate' where id=234177;
update claim set chorganisation_id=1034 where chorganisation_id=1228;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1034, 'ManualACSCarHire', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1034, 'ACSCarHire', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1228;
delete from insurer_chorganisation where chorganisation_id=1228;
delete from bre_band_organisation where chorganisation_id=1228;
delete from chorganisation where id=1228;

update claim set chorganisation_id=1200 where chorganisation_id=1327;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1200, 'ManualAccidentsHappen', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1200, 'AccidentsHappen', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1327;
delete from insurer_chorganisation where chorganisation_id=1327;
delete from bre_band_organisation where chorganisation_id=1327;
delete from chorganisation where id=1327;

update claim set chorganisation_id=1146 where chorganisation_id=1145;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1146, 'ManualAutoLegalProtection', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1146, 'AutoLegalProtection', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1145;
delete from insurer_chorganisation where chorganisation_id=1145;
delete from bre_band_organisation where chorganisation_id=1145;
delete from chorganisation where id=1145;

update claim set chorganisation_id=1210 where chorganisation_id in (1150,1285);
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1210, 'ManualAutoLogisticsSolutions', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1210, 'AutoLogisticsSolutions', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1210, 'ManualAutoLogisticSolutions', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1210, 'AutoLogisticSolutions', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id in (1150,1285);
delete from insurer_chorganisation where chorganisation_id in (1150,1285);
delete from bre_band_organisation where chorganisation_id in (1150,1285);
delete from chorganisation where id in (1150,1285);

update claim set chorganisation_id=1085 where chorganisation_id=1792;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1085, 'ManualBlakewaterSolicitors', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1085, 'BlakewaterSolicitors', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1792;
delete from insurer_chorganisation where chorganisation_id=1792;
delete from bre_band_organisation where chorganisation_id=1792;
delete from chorganisation where id=1792;

update claim set chorganisation_id=1044 where chorganisation_id=1560;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1044, 'ManualCaroleNash', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1044, 'CaroleNash', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1560;
delete from insurer_chorganisation where chorganisation_id=1560;
delete from bre_band_organisation where chorganisation_id=1560;
delete from chorganisation where id=1560;

update claim set cho_reference='01425824-duplicate' where id=158625;
update claim set chorganisation_id=1181 where chorganisation_id=1279;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1181, 'ManualCommercialLegal', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1181, 'CommercialLegal', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1279;
delete from insurer_chorganisation where chorganisation_id=1279;
delete from bre_band_organisation where chorganisation_id=1279;
delete from chorganisation where id=1279;

update claim set chorganisation_id=1401 where chorganisation_id=1402;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1401, 'ManualContacClaimsService', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1401, 'ContacClaimsService', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1402;
delete from insurer_chorganisation where chorganisation_id=1402;
delete from bre_band_organisation where chorganisation_id=1402;
delete from chorganisation where id=1402;

update claim set chorganisation_id=1050 where chorganisation_id in (1216,1171);
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1050, 'ManualCRASH', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1050, 'ManualCRASHCARE', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1050, 'CRASHCARE', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id in (1216,1171);
delete from insurer_chorganisation where chorganisation_id in (1216,1171);
delete from bre_band_organisation where chorganisation_id in (1216,1171);
delete from chorganisation where id in (1216,1171);

update claim set chorganisation_id=1137 where chorganisation_id in (1270,1641);
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1137, 'ManualDirectAccidentManagementLtd', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1137, 'ManualDirectAccident', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1137, 'ManualDirectAccidentManagement', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id in (1270,1641);
delete from insurer_chorganisation where chorganisation_id in (1270,1641);
delete from bre_band_organisation where chorganisation_id in (1270,1641);
delete from chorganisation where id in (1270,1641);

update claim set chorganisation_id=1408 where chorganisation_id=1055;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1408, 'ManualDWAClaims', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1408, 'DWAClaims', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1055;
delete from insurer_chorganisation where chorganisation_id=1055;
delete from bre_band_organisation where chorganisation_id=1055;
delete from chorganisation where id=1055;

update claim set chorganisation_id=1568 where chorganisation_id=1748;
delete from chorganisation_alias where chorganisation_id=1748;
delete from insurer_chorganisation where chorganisation_id=1748;
delete from bre_band_organisation where chorganisation_id=1748;
delete from chorganisation where id=1748;

update claim set chorganisation_id=1058 where chorganisation_id=1747;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1058, 'ManualGemini', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1058, 'Gemini', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1747;
delete from insurer_chorganisation where chorganisation_id=1747;
delete from bre_band_organisation where chorganisation_id=1747;
delete from chorganisation where id=1747;

update claim set chorganisation_id=1096 where chorganisation_id=1377;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1096, 'ManualGlaisyers', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1096, 'Glaisyers', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1377;
delete from insurer_chorganisation where chorganisation_id=1377;
delete from bre_band_organisation where chorganisation_id=1377;
delete from chorganisation where id=1377;

update claim set chorganisation_id=1240 where chorganisation_id=1239;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1240, 'ManualHadrianCoashworks Ltd', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1240, 'HadrianCoashworks', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1239;
delete from insurer_chorganisation where chorganisation_id=1239;
delete from bre_band_organisation where chorganisation_id=1239;
delete from chorganisation where id=1239;

update claim set chorganisation_id=1707 where chorganisation_id=1706;
delete from chorganisation_alias where chorganisation_id=1706;
delete from insurer_chorganisation where chorganisation_id=1706;
delete from bre_band_organisation where chorganisation_id=1706;
delete from chorganisation where id=1706;

update claim set chorganisation_id=1168 where chorganisation_id=1293;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1168, 'ManualIdealCarHireLtd', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1168, 'IdealCarHireLtd', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1293;
delete from insurer_chorganisation where chorganisation_id=1293;
delete from bre_band_organisation where chorganisation_id=1293;
delete from chorganisation where id=1293;

update claim set chorganisation_id=1562 where chorganisation_id=1557;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1562, 'ManualManualRedStarHireLimited', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1562, 'ManualRedStarHireLimited', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1562, 'RedStarHireLimited', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1562, 'ManualRedStarHireLtd', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1562, 'RedStarHireLtd', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1557;
delete from insurer_chorganisation where chorganisation_id=1557;
delete from bre_band_organisation where chorganisation_id=1557;
delete from chorganisation where id=1557;

update claim set chorganisation_id=1787 where chorganisation_id=1463;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1787, 'ManualMCEClaims', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1787, 'MCEClaims', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1463;
delete from insurer_chorganisation where chorganisation_id=1463;
delete from bre_band_organisation where chorganisation_id=1463;
delete from chorganisation where id=1463;

update claim set cho_reference='MCN724-duplicate' where id=171037;
update claim set chorganisation_id=1244 where chorganisation_id=1337;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1244, 'ManualMotorClaimsNetwork', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1244, 'MotorClaimsNetwork', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1337;
delete from insurer_chorganisation where chorganisation_id=1337;
delete from bre_band_organisation where chorganisation_id=1337;
delete from chorganisation where id=1337;

update claim set chorganisation_id=1225 where chorganisation_id in (1278,1565);
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1225, 'ManualMSLLegalExpenses', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1225, 'MSLLegalExpenses', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1225, 'ManualMSL', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1225, 'ManualMSLLegalExpensesLtd', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1225, 'MSLLegalExpensesLtd', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1225, 'ManualMSLLtd', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id in (1278,1565);
delete from insurer_chorganisation where chorganisation_id in (1278,1565);
delete from bre_band_organisation where chorganisation_id in (1278,1565);
delete from chorganisation where id in (1278,1565);

update claim set chorganisation_id=1384 where chorganisation_id=1322;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1384, 'ManualNationwideAssistance', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1384, 'NationwideAssistance', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1322;
delete from insurer_chorganisation where chorganisation_id=1322;
delete from bre_band_organisation where chorganisation_id=1322;
delete from chorganisation where id=1322;

update claim set chorganisation_id=1483 where chorganisation_id=1066;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1483, 'ManualNationwideVehicleHire', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1483, 'NationwideVehicleHire', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1066;
delete from insurer_chorganisation where chorganisation_id=1066;
delete from bre_band_organisation where chorganisation_id=1066;
delete from chorganisation where id=1066;

update claim set chorganisation_id=1165 where chorganisation_id=1280;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1165, 'ManualNortonCar&VanHire', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1165, 'ManualNortonCar&VanHireLtd', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1165, 'NortonCar&VanHire', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1165, 'NortonCar&VanHireLtd', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1280;
delete from insurer_chorganisation where chorganisation_id=1280;
delete from bre_band_organisation where chorganisation_id=1280;
delete from chorganisation where id=1280;

delete from chorganisation_alias where chorganisation_id=1157;
delete from insurer_chorganisation where chorganisation_id=1157;
delete from bre_band_organisation where chorganisation_id=1157;
delete from chorganisation where id=1157;

update claim set cho_reference='263303/SD-duplicate' where id=174021;
update claim set chorganisation_id=1294 where chorganisation_id=1136;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1294, 'ManualPerformanceCarHire', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1294, 'PerformanceCarHire', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1136;
delete from insurer_chorganisation where chorganisation_id=1136;
delete from bre_band_organisation where chorganisation_id=1136;
delete from chorganisation where id=1136;

update claim set chorganisation_id=1559 where chorganisation_id=1128;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1559, 'ManualPlantec', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1559, 'ManualPlantecLtd', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1128;;
delete from insurer_chorganisation where chorganisation_id=1128;;
delete from bre_band_organisation where chorganisation_id=1128;;
update insurer_billing_band_mapping set chorganisation_id=1559 where chorganisation_id=1128;
delete from chorganisation where id=1128;

update claim set chorganisation_id=1201 where chorganisation_id=1427;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1201, 'ManualPlatinumAssist', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1201, 'PlatinumAssist', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1427;
delete from insurer_chorganisation where chorganisation_id=1427;
delete from bre_band_organisation where chorganisation_id=1427;
delete from chorganisation where id=1427;

update claim set chorganisation_id=1068 where chorganisation_id in (1198,1199);
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1068, 'ManualPrestige', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id in (1198,1199);
delete from insurer_chorganisation where chorganisation_id in (1198,1199);
delete from bre_band_organisation where chorganisation_id in (1198,1199);
delete from chorganisation where id in (1198,1199);

update claim set chorganisation_id=1069 where chorganisation_id=1596;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1069, 'ManualRTAAssist', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1069, 'RTAAssist', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1596;
delete from insurer_chorganisation where chorganisation_id=1596;
delete from bre_band_organisation where chorganisation_id=1596;
delete from chorganisation where id=1596;

update claim set chorganisation_id=1142 where chorganisation_id=1195;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1142, 'ManualS&GResponce', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1195;
delete from insurer_chorganisation where chorganisation_id=1195;
delete from bre_band_organisation where chorganisation_id=1195;
delete from chorganisation where id=1195;

update claim set chorganisation_id=1226 where chorganisation_id=1484;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1226, 'ManualTrunammsAssessors', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1484;
delete from insurer_chorganisation where chorganisation_id=1484;
delete from bre_band_organisation where chorganisation_id=1484;
delete from chorganisation where id=1484;

update claim set chorganisation_id=1287 where chorganisation_id=1227;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1287, 'ManualUniqueClaimsSolutions', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1287, 'UniqueClaimsSolutions', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1227;
delete from insurer_chorganisation where chorganisation_id=1227;
delete from bre_band_organisation where chorganisation_id=1227;
delete from chorganisation where id=1227;

update claim set chorganisation_id=1783 where chorganisation_id=1238;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1783, 'ManualUnitownHireLtd', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1783, 'UnitownHireLtd', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1238;
delete from insurer_chorganisation where chorganisation_id=1238;
delete from bre_band_organisation where chorganisation_id=1238;
delete from chorganisation where id=1238;

update claim set chorganisation_id=1081 where chorganisation_id=1385;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1081, 'ManualVision', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1385;
delete from insurer_chorganisation where chorganisation_id=1385;
delete from bre_band_organisation where chorganisation_id=1385;
delete from chorganisation where id=1385;

update claim set chorganisation_id=1603 where chorganisation_id=1605;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1603, 'ManualWrightsAccidentRepair', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1603, 'WrightsAccidentRepair', 999, 999, now(), now(), 0;
delete from chorganisation_alias where chorganisation_id=1605;
delete from insurer_chorganisation where chorganisation_id=1605;
delete from bre_band_organisation where chorganisation_id=1605;
delete from chorganisation where id=1605;

update chorganisation set name='Manual MCE Insurance Ltd' where id=1786;
update chorganisation set name='Manual One Call Accident Management' where id=1339;
update chorganisation set name='Manual Provincewide Car Hire Ltd' where id=1777;
update chorganisation set name='Manual Southport Superbikes' where id=1185;
update chorganisation set name='Manual Stephen Chambers Solicitors' where id=1771;
update chorganisation set name='Manual WPS Insurance Brokers Ltd' where id=1539;

insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1786, 'MCEInsuranceLtd', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1339, 'OneCallAccidentManagement', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1777, 'ProvincewideCarHireLtd', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1185, 'SouthportSuperbikes', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1771, 'StephenChambersSolicitors', 999, 999, now(), now(), 0;
insert into chorganisation_alias(chorganisation_id, alias_name, created_by, last_modified_by, last_modified_date, created_date, version)
    select 1539, 'WPSInsuranceBrokers Ltd', 999, 999, now(), now(), 0;

--
-- End of CHOX-427
--
