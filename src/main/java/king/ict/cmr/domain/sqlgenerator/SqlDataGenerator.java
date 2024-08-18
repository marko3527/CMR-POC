package king.ict.cmr.domain.sqlgenerator;

public abstract class SqlDataGenerator implements SqlGenerator {

  String createdSql = "";

  public static SqlGenerator build(String variableName) {
    return null;
  }

  @Override
  public String generate() {
    return this.createdSql + ",";
  }

  @Override
  public String getCreatedSql() {
    return this.createdSql;
  }

  public SqlDataGenerator makeNotNull() {
    this.createdSql += " NOT NULL";
    return this;
  }

  public SqlDataGenerator defaultVal(boolean booleanValue) {
    this.createdSql += " DEFAULT " + booleanValue;
    return this;
  }

  public SqlDataGenerator defaultVal(String stringValue) {
    this.createdSql += " DEFAULT " + stringValue;
    return this;
  }
}
