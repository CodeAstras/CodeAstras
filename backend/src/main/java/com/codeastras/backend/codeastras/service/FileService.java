package com.codeastras.backend.codeastras.service;

import com.codeastras.backend.codeastras.Exception.ResourceNotFoundException;
import com.codeastras.backend.codeastras.entity.ProjectFile;
import com.codeastras.backend.codeastras.repository.ProjectFileRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    private final ProjectFileRepository fileRepo;

    public FileService(ProjectFileRepository fileRepo) {
        this.fileRepo = fileRepo;
    }

    public List<ProjectFile> findAllByProjectId(UUID projectId) {
        return fileRepo.findByProjectId(projectId);
    }

    public ProjectFile getFile(UUID projectId, String path) {
        ProjectFile file = fileRepo.findByProjectIdAndPath(projectId, path);
        if(file == null) {
            throw new ResourceNotFoundException("File not found");
        }
        return file;
    }

    public ProjectFile savedFileContent(UUID projectId, String path, String content) {
        ProjectFile file = fileRepo.findByProjectIdAndPath(projectId, path);
        if (file == null) {
            throw new ResourceNotFoundException("File not found" + path);
        }

        file.setContent(content);
        file.setUpdatedAt(Instant.now());
        ProjectFile saved = fileRepo.save(file);


        return saved;
    }
}
