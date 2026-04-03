package scriptmanager.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateUtil {
    private static final Logger LOGGER = Logger.getLogger(HibernateUtil.class.getName());

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder()
                        .configure()
                        .applySetting("connection.url", resolveSetting("SCRIPTMGR_DB_URL", "scriptmanager.db.url", "jdbc:mysql://localhost:3306/ScriptCoordinationDB"))
                        .applySetting("connection.username", resolveSetting("SCRIPTMGR_DB_USER", "scriptmanager.db.user", "root"))
                        .applySetting("connection.password", resolveSetting("SCRIPTMGR_DB_PASS", "scriptmanager.db.pass", "1"))
                        .build();

                MetadataSources sources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                LOGGER.log(Level.SEVERE, "Khởi tạo Hibernate SessionFactory thất bại", e);
            }
        }
        return sessionFactory;
    }

    private static String resolveSetting(String envKey, String propertyKey, String fallback) {
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        String propValue = System.getProperty(propertyKey);
        if (propValue != null && !propValue.isBlank()) {
            return propValue;
        }
        return fallback;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}