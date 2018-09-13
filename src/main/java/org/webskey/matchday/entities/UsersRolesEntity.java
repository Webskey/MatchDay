package org.webskey.matchday.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "USERS_ROLES")
@Data
public class UsersRolesEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column
	private int users_id;

	@Column
	private String role;

	@ManyToOne
	@JoinColumn(name = "users_id", insertable=false, updatable=false)
	private UsersEntity usersEntity;
}
