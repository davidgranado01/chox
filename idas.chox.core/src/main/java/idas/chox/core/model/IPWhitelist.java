package idas.chox.core.model;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class IPWhitelist extends Entity implements Serializable {
    static final Logger LOG = LoggerFactory.getLogger(IPWhitelist.class);
    private Insurer insurer;
    private Chorganisation chorganisation;
    private String ipAddress;
    private String description;

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Chorganisation getChorganisation() {
        return chorganisation;
    }

    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
