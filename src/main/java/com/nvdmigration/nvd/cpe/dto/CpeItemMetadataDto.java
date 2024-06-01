package com.omreon.accountservice.nvd.cpe.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class CpeItemMetadataDto implements Serializable {

    private String nvdId;

    private String status;

    private String modificationDate;

}
