package com.github.life.services;

import com.github.life.models.State;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileService {

	private final ParserService parserService;

	private State uploadedFile = null;

	public String uploadFile(MultipartFile file, int cols, int rows) throws IOException {
		uploadedFile = parserService.parse(file.getInputStream(), cols, rows);
		return file.getOriginalFilename();
	}

	public State getState() throws FileNotFoundException {
		if (Objects.isNull(uploadedFile)) {
			throw new FileNotFoundException("Please upload a *.lif file first.");
		}

		return uploadedFile;
	}
}
