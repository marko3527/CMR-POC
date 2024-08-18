package king.ict.cmr.domain.web;

import king.ict.cmr.domain.dbtype.mapper.ColumnTypeMapper;
import king.ict.cmr.domain.enumeration.ColumnTypeEnum;
import king.ict.cmr.domain.tabledefinition.dto.DropdownDto;
import king.ict.cmr.domain.tabledefinition.facade.TableDefinitionFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
public class OptionsController {

  private final ColumnTypeMapper columnTypeMapper;
  private final TableDefinitionFacade tableDefinitionFacade;

  @GetMapping("/db-types")
  public ResponseEntity<List<DropdownDto>> fetchDatabaseTypeOptions() {
    return ResponseEntity.ok(columnTypeMapper.mapToDto(ColumnTypeEnum.values()));
  }

  @GetMapping("/{tableName}/columns")
  public ResponseEntity<List<String>> fetchColumnsForTable(@PathVariable String tableName) {
    return ResponseEntity.ok(tableDefinitionFacade.fetchColumns(tableName));
  }

}
