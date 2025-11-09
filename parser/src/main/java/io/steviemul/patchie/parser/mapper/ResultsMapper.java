package io.steviemul.patchie.parser.mapper;

import com.contrastsecurity.sarif.ArtifactContent;
import com.contrastsecurity.sarif.ArtifactLocation;
import com.contrastsecurity.sarif.Location;
import com.contrastsecurity.sarif.Message;
import com.contrastsecurity.sarif.MultiformatMessageString;
import com.contrastsecurity.sarif.PhysicalLocation;
import com.contrastsecurity.sarif.Region;
import com.contrastsecurity.sarif.Result;
import io.steviemul.patchie.context.AggregatedContext;
import io.steviemul.patchie.context.ResultLocation;
import io.steviemul.patchie.context.RuleInformation;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ResultsMapper {

  public static final String UNKNOWN = "unknown";

  public static List<AggregatedContext> mapToContext(List<Result> results) {
    return results.stream().map(ResultsMapper::mapToContext).toList();
  }

  public static AggregatedContext mapToContext(Result result) {
    String text = Optional.ofNullable(result.getMessage()).map(Message::getText).orElse("");

    RuleInformation ruleInformation =
        RuleInformation.builder().ruleId(result.getRuleId()).description(text).build();

    AggregatedContext aggregatedContext = new AggregatedContext();

    aggregatedContext.setRuleInformation(ruleInformation);

    aggregatedContext.setResultLocation(location(result));

    return aggregatedContext;
  }

  private static ResultLocation location(Result result) {

    Location primaryLocation =
        Optional.ofNullable(result.getLocations())
            .filter(ResultsMapper::hasElements)
            .map(List::getFirst)
            .orElse(new Location());

    return location(primaryLocation);
  }

  private static ResultLocation location(Location location) {

    String file =
        Optional.ofNullable(location.getPhysicalLocation())
            .map(PhysicalLocation::getArtifactLocation)
            .map(ArtifactLocation::getUri)
            .orElse(UNKNOWN);

    int startLine = getRegionLocation(location, Region::getStartLine);
    int endLine = getRegionLocation(location, Region::getEndLine);
    int startColumn = getRegionLocation(location, Region::getStartColumn);
    int endColumn = getRegionLocation(location, Region::getEndColumn);

    String snippet =
        Optional.ofNullable(location.getPhysicalLocation())
            .map(PhysicalLocation::getRegion)
            .map(Region::getSnippet)
            .map(ResultsMapper::snippet)
            .orElse(UNKNOWN);

    return ResultLocation.builder()
        .startLine(startLine)
        .endLine(endLine)
        .startColumn(startColumn)
        .endColumn(endColumn)
        .file(file)
        .snippet(snippet)
        .build();
  }

  private static int getRegionLocation(Location location, Function<Region, Integer> mapper) {

    return Optional.ofNullable(location.getPhysicalLocation())
        .map(PhysicalLocation::getRegion)
        .map(mapper)
        .orElse(-1);
  }

  private static String snippet(ArtifactContent content) {

    return Optional.ofNullable(content)
        .map(ArtifactContent::getRendered)
        .map(MultiformatMessageString::getText)
        .orElse(content.getText());
  }

  private static boolean hasElements(Collection<?> elements) {
    return elements != null && !elements.isEmpty();
  }
}
