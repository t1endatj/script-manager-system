package scriptmanager.config;

public class RememberMeStoreTest {

    public static void main(String[] args) {
        RememberMeStore.clearRememberedUser();

        RememberMeStore.saveRememberedUser("admin");
        if (!RememberMeStore.isRemembered()) {
            throw new IllegalStateException("Expected remember-me state to be true after saving user.");
        }
        if (!"admin".equals(RememberMeStore.getRememberedUsername())) {
            throw new IllegalStateException("Expected remembered username to be 'admin'.");
        }

        RememberMeStore.clearRememberedUser();
        if (RememberMeStore.isRemembered()) {
            throw new IllegalStateException("Expected remember-me state to be false after clearing user.");
        }
    }
}


