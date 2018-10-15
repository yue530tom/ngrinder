package liquibase.database.core;

import liquibase.database.Database;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.BooleanType;
import liquibase.util.StringUtils;

@DataTypeInfo(name = "boolean", aliases = {"java.sql.Types.BOOLEAN", "java.lang.Boolean", "bit", "bool"}, minParameters = 0, maxParameters = 0, priority = LiquibaseDataType.PRIORITY_DATABASE)
public class BooleanTypeEx extends BooleanType {

	@Override
	public DatabaseDataType toDatabaseDataType(Database database) {
		String originalDefinition = StringUtils.trimToEmpty(getRawDefinition());
		if (database instanceof DB2Database || database instanceof FirebirdDatabase) {
			return new DatabaseDataType("SMALLINT");
		} else if (database instanceof MSSQLDatabase) {
			return new DatabaseDataType(database.escapeDataTypeName("bit"));
		} else if (database instanceof MySQLDatabase) {
			if (originalDefinition.toLowerCase().startsWith("bit")) {
				return new DatabaseDataType("BIT", getParameters());
			}
			return new DatabaseDataType("BIT", 1);
		} else if (database instanceof OracleDatabase) {
			return new DatabaseDataType("NUMBER", 1);
		} else if (database instanceof SybaseASADatabase || database instanceof SybaseDatabase) {
			return new DatabaseDataType("BIT");
		} else if (database instanceof DerbyDatabase) {
			if (((DerbyDatabase) database).supportsBooleanDataType()) {
				return new DatabaseDataType("BOOLEAN");
			} else {
				return new DatabaseDataType("SMALLINT");
			}
		} else if (database instanceof HsqlDatabase) {
			return new DatabaseDataType("BOOLEAN");
		} else if (database instanceof PostgresDatabase) {
			if (originalDefinition.toLowerCase().startsWith("bit")) {
				return new DatabaseDataType("BIT", getParameters());
			}
		} else if (database instanceof H2Database) {
			return new DatabaseDataType("CHAR(5)");
		}

		return super.toDatabaseDataType(database);
	}
}
