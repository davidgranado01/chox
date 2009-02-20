/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.exception;

import org.hibernate.HibernateException;

/**
 *
 * @author Emmanuel
 */
public class InvalidaUserDataException extends HibernateException {
    
    public InvalidaUserDataException(String msg)
    {
        super(msg);
    }
}
