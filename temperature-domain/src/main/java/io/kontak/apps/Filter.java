package io.kontak.apps;

import java.math.BigDecimal;

public record Filter(String roomId, String thermometerId, BigDecimal threshold) {

    public static Filter noFilter() {
        return new FilterBuilder().build();
    }

    public static FilterBuilder builder() {
        return new FilterBuilder();
    }

    public static class FilterBuilder {

        private String roomId;
        private String thermometerId;
        private BigDecimal threshold;

        public FilterBuilder roomId(String roomId) {
            this.roomId = roomId;
            return this;
        }

        public FilterBuilder thermometerId(String thermometerId) {
            this.thermometerId = thermometerId;
            return this;
        }

        public FilterBuilder threshold(BigDecimal threshold) {
            this.threshold = threshold;
            return this;
        }

        public Filter build() {
            return new Filter(roomId, thermometerId, threshold);
        }
    }
}
