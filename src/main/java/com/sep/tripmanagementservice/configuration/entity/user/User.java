package com.sep.tripmanagementservice.configuration.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.sep.tripmanagementservice.configuration.enums.Roles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

	@Column(name = "contact_no", nullable = false)
	private String contactNo;

	@Column(name = "address_line_1", nullable = true)
	private String addressLine1;

	@Column(name = "address_line_2", nullable = true)
	private String addressLine2;

	@Column(name = "address_line_3", nullable = true)
	private String addressLine3;

	@Column(name = "user_name", nullable = false, unique = true)
	private String userName;

	@Column(name = "password", nullable = false)
	private String password;

//	@ManyToMany
//	@LazyCollection(LazyCollectionOption.FALSE)
//	private Set<Role> role;

	@Enumerated(EnumType.STRING)
	private Roles role;

	@Transient
	private String masterToken;

	@Transient
	private String authToken;

	public String getOriginalUsername() {
		return userName;
	}
}
