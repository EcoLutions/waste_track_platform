package com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.mappers;

import com.ecolutions.platform.wastetrackplatform.profile.interfaces.rest.dto.response.PhotoResource;

public class PhotoResourceFromStringAssembler {

    public static PhotoResource toResourceFromString(String filePath) {
        return PhotoResource.builder()
            .filePath(filePath)
            .build();
    }
}