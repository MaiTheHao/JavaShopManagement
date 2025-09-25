package main.models;

public class Role extends Entity {
	protected String description;
	protected boolean isDefault;

	public Role(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public Role(String name, String description, boolean isDefault) {
		this.name = name;
		this.description = description;
		this.isDefault = isDefault;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
