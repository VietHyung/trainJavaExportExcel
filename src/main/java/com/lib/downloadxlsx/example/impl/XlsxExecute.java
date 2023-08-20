package com.lib.downloadxlsx.example.impl;

import com.lib.downloadxlsx.common.XlsxDownloadStreamingBody;
import com.lib.downloadxlsx.example.CsvEntity;
import com.lib.downloadxlsx.example.repo.RepositoryCsv;
import com.lib.downloadxlsx.inf.ExExecuteIf;
import com.lib.downloadxlsx.inf.RequestQueryIf;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * The type Xlsx execute.
 */
@Data
@Component("xlsx1")
public class XlsxExecute implements ExExecuteIf {

    @Value("${export.total}")
    private Long totalExport;

    @Resource
    private RepositoryCsv repositoryCsv;

    @Override
    public StreamingResponseBody execute(RequestQueryIf requestQuery) {
        if (totalExport != null) {
            this.exDownloadOption.setTotalExport(totalExport);
        }
        return new XlsxDownloadStreamingBody(this, CsvEntity.class, exDownloadOption, requestQuery);
    }

    @Override
    public Long totalCountData(RequestQueryIf requestQuery) {
        return repositoryCsv.countFindAll("huan");
    }

    @Override
    public List findData(PageRequest pageRequest, RequestQueryIf requestQuery) {
        return repositoryCsv.searchFindAll("huan", pageRequest);
    }
}
