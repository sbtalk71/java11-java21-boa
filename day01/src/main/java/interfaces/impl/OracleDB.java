package interfaces.impl;

import interfaces.MyDbDriver;

public class OracleDB implements MyDbDriver {
    @Override
    public String getConnection() {
        return "COnnected to Oracle Database";
    }

    @Override
    public String getDbVersion() {
        return "DB Version 12c";
    }
}
