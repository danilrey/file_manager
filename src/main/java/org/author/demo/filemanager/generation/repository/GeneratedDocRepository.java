package org.author.demo.filemanager.generation.repository;

import org.author.demo.filemanager.generation.model.GeneratedDocEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GeneratedDocRepository extends JpaRepository<GeneratedDocEntity, UUID> {

    @Query("SELECT g from GeneratedDocEntity g " +
            "WHERE lower(g.title) like lower(concat('%', :query, '%')) " +
            "or lower(g.content) like lower(concat('%', :query, '%')) " +
            "or lower(g.prompt) like lower(concat('%', :query, '%'))")
    List<GeneratedDocEntity> searchByQuery(@Param("query") String query);
}