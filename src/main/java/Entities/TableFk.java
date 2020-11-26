package Entities;


public class TableFk {
    public String columnName;
    public String referenceTable;
    public String referenceColumnName;

    public TableFk(String columnName, String referenceTable, String referenceColumnName) {
        this.columnName = columnName;
        this.referenceTable = referenceTable;
        this.referenceColumnName = referenceColumnName;
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
