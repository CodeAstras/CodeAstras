package com.codeastras.backend.codeastras.service;

import com.codeastras.backend.codeastras.exception.ResourceNotFoundException;
import com.codeastras.backend.codeastras.exception.ForbiddenException;
import com.codeastras.backend.codeastras.entity.Project;
import com.codeastras.backend.codeastras.entity.ProjectFile;
import com.codeastras.backend.codeastras.repository.ProjectFileRepository;
import com.codeastras.backend.codeastras.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    private final ProjectFileRepository fileRepo;
    private final ProjectRepository projectRepo;

    public FileService(ProjectFileRepository fileRepo, ProjectRepository projectRepo) {
        this.fileRepo = fileRepo;
        this.projectRepo = projectRepo;
    }

    // internal helper
    private Project getProjectOrThrow(UUID projectId) {
        return projectRepo.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
    }

    private void requireOwner(Project project, UUID userId) {
        if (project.getOwnerId() == null || !project.getOwnerId().equals(userId)) {
            throw new ForbiddenException("You are not the owner of this project");
        }
    }

    // List files (requires owner)
    public List<ProjectFile> findAllByProjectId(UUID projectId, UUID userId) {
        Project project = getProjectOrThrow(projectId);
        requireOwner(project, userId);
        return fileRepo.findByProjectId(projectId);
    }

    // Get single file (requires owner)
    public ProjectFile getFile(UUID projectId, String path, UUID userId) {
        Project project = getProjectOrThrow(projectId);
        requireOwner(project, userId);

        ProjectFile file = fileRepo.findByProjectIdAndPath(projectId, path);
        if (file == null) {
            throw new ResourceNotFoundException("File not found: " + path);
        }
        return file;
    }

    // Save file (requires owner)
    @Transactional
    public ProjectFile saveFileContent(UUID projectId, String path, String content, UUID userId) {
        Project project = getProjectOrThrow(projectId);
        requireOwner(project, userId);

        ProjectFile file = fileRepo.findByProjectIdAndPath(projectId, path);
        if (file == null) {
            throw new ResourceNotFoundException("File not found: " + path);
        }

        file.setContent(content);
        file.setUpdatedAt(Instant.now());
        ProjectFile saved = fileRepo.save(file);

        // Optional: publish FileUpdatedEvent asynchronously here

        return saved;
    }
}
