package com.ecolutions.platform.wastetrackplatform.iam.interfaces.acl;

import java.util.List;

public interface IamContextFacade {
    String createUser(String email, String password);
    String createUser(String email, String password, List<String> roleNames);
    String fetchUserIdByEmail(String email);
    String fetchEmailByUserId(String userId);
}
