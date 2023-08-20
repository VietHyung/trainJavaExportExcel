package com.lib.downloadxlsx.example.impl;

import com.lib.downloadxlsx.common.CsvDownloadStreamingResponseBody;
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
 * The type Ex execute.
 */
@Data
@Component("csvExecute1")
public class ExExecute implements ExExecuteIf {

  @Value("${export.total}")
  private Long totalExport;

  @Resource
  private RepositoryCsv repositoryCsv;

  /**
   * The Csv download option.
   */


  @Override
  public StreamingResponseBody execute(RequestQueryIf requestQuery) {
    if (totalExport != null) {
      this.exDownloadOption.setTotalExport(totalExport);
    }
    System.out.println(requestQuery);
    return new CsvDownloadStreamingResponseBody(this, CsvEntity.class, exDownloadOption, requestQuery);
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


