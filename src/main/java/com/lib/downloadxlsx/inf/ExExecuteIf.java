package com.lib.downloadxlsx.inf;

import com.lib.downloadxlsx.common.ExDownloadOption;
import com.lib.downloadxlsx.example.CsvEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

/**
 * The interface Ex execute if.
 */
public interface ExExecuteIf {

    /**
     * The constant exDownloadOption.
     */
    ExDownloadOption exDownloadOption = ExDownloadOption.DEFAULT_DOWNLOAD_OPTION;

    /**
     * Execute streaming response body.
     *
     * @param requestQuery the request query
     * @return the streaming response body
     */
    StreamingResponseBody execute(RequestQueryIf requestQuery);

    /**
     * Total count data long.
     *
     * @param requestQueryIf the request query if
     * @return the long
     */
    Long totalCountData(RequestQueryIf requestQueryIf);

    /**
     * Find data list.
     *
     * @param pageRequest    the page request
     * @param requestQueryIf the request query if
     * @return the list
     */
    List findData(PageRequest pageRequest, RequestQueryIf requestQueryIf);
}
