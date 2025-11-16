package uk.ac.ed.acp.cw2.dto.request;

import lombok.Getter;
import lombok.Setter;

/*
    A java request DTO representing a query request that can have an attribute, value, and operator
 */
@Getter
@Setter
public class QueryRequest {
    private String attribute;
    private String operator;
    private String value;

    public QueryRequest() {}
}
