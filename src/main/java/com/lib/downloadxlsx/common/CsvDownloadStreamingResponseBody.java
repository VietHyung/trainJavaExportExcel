package com.lib.downloadxlsx.common;

import com.lib.downloadxlsx.inf.ExExecuteIf;
import com.lib.downloadxlsx.inf.RequestQueryIf;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * The type Csv download streaming response body.
 */
public class CsvDownloadStreamingResponseBody implements StreamingResponseBody {

    private final ExExecuteIf exExecuteIf;
    private final Class<?> csvDtoClass;
    private final ExDownloadOption optionDto;
    private final RequestQueryIf parameters;
    private final Sort sort;

    /**
     * Instantiates a new Csv download streaming response body.
     *
     * @param exExecuteIf      the ex execute if
     * @param csvDtoClass      the csv dto class
     * @param exDownloadOption the ex download option
     * @param parameters       the parameters
     */
    public CsvDownloadStreamingResponseBody(ExExecuteIf exExecuteIf, Class<?> csvDtoClass, ExDownloadOption exDownloadOption, RequestQueryIf parameters) {
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
        Writer csvWriter = new BufferedWriter(new OutputStreamWriter(outputStream, this.optionDto.getCharset()));
        CSVFormat outputCsvFormat = ExDownloadHandler.csvFormatBuilder(this.optionDto);
        StringWriter stringWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(stringWriter, outputCsvFormat);

        if (this.optionDto.isOutputHeader()) {
            printer.printRecord(ExDownloadHandler.outHeader(this.csvDtoClass));
            csvWriter.write(stringWriter.toString());
            stringWriter.getBuffer().setLength(0);
            csvWriter.flush();
        }

        try (outputStream) {
            long no = 0L;
            Long total = exExecuteIf.totalCountData(parameters);
            int loop = (int) (total / optionDto.getTotalExport());
            for (int i = 0; i <= loop; i++) {
                PageRequest pageRequest = PageRequest.of(i, Math.toIntExact(optionDto.getTotalExport()), sort);
                List resultStream = exExecuteIf.findData(pageRequest, parameters);
                Iterator resultIterator = resultStream.stream().iterator();
                while (resultIterator.hasNext()) {
                    printer.printRecord(ExDownloadHandler.outCsvRecord(resultIterator.next(), ++no));
                    csvWriter.write(stringWriter.toString());
                    stringWriter.getBuffer().setLength(0);
                    csvWriter.flush();
                }
            }
        }
    }

}
