package chox.data;

import com.filesystemsoftware.utils.Cache;
import com.filesystemsoftware.utils.CacheMissException;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import com.filesystemsoftware.utils.StringEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

public class Claim {

    private long ID = -1;
    private String uuid = StringEncoder.getRandomString(64);
    private long rentalID = -1;
    private long insurerCountryID = -1;
    private String policyNumber;
    private String claimReference;
    private String comprehensive = "y";
    private String policyHolderName;
    private String vehicleRegistration;
    private String vehicleManufacturer;
    private String vehicleModel;
    private long vehicleClassID = -1;
    private String usable;
    private String damageDescription;
    private long tpInsurerCountryID = -1;
    private String tpPolicyNumber;
    private String tpClaimReference;
    private String tpVehicleRegistration;
    private String tpVehicleManufacturer;
    private String tpVehicleModel;
    private long tpVehicleClassID = -1;
    private String tpName;
    private String tpAddress1;
    private String tpAddress2;
    private String tpAddress3;
    private String tpAddress4;
    private String tpAddress5;
    private String tpPostcode;
    private String tpTelephoneDay;
    private String tpTelephoneEvening;
    private String tpEmail;
    private Timestamp incidentDate;
    private String location;
    private String policeInvolved = "n";
    private String incidentDescription;
    private String vehicleLocation;
    private long proposedRentalClassID=-1;
    private String claimStatus = "Awaiting Authorization";

    public static Claim getForRental(DBConnectionWrapper connection, Rental rental) throws SQLException {
        PreparedStatement s = connection.prepareStatement("select id from claim where rental_id=?");
        s.setLong(1, rental.getID());
        ResultSet rs = s.executeQuery();
        Claim c = null;
        if (rs.next()) {
            c = instantiate(connection, rs.getLong(1));
        }
        rs.close();
        s.close();

        return c;
    }

