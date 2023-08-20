package com.lib.downloadxlsx.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lib.downloadxlsx.anotation.ExColumn;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * The type Ex download handler.
 */
public class ExDownloadHandler {

  /**
   * Instantiates a new Ex download handler.
   */
  public ExDownloadHandler() {
  }

  /**
   * Csv format builder csv format.
   *
   * @param optionDto the option dto
   * @return the csv format
   */
  public static CSVFormat csvFormatBuilder(ExDownloadOption optionDto) {
    CSVFormat outputCsvFormat;
    if (optionDto.isForceQuote()) {
      outputCsvFormat = CSVFormat.DEFAULT.withQuote(optionDto.getQuoteCharacter()).withEscape(optionDto.getEscapeCharacter()).withRecordSeparator(optionDto.getLineSeparator()).withQuoteMode(QuoteMode.ALL);
    } else {
      outputCsvFormat = CSVFormat.DEFAULT.withQuote(optionDto.getQuoteCharacter()).withEscape(optionDto.getEscapeCharacter()).withRecordSeparator(optionDto.getLineSeparator()).withQuoteMode(QuoteMode.MINIMAL);
    }

    return outputCsvFormat;
  }

  /**
   * Create default cell style cell style.
   *
   * @param font      the font
   * @param cellStyle the cell style
   * @return the cell style
   */
  public static CellStyle createDefaultCellStyle(Font font, CellStyle cellStyle) {
    font.setFontHeightInPoints((short) 10);
    font.setFontName("Arial");
    cellStyle.setFont(font);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    cellStyle.setBorderLeft(BorderStyle.THIN);
    cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
    return cellStyle;
  }

  /**
   * Out header list.
   *
   * @param csvDtoClass the csv dto class
   * @return the list
   */
  public static List<String> outHeader(Class<?> csvDtoClass) {
    return getCsvHeader(csvDtoClass);
  }

  /**
   * Out csv record list.
   *
   * @param csvDto the csv dto
   * @param no     the no
   * @return the list
   */
  public static List<Object> outCsvRecord(Object csvDto, Long no) {
    return getRecordContent(csvDto, no);
  }

  private static List<Field> getFieldList(Class<?> csvDtoClass) {
    return Arrays.asList(csvDtoClass.getDeclaredFields());
  }

  private static List<String> getCsvHeader(Class<?> csvDtoClass) {
    List<String> csvHeader = new ArrayList();
    csvHeader.add("No");
    TreeMap<Integer, String> csvHeaderSorted = new TreeMap<>();
    List<Field> fields = getFieldList(csvDtoClass);
    Iterator var5 = fields.iterator();
    while(var5.hasNext()) {
      Field field = (Field)var5.next();
      if (!field.getName().toUpperCase().contains("serialVersion".toUpperCase())) {
        String columnHeader = translateHeader(field);
        if (columnHeader != null) {
          csvHeaderSorted.put(field.getAnnotation(ExColumn.class).order(), columnHeader);
        }
      }
    }
    var5 = csvHeaderSorted.entrySet().iterator();
    while(var5.hasNext()) {
      Entry<Integer, String> entry = (Entry)var5.next();
      csvHeader.add(entry.getValue());
    }

    return csvHeader;
  }

  private static String translateHeader(Field field) {
    ExColumn exColumn = field.getAnnotation(ExColumn.class);
    return exColumn != null ? exColumn.columnLabel() : null;
  }

  private static List<Object> getRecordContent(Object csvDto, Long no) {
    List<Object> recordContent = new ArrayList<>();
    recordContent.add(no);
    TreeMap<Integer, String> csvRecordContentSorted = new TreeMap();
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> csvDtoMap = objectMapper.convertValue(csvDto, Map.class);
    List<Field> csvDtoFieldList = getFieldList(csvDto.getClass());
    Iterator var7 = csvDtoFieldList.iterator();

    while(var7.hasNext()) {
      Field field = (Field)var7.next();
      if (!field.getName().toUpperCase().contains("serialVersion".toUpperCase())) {
        String fieldTranslatedValue = translateField(field, csvDtoMap.get(field.getName()));
        if (fieldTranslatedValue != null) {
          csvRecordContentSorted.put(field.getAnnotation(ExColumn.class).order(), fieldTranslatedValue);
        }
      }
    }

    var7 = csvRecordContentSorted.entrySet().iterator();

    while(var7.hasNext()) {
      Entry<Integer, String> entry = (Entry)var7.next();
      recordContent.add(entry.getValue());
    }

    return recordContent;
  }

  private static String translateField(Field field, Object fieldValue) {
    ExColumn exColumn = field.getAnnotation(ExColumn.class);
    if (exColumn != null) {
      if (fieldValue == null) {
        return "";
      } else {
        String returnValue;
        if (!StringUtils.isEmpty(exColumn.dateFormat())) {
          Date date = new Date(Long.parseLong(fieldValue.toString()));
          SimpleDateFormat format = new SimpleDateFormat(exColumn.dateFormat());
          format.setLenient(false);
          returnValue = format.format(date);
          return returnValue;
        } else if (!StringUtils.isEmpty(exColumn.numberFormat()) && fieldValue instanceof BigDecimal) {
          DecimalFormat format = new DecimalFormat(exColumn.numberFormat());
          returnValue = format.format(fieldValue);
          return returnValue;
        } else {
          returnValue = fieldValue.toString();
          return returnValue;
        }
      }
    } else {
      return null;
    }
  }

  /**
   * Gets http headers.
   *
   * @param fileName the file name
   * @return the http headers
   */
  public static HttpHeaders getHttpHeaders(String fileName) {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.TEXT_PLAIN);
    responseHeaders.set("Content-Disposition", "attachment; filename=" + fileName + ".csv");
    responseHeaders.set("responseType", "Async");
    return responseHeaders;
  }

  /**
   * Gets xlsx http headers.
   *
   * @param fileName the file name
   * @return the xlsx http headers
   */
  public static HttpHeaders getXlsxHttpHeaders(String fileName) {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
    responseHeaders.set("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
    responseHeaders.set("responseType", "Async");
    return responseHeaders;
  }
}
