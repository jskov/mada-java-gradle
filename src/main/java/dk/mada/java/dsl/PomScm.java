package dk.mada.java.dsl;

import org.gradle.api.provider.Property;

/**
 * POM Scm section.
 */
public interface PomScm {
    /** {@return the connection to the repository} */
    Property<String> getConnection();
    /** {@return the developer connection to the repository} */
    Property<String> getDeveloperConnection();
    /** {@return the URL to the repository} */
    Property<String> getUrl();
}