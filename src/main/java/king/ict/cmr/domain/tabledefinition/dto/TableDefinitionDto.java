package king.ict.cmr.domain.tabledefinition.dto;

import lombok.Data;

import java.util.List;

@Data
public class TableDefinitionDto {

  private String tableName;
  private List<ColumnDefinitionDto> columns;

}
