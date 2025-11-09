package io.steviemul.patchie.parser.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParseException extends RuntimeException {

  private final String message;
  private final Throwable cause;
}
