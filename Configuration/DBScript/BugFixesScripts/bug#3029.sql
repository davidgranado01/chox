--------------------------------------------------------------------------------
-- bug#3029 - Production - Daily Rates for LV update
--------------------------------------------------------------------------------
update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 38.95
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'MA';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 38.95
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M1A';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 38.95
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M2A';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 54.20
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M3A';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 54.20
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M4A';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 54.20
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M5A';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 54.20
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M6A';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 21.00
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S1A';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 22.79
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S2A';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 25.88
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S3A';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 27.94
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S4A';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 31.03
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S5A';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 33.09
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S6A';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 38.95
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'MEST';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 38.95
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M1EST';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 38.95
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M2EST';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 54.20
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M3EST';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 54.20
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M4EST';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 54.20
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M5EST';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 54.20
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M6EST';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 21.00
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S1EST';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 22.79
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S2EST';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 25.88
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S3EST';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 27.94
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S4EST';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 31.03
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S5EST';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 33.09
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S6EST';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 38.95
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'MESTA';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 38.95
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M1ESTA';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 38.95
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M2ESTA';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 54.20
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M3ESTA';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 54.20
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M4ESTA';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 54.20
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M5ESTA';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 54.20
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'M6ESTA';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 21.00
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S1ESTA';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 22.79
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S2ESTA';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 25.88
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S3ESTA';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 30.00
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S4ESTA';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 33.09
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S5ESTA';

update vehicle_class_price_special_rate
    set version = vehicle_class_price_special_rate.version + 1, price = 33.15
from vehicle_class vc
where vehicle_class_price_special_rate.insurer_id = 26
  and vehicle_class_price_special_rate.chorganisation_id = 1007
  and vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and vc.name = 'S6ESTA';

----------------------
-- End of bug#3029
----------------------