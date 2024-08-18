package king.ict.cmr.domain.enumeration;


import lombok.Getter;
import org.jooq.DataType;
import org.jooq.impl.SQLDataType;

import java.util.Arrays;

@Getter
public enum ColumnTypeEnum {

  BOOLEAN(1L, "BOOLEAN", SQLDataType.BOOLEAN),
  STRING(2L, "STRING", SQLDataType.VARCHAR),
  DATUM(3L, "DATUM", SQLDataType.DATE),
  NUMBER(4L, "NUMBER", SQLDataType.BIGINT);

  private Long idColumnType;
  private String value;
  private DataType<?> dataType;

  ColumnTypeEnum(Long idColumnType, String value, DataType<?> dataType) {
    this.idColumnType = idColumnType;
    this.value = value;
    this.dataType = dataType;
  }

  public static ColumnTypeEnum fromValue(String value) {
    return Arrays.stream(ColumnTypeEnum.values()).filter(type -> type.value.equals(value)).findFirst().orElse(null);
  }

  public static ColumnTypeEnum fromSqlDataType(DataType<?> dataType) {
    return Arrays.stream(ColumnTypeEnum.values()).filter(value -> dataType.equals(value.getDataType())).findFirst().orElse(null);
  }

}
