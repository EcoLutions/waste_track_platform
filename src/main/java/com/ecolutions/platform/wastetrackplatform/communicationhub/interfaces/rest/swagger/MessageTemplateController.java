package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.CreateMessageTemplateResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.request.UpdateMessageTemplateResource;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response.MessageTemplateResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/message-templates", produces = APPLICATION_JSON_VALUE)
@Tag(name = "MessageTemplate", description = "MessageTemplate Management Endpoints")
public interface MessageTemplateController {

    @PostMapping()
    @Operation(summary = "Create a new message template", description = "Creates a new message template in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "MessageTemplate created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<MessageTemplateResource> createMessageTemplate(@RequestBody CreateMessageTemplateResource resource);

    @GetMapping("/{id}")
    @Operation(summary = "Get message template by ID", description = "Retrieves a message template by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MessageTemplate retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "MessageTemplate not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<MessageTemplateResource> getMessageTemplateById(@PathVariable String id);

    @GetMapping()
    @Operation(summary = "Get all message templates", description = "Retrieves all message templates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MessageTemplates retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<List<MessageTemplateResource>> getAllMessageTemplates();

    @PutMapping("/{id}")
    @Operation(summary = "Update message template", description = "Updates an existing message template.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MessageTemplate updated successfully."),
            @ApiResponse(responseCode = "404", description = "MessageTemplate not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<MessageTemplateResource> updateMessageTemplate(@PathVariable String id, @RequestBody UpdateMessageTemplateResource resource);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete message template", description = "Deletes a message template by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "MessageTemplate deleted successfully."),
            @ApiResponse(responseCode = "404", description = "MessageTemplate not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<Void> deleteMessageTemplate(@PathVariable String id);
}