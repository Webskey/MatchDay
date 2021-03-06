package org.webskey.matchday.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "USERS_LOGIN")
@Data
public class UsersEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private int id;

	@Column
	private String username;

	@Column
	private String password;
	
	@Column
	private String email;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "usersEntity")
	private List<UsersRolesEntity> usersRoles;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "usersEntity", fetch = FetchType.LAZY)
	private ProfileEntity profileEntity;
}
