package com.omreon.accountservice.nvd.cpe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class CpeItemDto implements Serializable {

    private String name;

    private CpeReferanceListDto references;

    @JsonProperty("item-metadata")
    private CpeItemMetadataDto itemMetadata;

    private CpeTitle title;
}
