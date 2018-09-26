package com.xqx.user.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="u_user")
public class User {
	@Id
    @GeneratedValue
    private Long id;
    private String name;
    private String password;
    private Boolean forbidden;
    
    public User() {
	}

	public User(Long id, String name, String password,Boolean forbidden) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.forbidden = forbidden;
    }
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getForbidden() {
		return forbidden;
	}

	public void setForbidden(Boolean forbidden) {
		this.forbidden = forbidden;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + ", forbidden=" + forbidden + "]";
	}
}

