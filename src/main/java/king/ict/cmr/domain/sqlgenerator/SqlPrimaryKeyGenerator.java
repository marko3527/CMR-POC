package king.ict.cmr.domain.sqlgenerator;

public class SqlPrimaryKeyGenerator extends SqlDataGenerator{

  private String initialStatement = "%s BIGSERIAL PRIMARY KEY";

  public static SqlPrimaryKeyGenerator build(String variableName) {
    SqlPrimaryKeyGenerator sqlPrimaryKeyGenerator = new SqlPrimaryKeyGenerator();
    sqlPrimaryKeyGenerator.createdSql += String.format(sqlPrimaryKeyGenerator.initialStatement, variableName);
    return sqlPrimaryKeyGenerator;
  }

  @Override
  public String getCreatedSql() {
    return this.createdSql;
  }
}