    public static Claim instantiate(DBConnectionWrapper connection, long id) throws SQLException {
        String cacheKey = "chox.data.Claim.instantiate";
        try {
            return (Claim) Cache.getInstance().get(cacheKey, id);
        } catch (CacheMissException e) {
            PreparedStatement s = connection.prepareStatement("select uuid,rental_id,insurer_country_id,policy_number,claim_reference,comprehensive,policy_holder_name,vehicle_registration,vehicle_manufacturer,vehicle_model,vehicle_class_id,usable,damage_description,tp_insurer_country_id,tp_policy_number,tp_claim_reference,tp_vehicle_registration,tp_vehicle_manufacturer,tp_vehicle_model,tp_vehicle_class_id,tp_name,tp_address1,tp_address2,tp_address3,tp_address4,tp_address5,tp_postcode,tp_telephone_day,tp_telephone_evening,tp_email,incident_date,location,police_involved,incident_description,claim_status,vehicle_location,proposed_rental_class_id from claim where id=?");
            s.setLong(1, id);
            Claim o = null;
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                o = new Claim();
                o.setUuid(rs.getString(1));

                o.setRentalID(rs.getLong(2));
                o.setInsurerCountryID(rs.getLong(3));
                if (rs.wasNull()) {
                    o.setInsurerCountryID(-1);
                }
                o.setPolicyNumber(rs.getString(4));
                if (rs.wasNull()) {
                    o.setPolicyNumber(null);
                }
                o.setClaimReference(rs.getString(5));
                if (rs.wasNull()) {
                    o.setClaimReference(null);
                }
                o.setComprehensive(rs.getString(6));
                if (rs.wasNull()) {
                    o.setComprehensive(null);
                }
                o.setPolicyHolderName(rs.getString(7));

                o.setVehicleRegistration(rs.getString(8));
                o.setVehicleManufacturer(rs.getString(9));
                o.setVehicleModel(rs.getString(10));
                o.setVehicleClassID(rs.getLong(11));

                o.setUsable(rs.getString(12));

                o.setDamageDescription(rs.getString(13));
                if (rs.wasNull()) {
                    o.setDamageDescription(null);
                }
                o.setTpInsurerCountryID(rs.getLong(14));
                o.setTpPolicyNumber(rs.getString(15));
                if (rs.wasNull()) {
                    o.setTpPolicyNumber(null);
                }
                o.setTpClaimReference(rs.getString(16));
                if (rs.wasNull()) {
                    o.setTpClaimReference(null);
                }
                o.setTpVehicleRegistration(rs.getString(17));
                if (rs.wasNull()) {
                    o.setTpVehicleRegistration(null);
                }
                o.setTpVehicleManufacturer(rs.getString(18));
                if (rs.wasNull()) {
                    o.setTpVehicleManufacturer(null);
                }
                o.setTpVehicleModel(rs.getString(19));
                if (rs.wasNull()) {
                    o.setTpVehicleModel(null);
                }
                o.setTpVehicleClassID(rs.getLong(20));
                if (rs.wasNull()) {
                    o.setTpVehicleClassID(-1);
                }
                o.setTpName(rs.getString(21));
                if (rs.wasNull()) {
                    o.setTpName(null);
                }
                o.setTpAddress1(rs.getString(22));
                if (rs.wasNull()) {
                    o.setTpAddress1(null);
                }
                o.setTpAddress2(rs.getString(23));
                if (rs.wasNull()) {
                    o.setTpAddress2(null);
                }
                o.setTpAddress3(rs.getString(24));
                if (rs.wasNull()) {
                    o.setTpAddress3(null);
                }
                o.setTpAddress4(rs.getString(25));
                if (rs.wasNull()) {
                    o.setTpAddress4(null);
                }
                o.setTpAddress5(rs.getString(26));
                if (rs.wasNull()) {
                    o.setTpAddress5(null);
                }
                o.setTpPostcode(rs.getString(27));
                if (rs.wasNull()) {
                    o.setTpPostcode(null);
                }
                o.setTpTelephoneDay(rs.getString(28));
                if (rs.wasNull()) {
                    o.setTpTelephoneDay(null);
                }
                o.setTpTelephoneEvening(rs.getString(29));
                if (rs.wasNull()) {
                    o.setTpTelephoneEvening(null);
                }
                o.setTpEmail(rs.getString(30));
                if (rs.wasNull()) {
                    o.setTpEmail(null);
                }
                o.setIncidentDate(rs.getTimestamp(31));

                o.setLocation(rs.getString(32));
                if (rs.wasNull()) {
                    o.setLocation(null);
                }
                o.setPoliceInvolved(rs.getString(33));
                o.setIncidentDescription(rs.getString(34));
                if (rs.wasNull()) {
                    o.setIncidentDescription(null);
                }
                o.setClaimStatus(rs.getString(35));
                o.setVehicleLocation(rs.getString(36));
                if(rs.wasNull())
                    o.setVehicleLocation(null);
                o.setProposedRentalClassID(rs.getLong(37));
                o.ID = id;
            }
            rs.close();
            s.close();
            Cache.getInstance().put(cacheKey, id, o);
            return o;
        }
    }

    public void save(DBConnectionWrapper connection) throws SQLException {
        if (getID() == -1) {
            PreparedStatement s = connection.prepareStatement("insert into claim(uuid,rental_id,insurer_country_id,policy_number,claim_reference,comprehensive,policy_holder_name,vehicle_registration,vehicle_manufacturer,vehicle_model,vehicle_class_id,usable,damage_description,tp_insurer_country_id,tp_policy_number,tp_claim_reference,tp_vehicle_registration,tp_vehicle_manufacturer,tp_vehicle_model,tp_vehicle_class_id,tp_name,tp_address1,tp_address2,tp_address3,tp_address4,tp_address5,tp_postcode,tp_telephone_day,tp_telephone_evening,tp_email,incident_date,location,police_involved,incident_description,claim_status,vehicle_location,proposed_rental_class_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) returning id");
            s.setString(1, getUuid());

            s.setLong(2, getRentalID());
            if (getInsurerCountryID() == -1) {
                s.setNull(3, Types.INTEGER);
            } else {
                s.setLong(3, getInsurerCountryID());
            }
            if (getPolicyNumber() == null) {
                s.setNull(4, Types.VARCHAR);
            } else {
                s.setString(4, getPolicyNumber());
            }
            if (getClaimReference() == null) {
                s.setNull(5, Types.VARCHAR);
            } else {
                s.setString(5, getClaimReference());
            }
            if (getComprehensive() == null) {
                s.setNull(6, Types.VARCHAR);
            } else {
                s.setString(6, getComprehensive());
            }
            s.setString(7, getPolicyHolderName());
            s.setString(8, getVehicleRegistration());
            s.setString(9, getVehicleManufacturer());
            s.setString(10, getVehicleModel());
            s.setLong(11, getVehicleClassID());

            s.setString(12, getUsable());

            if (getDamageDescription() == null) {
                s.setNull(13, Types.VARCHAR);
            } else {
                s.setString(13, getDamageDescription());
            }
            s.setLong(14, getTpInsurerCountryID());

            if (getTpPolicyNumber() == null) {
                s.setNull(15, Types.VARCHAR);
            } else {
                s.setString(15, getTpPolicyNumber());
            }
            if (getTpClaimReference() == null) {
                s.setNull(16, Types.VARCHAR);
            } else {
                s.setString(16, getTpClaimReference());
            }
            if (getTpVehicleRegistration() == null) {
                s.setNull(17, Types.VARCHAR);
            } else {
                s.setString(17, getTpVehicleRegistration());
            }
            if (getTpVehicleManufacturer() == null) {
                s.setNull(18, Types.VARCHAR);
            } else {
                s.setString(18, getTpVehicleManufacturer());
            }
            if (getTpVehicleModel() == null) {
                s.setNull(19, Types.VARCHAR);
            } else {
                s.setString(19, getTpVehicleModel());
            }
            if (getTpVehicleClassID() == -1) {
                s.setNull(20, Types.INTEGER);
            } else {
                s.setLong(20, getTpVehicleClassID());
            }
            if (getTpName() == null) {
                s.setNull(21, Types.VARCHAR);
            } else {
                s.setString(21, getTpName());
            }
            if (getTpAddress1() == null) {
                s.setNull(22, Types.VARCHAR);
            } else {
                s.setString(22, getTpAddress1());
            }
            if (getTpAddress2() == null) {
                s.setNull(23, Types.VARCHAR);
            } else {
                s.setString(23, getTpAddress2());
            }
            if (getTpAddress3() == null) {
                s.setNull(24, Types.VARCHAR);
            } else {
                s.setString(24, getTpAddress3());
            }
            if (getTpAddress4() == null) {
                s.setNull(25, Types.VARCHAR);
            } else {
                s.setString(25, getTpAddress4());
            }
            if (getTpAddress5() == null) {
                s.setNull(26, Types.VARCHAR);
            } else {
                s.setString(26, getTpAddress5());
            }
            if (getTpPostcode() == null) {
                s.setNull(27, Types.VARCHAR);
            } else {
                s.setString(27, getTpPostcode());
            }
            if (getTpTelephoneDay() == null) {
                s.setNull(28, Types.VARCHAR);
            } else {
                s.setString(28, getTpTelephoneDay());
            }
            if (getTpTelephoneEvening() == null) {
                s.setNull(29, Types.VARCHAR);
            } else {
                s.setString(29, getTpTelephoneDay());
            }
            if (getTpEmail() == null) {
                s.setNull(30, Types.VARCHAR);
            } else {
                s.setString(30, getTpEmail());
            }
            s.setTimestamp(31, getIncidentDate());

            if (getLocation() == null) {
                s.setNull(32, Types.VARCHAR);
            } else {
                s.setString(32, getLocation());
            }
            s.setString(33, getPoliceInvolved());
            if (getIncidentDescription() == null) {
                s.setNull(34, Types.VARCHAR);
            } else {
                s.setString(34, getIncidentDescription());
            }
            s.setString(35, getClaimStatus());
            if(getVehicleLocation()==null)
                s.setNull(36, Types.VARCHAR);
            else
                s.setString(36,getVehicleLocation());
            if(getProposedRentalClassID()==-1)
                s.setNull(37,Types.INTEGER);
            else
                s.setLong(37,getProposedRentalClassID());
            ResultSet rs = s.executeQuery();
            rs.next();
            ID = rs.getLong(1);
            rs.close();
            s.close();
        } else {
            PreparedStatement s = connection.prepareStatement("update claim set uuid=?,rental_id=?,insurer_country_id=?,policy_number=?,claim_reference=?,comprehensive=?,policy_holder_name=?,vehicle_registration=?,vehicle_manufacturer=?,vehicle_model=?,vehicle_class_id=?,usable=?,damage_description=?,tp_insurer_country_id=?,tp_policy_number=?,tp_claim_reference=?,tp_vehicle_registration=?,tp_vehicle_manufacturer=?,tp_vehicle_model=?,tp_vehicle_class_id=?,tp_name=?,tp_address1=?,tp_address2=?,tp_address3=?,tp_address4=?,tp_address5=?,tp_postcode=?,tp_telephone_day=?,tp_telephone_evening=?,tp_email=?,incident_date=?,location=?,police_involved=?,incident_description=?,claim_status=?,vehicle_location=?,proposed_rental_class_id=? where id=?");
            s.setString(1, getUuid());

            s.setLong(2, getRentalID());
            if (getInsurerCountryID() == -1) {
                s.setNull(3, Types.INTEGER);
            } else {
                s.setLong(3, getInsurerCountryID());
            }
            if (getPolicyNumber() == null) {
                s.setNull(4, Types.VARCHAR);
            } else {
                s.setString(4, getPolicyNumber());
            }
            if (getClaimReference() == null) {
                s.setNull(5, Types.VARCHAR);
            } else {
                s.setString(5, getClaimReference());
            }
            if (getComprehensive() == null) {
                s.setNull(6, Types.VARCHAR);
            } else {
                s.setString(6, getComprehensive());
            }
            s.setString(7, getPolicyHolderName());
            s.setString(8, getVehicleRegistration());
            s.setString(9, getVehicleManufacturer());
            s.setString(10, getVehicleModel());
            s.setLong(11, getVehicleClassID());

            s.setString(12, getUsable());

            if (getDamageDescription() == null) {
                s.setNull(13, Types.VARCHAR);
            } else {
                s.setString(13, getDamageDescription());
            }
            s.setLong(14, getTpInsurerCountryID());

            if (getTpPolicyNumber() == null) {
                s.setNull(15, Types.VARCHAR);
            } else {
                s.setString(15, getTpPolicyNumber());
            }
            if (getTpClaimReference() == null) {
                s.setNull(16, Types.VARCHAR);
            } else {
                s.setString(16, getTpClaimReference());
            }
            if (getTpVehicleRegistration() == null) {
                s.setNull(17, Types.VARCHAR);
            } else {
                s.setString(17, getTpVehicleRegistration());
            }
            if (getTpVehicleManufacturer() == null) {
                s.setNull(18, Types.VARCHAR);
            } else {
                s.setString(18, getTpVehicleManufacturer());
            }
            if (getTpVehicleModel() == null) {
                s.setNull(19, Types.VARCHAR);
            } else {
                s.setString(19, getTpVehicleModel());
            }
            if (getTpVehicleClassID() == -1) {
                s.setNull(20, Types.INTEGER);
            } else {
                s.setLong(20, getTpVehicleClassID());
            }
            if (getTpName() == null) {
                s.setNull(21, Types.VARCHAR);
            } else {
                s.setString(21, getTpName());
            }
            if (getTpAddress1() == null) {
                s.setNull(22, Types.VARCHAR);
            } else {
                s.setString(22, getTpAddress1());
            }
            if (getTpAddress2() == null) {
                s.setNull(23, Types.VARCHAR);
            } else {
                s.setString(23, getTpAddress2());
            }
            if (getTpAddress3() == null) {
                s.setNull(24, Types.VARCHAR);
            } else {
                s.setString(24, getTpAddress3());
            }
            if (getTpAddress4() == null) {
                s.setNull(25, Types.VARCHAR);
            } else {
                s.setString(25, getTpAddress4());
            }
            if (getTpAddress5() == null) {
                s.setNull(26, Types.VARCHAR);
            } else {
                s.setString(26, getTpAddress5());
            }
            if (getTpPostcode() == null) {
                s.setNull(27, Types.VARCHAR);
            } else {
                s.setString(27, getTpPostcode());
            }
            if (getTpTelephoneDay() == null) {
                s.setNull(28, Types.VARCHAR);
            } else {
                s.setString(28, getTpTelephoneDay());
            }
            if (getTpTelephoneEvening() == null) {
                s.setNull(29, Types.VARCHAR);
            } else {
                s.setString(29, getTpTelephoneDay());
            }
            if (getTpEmail() == null) {
                s.setNull(30, Types.VARCHAR);
            } else {
                s.setString(30, getTpEmail());
            }
            s.setTimestamp(31, getIncidentDate());

            if (getLocation() == null) {
                s.setNull(32, Types.VARCHAR);
            } else {
                s.setString(32, getLocation());
            }
            s.setString(33, getPoliceInvolved());
            if (getIncidentDescription() == null) {
                s.setNull(34, Types.VARCHAR);
            } else {
                s.setString(34, getIncidentDescription());
            }
            s.setString(35, getClaimStatus());
            if(getVehicleLocation()==null)
                s.setNull(36, Types.VARCHAR);
            else
                s.setString(36,getVehicleLocation());
                        if(getProposedRentalClassID()==-1)
                s.setNull(37,Types.INTEGER);
            else
                s.setLong(37,getProposedRentalClassID());
            s.setLong(38, getID());
            s.executeUpdate();
            s.close();
        }
    }

    public long getID() {
        return ID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getRentalID() {
        return rentalID;
    }

    public void setRentalID(long rentalID) {
        this.rentalID = rentalID;
    }

    public long getInsurerCountryID() {
        return insurerCountryID;
    }

    public void setInsurerCountryID(long insurerCountryID) {
        this.insurerCountryID = insurerCountryID;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getClaimReference() {
        return claimReference;
    }

    public void setClaimReference(String claimReference) {
        this.claimReference = claimReference;
    }

    public String getComprehensive() {
        return comprehensive;
    }

    public void setComprehensive(String comprehensive) {
        this.comprehensive = comprehensive;
    }

    public String getPolicyHolderName() {
        return policyHolderName;
    }

    public void setPolicyHolderName(String policyHolderName) {
        this.policyHolderName = policyHolderName;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getVehicleManufacturer() {
        return vehicleManufacturer;
    }

    public void setVehicleManufacturer(String vehicleManufacturer) {
        this.vehicleManufacturer = vehicleManufacturer;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public long getVehicleClassID() {
        return vehicleClassID;
    }

    public void setVehicleClassID(long vehicleClassID) {
        this.vehicleClassID = vehicleClassID;
    }

    public String getUsable() {
        return usable;
    }

    public void setUsable(String usable) {
        this.usable = usable;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public long getTpInsurerCountryID() {
        return tpInsurerCountryID;
    }

    public void setTpInsurerCountryID(long tpInsurerCountryID) {
        this.tpInsurerCountryID = tpInsurerCountryID;
    }

    public String getTpPolicyNumber() {
        return tpPolicyNumber;
    }

    public void setTpPolicyNumber(String tpPolicyNumber) {
        this.tpPolicyNumber = tpPolicyNumber;
    }

    public String getTpClaimReference() {
        return tpClaimReference;
    }

    public void setTpClaimReference(String tpClaimReference) {
        this.tpClaimReference = tpClaimReference;
    }

    public String getTpVehicleRegistration() {
        return tpVehicleRegistration;
    }

    public void setTpVehicleRegistration(String tpVehicleRegistration) {
        this.tpVehicleRegistration = tpVehicleRegistration;
    }

    public String getTpVehicleManufacturer() {
        return tpVehicleManufacturer;
    }

    public void setTpVehicleManufacturer(String tpVehicleManufacturer) {
        this.tpVehicleManufacturer = tpVehicleManufacturer;
    }

    public String getTpVehicleModel() {
        return tpVehicleModel;
    }

    public void setTpVehicleModel(String tpVehicleModel) {
        this.tpVehicleModel = tpVehicleModel;
    }

    public long getTpVehicleClassID() {
        return tpVehicleClassID;
    }

    public void setTpVehicleClassID(long tpVehicleClassID) {
        this.tpVehicleClassID = tpVehicleClassID;
    }

    public String getTpName() {
        return tpName;
    }

    public void setTpName(String tpName) {
        this.tpName = tpName;
    }

    public String getTpAddress1() {
        return tpAddress1;
    }

    public void setTpAddress1(String tpAddress1) {
        this.tpAddress1 = tpAddress1;
    }

    public String getTpAddress2() {
        return tpAddress2;
    }

    public void setTpAddress2(String tpAddress2) {
        this.tpAddress2 = tpAddress2;
    }

    public String getTpAddress3() {
        return tpAddress3;
    }

    public void setTpAddress3(String tpAddress3) {
        this.tpAddress3 = tpAddress3;
    }

    public String getTpAddress4() {
        return tpAddress4;
    }

    public void setTpAddress4(String tpAddress4) {
        this.tpAddress4 = tpAddress4;
    }

    public String getTpAddress5() {
        return tpAddress5;
    }

    public void setTpAddress5(String tpAddress5) {
        this.tpAddress5 = tpAddress5;
    }

    public String getTpPostcode() {
        return tpPostcode;
    }

    public void setTpPostcode(String tpPostcode) {
        this.tpPostcode = tpPostcode;
    }

    public String getTpTelephoneDay() {
        return tpTelephoneDay;
    }

    public void setTpTelephoneDay(String tpTelephoneDay) {
        this.tpTelephoneDay = tpTelephoneDay;
    }

    public String getTpTelephoneEvening() {
        return tpTelephoneEvening;
    }

    public void setTpTelephoneEvening(String tpTelephoneEvening) {
        this.tpTelephoneEvening = tpTelephoneEvening;
    }

    public String getTpEmail() {
        return tpEmail;
    }

    public void setTpEmail(String tpEmail) {
        this.tpEmail = tpEmail;
    }

    public Timestamp getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(Timestamp incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPoliceInvolved() {
        return policeInvolved;
    }

    public void setPoliceInvolved(String policeInvolved) {
        this.policeInvolved = policeInvolved;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getVehicleLocation() {
        return vehicleLocation;
    }

    public void setVehicleLocation(String vehicleLocation) {
        this.vehicleLocation = vehicleLocation;
    }

    public long getProposedRentalClassID() {
        return proposedRentalClassID;
    }

    public void setProposedRentalClassID(long proposedRentalClassID) {
        this.proposedRentalClassID = proposedRentalClassID;
    }
}
