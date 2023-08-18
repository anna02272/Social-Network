package com.ftn.socialNetwork.repository;

import com.ftn.socialNetwork.model.entity.Group;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository  extends JpaRepository<Group, Long> {
  boolean existsByName(String name);
  @Query("SELECT DISTINCT g FROM Group g LEFT JOIN FETCH g.groupAdmin ga WHERE g.isSuspended = :isSuspended")
  List<Group> findAllByIsSuspendedWithGroupAdmins(@Param("isSuspended") boolean isSuspended);

}
