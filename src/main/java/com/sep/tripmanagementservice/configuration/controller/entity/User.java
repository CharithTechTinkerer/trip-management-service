package com.sep.tripmanagementservice.configuration.controller.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sep.tripmanagementservice.configuration.enums.Gender;
import com.sep.tripmanagementservice.configuration.enums.Roles;
import com.sep.tripmanagementservice.configuration.enums.Salutation;
import com.sep.tripmanagementservice.configuration.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "user_name", nullable = false, unique = true)
	private String userName;

	@Column(name = "nic")
	private String nic;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender;

	@Enumerated(EnumType.STRING)
	@Column(name = "salutation", nullable = false)
	private Salutation salutation;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "contact_no", nullable = false)
	private String contactNo;

	@Column(name = "address_line_1")
	private String addressLine1;

	@Column(name = "address_line_2")
	private String addressLine2;

	@Column(name = "address_line_3")
	private String addressLine3;

	@Column(name = "password", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Roles role;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "created_date", nullable = false)
	private LocalDateTime createdDate;

	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	@Transient
	private String masterToken;

	@Transient
	private String authToken;
}
