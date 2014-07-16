-----------------------------------------------------------------------
--                                                                   --
-- bug#2849 - Production - Subscriber Rate & Macro Mapping Update    --
--                                                                   --
-----------------------------------------------------------------------
update vehicle_class_price_special_rate
    set price = 92.68
from vehicle_class vc
where vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and name like 'F6%'
  and chorganisation_id = 1007
  and insurer_id in (3,19)
  and start_date = '2013-12-01';

update vehicle_class_price_special_rate
    set price = 61.78
from vehicle_class vc
where vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and name = 'P3'
  and chorganisation_id = 1007
  and insurer_id in (3,19)
  and start_date = '2013-12-01';

update vehicle_class_price_special_rate
    set price = 66.93
from vehicle_class vc
where vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and (name = 'P3A' or name='P3EST' or name='P3ESTA')
  and chorganisation_id = 1007
  and insurer_id in (3,19)
  and start_date = '2013-12-01';

update vehicle_class_price_special_rate
    set price = 72.08
from vehicle_class vc
where vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and name = 'P6'
  and chorganisation_id = 1007
  and insurer_id in (3,19)
  and start_date = '2013-12-01';

update vehicle_class_price_special_rate
    set price = 77.23
from vehicle_class vc
where vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and (name = 'P6A' or name='P6EST' or name='P6ESTA')
  and chorganisation_id = 1007
  and insurer_id in (3,19)
  and start_date = '2013-12-01';

update vehicle_class_price_special_rate
    set price = 102.98
from vehicle_class vc
where vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and name like 'SP8%'
  and chorganisation_id = 1007
  and insurer_id in (3,19)
  and start_date = '2013-12-01';

update vehicle_class_price_special_rate
    set price = 47.36
from vehicle_class vc
where vehicle_class_price_special_rate.vehicle_class_id = vc.id
  and name like 'CP%%'
  and chorganisation_id = 1007
  and insurer_id in (3,19)
  and start_date = '2013-12-01';




    -----------------------------------------------------------------------
    -- End of bug#2849                                                   --
    -----------------------------------------------------------------------
