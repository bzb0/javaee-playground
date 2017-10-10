package com.bzb.javaee;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * - CDI/EJB injection is possible without declaring a JAX-RS resource as an EJB or CDI bean. - The
 * - JAX-RS resource classes are request scoped and not singletons.
 */
@ApplicationPath("/")
public class PlaygroundApplication extends Application {
}
