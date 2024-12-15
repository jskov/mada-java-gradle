package dk.mada.java;

import java.util.List;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.publish.maven.MavenPom;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.api.publish.maven.tasks.GenerateMavenPom;
import org.gradle.api.tasks.compile.JavaCompile;
import org.jspecify.annotations.Nullable;

import dk.mada.java.dsl.Pom;
import dk.mada.java.dsl.PomDeveloperSpec.Developer;
import dk.mada.java.dsl.PomLicenseSpec.License;
import dk.mada.java.dsl.PomScm;

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
        Logger logger = project.getLogger();

        logger.lifecycle("Config plugin");

        project.getExtensions().create("madaJava", MadaJavaExtension.class);

        project.afterEvaluate(p -> p.getPlugins().withType(MavenPublishPlugin.class, mpp -> configurePublishing(p)));

        project.getPlugins().withType(JavaPlugin.class, jp -> applyPlugins(project));
    }

    private void configurePublishing(Project project) {
        Logger logger = project.getLogger();

        MadaJavaExtension ext = project.getExtensions().getByType(MadaJavaExtension.class);
        Pom conventionPom = ext.getPom();
        List<Developer> conventionDevelopers = conventionPom.getDevelopers();
        List<License> conventionLicenses = ext.getPom().getLicenses();
        @Nullable PomScm conventionScm = ext.getPom().getScm();

        project.getTasks().withType(GenerateMavenPom.class).configureEach(pt -> {
            logger.info(" mada.java configuring task {}", pt.getName());
            MavenPom pom = pt.getPom();

            pom.getName().set(conventionPom.getName());
            pom.getUrl().set(conventionPom.getUrl());
            pom.getDescription().set(conventionPom.getDescription());
            pom.setPackaging(conventionPom.getPackaging().getOrNull());

            pom.developers(developer -> conventionDevelopers.forEach(conventionDeveloper -> developer.developer(dev -> {
                dev.getId().set(conventionDeveloper.getId());
                dev.getName().set(conventionDeveloper.getName());
                dev.getEmail().set(conventionDeveloper.getEmail());
            })));

            pom.licenses(license -> conventionLicenses.forEach(conventionLicense -> license.license(lic -> {
                lic.getName().set(conventionLicense.getName());
                lic.getUrl().set(conventionLicense.getUrl());
            })));

            if (conventionScm != null) {
                pom.scm(scm -> {
                    scm.getConnection().set(conventionScm.getConnection());
                    scm.getDeveloperConnection().set(conventionScm.getDeveloperConnection());
                    scm.getUrl().set(conventionScm.getUrl());
                });
            }
        });

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
