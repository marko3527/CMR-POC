package king.ict.cmr.domain.sqlgenerator;

public class SqlBooleanGenerator extends SqlDataGenerator{

  private String initialStatement = "%s BOOLEAN";

  public static SqlBooleanGenerator build(String variableName) {
    SqlBooleanGenerator sqlBooleanGenerator = new SqlBooleanGenerator();
    sqlBooleanGenerator.createdSql += String.format(sqlBooleanGenerator.initialStatement, variableName);
    return sqlBooleanGenerator;
  }

  @Override
  public String getCreatedSql() {
    return this.createdSql;
  }

}
