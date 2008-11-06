package chox.decision;

import chox.data.Rental;
import com.filesystemsoftware.utils.DBConnectionWrapper;

public abstract class Decision {
    public abstract Boolean decide(DBConnectionWrapper connection,Rental rental) throws Exception;    
}
