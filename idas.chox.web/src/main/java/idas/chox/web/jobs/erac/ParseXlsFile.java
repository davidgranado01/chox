package idas.chox.web.jobs.erac;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseXlsFile {

	private static final Logger LOG = LoggerFactory
			.getLogger(ParseXlsFile.class);

	public List<List<Cell>> readExcelFile(InputStream inputStream) {

		List<List<Cell>> cellListHolder = new ArrayList<List<Cell>>();

		InputStream myInput;
		try {
			myInput = inputStream;

			/** Create a POIFSFileSystem object **/
			POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

			/** Create a workbook using the File System **/
			HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

			/** Get the first sheet from workbook **/
			HSSFSheet mySheet = myWorkBook.getSheetAt(0);

			/** We now need something to iterate through the cells. **/
			Iterator<Row> rowIter = mySheet.rowIterator();

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
		return cellListHolder;
	}

	/**
	 * This is convenience method used for testing purposes.
	 * 
	 * @param dataHolder
	 */
	public void iterateThroughTheXlsFile(List<List<Cell>> dataHolder) {

		for (int i = 0; i < dataHolder.size(); i++) {
			List<Cell> cellStoreList = (List<Cell>) dataHolder.get(i);
			for (int j = 0; j < cellStoreList.size(); j++) {
				HSSFCell myCell = (HSSFCell) cellStoreList.get(j);
				String stringCellValue = myCell.toString();
				// XXX just for testing purposes
				System.out.print(stringCellValue + "\t");
			}
			System.out.println();
		}
	}
}
