package idas.chox.web.scheduler;

import idas.chox.service.workflow.scheduleActivities.XlsFileParser;
import static org.junit.Assert.assertEquals;
import idas.chox.web.BaseWebTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

public class XlsFileParserTest extends BaseWebTest{

	@Autowired
	private XlsFileParser xlsFileParser;

	/**
	 * Test of readExcelFile method, of class XlsFileParser.
	 */
	@Test
	public void testReadExcelFile() throws Exception {
		File file = new ClassPathResource("CHOX Reservation Vs Ticket.xls")
				.getFile();
		InputStream inputStream = new FileInputStream(file);
		Map<Integer, List<String>> result = xlsFileParser.readExcelFile(inputStream);
		Assert.assertEquals(43, result.size());

		assertEquals("UNU1R044196", result.get(1).get(0));
		assertEquals("", result.get(1).get(1));

		assertEquals("Order Number Reservation", result.get(0).get(0));
		assertEquals("Order Number Ticket", result.get(0).get(1));

		assertEquals("UNU8R839380", result.get(42).get(0));
		assertEquals("UNU8D023582", result.get(42).get(1));
	}
}
