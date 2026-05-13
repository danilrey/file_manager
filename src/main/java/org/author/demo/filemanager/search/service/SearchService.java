package org.author.demo.filemanager.search.service;

import org.author.demo.filemanager.file.model.FileEntity;
import org.author.demo.filemanager.file.repository.FileRepository;
import org.author.demo.filemanager.generation.model.GeneratedDocEntity;
import org.author.demo.filemanager.generation.repository.GeneratedDocRepository;
import org.author.demo.filemanager.search.dto.SearchResponseDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SearchService {
    protected static final String FILE = "FILE";
    protected static final String GENERATED_DOC = "GENERATED_DOC";
    protected static final String QUERY_MUST_NOT_BE_EMPTY_MESSAGE = "Query must not be empty";

    private final FileRepository fileRepository;
    private final GeneratedDocRepository generatedDocRepository;

    public SearchService(FileRepository fileRepository, GeneratedDocRepository generatedDocRepository) {
        this.fileRepository = fileRepository;
        this.generatedDocRepository = generatedDocRepository;
    }

    public List<SearchResponseDto> search(String query, int limit) {
        if (isValidQuery(query)) {
            String normalizedQuery = query.trim();

            if (limit <= 0) {
                limit = 20;
            }

            List<FileEntity> fileEntities = fileRepository.searchByQuery(normalizedQuery);
            List<GeneratedDocEntity> generatedDocEntities = generatedDocRepository.searchByQuery(normalizedQuery);

            return buildSearchResults(limit, fileEntities, generatedDocEntities);
        } else {
            throw new IllegalArgumentException(QUERY_MUST_NOT_BE_EMPTY_MESSAGE);
        }
    }

    private List<SearchResponseDto> buildSearchResults(int limit, List<FileEntity> fileEntities, List<GeneratedDocEntity> generatedDocEntities) {
        return Stream.concat(
                        fileEntities.stream().map(this::fileEntityToDto),
                        generatedDocEntities.stream().map(this::generatedEntityToDto)
                )
                .sorted(Comparator
                        .comparing(SearchResponseDto::getCreatedAt,
                                Comparator.reverseOrder()))
                .limit(limit)
                .toList();
    }

    private boolean isValidQuery(String query) {
        return query != null && !query.trim().isEmpty();
    }

    private SearchResponseDto fileEntityToDto(FileEntity fileEntity) {
        SearchResponseDto searchResponseDto = new SearchResponseDto();
        searchResponseDto.setType(FILE);
        searchResponseDto.setId(fileEntity.getId());
        searchResponseDto.setTitle(fileEntity.getOriginalName());
        searchResponseDto.setCreatedAt(fileEntity.getCreatedAt());

        return searchResponseDto;
    }

    private SearchResponseDto generatedEntityToDto(GeneratedDocEntity generatedDocEntity) {
        SearchResponseDto searchResponseDto = new SearchResponseDto();
        searchResponseDto.setType(GENERATED_DOC);
        searchResponseDto.setId(generatedDocEntity.getId());
        searchResponseDto.setTitle(generatedDocEntity.getTitle());
        searchResponseDto.setCreatedAt(generatedDocEntity.getCreatedAt());

        return searchResponseDto;
    }
}
