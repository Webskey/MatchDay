package org.webskey.matchday.builders;

import java.util.Date;
import java.util.Optional;

import org.webskey.matchday.entities.ResetPasswordEntity;

public class ResetPasswordEntityBuilder {

	private static ResetPasswordEntity resetPasswordEntity;

	private static void buildNow() {
		resetPasswordEntity = new ResetPasswordEntity();
		resetPasswordEntity.setUsername("user");
		resetPasswordEntity.setToken("321-token-123");
		resetPasswordEntity.setDate(new Date());		
	}

	public static Optional<ResetPasswordEntity> getNow() {
		buildNow();
		return Optional.of(resetPasswordEntity);
	}

	private static void buildPast() {
		resetPasswordEntity = new ResetPasswordEntity();
		resetPasswordEntity.setUsername("user");
		resetPasswordEntity.setToken("321-token-123");
		Date date = new Date();
		date.setTime(date.getTime() - 96400000);
		//86400000ms = 24h
		resetPasswordEntity.setDate(date);
	}

	public static Optional<ResetPasswordEntity> getPast() {
		buildPast();
		return Optional.of(resetPasswordEntity);
	}
}
