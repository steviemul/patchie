package io.steviemul.patchie.generator.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatApiException extends RuntimeException {
  private final String message;
}
