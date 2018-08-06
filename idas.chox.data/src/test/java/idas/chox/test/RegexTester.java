package idas.chox.test;

import org.junit.Test;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 *
 * @author abrar
 */
@Ignore
@RunWith(BlockJUnit4ClassRunner.class)
public class RegexTester {

    private static final Log LOG = LogFactory.getLog(RegexTester.class);

    @Test
    public void testSbAppend(){
        //String regex = "^\s*sb\.append\(\s*\"(.*)\"\)\;\s*$/$1";
        String regex = "\\s*sb\\.append\\(\\\"(.*)\\s*";
        //String regex2 = "\\s*sb\\.append\\(\\s*\\"(.*)\\\"\\)\\;\\s*";
        String str = "     sb.append(\"    ddddd         ";
        System.out.println(str.replaceAll(regex, "$1"));
    }

    public static void main(String[] arg){
        new RegexTester().testSbAppend();
    }
}
