package idas.chox.core.util;

import idas.chox.core.model.Invoice;
import idas.chox.core.model.VehicleClass;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompareUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DateHelper.class);

    /*
     * Get Fields
     */
    private static Field[] getFields(Class<?> clazz) {
        // If class is null then return null
        if (clazz == null) {
            return null;
        }

        // Get all field of class object
        Field[] fields = clazz.getDeclaredFields();

        // Set accessible for fields
        for (Field field : fields) {
            field.setAccessible(true);
        }

        return fields;
    }

    /*
     * Compare the property differences of 2 object with type is clazz.
     */
    public static Map<String, Object[]> compare(Class<?> clazz, Object originalObject, Object modifiedObject, Map fieldNames) {
        Map<String, Object[]> ret = new HashMap<String, Object[]>();
        try {
            // Get fields
            Field[] fields = getFields(clazz);
            if (fields != null) {
                for (Field field : fields) {

                    if (fieldNames.containsKey(field.getName())) {

                        // Compare
                        Object oldObj = field.get(originalObject);
                        Object newObj = field.get(modifiedObject);
                        if (oldObj != null && newObj != null && !oldObj.equals(newObj)) {
                            // if hire vehicle vehicleClass is modified then add the vehicleClass Name(not VehicleClass object).
                            if (field.getName().equalsIgnoreCase("vehicleClass") && oldObj instanceof VehicleClass && newObj instanceof VehicleClass) {
                                VehicleClass vc1 = (VehicleClass) oldObj;
                                VehicleClass vc2 = (VehicleClass) newObj;
                                if (!vc1.getName().equals(vc2.getName())) {
                                    ret.put((String)fieldNames.get(field.getName()), new Object[]{vc1.getName(), vc2.getName()});
                                }
                                continue;
                            }
                            // if date object is modified then format it and add it to the map.
                            if (oldObj instanceof Date && newObj instanceof Date) {
                                if (DateHelper.getNumberOf24HourPeriodsBetween((Date) oldObj, (Date) newObj) > 0) {
                                    if (field.getName().equalsIgnoreCase("rentalStart") || field.getName().equalsIgnoreCase("rentalEnd")) {
                                        ret.put((String) fieldNames.get(field.getName()), new Object[]{DateHelper.getLocalDateTimeFormat().format((Date) oldObj),
                                            DateHelper.getLocalDateTimeFormat().format((Date) newObj)});
                                    } else {
                                        ret.put((String) fieldNames.get(field.getName()), new Object[]{DateHelper.getLocalDateFormat().format((Date) oldObj),
                                            DateHelper.getLocalDateFormat().format((Date) newObj)});
                                    }
                                }
                                continue;
                            }
                            // if string object is modified then add custom name for empty string. 
                            if (oldObj instanceof String && newObj instanceof String) {
                                if (!((String) oldObj).trim().equals(((String) newObj).trim())) {
                                    ret.put((String)fieldNames.get(field.getName()), new Object[]{((String) oldObj).trim().isEmpty() ? "Empty" : oldObj,
                                        ((String) newObj).trim().isEmpty() ? "Entry Removed" : newObj});
                                }
                                continue;
                            }
                            // for all other modified objects
                            ret.put((String)fieldNames.get(field.getName()), new Object[]{oldObj, newObj});
                            // check for the null values.
                        } else if (((oldObj != null && ((oldObj instanceof Boolean && oldObj.equals(true)) || (oldObj instanceof Number && !oldObj.equals(0)) || (oldObj instanceof String && !((String) oldObj).trim().isEmpty()) || (!(oldObj instanceof Boolean) && !(oldObj instanceof Number) && !(oldObj instanceof String)))) && newObj == null)
                                || ((newObj != null && ((newObj instanceof Boolean && newObj.equals(true)) || (newObj instanceof Number && !newObj.equals(0)) || (newObj instanceof String && !((String) newObj).trim().isEmpty()) || (!(newObj instanceof Boolean) && !(newObj instanceof Number) && !(newObj instanceof String)))) && oldObj == null)) {
                            // if Boolean then replace null with false.
                            if (newObj != null && newObj instanceof Boolean) {
                                ret.put((String)fieldNames.get(field.getName()), new Object[]{Boolean.FALSE, newObj});
                                continue;
                            } else if (oldObj != null && oldObj instanceof Boolean) {
                                ret.put((String)fieldNames.get(field.getName()), new Object[]{oldObj, Boolean.FALSE});
                                continue;
                            }
                            // if Number then replace null with Zero.
                            if (newObj != null && newObj instanceof Number) {
                                ret.put((String)fieldNames.get(field.getName()), new Object[]{BigDecimal.ZERO, newObj});
                                continue;
                            } else if (oldObj != null && oldObj instanceof Number) {
                                ret.put((String)fieldNames.get(field.getName()), new Object[]{oldObj, BigDecimal.ZERO});
                                continue;
                            }
                            // if String then replace null with custom string.
                            if (newObj != null && newObj instanceof String) {
                                ret.put((String)fieldNames.get(field.getName()), new Object[]{"Empty", newObj});
                                continue;
                            } else if (oldObj != null && oldObj instanceof String) {
                                ret.put((String)fieldNames.get(field.getName()), new Object[]{oldObj, "Empty"});
                                continue;
                            }
                            // for all other objects add as it is.
                            ret.put((String)fieldNames.get(field.getName()), new Object[]{oldObj, newObj});
                        }
                    }
                }
                fieldNames.clear(); // clear the values in the map to avoid duplicate key being entered by another entity.
            }
        } catch (Exception ex) {
            LOG.error("Exception : ", ex);
        }
        return ret;
    }
}