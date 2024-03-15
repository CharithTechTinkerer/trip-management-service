package com.sep.tripmanagementservice.configuration.dto.tripcategory;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TripCategoryDto {

    private UUID id;
    private String category_name;
    private String code;
    private String description;
    private boolean status;
    private LocalDateTime added_at;
    private LocalDateTime removed_at;
}