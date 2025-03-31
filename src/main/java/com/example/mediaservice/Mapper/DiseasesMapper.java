package com.example.mediaservice.Mapper;

import com.example.mediaservice.Entity.Diseases;
import com.example.mediaservice.Entity.Symptoms;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DiseasesMapper {
/*
    @Mapping(source = "symptoms", target = "symptoms_name")
*/
    DiseasesResponse toSymptoms(Diseases entity);
    @IterableMapping(elementTargetType = String.class)
    default List<String> mapSymptoms(List<Symptoms> symptoms) {
        return symptoms.stream()
                .map(Symptoms::getName)
                .collect(Collectors.toList());
    }
}
