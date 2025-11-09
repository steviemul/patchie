package io.steviemul.patchie.parser.sarif;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.steviemul.patchie.parser.exception.SarifValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SarifValidator {

  private static final String SARIF_SCHEMA_PATH = "/schema/sarif-2.1.0.json";

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final JsonSchemaFactory factory =
      JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);

  private final JsonSchema schema =
      factory.getSchema(SarifValidator.class.getResourceAsStream(SARIF_SCHEMA_PATH));

  public JsonNode validateSarif(InputStream sarifStream) {

    try {
      JsonNode sarifNode = objectMapper.readTree(sarifStream);

      Set<ValidationMessage> errors = schema.validate(sarifNode);

      if (!errors.isEmpty()) {
        throw new SarifValidationException(errors);
      }

      return sarifNode;
    } catch (IOException e) {
      log.error("Error validation sarif", e);

      throw new RuntimeException(e);
    }
  }
}
