package dk.mada.java;

import dk.mada.java.dsl.Pom;
import java.io.File;
import org.gradle.api.Action;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.Nested;

/**
 * Extension for mada-java plugin.
 */
public abstract class MadaJavaExtension {
    /** Creates a new instance. */
    public MadaJavaExtension() { // NOSONAR - must be public for Gradle
        // empty
    }

    /** {@return an optional directory to publish to} */
    public abstract DirectoryProperty getPublishTo();

    /**
     * Allow DSL to set publish-to property.
     *
     * @param dir the directory to publish to
     */
    public void publishTo(File dir) {
        getPublishTo().set(dir);
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
