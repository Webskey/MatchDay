package org.webskey.matchday.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.webskey.matchday.entities.UsersEntity;
import org.webskey.matchday.entities.UsersRolesEntity;

public class UsersEntityBuilder {

	private static UsersEntity usersEntity;
	
	 public static void build() {
		 usersEntity = new UsersEntity();
		 usersEntity.setId(2);
		 usersEntity.setUsername("user");
		 usersEntity.setPassword("password");
		 usersEntity.setEmail("yerbashop.project@gmail.com");
		 
		 List<UsersRolesEntity> usersRoles = new ArrayList<>();
		 usersRoles.add(UsersRolesEntityBuilder.get());
		 usersEntity.setUsersRoles(usersRoles);
		 
		 usersEntity.setProfileEntity(ProfileEntityBuilder.get().get());
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
