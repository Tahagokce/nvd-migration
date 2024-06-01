package com.nvdmigration.nvd.cpe.service.impl;


import com.nvdmigration.nvd.cpe.dto.CpeItemDto;
import com.nvdmigration.nvd.cpe.dto.CpeItemMetadataDto;
import com.nvdmigration.nvd.cpe.dto.CpeReferenceDto;
import com.nvdmigration.nvd.cpe.dto.CpeTitleDto;
import lombok.experimental.UtilityClass;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class CpeXmlParser {
    private final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();

    public List<CpeItemDto> parse(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = FACTORY.newDocumentBuilder();
        Document document = builder.parse(file);
        Element documentElement = document.getDocumentElement();
        validateNodeName(documentElement);
        return parseCpeItems(documentElement);
    }

    public List<CpeItemDto> parseCpeItems(Element element) {
        NodeList cpeItems = element.getElementsByTagName("cpe-item");
        List<CpeItemDto> cpeItemDtoList = new ArrayList<>();
        for (int i = 0; i < cpeItems.getLength(); i++) {
            Element cpeItemElement = (Element) cpeItems.item(i);

            String cpeItemName = cpeItemElement.getAttribute("name");
            CpeTitleDto cpeTitleDto = parseCpeItemTitle(cpeItemElement);
            List<CpeReferenceDto> referenceDtoList = parseCpeReferances(cpeItemElement);
            CpeItemMetadataDto cpeItemMetadataDto = parseCpeItemMetadata(cpeItemElement);

            CpeItemDto cpeItemDto = new CpeItemDto();
            cpeItemDto.setName(cpeItemName);
            cpeItemDto.setTitle(cpeTitleDto);
            cpeItemDto.setReferences(referenceDtoList);
            cpeItemDto.setMetadata(cpeItemMetadataDto);
            cpeItemDtoList.add(cpeItemDto);
        }

        return cpeItemDtoList;
    }

    public CpeTitleDto parseCpeItemTitle(Element element) {
        NodeList titleElement = element.getElementsByTagName("title");

        CpeTitleDto cpeTitleDto = new CpeTitleDto();

        Element itemTitle = (Element) titleElement.item(0);
        String value = itemTitle.getTextContent();
        String lang = itemTitle.getAttribute("lang");

        cpeTitleDto.setLang(lang);
        cpeTitleDto.setValue(value);

        return cpeTitleDto;
    }

    public CpeItemMetadataDto parseCpeItemMetadata(Element element) {
        NodeList metadataNodeList = element.getElementsByTagName("meta:item-metadata");

        CpeItemMetadataDto metadataDto = new CpeItemMetadataDto();

        Element metadata = (Element) metadataNodeList.item(0);

        String modificationDate = metadata.getAttribute("modification-date");
        String status = metadata.getAttribute("status");
        String nvdId = metadata.getAttribute("nvd-id");

        metadataDto.setNvdId(nvdId);
        metadataDto.setStatus(status);
        metadataDto.setModificationDate(modificationDate);

        return metadataDto;
    }

    public List<CpeReferenceDto> parseCpeReferances(Element element) {
        NodeList referencesElement = element.getElementsByTagName("references");

        List<CpeReferenceDto> referenceDtoList = new ArrayList<>();
        for (int g = 0; g < referencesElement.getLength(); g++) {
            Element referance = (Element) referencesElement.item(g);

            NodeList referanceItems = referance.getElementsByTagName("reference");
            for (int h = 0; h < referanceItems.getLength(); h++) {
                Element referanceItem = (Element) referanceItems.item(h);
                String value = referanceItem.getTextContent();
                String href = referanceItem.getAttribute("href");

                CpeReferenceDto referenceDto = new CpeReferenceDto();
                referenceDto.setHref(href);
                referenceDto.setValue(value);
                referenceDtoList.add(referenceDto);
            }
        }

        return referenceDtoList;
    }
    private void validateNodeName(Element element) {
        if (Objects.isNull(element)) {
            throw new NullPointerException();
        }

        if (!element.getNodeName().equals("cpe-list")) {
            throw new IllegalArgumentException(element + " has wrong node name. Should be " + "cpe-list");
        }
    }
}
