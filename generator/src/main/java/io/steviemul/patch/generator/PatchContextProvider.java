package io.steviemul.patch.generator;

import io.steviemul.patch.context.AggregatedContext;
import io.steviemul.patch.context.ContextProvider;
import org.springframework.stereotype.Service;

@Service
public class PatchContextProvider implements ContextProvider {

  @Override
  public AggregatedContext addContext(AggregatedContext aggregatedContext) {
    return null;
  }
}
