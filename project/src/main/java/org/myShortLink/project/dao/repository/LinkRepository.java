package org.myShortLink.project.dao.repository;

import org.myShortLink.project.dao.entity.Link;
import org.myShortLink.project.dao.repository.projection.GroupLinkCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LinkRepository extends JpaRepository<Link, Long> {

    @Query("SELECT l FROM Link l WHERE l.gid = :gid")
    Page<Link> findLinks(@Param("gid") String gid, Pageable pageable);

    @Query("""
            SELECT l.gid AS gid, COUNT(*) AS count
            FROM Link l
            WHERE l.archived = false AND l.enabled = true AND l.gid = :gid
            GROUP BY l.gid
            """)
    GroupLinkCount countGroupLink(@Param("gid") String gid);
}
