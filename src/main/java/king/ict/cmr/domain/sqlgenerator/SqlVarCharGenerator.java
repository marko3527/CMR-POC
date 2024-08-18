package king.ict.cmr.domain.sqlgenerator;

public class SqlVarCharGenerator extends SqlDataGenerator {

  private String initialStatement = "%s VARCHAR(100)";

  public static SqlVarCharGenerator build(String variableName) {
    SqlVarCharGenerator sqlVarCharGenerator = new SqlVarCharGenerator();
    sqlVarCharGenerator.createdSql += String.format(sqlVarCharGenerator.initialStatement, variableName);
    return sqlVarCharGenerator;
  }

  @Override
  public String getCreatedSql() {
    return this.createdSql;
  }
}
