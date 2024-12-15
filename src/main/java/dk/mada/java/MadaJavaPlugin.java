package dk.mada.java;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.compile.JavaCompile;

/**
 * A plugin defining the java conventions used for dk.mada projects.
 */
public final class MadaJavaPlugin implements Plugin<Project> {
    /** Constructs new instance. */
    public MadaJavaPlugin() {
        // empty
    }

    @Override
    public void apply(Project project) {
        project.getPlugins().withType(JavaPlugin.class, jp -> applyPlugins(project));
    }

    private void applyPlugins(Project project) {
        Logger logger = project.getLogger();
        logger.info("Applying mada.java plugin");

        addCustomTasks(project);

        configureClasspath(project);
        project.afterEvaluate(this::postEvalConfiguration);
    }

    private void addCustomTasks(Project project) {
        // preCommit task for easy validation
        project.getTasks().register("preCommit", t -> {
            t.dependsOn("check", "javadoc");
            t.setGroup("Verification tasks");
        });
    }

    private void postEvalConfiguration(Project project) {
        configureJavaExtension(project);
        project.getTasks().withType(JavaCompile.class, this::configureJavaCompile);
    }

    private void configureJavaExtension(Project project) {
        JavaPluginExtension ext = project.getExtensions().getByType(JavaPluginExtension.class);

        // Provide javadoc jar with artifacts
        ext.withJavadocJar();
        // Provide source jar with artifacts
        ext.withSourcesJar();
    }

    private void configureJavaCompile(JavaCompile task) {
        // All source is UTF-8 encoded
        task.getOptions().setEncoding("utf-8");
        // Include parameter names in classes for easier debugging/navigation without source
        task.getOptions().getCompilerArgs().add("-parameters");
    }

    private void configureClasspath(Project project) {
        // Allow easy null-validation
        project.getDependencies().add("compileOnly", "org.jspecify:jspecify:1.0.0");
    }
}
