package org.webskey.matchday.builders;

import org.webskey.matchday.dto.PasswordDto;

public class PasswordDtoBuilder {

	private static PasswordDto passwordDto;
	
	public static void build() {
		passwordDto = new PasswordDto();
		passwordDto.setPassword("passwordNew");
		passwordDto.setConfirmPassword("passwordNew");
		passwordDto.setOldPassword("password");
	}
	
	public static PasswordDto get() {
		build();
		return passwordDto;
	}
}
