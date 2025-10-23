package com.ecolutions.platform.wastetrackplatform.iam.application.acl;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SignUpCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.entities.Role;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetUserByIdQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetUserByEmailQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.UserCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.UserQueryService;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.acl.IamContextFacade;
import io.jsonwebtoken.lang.Strings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IamContextFacadeImpl implements IamContextFacade {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public IamContextFacadeImpl(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @Override
    public String createUser(String email, String password) {
        var signUpCommand = new SignUpCommand(email, password, List.of(Role.getDefaultRole()));
        var result = userCommandService.handle(signUpCommand);
        if (result.isEmpty()) return Strings.EMPTY;
        return result.get().getId();
    }

    @Override
    public String createUser(String email, String password, List<String> roleNames) {
        var roles = roleNames == null ? new ArrayList<Role>() : roleNames.stream().map(Role::toRoleFromName).toList();
        var signUpCommand = new SignUpCommand(email, password, roles);
        var result = userCommandService.handle(signUpCommand);
        if (result.isEmpty()) return Strings.EMPTY;
        return result.get().getId();
    }

    @Override
    public String fetchUserIdByEmail(String email) {
        var getUserByUsernameQuery = new GetUserByEmailQuery(email);
        var result = userQueryService.handle(getUserByUsernameQuery);
        if (result.isEmpty()) return Strings.EMPTY;
        return result.get().getId();
    }

    @Override
    public String fetchEmailByUserId(String userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var result = userQueryService.handle(getUserByIdQuery);
        if (result.isEmpty()) return Strings.EMPTY;
        return result.get().getEmail().value();
    }
}
