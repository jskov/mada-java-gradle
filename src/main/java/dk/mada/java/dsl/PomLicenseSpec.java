package dk.mada.java.dsl;

import org.gradle.api.Action;
import org.gradle.api.provider.Property;

/**
 * POM License section Gradle spec.
 */
public interface PomLicenseSpec {
    /**
     * Process new license action.
     *
     * @param action the action for the license
     */
    void license(Action<? super License> action);

    /**
     * POM License section.
     */
    public interface License {
        /** {@return the license name} */
        Property<String> getName();
        /** {@return a URL to the license description} */
        Property<String> getUrl();
    }
}