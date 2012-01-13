package idas.chox.web.jobs.erac;

import idas.chox.core.model.Claim;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ClaimService;
import idas.chox.web.security.WebUserService;

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
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseXlsFile {

	private static final Logger LOG = LoggerFactory
			.getLogger(ParseXlsFile.class);

	private ClaimService claimService;

	public List<List<Cell>> readExcelFile(InputStream inputStream) {

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
	// @Secured("ROLE_CHO")
	public void iterateThroughTheXlsFile(List<List<Cell>> dataHolder) {

		Claim claim = null;
		String stringCellValue = null;
		try {
			for (int i = 0; i < dataHolder.size(); i++) {
				List<Cell> cellStoreList = (List<Cell>) dataHolder.get(i);
				for (int j = 0; j < cellStoreList.size(); j++) {
					HSSFCell myCell = (HSSFCell) cellStoreList.get(j);
					stringCellValue = myCell.toString();
					// we don't do update on first line and we assume we will
					// always
					// have only two columns
					if (i != 0) {
						if (j == 0) {
							claim = claimService
									.getClaimByCHOReferenceNumber(stringCellValue);

							// we do update only on second column
						} else {
							claim.setChoReference(stringCellValue);
							claimService.updateClaim(claim);
						}

					}
					// XXX just for testing purposes
					System.out.print(stringCellValue + "\t");
				}
				System.out.println();
			}
		} catch (HibernateException e) {
			LOG.error("Can't update claim with cho_reference number: "
					+ stringCellValue + " " + e);
		}
	}

	public ClaimService getClaimService() {
		return claimService;
	}

	public void setClaimService(ClaimService claimService) {
		this.claimService = claimService;
	}

}
