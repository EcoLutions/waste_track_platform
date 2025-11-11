package com.ecolutions.platform.wastetrackplatform.iam.interfaces.acl;

import java.util.List;

public interface IamContextFacade {
    String createUser(String email, String username, String districtId);
    String createUser(String email, String username, List<String> roleNames, String districtId);
    String fetchUserIdByEmail(String email);
    String fetchEmailByUserId(String userId);
}
