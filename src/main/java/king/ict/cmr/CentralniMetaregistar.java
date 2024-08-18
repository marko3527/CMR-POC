package king.ict.cmr;

import king.ict.cmr.domain.sqlgenerator.SqlBooleanGenerator;
import king.ict.cmr.domain.sqlgenerator.SqlTableGenerator;
import king.ict.cmr.domain.sqlgenerator.SqlVarCharGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class CentralniMetaregistar {

  public static void main(String[] args) {

    String sql =
      SqlTableGenerator.build("testna_tablica").add(
        SqlVarCharGenerator.build("testna_kolona").makeNotNull()
      ).add(
        SqlBooleanGenerator.build("boolean_kolona").defaultVal(true)
      ).generate();

    SpringApplication.run(CentralniMetaregistar.class, args);
  }
}
