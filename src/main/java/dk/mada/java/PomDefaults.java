package dk.mada.java;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.publish.maven.MavenPomDeveloper;
import org.gradle.api.publish.maven.MavenPomDeveloperSpec;
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPomDeveloper;

public abstract class PomDefaults implements MavenPomDeveloperSpec {
    private ObjectFactory objectFactory;

    /** {@return the POM URL} */
    public abstract Property<String> getUrl();

    /** {@return the POM name} */
    public abstract Property<String> getName();
    
    /** {@return the POM description} */
    public abstract Property<String> getDescription();
    
    /** {@return the POM packaging} */
    public abstract Property<String> getPackaging();

    @Inject
    public PomDefaults(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }
        
    private List<MavenPomDeveloper> developers = new ArrayList<>();
    
//    public abstract DomainObjectSet<MavenPomDeveloperSpec> getDevelopers();
    
    public void developers(Action<? super MavenPomDeveloperSpec> action) {
        action.execute(this);
    }

    public List<MavenPomDeveloper> getDevelopers() {
        return developers;
    }

    @Override
    public void developer(Action<? super MavenPomDeveloper> action) {
        configureAndAdd(DefaultMavenPomDeveloper.class, action, developers);
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
