package com.sep.tripmanagementservice.configuration.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripCategoryDto {

    private Long id;
    private String categoryName;
    private String code;
    private String description;
    private boolean status;
    private LocalDateTime addedAt;
    private LocalDateTime removedAt;
}
