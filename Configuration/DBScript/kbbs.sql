--
-- SQL Updates to support KBBS dashboards
--
ALTER TABLE chorganisation ADD COLUMN enable_kbbs_dashboard boolean not null default false;
ALTER TABLE chorganisation ADD COLUMN kbbs_manager_password character varying;
ALTER TABLE chorganisation ADD COLUMN kbbs_operative_password character varying;

ALTER TABLE insurer ADD COLUMN enable_kbbs_dashboard boolean not null default false;
ALTER TABLE insurer ADD COLUMN kbbs_manager_password character varying;
ALTER TABLE insurer ADD COLUMN kbbs_operative_password character varying;

--
-- Manager Passwords KBBS dashboards
--
-- UserId	Insurer	    		Role		Pass
-- IMAN.3	RSA			Manager		Mp]9JnY
-- IMAN.6	Direct Line Group	Manager		]^Y#H{F
-- IMAN.18	Hastings Direct		Manager		[$4G17j
-- IMAN.19	Motability		Manager		Spc2@tO
-- IMAN.20	ERS Claims Limited	Manager		z0(7ql6
-- IMAN.22	Quote Me Happy		Manager		{eF&WL>
-- IMAN.23	esure			Manager		*+RJ{DC
-- IMAN.24	Octagon			Manager		eoj>u8l
-- IMAN.25	QBE			Manager		VA)p&hs
-- IMAN.26	LV=			Manager		$N9=8J}

-- beta passwords
--update insurer set kbbs_manager_password = 'Mp]9JnY', enable_kbbs_dashboard = true where id=3;
--update insurer set kbbs_manager_password=']^Y#H{F', enable_kbbs_dashboard = true where id=6;
--update insurer set kbbs_manager_password='[$4G17j', enable_kbbs_dashboard = true where id=18;
--update insurer set kbbs_manager_password='Spc2@tO', enable_kbbs_dashboard = true where id=19;
--update insurer set kbbs_manager_password='z0(7ql6', enable_kbbs_dashboard = true where id=20;
--update insurer set kbbs_manager_password='{eF&WL>', enable_kbbs_dashboard = true where id=22;
--update insurer set kbbs_manager_password='*+RJ{DC', enable_kbbs_dashboard = true where id=23;
--update insurer set kbbs_manager_password='eoj>u8l', enable_kbbs_dashboard = true where id=24;
--update insurer set kbbs_manager_password='VA)p&hs', enable_kbbs_dashboard = true where id=25;
--update insurer set kbbs_manager_password='$N9=8J}', enable_kbbs_dashboard = true where id=26;

-- production passwords
--UserId      Insurer         Role        Pass BETA   Pass Live
--IMAN.3      RSA             Manager Mp]9JnY     Y3jc[DX
--IMAN.6      Direct Line Group   Manager ]^Y#H{F     t3hvL[T
--IMAN.18     Hastings Direct     Manager [$4G17j     U]!N1GG
--IMAN.19     Motability          Manager Spc2@tO     CKL_f]v
--IMAN.20     ERS Claims Limited  Manager z0(7ql6     saTD=uN
--IMAN.22     Quote Me Happy  Manager {eF&WL>     8Xb(oIx
--IMAN.23     esure           Manager *+RJ{DC     wK[A&qc
--IMAN.24     Octagon         Manager eoj>u8l     36qG((8
--IMAN.25     QBE             Manager VA)p&hs     rR)GPs3
--IMAN.26     LV=             Manager $N9=8J}     2R$z1DD

update insurer set kbbs_manager_password = 'Y3jc[DX', enable_kbbs_dashboard = true where id=3;
update insurer set kbbs_manager_password='t3hvL[T', enable_kbbs_dashboard = true where id=6;
update insurer set kbbs_manager_password='U]!N1GG', enable_kbbs_dashboard = true where id=18;
update insurer set kbbs_manager_password='CKL_f]v', enable_kbbs_dashboard = true where id=19;
update insurer set kbbs_manager_password='saTD=uN', enable_kbbs_dashboard = true where id=20;
update insurer set kbbs_manager_password='8Xb(oIx', enable_kbbs_dashboard = true where id=22;
update insurer set kbbs_manager_password='wK[A&qc', enable_kbbs_dashboard = true where id=23;
update insurer set kbbs_manager_password='36qG((8', enable_kbbs_dashboard = true where id=24;
update insurer set kbbs_manager_password='rR)GPs3', enable_kbbs_dashboard = true where id=25;
update insurer set kbbs_manager_password='2R$z1DD', enable_kbbs_dashboard = true where id=26;
