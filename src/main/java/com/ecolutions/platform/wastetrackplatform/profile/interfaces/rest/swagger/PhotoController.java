package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.swagger;

import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.response.PhotoResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequestMapping(value = "/api/v1/photos", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Photo", description = "Photo Upload Endpoints")
public interface PhotoController {

    @PostMapping(value = "/upload", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a new photo", description = "Uploads a new photo file to the system for user profiles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo uploaded successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data or file type."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    ResponseEntity<PhotoResource> uploadPhoto(@RequestParam("file") MultipartFile file);
}