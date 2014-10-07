package idas.chox.reporttoexcel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author John
 */
public class Report {
    private String name;
    private String[] headers;
    private List<Object[]> body = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public List<Object[]> getBody() {
        return body;
    }

    public void setBody(List<Object[]> body) {
        this.body = body;
    }

}
