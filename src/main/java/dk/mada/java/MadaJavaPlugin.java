package dk.mada.java;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.compile.JavaCompile;

/**
 * A plugin defining the java conventions used for dk.mada projects.
 */
public final class MadaJavaPlugin implements Plugin<Project> {
    /** Constructs new instance. */
    public MadaJavaPlugin() {
        // Explicit default constructor to avoid javadoc warning
    }

    @Override
    public void apply(Project project) {
        project.getPlugins().withType(JavaPlugin.class, jp -> applyPlugins(project));
    }

    private void applyPlugins(Project project) {
        Logger logger = project.getLogger();
        logger.info("Applying mada.java plugin");

        configureClasspath(project);
        project.afterEvaluate(this::postEvalConfiguration);
    }
    
    private void postEvalConfiguration(Project project) {
        project.getTasks().withType(JavaCompile.class, this::configureJavaCompile);
    }
    
    private void configureJavaCompile(JavaCompile task) {
        // DOC: All source should be UTF-8 encoded
        task.getOptions().setEncoding("utf-8");
    }
    
    private void configureClasspath(Project project) {
        project.getDependencies().add("compileOnly", "org.jspecify:jspecify:1.0.0");
    }
}
