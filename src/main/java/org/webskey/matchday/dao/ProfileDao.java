package org.webskey.matchday.dao;

import org.springframework.data.repository.CrudRepository;
import org.webskey.matchday.entities.ProfileEntity;

public interface ProfileDao extends CrudRepository<ProfileEntity, Integer> {}
