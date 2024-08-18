package king.ict.cmr.domain.tabledefinition.dto;

import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
public class RowDto {
  private List<ColumnDto> columns;
  private boolean rowChanged;

  public boolean isRowNewValue() {
    return columns.stream().anyMatch(columnDto -> Objects.isNull(columnDto.getValue()) && columnDto.getColumnDefinition().isIdentity());
  }
  public boolean isRowNotNewValue() {
   return !isRowNewValue();
  }

  public String primaryKeyColumn() {
    return
      columns
        .stream()
        .filter(columnDto -> columnDto.getColumnDefinition().isIdentity())
        .map(columnDto -> columnDto.getColumnDefinition().getColumnName())
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Row has no identity"));
  }

  public Object primaryKeyValue() {
    Optional<Object> optional = columns
        .stream()
        .filter(columnDto -> columnDto.getColumnDefinition().isIdentity())
        .map(ColumnDto::getValue)
        .findFirst();
    return optional.orElse(null);
  }

}
