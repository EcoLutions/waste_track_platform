package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.outboundservices.templates;

import java.util.Map;

public interface TemplateService {
    String render(String templateName, Map<String, String> variables);
}
