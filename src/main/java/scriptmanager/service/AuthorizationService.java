package scriptmanager.service;

import scriptmanager.config.UserSession;
import scriptmanager.enums.UserRole;
import scriptmanager.exception.AuthorizationException;

public final class AuthorizationService {
    private AuthorizationService() {
    }

    public static void requireAdmin() {
        if (!UserSession.hasAnyRole(UserRole.ADMIN)) {
            throw new AuthorizationException("Bạn không có quyền thực hiện thao tác này.");
        }
    }

    public static void requireManagerOrAdmin() {
        if (!UserSession.hasAnyRole(UserRole.ADMIN, UserRole.MANAGER)) {
            throw new AuthorizationException("Bạn không có quyền thực hiện thao tác này.");
        }
    }
}
