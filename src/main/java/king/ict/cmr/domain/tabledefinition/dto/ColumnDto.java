package king.ict.cmr.domain.tabledefinition.dto;

import lombok.Data;

import java.util.HashMap;

@Data
public class ColumnDto {
  private ColumnDefinitionDto columnDefinition;
  private Object value;
  private boolean columnChanged;

  public Object getForeignKeyValue() {
    if (value instanceof HashMap<?,?>) {
      return ((HashMap)value).get("id");
    } else if (value instanceof DropdownDto) {
      return ((DropdownDto) value).getId();
    }
    return value;
  }

}
