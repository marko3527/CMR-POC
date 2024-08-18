package king.ict.cmr.domain.tabledefinition.facade;

import king.ict.cmr.domain.tabledefinition.dto.RowDto;
import king.ict.cmr.domain.tabledefinition.dto.TableDefinitionDto;
import king.ict.cmr.domain.tabledefinition.dto.TableValuesDto;

import java.util.List;

public interface TableDefinitionFacade {

  void createTable(TableDefinitionDto dto);

  List<TableDefinitionDto> getDefinedTables();

  TableValuesDto getValues(String tableName);

  TableValuesDto saveValues(String tableName, List<RowDto> rows);

  TableValuesDto deleteValue(String tableName, RowDto rowDto);

  List<String> fetchColumns(String tableName);

}
