package org.author.demo.filemanager.search.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SearchRequestDto {
    @NotBlank(message = "query is required")
    @Size(max = 200, message = "query is too long")
    private String query;

    @Min(value = 1, message = "limit must be more than 1")
    @Max(value = 100, message = "limit must be less than 100")
    private int limit = 20;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
