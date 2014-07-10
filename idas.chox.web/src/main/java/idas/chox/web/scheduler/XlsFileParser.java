package idas.chox.web.scheduler;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.util.DateHelper;

public class XlsFileParser {

    private static final Logger LOG = LoggerFactory.getLogger(XlsFileParser.class);
    private static final int CELLS_PER_ROW = 4;

    /**
     * Reads the excel file and populates the Map readable cell data.
     *
     * @param inputStream 
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

            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                List<Cell> cellStoreVector = new ArrayList<Cell>();
                int cellNumber = 0;
                boolean isDataExistsForThisRow = false;
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    /*
                     * Note that the 4th element might well not be cell 4, as
                     * the iterator will not return un-defined (null) cells.
                     * Call getColumnIndex() on the returned cells to know which
                     * cell they are.
                     */
                    if (myCell.getColumnIndex() == cellNumber) {
                        cellStoreVector.add(myCell);
                        cellNumber++;
                        if (myCell.getCellType() != Cell.CELL_TYPE_BLANK) {
                            isDataExistsForThisRow = true;
                        }
                    } else { // add null value to the omitted cells before the current cell.
                        for (; cellNumber < myCell.getColumnIndex(); cellNumber++) {
                            cellStoreVector.add(null);
                        }
                       cellStoreVector.add(myCell);
                       cellNumber++;
                        if (myCell.getCellType() != Cell.CELL_TYPE_BLANK) {
                            isDataExistsForThisRow = true;
                        }
                    }
                    /*
                     * Add null value to the omitted cells after the last cell 
                     * eg. if only 2 cell per row exists then add another 2 cells
                     * with null value. CELLS_PER_ROW can be increased if cells
                     * per row need increasing.
                     */
                    if (isDataExistsForThisRow 
                            && (myRow.getLastCellNum() - 1) == myCell.getColumnIndex() 
                            && myRow.getLastCellNum() < CELLS_PER_ROW) {
                        for (; cellNumber < CELLS_PER_ROW; cellNumber++) {
                            cellStoreVector.add(null);
                        }
                    }
                }
                cellListHolder.add(cellStoreVector);
            }
        } catch (IOException e) {
            LOG.error("Exception parsing xls file from given input stream: {}", e.getMessage(), e);
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
        for (int i = 0; i < dataHolder.size(); i++) {
            List<Cell> cellStoreList = dataHolder.get(i);
            List<String> cellStringList = new ArrayList<String>();
            for (int j = 0; j < cellStoreList.size(); j++) {
                HSSFCell myCell = (HSSFCell) cellStoreList.get(j);
                if (myCell != null && myCell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(myCell)
                        && !isTime(myCell)) {
                    LOG.debug("cell is date formated : {}", myCell.getNumericCellValue());
                    Date date = HSSFDateUtil.getJavaDate(myCell.getNumericCellValue());
                    cellStringList.add(DateHelper.getLocalDateFormat().format(date));
                } else if (myCell != null && myCell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(myCell)
                        && isTime(myCell)) {
                    LOG.debug("cell is time formated : {}", myCell.getNumericCellValue());
                    Date date = HSSFDateUtil.getJavaDate(myCell.getNumericCellValue());
                    cellStringList.add(DateHelper.getTimeFormat().format(date));
                } else if (myCell != null) {
                    LOG.debug("cell is a string : {}", myCell.getStringCellValue());
                    myCell.setCellType(Cell.CELL_TYPE_STRING);
                    cellStringList.add(myCell.getStringCellValue().trim());
                } else {
                    LOG.debug("cell is null");
                    cellStringList.add("");
                }
                LOG.debug("rowNumber - cellNmber - cellValue - {} : {} : {}", new Object[]{i,j,cellStringList.get(j)});
            }
            LOG.debug("Adding cell {}: {}", i, cellStringList);
            xlsDataMap.put(i, cellStringList);
        }
        return xlsDataMap;
    }

    private boolean isTime(HSSFCell myCell) {
      Date date = HSSFDateUtil.getJavaDate(myCell.getNumericCellValue());
      boolean isTime = false;
  
      /* get date year.
       *"Time-only" values have date set to 31-Dec-1899 so if year is "1899"
       * you can assume it is a "time-only" value 
       */
        String dateStamp = (new SimpleDateFormat("yyyy")).format(date);

        if (dateStamp.equals("1899")){
            isTime = true;
        }
        
        return isTime;
    }
    
     
}
