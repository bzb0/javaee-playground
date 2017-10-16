package com.bzb.javaee.model;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Value class that holds the finish time of a computation.
 */
@Value
@AllArgsConstructor
public class ComputationFinished {

  long finishTimeMillis;
}