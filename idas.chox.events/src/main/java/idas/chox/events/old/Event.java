package idas.chox.events.old;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.rits.cloning.Cloner;

/**
 *
 * @author John
 */
public final class Event implements Serializable {
    final private String name;
    final private int id;
    final private int insurerId;
    final private int choId;
    final private int claimId;
    final private int claimType;
    
    private Map<String, Object> parameters;

    public Map<String, Object> getParameters() {
        Cloner cloner = new Cloner();
        return cloner.deepClone(parameters);
    }

    public void setParameters(Map<String, Object> parameters) {
        Cloner cloner = new Cloner();
        this.parameters = cloner.deepClone(parameters);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public int getChoId() {
        return choId;
    }

    public int getClaimId() {
        return claimId;
    }

    public int getClaimType() {
        return claimType;
    }
 
    
    public Event (final String name, final int id, final int insurerId, final int choId, final int claimId, int claimType) {
        this.name = name; this.id = id; this.insurerId = insurerId; this.choId = choId; this.claimId = claimId; this.claimType = claimType;
        parameters = new HashMap<String, Object>(10);
    }
    
    public void addParameter(String name, Object value) {
        parameters.put(name, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Event ").append(name).append("[id=").append(String.valueOf(id))
                .append("]  between insId=").append(insurerId).append(" & choId=").append(choId)
                .append(", claimId=").append(claimId).append(", type=").append(claimType);
        sb.append("\n Parameters are: \n");
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            sb.append("    ").append(key).append(" = ").append(value).append("\n");
        }
        sb.append("--\n");
        return sb.toString();
    }
    
}
