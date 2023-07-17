package com.example.defecttrackerserver.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatedResponse<T> {
    private List<T> content;
    private int totalPages;
    private int totalElements;
    private int currentPage;
    private Object filterValues;

    public PaginatedResponse(List<T> content, int totalPages, int totalElements,
                             int currentPage, Object filterValues) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.filterValues = filterValues;
    }
}
