package com.mygarden.app.models;

public class Plant {
	private String name;
	private String imagePath;

	public Plant() {
		this.name = "Unknown";
		this.imagePath = "/images/BasePlant.jpeg";
	}

	public Plant(String name, String imagePath) {
		this.name = name;
		this.imagePath = imagePath;
	}

	public String getName() {
		return name;
	}

	public String getImagePath() {
		return imagePath;
	}

	@Override
	public String toString() {
		return name;
	}
}
