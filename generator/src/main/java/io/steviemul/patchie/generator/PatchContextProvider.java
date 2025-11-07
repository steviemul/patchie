package io.steviemul.patchie.generator;

import io.steviemul.patchie.context.AggregatedContext;
import io.steviemul.patchie.context.ContextProvider;
import org.springframework.stereotype.Service;

@Service
public class PatchContextProvider implements ContextProvider {

  @Override
  public AggregatedContext addContext(AggregatedContext aggregatedContext) {
    return null;
  }
}
