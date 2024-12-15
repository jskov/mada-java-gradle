package dk.mada.java;

import org.gradle.api.Action;
import org.gradle.api.tasks.Nested;

import dk.mada.java.dsl.Pom;

public abstract class MadaJavaExtension {
    @Nested
    public abstract Pom getPom();
    
    public void pom(Action<? super Pom> action) {
        action.execute(getPom());
    }
}
