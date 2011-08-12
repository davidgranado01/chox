/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.uploadclient;

import org.kohsuke.args4j.Option;

/**
 *
 * @author John
 */
public class Options {
    
    @Option(name="-u")
    private String userName = "op@cho.com";
    @Option(name="-p")
    private String password = "C0mpliance"; 
    @Option(name="-w")
    private String wsdlLocation = "http://localhost:8080/services/UploadService/uploadBordereau";
    @Option(name="-f")
    private String filename = "src/main/resources/testBordereau.xml";
   


    
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWsdlLocation() {
        return wsdlLocation;
    }

    public void setWsdlLocation(String wsdlLocation) {
        this.wsdlLocation = wsdlLocation;
    }

}
