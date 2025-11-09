package io.steviemul.patchie.context;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResultLocation {

  private final int startLine;
  private final int startColumn;
  private final int endLine;
  private final int endColumn;
  private final String file;
  private final String snippet;
}
