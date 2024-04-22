package com.sep.tripmanagementservice.configuration.vo;

import java.util.List;

import com.sep.tripmanagementservice.configuration.entity.TripCategory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripCategoryResponseVo {

	Long count;
	List<TripCategory> tripCategories;

}
