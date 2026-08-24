package com.vaadin.demo.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class SourceService {

    public String getSource(Class<?> clazz) {
        String path = "source/" + clazz.getName().replace('.', '/') + ".java";
        try {
            ClassPathResource resource = new ClassPathResource(path);
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "// Source not available for " + clazz.getSimpleName() + "\n// Path: " + path;
        }
    }

    public String getGitHubUrl(Class<?> clazz) {
        String path = "src/main/java/" + clazz.getName().replace('.', '/') + ".java";
        return "https://github.com/vaadin/vaadin-demo/blob/main/" + path + "#L1";
    }
}
