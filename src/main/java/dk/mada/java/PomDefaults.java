package dk.mada.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.publish.maven.MavenPomDeveloper;
import org.gradle.api.publish.maven.MavenPomDeveloperSpec;
import org.gradle.api.publish.maven.MavenPomLicense;
import org.gradle.api.publish.maven.MavenPomLicenseSpec;
import org.gradle.api.publish.maven.MavenPomScm;
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPomDeveloper;
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPomLicense;
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPomScm;
import org.jspecify.annotations.Nullable;

public abstract class PomDefaults implements MavenPomDeveloperSpec, MavenPomLicenseSpec {
    private final ObjectFactory objectFactory;

    @Nullable private MavenPomScm scm;
    
    /** {@return the POM URL} */
    public abstract Property<String> getUrl();

    /** {@return the POM name} */
    public abstract Property<String> getName();
    
    /** {@return the POM description} */
    public abstract Property<String> getDescription();
    
    /** {@return the POM packaging} */
    public abstract Property<String> getPackaging();

    private final List<MavenPomDeveloper> developers = new ArrayList<>();
    private final List<MavenPomLicense> licenses = new ArrayList<>();

    @Inject
    public PomDefaults(@Nullable ObjectFactory objectFactory) {
        this.objectFactory = Objects.requireNonNull(objectFactory);
    }

    
    public @Nullable MavenPomScm getScm() {
        return scm;
    }

    public void scm(Action<? super MavenPomScm> action) {
        if (scm == null) {
            scm = objectFactory.newInstance(DefaultMavenPomScm.class, objectFactory);
        }
        action.execute(scm);
    }

    public List<MavenPomDeveloper> getDevelopers() {
        return developers;
    }

    public void developers(Action<? super MavenPomDeveloperSpec> action) {
        action.execute(this);
    }

    @Override
    public void developer(Action<? super MavenPomDeveloper> action) {
        configureAndAdd(DefaultMavenPomDeveloper.class, action, developers);
    }

    
    public void licenses(Action<? super MavenPomLicenseSpec> action) {
        action.execute(this);
    }

    public List<MavenPomLicense> getLicenses() {
        return licenses;
    }

    @Override
    public void license(Action<? super MavenPomLicense> action) {
        configureAndAdd(DefaultMavenPomLicense.class, action, licenses);
    }

    private <T> void configureAndAdd(Class<? extends T> clazz, Action<? super T> action, List<T> items) {
        T item = objectFactory.newInstance(clazz, objectFactory);
        action.execute(item);
        items.add(item);
    }
//    @Nullable
//    public Developer developer(MavenPomDeveloper dev) {
//        System.out.println("NA " + dev);
//        return null;
//        //return p.getObjects().newInstance(Developer.class, map);
//    }

    
    
    public interface Developer {
        Property<String> getId();

        /** {@return the developer name} */
        Property<String> getName();
        /** {@return the developer email} */
        Property<String> getEmail();
    }
}
