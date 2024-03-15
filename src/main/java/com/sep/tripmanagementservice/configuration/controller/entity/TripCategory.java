package com.sep.tripmanagementservice.configuration.controller.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TRIP_CATEGORIES")
public class TripCategory {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(name="category_name", nullable=false)
    private String category_name;
    @Column(name="description", nullable=false)
    private String description;
    @Column(name="code", nullable=false)
    private String code;
    @Column(name="status", nullable=false)
    private boolean status;
    @Column(name="added_at", nullable=false)
    private LocalDateTime added_at;
    @Column(name="removed_at", nullable=true)
    private LocalDateTime removed_at;

}
