package com.ftn.socialNetwork.repository;

import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.model.entity.GroupAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository  extends JpaRepository<Group, Long> {

}
