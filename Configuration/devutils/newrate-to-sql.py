filepath = 'ERACRates20200109.csv'
insurers = []
vclasses = []
vc_col = 1
offset = 2 #index of first insurer column


with open(filepath) as fp:
   line = fp.readline() #
   header_date = line.split(',')

   line = fp.readline() #skip header
   header_ins = line.split(',')

   for x in range (offset, len(header_date)) :
       insurers.append ( { 'name' : header_ins[x].strip(), 'label' : header_ins[x].strip().lower().replace(" ",""), 'date' : header_date[x].strip(), 'rates': []} )

   line = fp.readline()
   while line:
       cols = line.split(',');
       vclasses.append(cols);       
       line = fp.readline()
       # not needed
       #for x in range (len(insurers)) :
       #    insurers[x]['rates'].append(cols [x+offset])
fp.close()       

#Create the SQL script

print """
-- VUL-3117 (CHOX): adding new set of ERAC special rates for RSA, Motability, tesco and ers"
-- version is set to 0, which is an arbitrary new value
-- age is set up to 99.00 arbitrarily
-- created_by and last_modified_by are set up to 999
-- created_date and last_modified_date are set up to now
-- Expected organisations ids:
--    erac (cho) id  = 1007
--    rsa id = 3
--    motability id = 19 
--    tescouw id = 27
--    ers id = 20

"""
print "-- Number of new entries expected for each of the {} insurer : {}".format (len(insurers), len(vclasses));

print """

DO $$
DECLARE var_ver integer := 0;
DECLARE var_erac integer;  --1007
"""

for x in range (len(insurers)) :
    print "DECLARE var_date_{ins} date := '{dt}';".format(ins=insurers[x]['label'],dt=insurers[x]['date']) 
    print "DECLARE var_{} integer;".format(insurers[x]['label'])  

print ""

for cols in vclasses :
       print "DECLARE var_{} integer;".format(cols[vc_col]);

print """

BEGIN
SELECT id from chorganisation where name = 'Enterprise Rent-A-Car UK Ltd' INTO var_erac;

"""
for x in range (len(insurers)) :
    print "SELECT id from insurer where name like '{}%' INTO var_{};".format(insurers[x]['name'], insurers[x]['label'])
print ""

for cols in vclasses :
    print "SELECT id from vehicle_class where name = '{vc}' INTO var_{vc};".format(vc=cols[vc_col]);
print ""

for cols in vclasses:
    for x in range (len(insurers)) :
        print "INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )"
        print "SELECT var_ver, var_{vc}, var_{ins}, var_erac, {val}, var_date_{ins}, 999, now(), 999, now(), 99.00".format(vc = cols[vc_col].strip(), ins = insurers[x]['label'], val = cols[x+offset].strip())
        print "WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_{vc} AND vehicle_class_price_special_rate.insurer_id = var_{ins} AND vehicle_class_price_special_rate.chorganisation_id = var_erac AND vehicle_class_price_special_rate.start_date  >= var_date_{ins});".format(vc = cols[1].strip(), ins = insurers[x]['label'])                  
        print ""

print """
END $$;
"""











       


