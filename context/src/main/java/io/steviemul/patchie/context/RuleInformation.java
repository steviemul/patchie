package io.steviemul.patchie.context;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class RuleInformation {

  private final String ruleId;
  private final String description;
}
