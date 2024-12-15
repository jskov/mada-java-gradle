package dk.mada.java;

import org.gradle.api.Action;
import org.gradle.api.tasks.Nested;

public abstract class MadaJavaExtension {
    @Nested
    public abstract PomDefaults getPom();
    
    public void pom(Action<? super PomDefaults> action) {
        action.execute(getPom());
    }
}
