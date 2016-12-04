package com.wtoldt.controller.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wtoldt.dao.ImageDao;
import com.wtoldt.domain.AbstractRatedImage;

@CrossOrigin
@RestController
public class ImageApiController {

	@Autowired
	private ImageDao imageDao;

	@RequestMapping("/api/images")
	public Map<String, String> getImage() throws IOException {

		final AbstractRatedImage image = imageDao.getImage();
		return formatResponse(image);
	}

	@RequestMapping("/api/images/{id}")
	public Map<String, String> getImage(
			@PathVariable Integer id,
			@RequestParam(name="direction", required=false) Integer direction) throws IOException {

		AbstractRatedImage image = imageDao.getImage(id);
		if (direction != null) {
			image = imageDao.getNextNonTrashedImage(id, direction);
		}
		return formatResponse(image);
	}

	@RequestMapping("/api/images/{id}/rate")
	public Map<String, String> rateImage(
			@PathVariable Integer id,
			@RequestParam(name="rating", required=true) String rating) throws IOException {

		final AbstractRatedImage image = imageDao.rateImage(id, rating);
		return formatResponse(image);
	}

	private Map<String, String> formatResponse(AbstractRatedImage image) throws IOException {
		final Map<String, String> responseMap = new HashMap<>();
		final String path = image.getOriginal().toString().replaceAll("src\\\\main\\\\resources\\\\static", "");

		responseMap.put("url", path);
		responseMap.put("height", String.valueOf(image.getHeight()));
		responseMap.put("width", String.valueOf(image.getWidth()));
		responseMap.put("rating", image.getRating());
		responseMap.put("id", String.valueOf(image.getId()));
		responseMap.put("total", String.valueOf(imageDao.getTotalImages()));

		return responseMap;
	}
}
