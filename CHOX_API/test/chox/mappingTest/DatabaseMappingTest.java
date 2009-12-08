package chox.mappingTest;

import org.junit.*;
import org.unitils.UnitilsJUnit4;
import org.unitils.orm.hibernate.*;
import org.unitils.spring.annotation.SpringApplicationContext;

@SpringApplicationContext({"classpath:application-config.xml"})
public class DatabaseMappingTest extends UnitilsJUnit4{
    
    @Test
    public void testMappingToDatabase() {
        HibernateUnitils.assertMappingWithDatabaseConsistent();
    }
    
}