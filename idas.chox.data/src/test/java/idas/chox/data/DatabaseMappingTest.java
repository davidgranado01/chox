package idas.chox.data;


import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.orm.hibernate.HibernateUnitils;
import org.unitils.spring.annotation.SpringApplicationContext;

@SpringApplicationContext({"classpath:application-mapping-test.xml"})
public class DatabaseMappingTest extends UnitilsJUnit4{
    
    @Test
    public void testMappingToDatabase() {
        HibernateUnitils.assertMappingWithDatabaseConsistent();
    }
    
}