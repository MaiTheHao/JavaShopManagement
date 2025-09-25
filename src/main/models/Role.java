package main.models;

public class Role extends Entity {
	protected String description;

	public Role(String name, String description) {
		this.setName(name);
		this.setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
