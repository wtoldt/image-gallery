package com.wtoldt.domain;

import java.nio.file.Path;

public abstract class AbstractRatedImage {

	private final Path original;
	private Path image;
	private int id;
	private int height;
	private int width;

	public AbstractRatedImage(Path image) {
		super();
		this.original = image;
		this.image = image;
	}

	public abstract String getRating();

	public Path getImage() {
		return image;
	}

	public void setImage(Path image) {
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Path getOriginal() {
		return original;
	}

}
