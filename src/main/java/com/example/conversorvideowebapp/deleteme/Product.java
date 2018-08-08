package com.example.conversorvideowebapp.deleteme;

public class Product {

	private String id;
	private String name;
	private String description;
	private String type;
	private String category;
	private Double price;

	public Product(String id, String name, String description, String type, String category, Double price) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.type = type;
		this.category = category;
		this.price = price;
	}

	public Product() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
