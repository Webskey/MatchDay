package org.webskey.matchday.builders;

import org.webskey.matchday.entities.UsersRolesEntity;

public class UsersRolesEntityBuilder {
	
	private static UsersRolesEntity usersRolesEntity;
	
	public static void build() {
		usersRolesEntity = new UsersRolesEntity();
		usersRolesEntity.setId(2);
		usersRolesEntity.setUsers_id(2);
		usersRolesEntity.setRole("USER");
	}
	
	public static UsersRolesEntity get() {
		build();
		return usersRolesEntity;
	}
}
