package scriptmanager.config;

import scriptmanager.entity.user.NguoiDung;
import scriptmanager.enums.UserRole;

public final class UserSession {
    private static volatile NguoiDung currentUser;

    private UserSession() {
    }

    public static void setCurrentUser(NguoiDung user) {
        currentUser = user;
    }

    public static NguoiDung getCurrentUser() {
        return currentUser;
    }

    public static Integer getCurrentUserId() {
        return currentUser != null ? currentUser.getMaND() : null;
    }

    public static UserRole getCurrentRole() {
        return currentUser == null ? UserRole.USER : UserRole.fromDb(currentUser.getQuyenHan());
    }

    public static boolean hasAnyRole(UserRole... roles) {
        UserRole role = getCurrentRole();
        for (UserRole r : roles) {
            if (r == role) {
                return true;
            }
        }
        return false;
    }

    public static void clear() {
        currentUser = null;
    }
}

