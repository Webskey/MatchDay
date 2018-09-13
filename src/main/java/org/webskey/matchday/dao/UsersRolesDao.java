package org.webskey.matchday.dao;

import org.springframework.data.repository.CrudRepository;
import org.webskey.matchday.entities.UsersRolesEntity;

public interface UsersRolesDao extends CrudRepository<UsersRolesEntity, Integer> {}
