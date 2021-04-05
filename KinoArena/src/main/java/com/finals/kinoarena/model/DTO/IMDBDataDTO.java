package com.finals.kinoarena.model.DTO;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;


    @Getter
    @Setter
    @NoArgsConstructor
    public class IMDBDataDTO {

        private JsonNode titles;
        private JsonNode names;
        private JsonNode companies;

        public IMDBDataDTO(JsonNode jsonNode) {
            this.titles = jsonNode.get("titles");
            this.names = jsonNode.get("names");
            this.companies = jsonNode.get("companies");
        }
}
