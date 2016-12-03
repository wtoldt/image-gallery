package com.wtoldt.domain;

import java.nio.file.Path;

public class TrashedImage extends AbstractRatedImage {

	public TrashedImage(Path image) {
		super(image);
	}

	@Override
	public String getRating() {
		return "trash";
	}

}
