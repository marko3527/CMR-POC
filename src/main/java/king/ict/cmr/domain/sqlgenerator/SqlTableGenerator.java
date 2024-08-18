package king.ict.cmr.domain.sqlgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SqlTableGenerator implements SqlGenerator {

  private final String initialStatement = "CREATE TABLE IF NOT EXISTS %s(";
  private String createdSql = "";
  private final List<SqlDataGenerator> columns = new ArrayList<>();

  public static SqlTableGenerator build(String variableName) {
    SqlTableGenerator sqlTableGenerator = new SqlTableGenerator();
    sqlTableGenerator.createdSql += String.format(sqlTableGenerator.initialStatement, variableName);
    sqlTableGenerator.columns.add(SqlPrimaryKeyGenerator.build("id_" + variableName));
    return sqlTableGenerator;
  }

  public SqlTableGenerator add(SqlDataGenerator sqlGenerator) {
    this.columns.add(sqlGenerator);
    return this;
  }

  @Override
  public String generate() {
    return this.createdSql + this.columns.stream().map(SqlGenerator::getCreatedSql).collect(Collectors.joining(",")) + ");";
  }

  @Override
  public String getCreatedSql() {
    return this.createdSql;
  }
}
