package org.webskey.matchday.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "RESET_PASSWORD")
@Data
public class ResetPasswordEntity {
	@Id
	@Column
	private String username;

	@Column
	private String token;

	@Column(name="date_sent")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
}
