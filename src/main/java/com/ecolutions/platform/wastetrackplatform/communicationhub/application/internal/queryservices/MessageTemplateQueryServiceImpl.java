package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.MessageTemplate;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetMessageTemplateByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetAllMessageTemplatesQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.queries.MessageTemplateQueryService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.infrastructure.persistence.jpa.repositories.MessageTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageTemplateQueryServiceImpl implements MessageTemplateQueryService {
    private final MessageTemplateRepository messageTemplateRepository;

    @Override
    public Optional<MessageTemplate> handle(GetMessageTemplateByIdQuery query) {
        try {
            return messageTemplateRepository.findById(query.messageTemplateId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve message template: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MessageTemplate> handle(GetAllMessageTemplatesQuery query) {
        try {
            return messageTemplateRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve message templates: " + e.getMessage(), e);
        }
    }
}