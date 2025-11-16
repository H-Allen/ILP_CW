package uk.ac.ed.acp.cw2.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
    Java DTO class that represents a GeoJSON response for endpoint 5 in the specification
*/
@Getter
@Setter
public class GeoJsonResponse {
    private String type = "FeatureCollection";
    private List<Feature> features;

    public GeoJsonResponse() {}

    @Getter
    @Setter
    public static class Feature {
        private String type = "Feature";
        private Properties properties;
        private Geometry geometry;

        public Feature() {}

        @Getter
        @Setter
        public static class Properties {
            private String droneId;
            public Properties() {}
        }

        @Getter
        @Setter
        public static class Geometry {
            private String type = "LineString";
            private List<List<Double>> coordinates;

            public Geometry() {}
        }
    }
}
