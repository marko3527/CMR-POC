package king.ict.cmr.domain.tabledefinition.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColumnDefinitionDto {
  private String columnName;
  private String columnType;
  private boolean mandatory;
  private boolean unique;
  private Object defaultValue;
  private boolean foreignKey;
  private String referencedTableName;
  private String fkDisplayValueColumn;
  private boolean multiselectColumn;
  private boolean identity;
  private List<DropdownDto> foreignKeyOptions;

}
