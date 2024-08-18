package king.ict.cmr.domain.tabledefinition.facade;

import king.ict.cmr.domain.enumeration.ColumnTypeEnum;
import king.ict.cmr.domain.tabledefinition.dto.*;
import king.ict.cmr.domain.tabledefinition.mapper.TableDefinitionMapper;
import king.ict.cmr.domain.tableforeignkeydisplayvalue.TableForeignKeyDisplayValue;
import king.ict.cmr.domain.tableforeignkeydisplayvalue.repository.TableForeignKeyDisplayValueRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TableDefinitionFacadeImpl implements TableDefinitionFacade {

  private final DSLContext dslContext;
  private final TableDefinitionMapper tableDefinitionMapper;
  private final TableForeignKeyDisplayValueRepository tableForeignKeyDisplayValueRepository;

  @Override
  public void createTable(TableDefinitionDto dto) {
    dslContext.createTableIfNotExists(DSL.table(DSL.name(dto.getTableName())))
      .column(DSL.field(DSL.name("id_" + dto.getTableName()), SQLDataType.BIGINT.identity(true)))
      .columns(dto.getColumns().stream().map(this::createColumn).collect(Collectors.toList()))
      .constraint(createPrimaryKeyConstraint(dto))
      .constraints(dto.getColumns().stream().filter(ColumnDefinitionDto::isUnique).map(this::createUniqueConstraints).collect(Collectors.toList()))
      .constraints(dto.getColumns().stream().filter(ColumnDefinitionDto::isForeignKey).map(col -> createForeignKeyConstraint(dto.getTableName(), col)).collect(Collectors.toList()))
      .execute();
  }

  @Override
  public List<TableDefinitionDto> getDefinedTables() {
    List<String> tableNames = dslContext.meta().getSchemas("public").get(0).getTables().stream().map(Named::getName).toList();
    return tableNames.stream().map(tableName -> {
      TableDefinitionDto dto = new TableDefinitionDto();
      dto.setTableName(tableName);
      return dto;
    }).toList();
  }

  @Override
  public TableValuesDto getValues(String tableName) {
    List<ColumnDefinitionDto> columnDefinitions = getColumnsForTable(tableName);

    Result<Record> records =
      dslContext
        .select(
          columnDefinitions.stream().map(columnDef -> DSL.field(DSL.name(columnDef.getColumnName()))).collect(Collectors.toList()))
        .from(DSL.name(tableName))
        .fetch();

    TableValuesDto dto = new TableValuesDto();
    dto.setColumns(columnDefinitions);
    List<RowDto> rows = records.stream().map(record -> tableDefinitionMapper.mapToRowDto(tableName, record, columnDefinitions)).toList();
    dto.setRows(rows);
    return dto;
  }

  @Override
  public TableValuesDto saveValues(String tableName, List<RowDto> rows) {
    List<RowDto> changedRows = rows.stream().filter(RowDto::isRowChanged).filter(RowDto::isRowNotNewValue).toList();
    List<RowDto> newRows = rows.stream().filter(RowDto::isRowChanged).filter(RowDto::isRowNewValue).toList();

    changedRows.forEach(row -> {
      dslContext
        .update(DSL.table(DSL.name(tableName)))
        .set(
          row.getColumns().stream().filter(ColumnDto::isColumnChanged).collect(
            Collectors.toMap(column -> column.getColumnDefinition().getColumnName(), col -> {
              if (col.getColumnDefinition().isForeignKey()) {
                return col.getForeignKeyValue();
              }
              return col.getValue();
            })
          ))
        .where(
          DSL.field(DSL.name(row.primaryKeyColumn())).eq(row.primaryKeyValue())
        ).execute();
    });

    newRows.forEach(row -> {
      dslContext.insertInto(DSL.table(DSL.name(tableName)))
      .columns(
        row.getColumns().stream().filter(columnDto -> !columnDto.getColumnDefinition().getColumnName().equals(row.primaryKeyColumn())).map(column -> DSL.field(DSL.name(column.getColumnDefinition().getColumnName()))).toList()
      )
      .values(
        row.getColumns().stream().map(col -> {
          if (col.getColumnDefinition().isForeignKey()) {
            return col.getForeignKeyValue();
          }
          return col.getValue();
        }).filter(value -> !Objects.isNull(value)).toList()
      ).execute();
    });
    return getValues(tableName);
  }

  @Override
  public TableValuesDto deleteValue(String tableName, RowDto rowDto) {
    dslContext.deleteFrom(DSL.table(DSL.name(tableName)))
      .where(
        DSL.field(DSL.name(rowDto.primaryKeyColumn())).eq(rowDto.primaryKeyValue())
      )
      .execute();
    return getValues(tableName);
  }

  @Override
  public List<String> fetchColumns(String tableName) {
    return Arrays
      .stream(dslContext.meta().getSchemas("public").get(0).getTable(tableName).fields())
      .map(Field::getName)
      .toList();
  }

  private Field<?> createColumn(ColumnDefinitionDto column) {
    ColumnTypeEnum dataType = ColumnTypeEnum.fromValue(column.getColumnType());
    return switch (dataType) {
      case BOOLEAN -> createBooleanColumn(column);
      case DATUM -> createDateColumn(column);
      case STRING -> createVarCharColumn(column);
      case NUMBER -> createNumberColumn(column);
    };
  }

  private Field<?> createBooleanColumn(ColumnDefinitionDto column) {
    return DSL.field(column.getColumnName(), SQLDataType.BOOLEAN.nullable(!column.isMandatory()).defaultValue((Boolean) column.getDefaultValue()));
  }
  private Field<?> createDateColumn(ColumnDefinitionDto column) {
    return DSL.field(column.getColumnName(), SQLDataType.DATE.nullable(!column.isMandatory()).defaultValue((Date) column.getDefaultValue()));
  }
  private Field<?> createVarCharColumn(ColumnDefinitionDto column) {
    return DSL.field(column.getColumnName(), SQLDataType.VARCHAR(100).nullable(!column.isMandatory()).defaultValue((String)column.getDefaultValue()));
  }

  private Field<?> createNumberColumn(ColumnDefinitionDto column) {
    return DSL.field(column.getColumnName(), SQLDataType.BIGINT.nullable(!column.isMandatory()));
  }

  private Constraint createUniqueConstraints(ColumnDefinitionDto column) {
    return DSL.constraint(DSL.name(column.getColumnName() + "_unique")).unique(createColumn(column));
  }

  @Transactional
  public Constraint createForeignKeyConstraint(String tableName, ColumnDefinitionDto column) {
    TableForeignKeyDisplayValue value = new TableForeignKeyDisplayValue();
    value.setForeignKeyDisplayColumn(column.getFkDisplayValueColumn());
    value.setReferencedTableName(column.getReferencedTableName());
    value.setForeignKeyColumn(column.getColumnName());
    value.setTableName(tableName);
    tableForeignKeyDisplayValueRepository.save(value);
    return DSL
      .constraint(DSL.name("fk_" + column.getColumnName() + "_" + column.getReferencedTableName()))
      .foreignKey(DSL.name(column.getColumnName()))
      .references(DSL.name(column.getReferencedTableName()), DSL.name("id_" + column.getReferencedTableName()));
  }

  private Constraint createPrimaryKeyConstraint(TableDefinitionDto dto) {
    return DSL
      .constraint(DSL.name("pk_" + dto.getTableName()))
      .primaryKey(DSL.name("id_" + dto.getTableName()));
  }

  private List<ColumnDefinitionDto> getColumnsForTable(String tableName) {
    return Arrays
      .stream(Objects.requireNonNull(dslContext.meta().getSchemas("public").get(0).getTable(DSL.name(tableName)))
        .fields()).map(field -> tableDefinitionMapper.mapFieldToColumnDefinitionDto(tableName, field)).toList();
  }

}
