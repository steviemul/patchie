package io.steviemul.patchie.context;

import lombok.Data;

@Data
public class AggregatedContext {
  private RuleInformation ruleInformation;
  private ResultLocation resultLocation;

  private String code;
  private String patch;
}
