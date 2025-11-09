package io.steviemul.patchie.generator.template;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;

public class TemplateReader {

  private static final String TEMPLATE_ROOT = "templates";
  private static final String CODE_FIX_TEMPLATE = TEMPLATE_ROOT + "/CodeFixPrompt.template";

  public static PromptTemplate getCodeFixTemplate() {

    return PromptTemplate.builder().resource(new ClassPathResource(CODE_FIX_TEMPLATE)).build();
  }
}
