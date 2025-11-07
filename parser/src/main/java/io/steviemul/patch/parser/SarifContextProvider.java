package io.steviemul.patch.parser;

import io.steviemul.patch.context.AggregatedContext;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Builder
public class SarifContextProvider {

  private final String resultsLocation;

  public List<AggregatedContext> getInitialContext() {
    return Collections.emptyList();
  }
}
