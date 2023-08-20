package com.lib.downloadxlsx.example.controller;

import com.lib.downloadxlsx.common.ExDownloadHandler;
import com.lib.downloadxlsx.example.impl.ExExecute;
import com.lib.downloadxlsx.example.impl.RequestQueryImp;
import com.lib.downloadxlsx.example.impl.XlsxExecute;
import com.lib.downloadxlsx.inf.RequestQueryIf;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;

/**
 * The type Download csv controller.
 */
@RestController
@RequestMapping("/api/")
public class DownloadCsvController {

    @Resource(name = "csvExecute1")
    private ExExecute csvExecute;

    @Resource(name = "xlsx1")
    private XlsxExecute xlsxExecute;

    /**
     * Download checked all csv response entity.
     *
     * @return the response entity
     */
    @GetMapping("csv1")
    public ResponseEntity<StreamingResponseBody> downloadCheckedAllCsv() {
        RequestQueryImp build = RequestQueryImp.builder().build();
        System.out.println(build);
        StreamingResponseBody streamingResponseBody = csvExecute.execute(build);
        HttpHeaders responseHeaders = ExDownloadHandler.getHttpHeaders("filename");
        return new ResponseEntity<>(streamingResponseBody, responseHeaders, HttpStatus.OK);
    }

    /**
     * Download checked all c 00 sv response entity.
     *
     * @return the response entity
     */
    @GetMapping("xlsx1")
    public ResponseEntity<StreamingResponseBody> downloadCheckedAllC00sv(@RequestParam String by, @RequestParam String order ) {
        RequestQueryImp build = RequestQueryImp.builder().by(by).order(order).build();
        System.out.println(build);
        StreamingResponseBody streamingResponseBody = xlsxExecute.execute(build);
        HttpHeaders responseHeaders = ExDownloadHandler.getXlsxHttpHeaders("filename");
        return new ResponseEntity<>(streamingResponseBody, responseHeaders, HttpStatus.OK);
    }

}
