package io.kontak.apps;

public record Filter(String roomId, String thermometerId) {

    public static Filter noCriteria() {
        return new CriteriaBuilder().build();
    }

    public static CriteriaBuilder builder() {
        return new CriteriaBuilder();
    }

    public static class CriteriaBuilder {

        private String roomId;
        private String thermometerId;

        public CriteriaBuilder roomId(String roomId) {
            this.roomId = roomId;
            return this;
        }

        public CriteriaBuilder thermometerId(String thermometerId) {
            this.thermometerId = thermometerId;
            return this;
        }

        public Filter build() {
            return new Filter(roomId, thermometerId);
        }
    }
}
