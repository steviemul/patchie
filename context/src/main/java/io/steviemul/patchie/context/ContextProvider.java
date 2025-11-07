package io.steviemul.patchie.context;

public interface ContextProvider {
  AggregatedContext addContext(AggregatedContext aggregatedContext);
}
