package io.steviemul.patchie.parser.exception;

import com.networknt.schema.ValidationMessage;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SarifValidationException extends RuntimeException {

  private final Collection<ValidationMessage> errors;
}
