package dk.mada.java.dsl;

import org.gradle.api.Action;
import org.gradle.api.provider.Property;

/**
 * POM Developer section Gradle spec.
 */
public interface PomDeveloperSpec {
    /**
     * Process new developer action.
     *
     * @param action the action for the developer
     */
    void developer(Action<? super Developer> action);

    /**
     * POM Developer section.
     */
    public interface Developer {
        /** {@return the developer id} */
        Property<String> getId();

        /** {@return the developer name} */
        Property<String> getName();

        /** {@return the developer email} */
        Property<String> getEmail();
    }
}
