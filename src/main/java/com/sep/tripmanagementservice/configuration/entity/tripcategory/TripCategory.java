package com.sep.tripmanagementservice.configuration.entity.tripcategory;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Trip_Category")
public class TripCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="category_name", nullable=false)
    private String categoryName;
    @Column(name="description", nullable=false)
    private String description;
    @Column(name="code", nullable=false)
    private String code;
    @Column(name="status")
    private boolean status;
    @Column(name="added_at", nullable=false)
    private LocalDateTime addedAt;
    @Column(name="removed_at", nullable=true)
    private LocalDateTime removedAt;

}
