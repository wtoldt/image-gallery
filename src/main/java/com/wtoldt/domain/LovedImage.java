package com.wtoldt.domain;

import java.nio.file.Path;

public class LovedImage extends AbstractRatedImage {

	public LovedImage(Path image) {
		super(image);
	}

	@Override
	public String getRating() {
		return "love";
	}

}
