package org.example.utils;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;


public class ExcelUtils implements AutoCloseable {

    private final Path filePath;
    private Workbook workbook;
    private boolean dirty = false; // tracks if changes were made

    private ExcelUtils(Path filePath, Workbook workbook) {
        this.filePath = filePath;
        this.workbook = workbook;
    }

    /**
     * Open existing .xlsx file or create a new one if it does not exist.
     */
    public static ExcelUtils open(String file) throws IOException {
        Path path = Path.of(file);
        Workbook wb;
        if (Files.exists(path) && Files.size(path) > 0) {
            try (InputStream is = Files.newInputStream(path)) {
                wb = new XSSFWorkbook(is);
            }
        } else {
            wb = new XSSFWorkbook();
        }
        return new ExcelUtils(path, wb);
    }

    /**
     * Create a new workbook in memory, not yet bound to a file. Use saveAs(...) to persist.
     */
    public static ExcelUtils createEmpty() {
        try (XSSFWorkbook xssfWorkbook = new XSSFWorkbook()) {
            return new ExcelUtils(null, xssfWorkbook);
        } catch (IOException e) {
            LogUtils.logError("Error Creating Empty Excel workbook", Arrays.toString( e.getStackTrace()));
        }
        return null;
    }

    /**
     * Get existing sheet or create one if missing.
     */
    public Sheet getOrCreateSheet(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
            dirty = true;
        }
        return sheet;
    }

    /**
     * Get sheet by name; null if not found.
     */
    public Sheet getSheet(String sheetName) {
        return workbook.getSheet(sheetName);
    }

    /**
     * Ensure row exists.
     */
    public Row getOrCreateRow(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
            dirty = true;
        }
        return row;
    }

    /**
     * Ensure cell exists.
     */
    public Cell getOrCreateCell(Row row, int colIndex) {
        return row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    }

    /**
     * Read cell as String (formats numbers/dates via DataFormatter).
     */
    public String readCellAsString(Sheet sheet, int rowIndex, int colIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) return "";
        Cell cell = row.getCell(colIndex);
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }

    /**
     * Write a String value.
     */
    public ExcelUtils writeCell(Sheet sheet, int rowIndex, int colIndex, String value) {
        Row row = getOrCreateRow(sheet, rowIndex);
        Cell cell = getOrCreateCell(row, colIndex);
        cell.setCellValue(value);
        dirty = true;
        return this;
    }

    public ExcelUtils writeCell(String sheetName, int rowIndex, int colIndex, String value) {
        Sheet sheet = getSheet(sheetName);
        Row row = getOrCreateRow(sheet, rowIndex);
        Cell cell = getOrCreateCell(row, colIndex);
        cell.setCellValue(value);
        dirty = true;
        return this;
    }
    public ExcelUtils writeDateCell(String sheetName, int rowIndex, int colIndex, LocalDate value) {
        Sheet sheet = getSheet(sheetName);
        Row row = getOrCreateRow(sheet, rowIndex);
        Cell cell = getOrCreateCell(row, colIndex);
        cell.setCellValue(value);
        dirty = true;
        return this;
    }

    /**
     * Write a double value.
     */
    public ExcelUtils writeCell(Sheet sheet, int rowIndex, int colIndex, double value) {
        Row row = getOrCreateRow(sheet, rowIndex);
        Cell cell = getOrCreateCell(row, colIndex);
        cell.setCellValue(value);
        dirty = true;
        return this;
    }


    /**
     * Write a boolean value.
     */
    public ExcelUtils writeCell(Sheet sheet, int rowIndex, int colIndex, boolean value) {
        Row row = getOrCreateRow(sheet, rowIndex);
        Cell cell = getOrCreateCell(row, colIndex);
        cell.setCellValue(value);
        dirty = true;
        return this;
    }

    /**
     * Append a row with values (String-based). Returns the new row index.
     */
    public int appendRow(Sheet sheet, List<String> values) {
        int newIndex = sheet.getLastRowNum() + 1;
        Row row = sheet.getRow(newIndex);
        if (row == null) {
            row = sheet.createRow(newIndex);
        } else {
            // If lastRowNum reported previous non-empty, move to next
            newIndex = newIndex + 1;
            row = sheet.createRow(newIndex);
        }
        for (int i = 0; i < values.size(); i++) {
            Cell cell = getOrCreateCell(row, i);
            cell.setCellValue(values.get(i));
        }
        dirty = true;
        return newIndex;
    }

    /**
     * Auto-size columns for the given sheet based on header width.
     */
    public ExcelUtils autoSizeColumns(Sheet sheet) {
        if (sheet.getPhysicalNumberOfRows() == 0) return this;
        Row header = sheet.getRow(sheet.getFirstRowNum());
        if (header == null) return this;
        int lastCol = header.getLastCellNum();
        if (lastCol < 0) return this;
        for (int c = 0; c < lastCol; c++) {
            sheet.autoSizeColumn(c);
        }
        return this;
    }

    /**
     * Read sheet as List<Map<String,String>>, using headerRowIndex as the header.
     */
    public List<Map<String, String>> readSheetAsMaps(Sheet sheet, int headerRowIndex) {
        List<Map<String, String>> results = new ArrayList<>();
        Row headerRow = sheet.getRow(headerRowIndex);
        if (headerRow == null) return results;

        int lastCol = headerRow.getLastCellNum();
        List<String> headers = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        for (int c = 0; c < lastCol; c++) {
            String header = formatter.formatCellValue(headerRow.getCell(c));
            headers.add(header != null ? header.trim() : "");
        }

        int firstDataRow = headerRowIndex + 1;
        int lastRow = sheet.getLastRowNum();
        for (int r = firstDataRow; r <= lastRow; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            Map<String, String> map = new LinkedHashMap<>();
            boolean allBlank = true;
            for (int c = 0; c < lastCol; c++) {
                Cell cell = row.getCell(c);
                String val = formatter.formatCellValue(cell);
                if (val != null && !val.isBlank()) allBlank = false;
                map.put(headers.get(c), val);
            }
            if (!allBlank) {
                results.add(map);
            }
        }
        return results;
    }

    /**
     * Get column index by header name (case-insensitive match, trims). Returns -1 if not found.
     */
    public int getColumnIndexByHeader(Sheet sheet, String headerName) {
        if (sheet.getPhysicalNumberOfRows() == 0) return -1;
        Row headerRow = sheet.getRow(sheet.getFirstRowNum());
        if (headerRow == null) return -1;
        DataFormatter formatter = new DataFormatter();
        String target = headerName.trim().toLowerCase(Locale.ROOT);
        short lastCell = headerRow.getLastCellNum();
        for (int c = 0; c < lastCell; c++) {
            String h = formatter.formatCellValue(headerRow.getCell(c)).trim().toLowerCase(Locale.ROOT);
            if (h.equals(target)) return c;
        }
        return -1;
    }

    /**
     * Save to the originally opened file.
     */
    public ExcelUtils save() throws IOException {
        if (filePath == null) {
            throw new IllegalStateException("Workbook was created in-memory. Use saveAs(...) first.");
        }
        if (!dirty) return this; // no changes, skip IO
        try (OutputStream os = Files.newOutputStream(filePath)) {
            workbook.write(os);
        }
        dirty = false;
        return this;
    }

    /**
     * Save as a new file path.
     */
    public ExcelUtils saveAs(String file) throws IOException {
        Path target = Path.of(file);
        try (OutputStream os = Files.newOutputStream(target)) {
            workbook.write(os);
        }
        dirty = false;
        return this;
    }

    /**
     * Close workbook resources.
     */
    @Override
    public void close() {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException ignored) {
                //ignored due to no need to close if itsio exception
            }
            workbook = null;
        }
    }

    /**
     * Get underlying workbook (e.g., for advanced styling).
     */
    public Workbook getWorkbook() {
        return workbook;
    }

    /**
     * Convenience: write headers to the first row.
     */
    public ExcelUtils writeHeader(Sheet sheet, List<String> headers) {
        Row headerRow = getOrCreateRow(sheet, sheet.getFirstRowNum());
        for (int c = 0; c < headers.size(); c++) {
            Cell cell = getOrCreateCell(headerRow, c);
            cell.setCellValue(headers.get(c));
        }
        dirty = true;
        return this;
    }
}

