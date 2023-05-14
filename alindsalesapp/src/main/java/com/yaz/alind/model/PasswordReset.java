package com.yaz.alind.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the password_resets database table.
 * 
 */
@Entity
@Table(name="password_resets")
@NamedQuery(name="PasswordReset.findAll", query="SELECT p FROM PasswordReset p")
public class PasswordReset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="created_at")
	private Timestamp createdAt;

	private String email;

	private String token;

	public PasswordReset() {
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}