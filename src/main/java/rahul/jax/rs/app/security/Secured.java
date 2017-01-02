package rahul.jax.rs.app.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

/**
 * {@link NameBinding} is a meta-annotation to create name-binding annotations for filters and interceptors.
 * This annotation will define the secured resources.
 * 
 * @author rahul.kh
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Secured {
    Roles[] value() default {};
}
