package uk.ac.ed.acp.cw2.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Query {
    private String attribute;
    private String operator;
    private String value;

    public Query() {}

}
