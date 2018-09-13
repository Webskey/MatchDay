package org.webskey.matchday.dao;

import org.springframework.data.repository.CrudRepository;
import org.webskey.matchday.entities.UsersEntity;

public interface UsersDao extends CrudRepository<UsersEntity, Integer> {
	UsersEntity findByUsername(String username);
}
