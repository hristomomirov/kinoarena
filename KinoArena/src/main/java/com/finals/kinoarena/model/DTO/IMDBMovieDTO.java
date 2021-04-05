package com.finals.kinoarena.model.DTO;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    public IMDBMovieDTO(JsonNode jsonNode) {
        this.imdbId = jsonNode.get("id").asText().trim();
        this.title = jsonNode.get("title").asText().trim();
        this.year = jsonNode.get("year").asText().trim();
        this.plot = jsonNode.get("plot").asText().trim();
        this.length = calculateLength(jsonNode.get("length").asText().trim());
        this.rating = jsonNode.get("rating").asDouble();
        this.poster = jsonNode.get("poster").asText().trim();
        ArrayNode arrayNode = (ArrayNode) jsonNode.get("cast");
        this.lead = arrayNode.get(1).get("actor").asText().trim();
    }

    private int calculateLength(String length) {
        int minutes;
        char[] chars = length.toCharArray();
        int hours = Character.getNumericValue(chars[0]) * 60;
        List<Integer> listMinutes = new ArrayList<>();
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == ' ') {
                listMinutes.add(Character.getNumericValue(chars[i + 1]));
                if (Character.isDigit(chars[i + 2])) {
                    listMinutes.add(Character.getNumericValue(chars[i + 2]));
                } else {
                    break;
                }
            }
        }
        if (listMinutes.size() == 1) {
            minutes = listMinutes.get(0);
        } else {
            minutes = listMinutes.get(0) * 10 + listMinutes.get(1);
        }
        return hours + minutes;
    }
}
