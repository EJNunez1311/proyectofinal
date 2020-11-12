package Entities;

public class DatabaseInfo {

    public String dbname;
    public String dbUsername;
    public String dbPassword;

    public DatabaseInfo(String dbname, String dbUsername, String dbPassword) {
        this.dbname = dbname;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
}
