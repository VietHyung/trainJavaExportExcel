# lib_common_ex
export csv and xlsx
## Clone sources:
```
git clone https://gitlab.com/vanhuan/lib_common_ex.git
git checkout master
```
## Build sources
```
mvn clean install
add to pom:
    <dependency>
        <groupId>com.lib</groupId>
        <artifactId>download-xlsx</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
start your project
```

## Example
```
View detail package example in this sources
1. Create service implement ExExecuteIf and implement 3 method.
2. Request payload implement RequestQueryIf.
### Services ###
    @Data
    @Component("xlsx1")
    public class XlsxExecute implements ExExecuteIf {
    
        @Value("${export.total}")
        private Long totalExport;
    
        @Resource
        private RepositoryCsv repositoryCsv;
    
        @Override
        public StreamingResponseBody execute(RequestQueryIf requestQuery) {
            if (totalExport != null) {
                this.exDownloadOption.setTotalExport(totalExport);
            }
            return new XlsxDownloadStreamingBody(this, CsvEntity.class, exDownloadOption, requestQuery);
        }
    
        @Override
        public Long totalCountData(RequestQueryIf requestQuery) {
            return repositoryCsv.countFindAll("huan");
        }
    
        @Override
        public List findData(PageRequest pageRequest, RequestQueryIf requestQuery) {
            return repositoryCsv.searchFindAll("huan", pageRequest);
        }
    }
    
    ---------------------------------------------------
    ### Repository ###
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
    
    ------------------------------------------------
    ### Entity ###
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
    
```
