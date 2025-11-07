package io.steviemul.patchie.resolver;

import io.steviemul.patchie.context.AggregatedContext;
import io.steviemul.patchie.context.ContextProvider;
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
