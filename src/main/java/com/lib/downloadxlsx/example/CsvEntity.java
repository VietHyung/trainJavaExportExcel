package com.lib.downloadxlsx.example;

import com.lib.downloadxlsx.anotation.ExColumn;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * The type Csv entity.
 */
@Entity
@Data
@NoArgsConstructor
public class CsvEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @ExColumn(columnLabel = "User Name", order = 1)
  private String userName;
  @ExColumn(columnLabel = "Note", order = 2)
  private String note;
  @ExColumn(columnLabel = "Birth", order = 3, dateFormat = "dd/MM/yyyy")
  private Date birth;
  private String status;
}