package com.ftn.socialNetwork.repository;

import com.ftn.socialNetwork.model.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
}
