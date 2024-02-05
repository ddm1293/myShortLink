package org.myShortLink.project.dao.repository;

import org.myShortLink.project.dao.entity.LinkRouter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LinkRouterRepository extends JpaRepository<LinkRouter, Long> {

    @Query("SELECT l FROM LinkRouter l WHERE l.fullShortUrl = :fullShortUrl")
    Optional<LinkRouter> getLinkRouterFromFullShortUrl(@Param("fullShortUrl") String fullShortUrl);
}

