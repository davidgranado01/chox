package idas.chox.core.services;

import idas.chox.core.model.WebBordereau;

public interface WebBordereauService {

    void saveBordereau(WebBordereau bordereau);

    WebBordereau getBordereauById(int webBordereauId);

    boolean deleteBordereau(WebBordereau webBordereau);
    
    boolean deleteBordereau(int webBordereauId);

}
