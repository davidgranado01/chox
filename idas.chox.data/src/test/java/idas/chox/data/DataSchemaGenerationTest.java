package idas.chox.data;

import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml"})
public class DataSchemaGenerationTest {

    @Autowired
    LocalSessionFactoryBean localSessionFactoryBean;

    @Test
    @Transactional
    public void testFile() {

//        SchemaExport schemaExport = new SchemaExport(localSessionFactoryBean.getConfiguration());
//        schemaExport.execute(true, false, false, true);
//        System.out.print(schemaExport.toString());
    }
}
