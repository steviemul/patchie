package io.steviemul.patchie.parser.sarif;

import com.contrastsecurity.sarif.Run;
import com.contrastsecurity.sarif.SarifSchema210;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.steviemul.patchie.context.AggregatedContext;
import io.steviemul.patchie.parser.exception.ParseException;
import io.steviemul.patchie.parser.mapper.ResultsMapper;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SarifParser {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final SarifSchema210 sarif;

  public SarifParser(String sarifLocation) {
    this.sarif = getParsedSarif(sarifLocation);
  }

  private SarifSchema210 getParsedSarif(String sarifLocation) {

    SarifValidator validator = new SarifValidator();

    try (InputStream is = new FileInputStream(sarifLocation)) {
      JsonNode sarifJson = validator.validateSarif(is);

      return objectMapper.convertValue(sarifJson, SarifSchema210.class);
    } catch (Exception e) {
      throw new ParseException("Failed to parse sarif json", e);
    }
  }

  public List<AggregatedContext> resultsToContext() {

    return Optional.ofNullable(sarif.getRuns())
        .map(List::getFirst)
        .map(Run::getResults)
        .map(ResultsMapper::mapToContext)
        .orElse(Collections.emptyList());
  }
}
