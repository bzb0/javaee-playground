package com.bzb.javaee.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * A CDI producer for the nashorn JavaScript engine. The JavaScript engine can be easily injected as follows:
 * </p>
 *
 * <pre>
 * public class JavaScriptExecutor {
 *   &#064;Inject
 *   ScriptEngine engine;
 * }
 * </pre>
 */
@ApplicationScoped
public class JavaScriptEngineProducer {

  private static final String JS_ENGINE_NAME = "nashorn";

  @Produces
  public ScriptEngine getJavaScriptEngine() {
    ScriptEngineManager factory = new ScriptEngineManager();
    return factory.getEngineByName(JS_ENGINE_NAME);
  }
}
