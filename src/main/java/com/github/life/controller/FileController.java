package com.github.life.controller;

import com.github.life.models.State;
import com.github.life.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	@PostMapping("/upload-file")
	public ResponseEntity<String> uploadFile(
			@RequestParam MultipartFile file,
			@RequestParam int cols,
			@RequestParam int rows) throws IOException {
		return ResponseEntity.ok(fileService.uploadFile(file, cols, rows));
	}

	@GetMapping("/get-state")
	public ResponseEntity<State> getState() {
		return ResponseEntity.ok(fileService.getState());
	}

}
