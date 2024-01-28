package org.myShortLink.project.dao.repository;

import org.myShortLink.project.dao.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {
}
