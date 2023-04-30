package org.teknichrono.mgp.client.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SessionFileType {

  @JsonProperty("analysis_by_lap") ANALYSIS_BY_LAP,
  @JsonProperty("fast_lap_rider") FAST_LAP_RIDER,
  @JsonProperty("combined_classification") COMBINED_CLASSIFICATION,
  @JsonProperty("session") SESSION,
  @JsonProperty("best_partial_time") BEST_PARTIAL_TIME,
  @JsonProperty("combined_practice") COMBINED_PRACTICE,
  @JsonProperty("classification") CLASSIFICATION,
  @JsonProperty("analysis") ANALYSIS,
  @JsonProperty("maximum_speed") MAXIMUM_SPEED,
  @JsonProperty("lap_chart") LAP_CHART,
  @JsonProperty("grid") GRID,
  @JsonProperty("fast_lap_sequence") FAST_LAP_SEQUENCE,
  @JsonProperty("average_speed") AVERAGE_SPEED,
  @JsonProperty("world_standing") WORLD_STANDING;

}
