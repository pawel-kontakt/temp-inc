package io.kontak.apps;

public record Pagination(Integer limit, Integer offset) {

    public static Pagination firstPage() {
        return new Pagination(10, 0);
    }
}
