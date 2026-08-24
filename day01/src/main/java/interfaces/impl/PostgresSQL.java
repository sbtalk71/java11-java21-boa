package interfaces.impl;

import interfaces.MyDbDriver;

public class PostgresSQL implements MyDbDriver {
    @Override
    public String getConnection() {
        return "COnnected to PostGres Database";
    }

    @Override
    public String getDbVersion() {
        return "DB Version 16";
    }

    @Override
    public boolean isClusterSupported() {
       return true;
    }
}
