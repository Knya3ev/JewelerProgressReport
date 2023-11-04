package com.example.JewelerProgressReport.util.map;

import com.example.JewelerProgressReport.jewelry.Jewelry;
import com.example.JewelerProgressReport.jewelry.response.JewelryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface JewelryMapper {

    @Mapping(target = "resizes", expression = "java(jewelry.getJewelryResizes().stream().map(i -> i.getResize().getRingResizing()).toList())")
    @Mapping(target = "jewelleryProduct", expression = "java(jewelry.getJewelleryProduct().getRu())")
    JewelryResponse toJewelryResponse(Jewelry jewelry);

}
