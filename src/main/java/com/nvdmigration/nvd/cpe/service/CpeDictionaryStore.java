package com.nvdmigration.nvd.cpe.service;

import com.nvdmigration.nvd.cpe.dto.CpeItemDto;
import com.nvdmigration.nvd.cpe.parser.CpeXmlParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;


@Service
@Slf4j
@RequiredArgsConstructor
public class CpeDictin {
    @Value("${cpe.file-path}")
    private String filePath;

    @Value("${cpe.file-name}")
    private String fileName;

    @Value("${cpe.is-migrate}")
    private boolean isMigrate;

    private List<CpeItemDto> cpeItemDtoList;

    @PostConstruct
    public void initializeMigrate() {
        if (isMigrate) {
            readFileAndConvertToPojo();
        }
    }

    private void readFileAndConvertToPojo()  {
        try {
            File file = new File(filePath + fileName);
            cpeItemDtoList = CpeXmlParser.parse(file);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            log.error(e.getMessage());
            cpeItemDtoList = new ArrayList<>();
        }
    }

    public List<CpeItemDto> subList(int start, int end){
        Objects.requireNonNull(cpeItemDtoList);
        return cpeItemDtoList.subList(start, end);
    }

    public List<CpeItemDto> all(){
        return cpeItemDtoList;
    }
    
    public int size(){
        return cpeItemDtoList.size();
    }

    public void setCpeItemDtoList(List<CpeItemDto> cpeItemDtoList) {
        this.cpeItemDtoList = cpeItemDtoList;
    }
}
