package com.codeastras.backend.codeastras.controller;

import com.codeastras.backend.codeastras.entity.ProjectFile;
import com.codeastras.backend.codeastras.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/projects")
@CrossOrigin(origins = "*")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public List<ProjectFile> getProjectFiles(@PathVariable UUID projectId) {
        return fileService.findAllByProjectId(projectId);
    }

    @GetMapping(path ="/{projectId}/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectFile> getFile (
        @PathVariable UUID projectId,
        @RequestParam String path
    ) {
        ProjectFile file = fileService.getFile(projectId, path);
        return ResponseEntity.ok(file);
    }

    public ResponseEntity<ProjectFile> saveFile(
            @PathVariable UUID projectId,
            @RequestParam String path,
            @RequestBody String content
    ) {
        ProjectFile updated = fileService.savedFileContent(projectId, path, content);
        return ResponseEntity.ok(updated);
    }
}
