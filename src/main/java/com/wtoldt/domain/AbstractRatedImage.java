package com.wtoldt.domain;

import java.nio.file.Path;

public abstract class AbstractRatedImage {

	private Path image;
	private int id;

	public AbstractRatedImage(Path image) {
		super();
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


}
