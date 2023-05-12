package com.ftn.socialNetwork.repository;

import com.ftn.socialNetwork.model.entity.Banned;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannedRepository extends JpaRepository<Banned, Long> {
}
