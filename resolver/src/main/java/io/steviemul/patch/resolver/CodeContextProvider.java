package io.steviemul.patch.resolver;

import io.steviemul.patch.context.AggregatedContext;
import io.steviemul.patch.context.ContextProvider;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class CodeContextProvider implements ContextProvider {

  private final String sourceCodeRoot;

  @Override
  public AggregatedContext addContext(AggregatedContext aggregatedContext) {
    return null;
  }
}
