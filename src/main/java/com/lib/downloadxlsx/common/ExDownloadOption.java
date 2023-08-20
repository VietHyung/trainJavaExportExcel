package com.lib.downloadxlsx.common;

import lombok.Data;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * The type Ex download option.
 */
@Data
public class ExDownloadOption {

    static {
        DEFAULT_DOWNLOAD_OPTION = new ExDownloadOption("\r\n", ",", false,
                StandardCharsets.UTF_8, true, '"', null, 1000L);
    }

    /**
     * The constant DEFAULT_DOWNLOAD_OPTION.
     */
    public static final ExDownloadOption DEFAULT_DOWNLOAD_OPTION;

    private String lineSeparator;

    private String columnSeparator;

    private boolean forceQuote;

    private Charset charset;

    private boolean outputHeader;

    private Character quoteCharacter;

    private Character escapeCharacter;

    private Long totalExport;

    /**
     * Instantiates a new Ex download option.
     *
     * @param lineSeparator   the line separator
     * @param columnSeparator the column separator
     * @param forceQuote      the force quote
     * @param charset         the charset
     * @param outputHeader    the output header
     * @param quoteCharacter  the quote character
     * @param escapeCharacter the escape character
     * @param totalExport     the total export
     */
    public ExDownloadOption(String lineSeparator, String columnSeparator, boolean forceQuote, Charset charset,
                            boolean outputHeader, Character quoteCharacter, Character escapeCharacter, Long totalExport) {
        this.lineSeparator = lineSeparator;
        this.columnSeparator = columnSeparator;
        this.forceQuote = forceQuote;
        this.charset = charset;
        this.outputHeader = outputHeader;
        this.quoteCharacter = quoteCharacter;
        this.escapeCharacter = escapeCharacter;
        this.totalExport = totalExport;
    }
}
