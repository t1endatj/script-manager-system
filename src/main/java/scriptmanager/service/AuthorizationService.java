package scriptmanager.service;

import scriptmanager.config.UserSession;
import scriptmanager.enums.UserRole;

public final class AuthorizationService {
    private AuthorizationService() {
    }

    public static void requireAdmin() {
        if (!UserSession.hasAnyRole(UserRole.ADMIN)) {
            throw new SecurityException("Bạn không có quyền thực hiện thao tác này.");
        }
    }

    public static void requireManagerOrAdmin() {
        if (!UserSession.hasAnyRole(UserRole.ADMIN, UserRole.MANAGER)) {
            throw new SecurityException("Bạn không có quyền thực hiện thao tác này.");
        }
    }
}

