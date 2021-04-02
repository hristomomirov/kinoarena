package com.finals.kinoarena.model.DTO;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IMDBMovieDTO {

    private String title;
    private String year;
    private String plot;
    private double rating;
    private String poster;
    private String lead;

    public IMDBMovieDTO(JsonNode jsonNode) {
        this.title = jsonNode.get("title").asText();
        this.year = jsonNode.get("year").asText();
        this.plot = jsonNode.get("plot").asText();
        this.rating = jsonNode.get("rating").asDouble();
        this.poster = jsonNode.get("poster").asText();
        ArrayNode arrayNode =(ArrayNode) jsonNode.get("cast");
        this.lead = arrayNode.get(1).get("actor").asText();
    }
}
