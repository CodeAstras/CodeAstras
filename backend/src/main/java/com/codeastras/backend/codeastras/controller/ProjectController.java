package com.codeastras.backend.codeastras.controller;

import com.codeastras.backend.codeastras.dto.CreateProjectRequest;
import com.codeastras.backend.codeastras.entity.Project;
import com.codeastras.backend.codeastras.service.ProjectService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Project createProject(
            @RequestBody CreateProjectRequest req,
            Authentication authentication
    ) {
        // userId is stored as principal via JwtAuthenticationFilter
        UUID userId = (UUID) authentication.getPrincipal();

        return service.createProject(req, userId);
    }
}
