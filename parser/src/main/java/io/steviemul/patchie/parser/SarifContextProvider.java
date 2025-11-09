package io.steviemul.patchie.parser;

import io.steviemul.patchie.context.AggregatedContext;
import io.steviemul.patchie.parser.sarif.SarifParser;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class SarifContextProvider {

  private final String resultsLocation;

  public List<AggregatedContext> getInitialContext() {

    SarifParser sarifParser = new SarifParser(resultsLocation);

    return sarifParser.resultsToContext();
  }
}
