package Entities;


public class TableFk {
    public String schema;
    public String tableName;
    public String columnName;
    public String referenceTable;
    public String referenceColumnName;


    public TableFk(String schema, String tableName, String columnName, String referenceTable, String referenceColumnName) {
        this.schema = schema;
        this.tableName = tableName;
        this.columnName = columnName;
        this.referenceTable = referenceTable;
        this.referenceColumnName = referenceColumnName;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getReferenceTable() {
        return referenceTable;
    }

    public void setReferenceTable(String referenceTable) {
        this.referenceTable = referenceTable;
    }

    public String getReferenceColumnName() {
        return referenceColumnName;
    }

    public void setReferenceColumnName(String referenceColumnName) {
        this.referenceColumnName = referenceColumnName;
    }
}
