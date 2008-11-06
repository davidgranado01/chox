package chox.processor;

import chox.data.Rental;
import com.filesystemsoftware.utils.DBConnectionWrapper;

public abstract class AbstractRentalProcessor {

    public abstract void process(DBConnectionWrapper connection,Rental rental) throws Exception;
    
}
