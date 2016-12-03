package com.wtoldt.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wtoldt.dao.ImageDao;
import com.wtoldt.domain.AbstractRatedImage;

@CrossOrigin
@RestController
public class ImageApiController {

	@Autowired
	private ImageDao imageDao;

	@RequestMapping("/api/images")
	public Map<String, String> getImage() {
		final Map<String, String> responseMap = new HashMap<>();
		final AbstractRatedImage image = imageDao.getImage();

		final String path = image.getImage().toString().replaceAll("src\\\\main\\\\resources\\\\static", "");
		responseMap.put("url", path);
		responseMap.put("rating", image.getRating());
		responseMap.put("id", String.valueOf(image.getId()));

		return responseMap;
	}

	@RequestMapping("/api/images/{id}")
	public Map<String, String> getImage(@PathVariable Integer id) {
		final Map<String, String> responseMap = new HashMap<>();
		final AbstractRatedImage image = imageDao.getImage(id);

		final String path = image.getImage().toString().replaceAll("src\\\\main\\\\resources\\\\static", "");
		responseMap.put("url", path);
		responseMap.put("rating", image.getRating());
		responseMap.put("id", String.valueOf(image.getId()));

		return responseMap;
	}
}
