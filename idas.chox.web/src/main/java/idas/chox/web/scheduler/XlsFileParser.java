package idas.chox.web.scheduler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.context.SecurityContextHolder;

public class XlsFileParser {

	private static final Logger LOG = LoggerFactory
			.getLogger(XlsFileParser.class);
	
	/**
	 * Reads the excel file and populates the Map readable cell data.
	 * 
	 * @param InputStream
	 *            - excelFile in byteFormat
	 * @return Map<Integer, List<String>> integer is the cell row number and
	 *         List is the data returned in row.
	 */
	public Map<Integer, List<String>> readExcelFile(InputStream inputStream) {

		List<List<Cell>> cellListHolder = new ArrayList<List<Cell>>();

		try {

			/** Create a POIFSFileSystem object **/
			POIFSFileSystem myFileSystem = new POIFSFileSystem(inputStream);

			/** Create a workbook using the File System **/
			HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

			/** Get the first sheet from workbook **/
			HSSFSheet mySheet = myWorkBook.getSheetAt(0);

			/** We now need something to iterate through the cells. **/
			Iterator<Row> rowIter = mySheet.rowIterator();
			SecurityContextHolder.getContext().getAuthentication().getAuthorities();
			while (rowIter.hasNext()) {
				HSSFRow myRow = (HSSFRow) rowIter.next();
				Iterator<Cell> cellIter = myRow.cellIterator();
				List<Cell> cellStoreVector = new ArrayList<Cell>();
				while (cellIter.hasNext()) {
					HSSFCell myCell = (HSSFCell) cellIter.next();
					cellStoreVector.add(myCell);
					
				}
				cellListHolder.add(cellStoreVector);
			}
		} catch (IOException e) {
			LOG.error("Can't parse xls from given input stream. " + e);
		}

		return iterateThroughTheXlsFile(cellListHolder);
	}

	/**
	 * Iterates through the dataHolder list of Cells, and populates the map with
	 * readable cell data.
	 * 
	 * @param List<List<Cell>>
	 * @return Map<Integer, List<String>> integer is the cell row number and
	 *         List is the data returned in row.
	 */
	private Map<Integer, List<String>> iterateThroughTheXlsFile(List<List<Cell>> dataHolder) {
		Map<Integer, List<String>> xlsDataMap = new HashMap<Integer, List<String>>();
		String stringCellValue = null;
		try {
			for (int i = 0; i < dataHolder.size(); i++) {
				List<Cell> cellStoreList = (List<Cell>) dataHolder.get(i);
				List<String> cellStringList = new ArrayList<String>();
				for (int j = 0; j < cellStoreList.size(); j++) {
					HSSFCell myCell = (HSSFCell) cellStoreList.get(j);
					cellStringList.add(myCell.toString());
				}
				xlsDataMap.put(i, cellStringList);
			}
		} catch (HibernateException e) {
			LOG.error("Can't update claim with cho_reference number: "
					+ stringCellValue + " " + e);
		}
		return xlsDataMap;
	}

}
