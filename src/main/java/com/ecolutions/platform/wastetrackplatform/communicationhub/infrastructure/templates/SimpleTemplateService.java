package com.ecolutions.platform.wastetrackplatform.communicationhub.infrastructure.templates;

import com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.templates.TemplateService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

@Service
public class SimpleTemplateService implements TemplateService {
    private static final String TEMPLATE_BASE_PATH = "templates/email/";

    @Override
    public String render(String templateName, Map<String, String> variables) {
        try {
            String template = loadTemplate(templateName);

            return replaceVariables(template, variables);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template: " + templateName, e);
        }
    }

    private String loadTemplate(String templateName) throws IOException {
        String fullPath = TEMPLATE_BASE_PATH + templateName;
        ClassPathResource resource = new ClassPathResource(fullPath);

        if (!resource.exists()) {
            throw new IOException("Template not found: " + fullPath);
        }

        byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private String replaceVariables(String template, Map<String, String> variables) {
        String result = template;

        if (variables != null) {
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                String value = entry.getValue() != null ? entry.getValue() : "";
                result = result.replace(placeholder, value);
            }
        }

        return result;
    }

}
