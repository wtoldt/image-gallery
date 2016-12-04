package com.wtoldt.dao;

import java.awt.image.BufferedImage;
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

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.wtoldt.domain.AbstractRatedImage;
import com.wtoldt.domain.LovedImage;
import com.wtoldt.domain.ReactRatedImage;
import com.wtoldt.domain.TrashedImage;
import com.wtoldt.domain.UnratedImage;

/**
 * @author wtoldt
 *
 */
@Component
public class ImageDao {

	final Path srcImgDir = Paths.get("src/main/resources/static/images/src");
	final Path unratedImgDir = Paths.get("src/main/resources/static/images/unrated");
	final Path lovedImgDir = Paths.get("src/main/resources/static/images/loved");
	final Path trashedImgDir = Paths.get("src/main/resources/static/images/trashed");
	final Path reactionRatedImgDir = Paths.get("src/main/resources/static/images/reactionRated");
	private final List<AbstractRatedImage> images = new ArrayList<>();

	public ImageDao() throws IOException {
		final List<AbstractRatedImage> srcImages = new ArrayList<>();
		// copy all images from src into unratedImgDur
		try (DirectoryStream<Path> stream =
				Files.newDirectoryStream(srcImgDir, "*.{jpg,png,gif,jpeg}")) {

			for (final Path entry: stream) {
				final Path destinationFile = Paths.get(unratedImgDir.toString(), entry.getFileName().toString());
				Files.copy(entry, destinationFile, StandardCopyOption.REPLACE_EXISTING);

				//Sets original file as file in src dir
				final UnratedImage image = new UnratedImage(entry);
				//sets the file that will be moved with ratings as unrated file
				image.setImage(destinationFile);

				srcImages.add(image);
			}
		}

		Collections.shuffle(srcImages);
		int i = 0;
		for (final AbstractRatedImage image : srcImages) {

			try {
				final BufferedImage bufferedImage = ImageIO.read(image.getOriginal().toFile());

				//if bufferedImage is null, image is invalid
				if (bufferedImage != null) {
					image.setId(i);
					image.setHeight(bufferedImage.getHeight());
					image.setWidth(bufferedImage.getWidth());
					images.add(image);
					i++;
				}
			} catch (final Exception e) {
				// TODO: handle exception
				System.out.println(image.getOriginal().toString());
			}
		}
	}

	public int getTotalImages() {
		return this.images.size();
	}

	/**
	 * Returns random image from images.
	 * @return Image
	 */
	public AbstractRatedImage getImage() {
		final Random random = new Random();
		final int randomIndex = random.nextInt(images.size());
		final AbstractRatedImage image = images.get(randomIndex);
		return image;
	}

	/**
	 * Handles rollover in the following ways:
	 * <li>If index is negative, returns last image in list.</li>
	 * <li>If index is larger than the list size, returns first image in list.</li>
	 * @param index
	 * @return Image at index (or rolled over image)
	 */
	public AbstractRatedImage getImage(int index) {
		AbstractRatedImage image;

		//if index is negative, roll over
		if (index < 0) {
			image = images.get(images.size()-1);

		//if index is too big, roll over
		} else if (index > images.size()-1) {
			image = images.get(0);
		} else {
			image = images.get(index);
		}
		return image;
	}

	/**
	 * Recursively finds the next non trash image.
	 * If mode is negative, traverse down the list.
	 * If mode is 0 or positive, traverse up the list.
	 * @param index
	 * @param direction
	 * @return Image not rated as trash
	 */
	public AbstractRatedImage getNextNonTrashedImage(int index, int direction) {

		//we use getImage because it handles rollover
		AbstractRatedImage image = getImage(index);
		final int i = image.getId();
		if (image.getRating() == "trash") {
			if (direction < 0) {
				image = getNextNonTrashedImage(i -1, direction);
			} else if (direction >= 0) {
				image = getNextNonTrashedImage(i +1, direction);
			}
		}
		return image;
	}

	/**
	 * Rates an image. Moves image to appropriate location,
	 * updates list.
	 * @param index
	 * @param rating
	 * @return newly rated image
	 * @throws IOException
	 */
	public AbstractRatedImage rateImage(int index, String rating) throws IOException {
		final AbstractRatedImage image = getImage(index);
		AbstractRatedImage ratedImage = image;
		Path movedImage = image.getImage();

		if (rating.equalsIgnoreCase("trash")) {
			movedImage = moveImage(image.getImage(), trashedImgDir);
			ratedImage = new TrashedImage(image.getOriginal());

		} else if (rating.equalsIgnoreCase("love")) {
			movedImage = moveImage(image.getImage(), lovedImgDir);
			ratedImage = new LovedImage(image.getOriginal());

		} else if (rating.equalsIgnoreCase("react")) {
			movedImage = moveImage(image.getImage(), reactionRatedImgDir);
			ratedImage = new ReactRatedImage(image.getOriginal());

		} else if (rating.equalsIgnoreCase("unrated")) {
			movedImage = moveImage(image.getImage(), unratedImgDir);
			ratedImage = new UnratedImage(image.getOriginal());
		}

		images.set(image.getId(), ratedImage);
		ratedImage.setId(image.getId());
		ratedImage.setImage(movedImage);

		return ratedImage;
	}

	/**
	 * Moves image to destination, delete image
	 * and returns destination image.
	 * @param image
	 * @param dest
	 * @return destinationImage
	 * @throws IOException
	 */
	private Path moveImage(Path image, Path dest) throws IOException {
		final Path destinationFile = Paths.get(dest.toString(), image.getFileName().toString());
		Files.copy(image, destinationFile, StandardCopyOption.REPLACE_EXISTING);
		Files.delete(image);
		return destinationFile;
	}
}

