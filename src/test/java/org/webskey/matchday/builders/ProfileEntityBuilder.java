package org.webskey.matchday.builders;

import org.webskey.matchday.entities.ProfileEntity;

public class ProfileEntityBuilder {

	private static ProfileEntity profileEntity;
	
	public static void build() {
		profileEntity = new ProfileEntity();
		profileEntity.setId(2);
		profileEntity.setFirstname("Firstname");
		profileEntity.setLastname("Lastname");
		profileEntity.setPhoneNumber("123-456-789");
		profileEntity.setDateOfBirth("15-02-1999");
		profileEntity.setCountry("Poland");
		profileEntity.setCity("Warsaw");
		profileEntity.setAdress("Aleje Jerozolimskie 15/8");
		profileEntity.setZipcode("11-222");
	}
	
	public static ProfileEntity get() {
		build();
		return profileEntity;
	}
}
