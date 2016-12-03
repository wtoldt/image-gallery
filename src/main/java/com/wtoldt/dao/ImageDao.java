package com.wtoldt.dao;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.wtoldt.domain.AbstractRatedImage;
import com.wtoldt.domain.LovedImage;
import com.wtoldt.domain.ReactRatedImage;
import com.wtoldt.domain.TrashedImage;
import com.wtoldt.domain.UnratedImage;

@Component
public class ImageDao {


	private final List<AbstractRatedImage> images = new ArrayList<>();

	public ImageDao() throws IOException {
		final Path unratedImgDir = Paths.get("src/main/resources/static/images/unrated");
		final Path lovedImgDir = Paths.get("src/main/resources/static/images/loved");
		final Path trashedImgDir = Paths.get("src/main/resources/static/images/trashed");
		final Path reactionRatedImgDir = Paths.get("src/main/resources/static/images/reactionRated");

		try (DirectoryStream<Path> stream =
				Files.newDirectoryStream(unratedImgDir, "*.{jpg,png,gif,jpeg}")) {


			for (final Path entry: stream) {
				final Path destinationFile = Paths.get("src/main/resources/static/images", entry.getFileName().toString());

				//Files.copy(entry, destinationFile, StandardCopyOption.REPLACE_EXISTING);
				images.add(new UnratedImage(entry));

			}
		}

		try (DirectoryStream<Path> stream =
				Files.newDirectoryStream(lovedImgDir, "*.{jpg,png,gif,jpeg}")) {

			for (final Path entry: stream) {
				images.add(new LovedImage(entry));
			}
		}

		try (DirectoryStream<Path> stream =
				Files.newDirectoryStream(trashedImgDir, "*.{jpg,png,gif,jpeg}")) {

			for (final Path entry: stream) {
				images.add(new TrashedImage(entry));
			}
		}

		try (DirectoryStream<Path> stream =
				Files.newDirectoryStream(reactionRatedImgDir, "*.{jpg,png,gif,jpeg}")) {

			for (final Path entry: stream) {
				images.add(new ReactRatedImage(entry));
			}
		}

		Collections.shuffle(images);
	}

	public AbstractRatedImage getImage() {
		final Random random = new Random();
		final int randomIndex = random.nextInt(images.size());
		final AbstractRatedImage image = images.get(randomIndex);
		image.setId(randomIndex);
		return image;
	}

	public AbstractRatedImage getImage(int index) {
		AbstractRatedImage image;

		//if index is negative, roll over
		if (index < 0) {
			image = images.get(images.size()-1);
			image.setId(images.size()-1);

		//if index is too big, roll over
		} else if (index > images.size()-1) {
			image = images.get(0);
			image.setId(0);
		} else {
			image = images.get(index);
			image.setId(index);
		}
		return image;
	}

	public AbstractRatedImage getNextNonTrashedImage(int index, int mode) {
		AbstractRatedImage image = getImage(index);
		final int i = image.getId();
		if (image.getRating() == "trash") {
			if (mode < 0) {
				image = getNextNonTrashedImage(i -1, mode);
			} else if (mode > 0) {
				image = getNextNonTrashedImage(i +1, mode);
			}
		}
		return image;
	}
}
