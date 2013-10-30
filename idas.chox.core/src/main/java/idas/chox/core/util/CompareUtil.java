package idas.chox.core.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import idas.chox.core.model.VehicleClass;

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
                        if (oldObj != null && newObj != null && !compareObect(oldObj, newObj)) {

                            // if date object is modified then format it and add it to the map.
                            if (oldObj instanceof Date && newObj instanceof Date) {
                                if (field.getName().equalsIgnoreCase("rentalStart") || field.getName().equalsIgnoreCase("rentalEnd")) {
                                    ret.put((String) fieldNames.get(field.getName()), new Object[]{DateHelper.getLocalDateTimeFormat().format((Date) oldObj),
                                        DateHelper.getLocalDateTimeFormat().format((Date) newObj)});
                                } else {
                                    ret.put((String) fieldNames.get(field.getName()), new Object[]{DateHelper.getLocalDateFormat().format((Date) oldObj),
                                        DateHelper.getLocalDateFormat().format((Date) newObj)});
                                }
                                continue;
                            }

                            // if string object is modified then add custom name for empty string. 
                            if (oldObj instanceof String && newObj instanceof String) {
                                ret.put((String) fieldNames.get(field.getName()), new Object[]{((String) oldObj).trim().isEmpty() ? "No Value" : oldObj,
                                    ((String) newObj).trim().isEmpty() ? "Entry Removed" : newObj});
                                continue;
                            }

                            // if hire vehicle vehicleClass is modified then add the vehicleClass Name(not VehicleClass object).
                            if (field.getName().equalsIgnoreCase("vehicleClass") && oldObj instanceof VehicleClass && newObj instanceof VehicleClass) {
                                VehicleClass vc1 = (VehicleClass) oldObj;
                                VehicleClass vc2 = (VehicleClass) newObj;
                                if (!vc1.getName().equals(vc2.getName())) {
                                    ret.put((String) fieldNames.get(field.getName()), new Object[]{vc1.getName(), vc2.getName()});
                                }
                                continue;
                            }
                            
                            if (oldObj instanceof Boolean && newObj instanceof Boolean) {
                                ret.put((String) fieldNames.get(field.getName()), new Object[]{((Boolean) oldObj) ? "Yes" : "No",
                                    ((Boolean) newObj) ? "Yes" : "No"});
                                continue;
                            }
                            
                            // for all other modified objects
                            ret.put((String) fieldNames.get(field.getName()), new Object[]{oldObj, newObj});

                            /* 
                             * if one of the obj is null then the other obj value should not be in one of ( (if it is number)zero,
                             * (if it is boolean)false, (if it is string)empty).
                             */
                        } else if ((newObj != null && oldObj == null && !compareObjValueEqulentToDefaultValue(newObj))
                                || (newObj == null && oldObj != null && !compareObjValueEqulentToDefaultValue(oldObj))) {
                            // if Boolean then replace null with No. 
                            if (newObj != null && newObj instanceof Boolean) {
                                ret.put((String) fieldNames.get(field.getName()), new Object[]{"No", ((Boolean) newObj) ? "Yes" : "No"});
                                continue;
                            } else if (oldObj != null && oldObj instanceof Boolean) {
                                ret.put((String) fieldNames.get(field.getName()), new Object[]{((Boolean) oldObj) ? "Yes" : "No", "No"});
                                continue;
                            }
                            // if Number then replace null with Zero.
                            if (newObj != null && newObj instanceof Number) {
                                ret.put((String) fieldNames.get(field.getName()), new Object[]{BigDecimal.ZERO, newObj});
                                continue;
                            } else if (oldObj != null && oldObj instanceof Number) {
                                ret.put((String) fieldNames.get(field.getName()), new Object[]{oldObj, BigDecimal.ZERO});
                                continue;
                            }
                            // if String then replace null with custom string.
                            if (newObj != null && newObj instanceof String) {
                                ret.put((String) fieldNames.get(field.getName()), new Object[]{"No Value", newObj});
                                continue;
                            } else if (oldObj != null && oldObj instanceof String) {
                                ret.put((String) fieldNames.get(field.getName()), new Object[]{oldObj, "No Value"});
                                continue;
                            }
                            // for all other objects add as it is.
                            ret.put((String) fieldNames.get(field.getName()), new Object[]{oldObj, newObj});
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception : ", ex);
        }
        return ret;
    }

    public static boolean compareNumber(final Number x, final Number y) {
        return (toBigDecimal(x).compareTo(toBigDecimal(y))) == 0 ? true : false;
    }

    public static BigDecimal toBigDecimal(final Number number) {
        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        }
        if (number instanceof BigInteger) {
            return new BigDecimal((BigInteger) number);
        }
        if (number instanceof Byte || number instanceof Short
                || number instanceof Integer || number instanceof Long) {
            return new BigDecimal(number.longValue());
        }
        if (number instanceof Float || number instanceof Double) {
            return new BigDecimal(number.doubleValue());
        }

        try {
            return new BigDecimal(number.toString());
        } catch (final Exception ex) {
            LOG.error("Exception : ", ex);
            return BigDecimal.ZERO;
        }
    }

    public static boolean compareObect(final Object oldObj, final Object newObj) {
        if (oldObj instanceof Number && newObj instanceof Number) {
            return compareNumber((Number) oldObj, (Number) newObj);
        } else if (oldObj instanceof Date && newObj instanceof Date) {
            return ((Date) oldObj).compareTo((Date) newObj) == 0;
        } else if (oldObj instanceof String && newObj instanceof String) {
            return ((String) oldObj).trim().equals(((String) newObj).trim());
        } else {
            return oldObj.equals(newObj);
        }
    }

    public static boolean compareObjValueEqulentToDefaultValue(final Object obj) {
        if (obj instanceof Boolean && obj.equals(false)) {
            return true;
        } else if (obj instanceof Number && compareNumber((Number) obj, (Number) 0)) {
            return true;
        } else if (obj instanceof String && ((String) obj).trim().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}