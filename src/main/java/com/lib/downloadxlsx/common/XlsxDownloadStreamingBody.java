package com.lib.downloadxlsx.common;

import com.lib.downloadxlsx.inf.ExExecuteIf;
import com.lib.downloadxlsx.inf.RequestQueryIf;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

/**
 * The type Xlsx download streaming body.
 */
public class XlsxDownloadStreamingBody implements StreamingResponseBody {
    private final ExExecuteIf exExecuteIf;
    private final Class<?> csvDtoClass;
    private final ExDownloadOption optionDto;
    private final RequestQueryIf parameters;
    private final Sort sort;

    /**
     * Instantiates a new Xlsx download streaming body.
     *
     * @param exExecuteIf      the ex execute if
     * @param csvDtoClass      the csv dto class
     * @param exDownloadOption the ex download option
     * @param parameters       the parameters
     */
    public XlsxDownloadStreamingBody(ExExecuteIf exExecuteIf, Class<?> csvDtoClass, ExDownloadOption exDownloadOption, RequestQueryIf parameters) {
        this.csvDtoClass = csvDtoClass;
        this.exExecuteIf = exExecuteIf;
        ExDownloadOption optionDtoParam = exDownloadOption;
        if (optionDtoParam == null) {
            optionDtoParam = ExDownloadOption.DEFAULT_DOWNLOAD_OPTION;
        }
        this.optionDto = optionDtoParam;
        this.parameters = parameters;
        this.sort = Sort.by("DESC".equalsIgnoreCase(parameters.getOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC, parameters.getBy());
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        try (outputStream; Workbook workbook = new XSSFWorkbook();) {
            Sheet sheet = workbook.createSheet(csvDtoClass.getSimpleName());
            Sheet sheet2 = workbook.createSheet("QuerywithStatus");
            Font font = workbook.createFont();
            CellStyle borderStyle = workbook.createCellStyle();
            CellStyle style = workbook.createCellStyle();
            CellStyle headerStyle = workbook.createCellStyle();
            // Header style
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 16); // Cỡ chữ to hơn
            headerFont.setColor(IndexedColors.WHITE.getIndex()); // Màu chữ trắng
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex()); // Màu nền xanh lục đậm
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Border style
            ExDownloadHandler.createDefaultCellStyle(font, borderStyle);
            ExDownloadHandler.createDefaultCellStyle(font, style);
            borderStyle.setAlignment(HorizontalAlignment.CENTER);
            borderStyle.setFillBackgroundColor(IndexedColors.RED.getIndex());
            style.setAlignment(HorizontalAlignment.CENTER);
            font.setFontName("Times New Roman");
            font.setBold(true);
            font.setFontHeightInPoints((short) 14); // font size
            // Header
            Row headerRow = sheet.createRow(0);
            Row headerRow2 = sheet2.createRow(2);
            // Tạo header total count cho sheet 2
            Row headerRowTotalCount = sheet2.createRow(0);
            Cell headerCell = headerRowTotalCount.createCell(0);
            headerCell.setCellValue("Total Count Data: ");
            headerCell.setCellStyle(headerStyle);
            // Gọi hàm totalCountData và in vào ô B1 sheet 2
            Long totalCount = exExecuteIf.totalCountData(parameters);
            Cell countCell = headerRowTotalCount.createCell(1);
            countCell.setCellValue(totalCount);
            List<String> headers = ExDownloadHandler.outHeader(this.csvDtoClass);
            if (this.optionDto.isOutputHeader()) {
                for (int col = 0; col < headers.size(); col++) {
//                    sheet.autoSizeColumn(col);
                    Cell cell = headerRow.createCell(col);
                    Cell cell2 = headerRow2.createCell(col);
                    //cell.setCellStyle(borderStyle);
                    cell.setCellStyle(headerStyle);
                    cell.setCellValue(headers.get(col));
                    cell2.setCellStyle(borderStyle);
                    cell2.setCellValue(headers.get(col));
                }
            }
            // them header age
            Cell ageHeader = headerRow2.createCell(headers.size()); // Thêm cột sau các cột hiện có
            ageHeader.setCellStyle(borderStyle);
            ageHeader.setCellValue("Age");

            long no = 0L;
            Long total = exExecuteIf.totalCountData(parameters);
            int loop = (int) (total / optionDto.getTotalExport());
            int rowIdx1 = 1;
            int rowIdx2 = 3;
            for (int i = 0; i <= loop; i++) {
                PageRequest pageRequest = PageRequest.of(i, Math.toIntExact(optionDto.getTotalExport()), sort);
                List resultStream = exExecuteIf.findData(pageRequest, parameters);
                Iterator resultIterator = resultStream.stream().iterator();
                while (resultIterator.hasNext()) {
                    Row row = sheet.createRow(rowIdx1++);
                    Row row2 = sheet2.createRow(rowIdx2++);
                    List<Object> contents = ExDownloadHandler.outCsvRecord(resultIterator.next(), ++no);
                    for (int col = 0; col < contents.size(); col++) {
                        Cell cell = row.createCell(col);
                        Cell cell2 = row2.createCell(col);
                        cell.setCellStyle(style);
                        cell2.setCellStyle(style);
                        setCellValue(cell, contents.get(col));
                        setCellValue(cell2, contents.get(col));
                    }
                    // Tính tuổi và điền vào cột mới
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String dobString = (String) contents.get(3);
                    LocalDate dob = LocalDate.parse(dobString, formatter);
                    int age = Year.now().getValue() - dob.getYear();
                    Cell ageCell = row2.createCell(headers.size()); // Điền vào cột mới
                    ageCell.setCellStyle(style);
                    ageCell.setCellValue(age);
                }
            }
            for (int col = 0; col <= headers.size(); col++) {
                sheet.autoSizeColumn(col);
                sheet2.autoSizeColumn(col);
            }
            workbook.write(outputStream);
        }
    }

    private void setCellValue(Cell cell, Object value) {
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }
}
