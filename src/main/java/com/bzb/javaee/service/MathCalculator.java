package com.bzb.javaee.service;

import static java.util.stream.Collectors.toMap;

import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Stream;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

/**
 * A math calculator, that uses the JVM JavaScript engine to calculate the exponentiation of two numbers.
 */
@ApplicationScoped
public class MathCalculator {

  private final static String JS_POWER_SCRIPT = "function pow(base, exponent) { return Math.pow(base, exponent); } pow(base, exponent);";

  @Inject
  private ScriptEngine jsEngine;

  /**
   * Calculates the base raised to the power of exponent.
   *
   * @param base     The base.
   * @param exponent The exponent.
   * @return Base raised to the power of exponent.
   */
  public Number power(Integer base, Integer exponent) {
    Bindings bindings = new SimpleBindings(Stream.of(new SimpleEntry<>("base", base), new SimpleEntry<>("exponent", exponent))
        .collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue)));
    try {
      Object result = jsEngine.eval(JS_POWER_SCRIPT, bindings);
      if (result instanceof Number) {
        return (Number) result;
      } else {
        throw new IllegalArgumentException("Unknown result type: " + result.getClass());
      }
    } catch (ScriptException e) {
      e.printStackTrace();
      return -1;
    }
  }
}
