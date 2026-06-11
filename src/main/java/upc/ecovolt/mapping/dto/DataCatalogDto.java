package upc.ecovolt.mapping.dto;

import lombok.Data;

public class DataCatalogDto {
    @Data
    public static class Request {
        private String description;
        private Long idCatalog;
    }

    @Data
    public static class Response {
        private Long idDataCatalog;
        private String description;
        private Long idCatalog;
        private String catalogName;
    }
}
