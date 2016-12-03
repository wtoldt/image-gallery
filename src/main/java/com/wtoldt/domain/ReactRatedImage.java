package com.wtoldt.domain;

import java.nio.file.Path;

public class ReactRatedImage extends AbstractRatedImage {

	public ReactRatedImage(Path image) {
		super(image);
	}

	@Override
	public String getRating() {
		return "react";
	}

}
