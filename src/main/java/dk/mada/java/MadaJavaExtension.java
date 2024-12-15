package dk.mada.java;

import org.gradle.api.Action;
import org.gradle.api.tasks.Nested;

import dk.mada.java.dsl.Pom;

/**
 * Extension for mada-java plugin.
 */
public abstract class MadaJavaExtension {
    /** Creates a new instance. */
    public MadaJavaExtension() { // NOSONAR - must be public for Gradle
        // empty
    }

    /** {@return the Pom configuration} */
    @Nested
    public abstract Pom getPom();

    /**
     * Process new Pom action.
     *
     * @param action the action for the Pom
     */
    public void pom(Action<? super Pom> action) {
        action.execute(getPom());
    }
}
