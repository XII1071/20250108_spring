package com.example.ex3.repository;

import com.example.ex3.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
//  List<Memo>
}
