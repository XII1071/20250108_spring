package com.example.apiserver.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchJournalRepository {

  Page<Object[]> searchPage(String type, String keyword, Pageable pageable);

}
