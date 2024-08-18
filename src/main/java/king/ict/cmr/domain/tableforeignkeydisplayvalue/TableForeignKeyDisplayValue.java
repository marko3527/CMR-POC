package king.ict.cmr.domain.tableforeignkeydisplayvalue;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "table_foreign_key_display_value", schema = "predefined_tables")
public class TableForeignKeyDisplayValue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_table_foreign_key_display_value")
  private Long idTableForeignKeyDisplayValue;

  @Column(name = "table_name")
  private String tableName;

  @Column(name = "foreign_key_column")
  private String foreignKeyColumn;

  @Column(name = "referenced_table_name")
  private String referencedTableName;

  @Column(name = "foreign_key_display_column")
  private String foreignKeyDisplayColumn;
}
