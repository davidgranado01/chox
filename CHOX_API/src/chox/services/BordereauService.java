/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Bordereau;

public interface BordereauService {
    public Boolean saveObj(Bordereau obj);
    public Bordereau getObject(int id);
    public Bordereau getObject(String fileName);
    public boolean deleteObject(String fileName);
}