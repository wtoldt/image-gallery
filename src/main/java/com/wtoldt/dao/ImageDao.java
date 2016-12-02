package com.wtoldt.dao;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class ImageDao {


	private final List<Path> images = new ArrayList<>();

	public ImageDao() throws IOException {
		final Path imgDir = Paths.get("src/main/resources/static/images");

		try (DirectoryStream<Path> stream =
				Files.newDirectoryStream(imgDir, "*.{jpg,png,gif,jpeg}")) {


			for (final Path entry: stream) {
				final Path destinationFile = Paths.get("src/main/resources/static/images", entry.getFileName().toString());

				Files.copy(entry, destinationFile, StandardCopyOption.REPLACE_EXISTING);
				images.add(destinationFile);

			}
		}

		Collections.shuffle(images);
	}

	public Path getImage() {
		final Random random = new Random();
		final int randomIndex = random.nextInt(images.size());
		return images.get(randomIndex);
	}

	public Path getImage(int index) {
		return images.get(index);
	}
}
