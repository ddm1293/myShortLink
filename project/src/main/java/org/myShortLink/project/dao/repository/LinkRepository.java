package org.myShortLink.project.dao.repository;

import org.myShortLink.project.dao.entity.Link;
import org.myShortLink.project.dao.repository.projection.GroupLinkCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {

    @Query("""
            SELECT l
            FROM Link l
            WHERE l.gid = :gid AND l.archived = false AND l.enabled = true
            """)
    Page<Link> findLinksUnderSameGroup(@Param("gid") String gid, Pageable pageable);

    @Query("""
        SELECT l
        FROM Link l
        WHERE l.gid IN :gidList AND l.archived = false AND l.enabled = false
        """)
    Page<Link> findDisabledLinksUnderSameGroup(@Param("gidList") List<String> gidList, Pageable pageable);

    @Query("""
            SELECT l.gid AS gid, COUNT(*) AS count
            FROM Link l
            WHERE l.archived = false AND l.enabled = true AND l.gid = :gid
            GROUP BY l.gid
            """)
    GroupLinkCount countGroupLink(@Param("gid") String gid);

    @Query("""
            SELECT l
            FROM Link l
            WHERE l.gid = :gid AND l.fullShortUrl = :fullShortUrl AND l.archived = :archived AND l.enabled = :enabled
            """)
    Optional<Link> findLink(
            @Param("gid") String gid,
            @Param("fullShortUrl") String fullShortUrl,
            @Param("archived") Boolean archived,
            @Param("enabled") Boolean enabled
    );

    @Modifying
    @Query("DELETE FROM Link l WHERE l.gid = :gid AND l.fullShortUrl = :fullShortUrl")
    void deleteLink(@Param("gid") String gid, @Param("fullShortUrl") String fullShortUrl);

    @Modifying
    @Query("UPDATE Link l SET l.enabled = false WHERE l.id = :id AND l.gid = :gid")
    void disableLink(@Param("id") Long id, @Param("gid") String gid);

    @Modifying
    @Query("UPDATE Link l SET l.enabled = true WHERE l.id = :id AND l.gid = :gid")
    void enableLink(@Param("id") Long id, @Param("gid") String gid);

    @Modifying
    @Query("""
            UPDATE Link l
            SET l.archived = true, l.enabled = false
            WHERE l.id = :id AND l.gid = :gid
            """)
    void archiveLink(@Param("id") Long id, @Param("gid") String gid);
}
