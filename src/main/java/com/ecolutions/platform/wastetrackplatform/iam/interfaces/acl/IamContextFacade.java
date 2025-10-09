package com.ecolutions.platform.wastetrackplatform.iam.interfaces.acl;

import java.util.List;

public interface IamContextFacade {
    String createUser(String username, String password);
    String createUser(String username, String password, List<String> roleNames);
    String fetchUserIdByUsername(String username);
    String fetchUsernameByUserId(String userId);
}
