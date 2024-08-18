package king.ict.cmr.domain.tableforeignkeydisplayvalue.repository;

import king.ict.cmr.domain.tableforeignkeydisplayvalue.TableForeignKeyDisplayValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableForeignKeyDisplayValueRepository extends JpaRepository<TableForeignKeyDisplayValue, Long> {

    TableForeignKeyDisplayValue findByTableNameAndReferencedTableName(String tableName, String referenceTableName);

    TableForeignKeyDisplayValue findByTableNameAndForeignKeyColumn(String tableName, String foreignKeyColumn);

    boolean existsByTableNameAndForeignKeyColumn(String tableName, String foreignKeyColumn);

}
