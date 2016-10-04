package com.wtoldt.controller.api;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wtoldt.dao.ImageDao;

@RestController
public class ImageApiController {

	@Autowired
	private ImageDao imageDao;

	@RequestMapping("/api/images")
	public Map<String, String> getImage() {
		final String path = String.format("/images/%s", imageDao.getImage().getFileName().toString());
		return Collections.singletonMap("image", path);
	}
}
