package com.soon.cboard.repository;

import com.soon.cboard.entity.Cboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CboardRepository extends JpaRepository<Cboard, Long> {

    Page<Cboard> findAllByOrderByCreatedAtDesc(Pageable pageable);


}
