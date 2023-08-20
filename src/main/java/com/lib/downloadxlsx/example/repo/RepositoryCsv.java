package com.lib.downloadxlsx.example.repo;

import com.lib.downloadxlsx.example.CsvEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The interface Repository csv.
 */
public interface RepositoryCsv extends JpaRepository<CsvEntity, Long> {

  /**
   * Search find all list.
   *
   * @param pageRequest the page request
   * @return the list
   */
  @Query(value = "select * from csv_entity where note like %:note%", nativeQuery = true)
  List<CsvEntity> searchFindAll(@Param("note") String note, Pageable pageRequest);

  /**
   * Count find all long.
   *
   * @return the long
   */
  @Query(value = "select count(1) from csv_entity where note like %:note%", nativeQuery = true)
  Long countFindAll(@Param("note") String note);

}
