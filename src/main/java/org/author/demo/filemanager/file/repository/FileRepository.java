package org.author.demo.filemanager.file.repository;

import org.author.demo.filemanager.file.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, UUID> {
    @Query("SELECT DISTINCT f from FileEntity f " +
            "LEFT JOIN f.tags t " +
            "WHERE lower(f.originalName) like lower(concat('%', :query, '%')) " +
            "     or lower(f.category) like lower(concat('%', :query, '%')) " +
            "     or lower(f.summary) like lower(concat('%', :query, '%')) " +
            "     or lower(t) like lower(concat('%', :query, '%'))")
    List<FileEntity> searchByQuery(@Param("query") String query);
}
