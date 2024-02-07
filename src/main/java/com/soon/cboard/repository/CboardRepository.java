package com.soon.cboard.repository;

import com.soon.cboard.entity.Cboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CboardRepository extends JpaRepository<Cboard, Long> {

    @Query(value = "SELECT * FROM cboard ORDER BY created_at DESC LIMIT :offset, :pageSize", nativeQuery = true)
    List<Cboard> findBoardsByPage(int offset, int pageSize);

}
