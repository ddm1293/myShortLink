package org.myShortLink.admin.dao.repository;

import org.myShortLink.admin.dao.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("""
            SELECT g
            FROM Group g
            WHERE (:username IS NULL OR g.username = :username) AND g.archived = false
            ORDER BY g.sortOrder DESC, g.updatedAt DESC
            """)
    List<Group> getGroups(@Param("username") String username);

}
