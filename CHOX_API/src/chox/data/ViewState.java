/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.data;

/**
 *
 * @author Emmanuel
 */
public class ViewState {

    public static final long maxAge = 1000 * 5;
    Long birth = System.currentTimeMillis();

    public ViewState() {
    }
    

    public boolean isExpired() {
        synchronized (birth) {            
            return System.currentTimeMillis() - birth > maxAge;
        }
    }

    public void refresh() {
        synchronized (birth) {
            birth = System.currentTimeMillis();
            //System.out.println("Refreshed to " + birth.toString());
        }
    }
}
