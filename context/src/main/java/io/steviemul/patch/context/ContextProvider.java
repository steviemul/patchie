package io.steviemul.patch.context;

public interface ContextProvider {
  AggregatedContext addContext(AggregatedContext aggregatedContext);
}
