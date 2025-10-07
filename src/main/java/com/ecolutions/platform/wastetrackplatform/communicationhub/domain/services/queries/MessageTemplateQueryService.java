package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.MessageTemplate;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetMessageTemplateByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetAllMessageTemplatesQuery;

import java.util.List;
import java.util.Optional;

public interface MessageTemplateQueryService {
    Optional<MessageTemplate> handle(GetMessageTemplateByIdQuery query);
    List<MessageTemplate> handle(GetAllMessageTemplatesQuery query);
}