package org.webskey.matchday.builders;

import java.util.List;
import java.util.Optional;

import org.webskey.matchday.entities.UsersEntity;

public class UsersEntityBuilder {

	private static UsersEntity usersEntity;
	
	 public static void build() {
		 usersEntity = new UsersEntity();
		 usersEntity.setId(2);
		 usersEntity.setUsername("user");
		 usersEntity.setPassword("password");
		 usersEntity.setEmail("yerbashop.project@gmail.com");
		 usersEntity.setUsersRoles(List.of(UsersRolesEntityBuilder.get()));
	 }
	 
	public static Optional<UsersEntity> get() {
		build();
		return Optional.of(usersEntity);
	}
	
	public static Optional<UsersEntity> getWithNullRoles() {
		build();
		usersEntity.setUsersRoles(null);
		return Optional.of(usersEntity);
	}
}
