package king.ict.cmr.domain.tabledefinition.dto;

import lombok.Data;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.util.List;

@Data
public class TableValuesDto {

  private List<ColumnDefinitionDto> columns;
  private List<RowDto> rows;

  public static Field getPrimaryKeyColumn(String tableName, List<ColumnDto> columns) {
    return columns.stream()
      .map(ColumnDto::getColumnDefinition)
      .filter(columnName -> columnName.getColumnName().equals("id_" + tableName))
      .map(colDefDto -> DSL.field(DSL.name(colDefDto.getColumnName())))
     .findFirst().orElse(null);
  }

  public static Object getPrimaryKeyValue(String tableName, List<ColumnDto> columns) {
    List<Object> primaryKeyValue = columns.stream()
      .filter(col -> col.getColumnDefinition().getColumnName().equals("id_" + tableName))
      .map(ColumnDto::getValue)
      .toList();
    if (primaryKeyValue.size() > 0) {
      return primaryKeyValue.get(0);
    };
    return null;
  }

}
