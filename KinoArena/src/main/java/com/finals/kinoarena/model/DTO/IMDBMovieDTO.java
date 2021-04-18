package com.finals.kinoarena.model.DTO;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.finals.kinoarena.util.Constants;
import com.finals.kinoarena.util.exceptions.BadRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class IMDBMovieDTO {

    private String imdbId;
    private String title;
    private String year;
    private String plot;
    private int length;
    private double rating;
    private String poster;
    private String lead;

    public IMDBMovieDTO(JsonNode jsonNode) throws BadRequestException {
        this.imdbId = jsonNode.get("id").asText().trim();
        this.title = jsonNode.get("title").asText().trim();
        this.year = jsonNode.get("year").asText().trim();
        this.plot = jsonNode.get("plot").asText().trim();
        this.length = calculateLength(jsonNode.get("length").asText().trim());
        this.rating = jsonNode.get("rating").asDouble();
        this.poster = jsonNode.get("poster").asText().trim();
        ArrayNode arrayNode = (ArrayNode) jsonNode.get("cast");
        this.lead = arrayNode.get(0).get("actor").asText().trim();
    }

    private int calculateLength(String length) throws BadRequestException {
        int minutes = 0;
        Pattern pattern = Pattern.compile(Constants.MOVIE_LENGTH_REGEX);
        Matcher matcher = pattern.matcher(length);
        if (!matcher.matches()) {
            throw new BadRequestException("Invalid Length");
        }
        if (matcher.group(6) != null) {
            return Integer.parseInt(matcher.group(6));
        }
        if (matcher.group(4) != null) {
            minutes = Integer.parseInt(matcher.group(4));
        }
        return Integer.parseInt(matcher.group(2)) * 60 + minutes;
    }
}
