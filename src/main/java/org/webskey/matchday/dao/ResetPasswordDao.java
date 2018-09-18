package org.webskey.matchday.dao;

import org.springframework.data.repository.CrudRepository;
import org.webskey.matchday.entities.ResetPasswordEntity;

public interface ResetPasswordDao extends CrudRepository<ResetPasswordEntity, String> {}
