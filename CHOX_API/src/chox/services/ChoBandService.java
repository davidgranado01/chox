/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.ChoBand;

public interface ChoBandService {
    
    ChoBand getChoBandByChorganisationId(int Id);
    ChoBand getChoBandByChorganisationIdAndInsurerId(int orgId, int insurerId);
}
