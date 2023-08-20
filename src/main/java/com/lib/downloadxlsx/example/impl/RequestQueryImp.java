package com.lib.downloadxlsx.example.impl;

import com.lib.downloadxlsx.inf.RequestQueryIf;
import lombok.Builder;
import lombok.Data;

/**
 * The type Request query imp.
 */
@Builder
@Data
public class RequestQueryImp implements RequestQueryIf {

    /**
     * The constant by.
     */
    private String by = "id";
    /**
     * The constant order.
     */
    private String order = "desc";

    @Override
    public String getOrder() {
        return order;
    }

    @Override
    public String getBy() {
        return by;
    }
}
