package com.ftn.socialNetwork.repository;

import com.ftn.socialNetwork.model.entity.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRequestRepository extends JpaRepository<GroupRequest, Long> {
}
