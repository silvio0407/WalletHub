package com.efe.enumeration;

public enum SituationDurationEnum {

	HOURLY(100, "hourly"),
	DAILY(250, "daily");
	
	private Integer id;

	private String description;

	SituationDurationEnum(Integer id, String description) {
		this.id = id;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
	
}
