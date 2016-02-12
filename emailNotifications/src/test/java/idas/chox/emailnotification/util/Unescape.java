package idas.chox.emailnotification.util;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Test;

public class Unescape {

    @Test
    public void testUnescape() {
        String input = "&lt; Test &gt;";
        String output = "< Test >";
        String cleanedMessage = StringEscapeUtils.unescapeHtml4(input);
        assertEquals(output, cleanedMessage);
    }

}
