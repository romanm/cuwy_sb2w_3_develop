package org.cuwy1.model1;

public class Patient {
	private Integer id;
	private String name;

	@Override
	public String toString() {
		return "Patient [name=" + name + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}
