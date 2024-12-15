package dk.mada.java.dsl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.publish.maven.MavenPomLicense;
import org.gradle.api.publish.maven.MavenPomScm;
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPomScm;
import org.jspecify.annotations.Nullable;

import dk.mada.java.dsl.PomLicenseSpec.License;

public abstract class Pom implements PomDeveloperSpec, PomLicenseSpec {
    private final ObjectFactory objectFactory;

    @Nullable private PomScm scm;
    
    /** {@return the POM URL} */
    public abstract Property<String> getUrl();

    /** {@return the POM name} */
    public abstract Property<String> getName();
    
    /** {@return the POM description} */
    public abstract Property<String> getDescription();
    
    /** {@return the POM packaging} */
    public abstract Property<String> getPackaging();

    private final List<Developer> developers = new ArrayList<>();
    private final List<License> licenses = new ArrayList<>();

    @Inject
    public Pom(@Nullable ObjectFactory objectFactory) {
        this.objectFactory = Objects.requireNonNull(objectFactory);
    }

    
    public @Nullable PomScm getScm() {
        return scm;
    }

    public void scm(Action<? super PomScm> action) {
        if (scm == null) {
            scm = objectFactory.newInstance(PomScm.class);
        }
        action.execute(scm);
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void developers(Action<? super PomDeveloperSpec> action) {
        action.execute(this);
    }

    @Override
    public void developer(Action<? super Developer> action) {
        configureAndAdd(Developer.class, action, developers);
    }

    
    public void licenses(Action<? super PomLicenseSpec> action) {
        action.execute(this);
    }

    public List<License> getLicenses() {
        return licenses;
    }

    @Override
    public void license(Action<? super License> action) {
        configureAndAdd(License.class, action, licenses);
    }

    private <T> void configureAndAdd(Class<? extends T> clazz, Action<? super T> action, List<T> items) {
        T item = objectFactory.newInstance(clazz);
        action.execute(item);
        items.add(item);
    }
}
