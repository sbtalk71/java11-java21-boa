package interfaces;

import interfaces.impl.MySqlDB;
import interfaces.impl.OracleDB;
import interfaces.impl.PostgresSQL;

import java.util.Optional;

public class DriverFactory {

    public static Optional<MyDbDriver> create(String dbType){
        if(dbType.equals("oracle")){
            return Optional.of(new OracleDB());
        }else if(dbType.equals("mysql")){
            return Optional.of(new MySqlDB());
        }else if(dbType.equals("postgres")){
            return Optional.of(new PostgresSQL());
        }else{
            //throw new IllegalArgumentException("driver not supported");
            return Optional.empty();
        }
    }
}
