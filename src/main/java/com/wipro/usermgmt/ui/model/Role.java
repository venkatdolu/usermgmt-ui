package com.wipro.usermgmt.ui.model;
import java.io.Serializable;


/**
 * @author Venkat
 * 
 * Description: Role to define for user
 *
 */
public class Role implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;
	

	public Role() {
		super();
	}

	public Role(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
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

}
