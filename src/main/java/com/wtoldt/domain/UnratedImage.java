package com.wtoldt.domain;

import java.nio.file.Path;

public class UnratedImage extends AbstractRatedImage {

	public UnratedImage(Path image) {
		super(image);
	}

	@Override
	public String getRating() {
		return "unrated";
	}

}
