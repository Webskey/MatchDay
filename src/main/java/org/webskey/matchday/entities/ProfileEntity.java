package org.webskey.matchday.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "USERS_PROFILE")
@Data
public class ProfileEntity {
	@Id
	@Column(name="users_id")
	private int id;

	@Column
	private String firstname;

	@Column
	private String lastname;

	@Column(name="phone_number")
	private String phoneNumber;

	@Column(name="date_of_birth")
	private String dateOfBirth;

	@Column
	private String country;	

	@Column
	private String city;

	@Column
	private String adress;

	@Column
	private String zipcode;	

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "users_id")
	private UsersEntity usersEntity;
}
