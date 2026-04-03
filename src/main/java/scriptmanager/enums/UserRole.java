package scriptmanager.enums;

public enum UserRole {
    ADMIN,
    MANAGER,
    USER;

    public static UserRole fromDb(String value) {
        if (value == null || value.isBlank()) {
            return USER;
        }

        String normalized = value.trim().toUpperCase();
        return switch (normalized) {
            case "QUANTRI", "ADMIN" -> ADMIN;
            case "MANAGER", "QUANLY", "QUAN_LY" -> MANAGER;
            case "NHANVIEN", "USER" -> USER;
            default -> USER;
        };
    }
}

