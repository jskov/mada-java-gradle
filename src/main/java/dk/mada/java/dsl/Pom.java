package dk.mada.java.dsl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.jspecify.annotations.Nullable;

/**
 * POM configuration (and Gradle DSL spec).
 */
public abstract class Pom implements PomDeveloperSpec, PomLicenseSpec {
    /** The Gradle object factory. */
    private final ObjectFactory objectFactory;
    /** The defined POM developers. */
    private final List<Developer> developers = new ArrayList<>();
    /** The the defined POM licenses. */
    private final List<License> licenses = new ArrayList<>();
    /** The optional POM SCM. */
    @Nullable private PomScm scm;

    /**
     * Creates new instance.
     *
     * @param objectFactory the Gradle object factory
     */
    @SuppressWarnings("InjectOnConstructorOfAbstractClass")
    @Inject
    public Pom(@Nullable ObjectFactory objectFactory) { // NOSONAR - must be public for Gradle to find it
        this.objectFactory = Objects.requireNonNull(objectFactory);
    }

    /** {@return the optionally defined Pom SCM section} */
    public @Nullable PomScm getScm() {
        return scm;
    }

    /**
     * Run spec action on the POM SCM section.
     *
     * Defines the POM SCM if it has not already been defined.
     *
     * @param action the action to run
     */
    public void scm(Action<? super PomScm> action) {
        if (scm == null) {
            scm = objectFactory.newInstance(PomScm.class);
        }
        action.execute(scm);
    }

    /** {@return the developers} */
    public List<Developer> getDevelopers() {
        return developers;
    }

    /**
     * Apply action to developers Gradle spec.
     *
     * @param action the action to apply
     */
    public void developers(Action<? super PomDeveloperSpec> action) {
        action.execute(this);
    }

    /**
     * Create and apply action to a new developer instance.
     *
     * @param action the action the apply to the new instance
     */
    @Override
    public void developer(Action<? super Developer> action) {
        configureAndAdd(Developer.class, action, developers);
    }

    /** {@return the licenses} */
    public List<License> getLicenses() {
        return licenses;
    }

    /**
     * Apply action to licenses Gradle spec.
     *
     * @param action the action to apply
     */
    public void licenses(Action<? super PomLicenseSpec> action) {
        action.execute(this);
    }

    /**
     * Create and apply action to a new license instance.
     *
     * @param action the action the apply to the new instance
     */
    @Override
    public void license(Action<? super License> action) {
        configureAndAdd(License.class, action, licenses);
    }

    /** {@return the POM URL} */
    public abstract Property<String> getUrl();

    /** {@return the POM name} */
    public abstract Property<String> getName();

    /** {@return the POM description} */
    public abstract Property<String> getDescription();

    /** {@return the POM packaging} */
    public abstract Property<String> getPackaging();

    /**
     * Adds a new spec instance to a list and runs action on it.
     *
     * @param <T>    the spec type
     * @param clazz  the implementation class
     * @param action the action to run on the new instance
     * @param items  the list to add the instance to
     */
    private <T> void configureAndAdd(Class<? extends T> clazz, Action<? super T> action, List<T> items) {
        T item = objectFactory.newInstance(clazz);
        action.execute(item);
        items.add(item);
    }
}
