package org.webskey.matchday.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.webskey.matchday.entities.UsersEntity;

public interface UsersDao extends CrudRepository<UsersEntity, Integer> {
	Optional<UsersEntity> findByUsername(String username);
	Optional<UsersEntity> findByEmail(String email);
}
