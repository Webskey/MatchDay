package org.webskey.matchday.builders;

import org.webskey.matchday.dto.UsersDto;

public class UsersDtoBuilder {


	private static UsersDto usersDto;

	public static void build() {
		usersDto = new UsersDto();
		usersDto.setId(2);
		usersDto.setUsername("user");
		usersDto.setPassword("password");
		usersDto.setEmail("yerbashop.project@gmail.com");
	}

	public static UsersDto get() {
		build();
		return usersDto;
	}
}
