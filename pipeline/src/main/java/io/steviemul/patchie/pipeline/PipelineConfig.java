package io.steviemul.patchie.pipeline;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PipelineConfig {
  private final String root;
  private final String resultsFile;
  private final String outputLocation;
  private final int maximumPatches;
}
