package org.myShortLink.admin.dao.repository;

import org.myShortLink.admin.dao.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("""
            SELECT g
            FROM Group g
            WHERE g.username = :username AND g.archived = false
            ORDER BY g.sortOrder DESC, g.updatedAt DESC
            """)
    List<Group> getGroups(@Param("username") String username);


    @Query("""
            SELECT g
            FROM Group g
            WHERE g.username = :username AND g.gid = :gid AND g.archived = false
            """)
    Optional<Group> getGroup(@Param("username") String username,
                             @Param("gid") String gid);
}
