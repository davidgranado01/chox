package idas.chox.core.services;

import idas.chox.core.model.WebBordereau;

public interface WebBordereauService {

    public void saveBordereau(WebBordereau bordereau);

    public WebBordereau getBordereauById(int webBordereauId);

    public boolean deleteBordereau(WebBordereau webBordereau);
    
    public boolean deleteBordereau(int webBordereauId);

}
