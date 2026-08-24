package interfaces.impl;

import interfaces.MyDbDriver;

public class MySqlDB implements MyDbDriver {
    @Override
    public String getConnection() {
        return "COnnected to MySql Database";
    }

    @Override
    public String getDbVersion() {
        return "DB Version 8.0";
    }
}
