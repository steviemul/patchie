package io.steviemul.patchie.context;

import lombok.Data;

@Data
public class AggregatedContext {
  private RuleInformation ruleInformation;
  private String fileLocation;
  private int lineNumber;
  private String code;
  private String patch;
}
