package king.ict.cmr.domain.sqlgenerator;


public interface SqlGenerator {

  static SqlGenerator build(String variableName){
    return null;
  }

  String generate();

  String getCreatedSql();
}
