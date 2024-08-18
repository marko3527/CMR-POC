package king.ict.cmr.domain.tabledefinition.mapper;

import king.ict.cmr.domain.enumeration.ColumnTypeEnum;
import king.ict.cmr.domain.tabledefinition.dto.ColumnDefinitionDto;
import king.ict.cmr.domain.tabledefinition.dto.ColumnDto;
import king.ict.cmr.domain.tabledefinition.dto.DropdownDto;
import king.ict.cmr.domain.tabledefinition.dto.RowDto;
import king.ict.cmr.domain.tableforeignkeydisplayvalue.TableForeignKeyDisplayValue;
import king.ict.cmr.domain.tableforeignkeydisplayvalue.repository.TableForeignKeyDisplayValueRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.QOM;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TableDefinitionMapper {

  private final DSLContext dslContext;
  private final TableForeignKeyDisplayValueRepository tableForeignKeyDisplayValueRepository;

  public ColumnDefinitionDto mapFieldToColumnDefinitionDto(String tableName, Field<?> field) {
    ColumnDefinitionDto dto = new ColumnDefinitionDto();
    dto.setColumnName(field.getName());
    dto.setIdentity(field.getDataType().identity());
    dto.setMandatory(!field.getDataType().nullable());

    dto.setForeignKey(tableForeignKeyDisplayValueRepository.existsByTableNameAndForeignKeyColumn(tableName, field.getName()));

    if (dto.isForeignKey()) {
      TableForeignKeyDisplayValue val = tableForeignKeyDisplayValueRepository.findByTableNameAndForeignKeyColumn(tableName, field.getName());
      Result<Record> records = dslContext
        .select(
          List.of(
            DSL.field(DSL.name("id_" + val.getReferencedTableName())),
            DSL.field(DSL.name(val.getForeignKeyDisplayColumn()))
          )
        )
        .from(DSL.table(DSL.name(val.getReferencedTableName())))
        .fetch();
      dto.setForeignKeyOptions(records.stream().map(this::toDropdownDto).toList());
    }

    if (Objects.nonNull(field.getDataType().defaultValue())) {
      QueryPart queryPart = field.getDataType().defaultValue();
      Field<?> evaluatedField = DSL.field(queryPart.toString(), field.getDataType());
      Result<? extends Record1<?>> result = dslContext.select(evaluatedField).fetch();
      dto.setDefaultValue(result.get(0).value1());
    }

    dto.setColumnType(ColumnTypeEnum.fromSqlDataType(field.getDataType().getSQLDataType()).getValue());
    return dto;
  }

  public RowDto mapToRowDto(String tableName, Record record, List<ColumnDefinitionDto> columnDefinitions) {
    RowDto rowDto = new RowDto();
    List<ColumnDto> columns = new ArrayList<>();
    for (int i = 0; i < columnDefinitions.size(); i++) {
      ColumnDto column = new ColumnDto();
      column.setColumnDefinition(columnDefinitions.get(i));

      if (columnDefinitions.get(i).isForeignKey()) {
        TableForeignKeyDisplayValue val = tableForeignKeyDisplayValueRepository.findByTableNameAndForeignKeyColumn(tableName, columnDefinitions.get(i).getColumnName());
        DropdownDto dto = new DropdownDto();
        dto.setId((Long)record.getValue(i));
        dto.setValue(
          dslContext
            .select(DSL.field(DSL.name(val.getForeignKeyDisplayColumn())))
            .from(DSL.table(DSL.name(val.getReferencedTableName())))
            .where(DSL.field(DSL.name("id_" + val.getReferencedTableName())).eq(record.getValue(i)))
            .fetch().get(0).getValue(0)
        );
        column.setValue(dto);
      } else {
        column.setValue(record.getValue(i));
      }
      columns.add(column);
    }
    rowDto.setColumns(columns);
    return rowDto;

  }

  public DropdownDto toDropdownDto(Record record) {
    DropdownDto dto = new DropdownDto();
    dto.setId((Long) record.getValue(0));
    dto.setValue(record.getValue(1));
    return dto;
  }

}
