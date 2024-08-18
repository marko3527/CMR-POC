package king.ict.cmr.domain.web;

import king.ict.cmr.domain.tabledefinition.dto.RowDto;
import king.ict.cmr.domain.tabledefinition.dto.TableDefinitionDto;
import king.ict.cmr.domain.tabledefinition.dto.TableValuesDto;
import king.ict.cmr.domain.tabledefinition.facade.TableDefinitionFacade;
import lombok.RequiredArgsConstructor;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/database")
@RequiredArgsConstructor
public class DatabaseController {

  private final TableDefinitionFacade tableDefinitionFacade;

  @PostMapping("")
  public void generateTable(@RequestBody TableDefinitionDto tableDefinitionDto) {
    tableDefinitionFacade.createTable(tableDefinitionDto);
  }

  @GetMapping("/tables")
  public ResponseEntity<List<TableDefinitionDto>> getDefinedDatatables() {
    return ResponseEntity.ok(tableDefinitionFacade.getDefinedTables());
  }

  @GetMapping("/values/{tableName}")
  public ResponseEntity<TableValuesDto> getTableValues(@PathVariable String tableName) {
    return ResponseEntity.ok(tableDefinitionFacade.getValues(tableName));
  }

  @PostMapping("/values/{tableName}")
  public ResponseEntity<TableValuesDto> saveTableValues(@PathVariable String tableName, @RequestBody List<RowDto> rows) {
    return ResponseEntity.ok(tableDefinitionFacade.saveValues(tableName, rows));
  }

  @PostMapping("/values/{tableName}/delete")
  public ResponseEntity<TableValuesDto> deleteValue(@PathVariable String tableName, @RequestBody RowDto row) {
    return ResponseEntity.ok(tableDefinitionFacade.deleteValue(tableName, row));
  }

}
