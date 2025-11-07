package io.steviemul.patchie.context;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class RuleInformation {

  private final String ruleId;
  private final String description;
}
