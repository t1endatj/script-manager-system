package scriptmanager.config;

import java.util.prefs.Preferences;

public final class RememberMeStore {

    private static final Preferences PREFS = Preferences.userNodeForPackage(RememberMeStore.class);
    private static final String KEY_REMEMBER = "remember.me";
    private static final String KEY_USERNAME = "remember.username";

    private RememberMeStore() {
    }

    public static boolean isRemembered() {
        return PREFS.getBoolean(KEY_REMEMBER, false)
                && !PREFS.get(KEY_USERNAME, "").isBlank();
    }

    public static String getRememberedUsername() {
        return PREFS.get(KEY_USERNAME, "");
    }

    public static void saveRememberedUser(String username) {
        PREFS.putBoolean(KEY_REMEMBER, true);
        PREFS.put(KEY_USERNAME, username);
    }

    public static void clearRememberedUser() {
        PREFS.putBoolean(KEY_REMEMBER, false);
        PREFS.remove(KEY_USERNAME);
    }
}
