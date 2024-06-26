package com.ftn.socialNetwork.indexrepository;

import com.ftn.socialNetwork.indexmodel.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<Table, Integer> {
}
