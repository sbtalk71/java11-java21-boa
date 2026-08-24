package interfaces;

import interfaces.impl.MySqlDB;
import interfaces.impl.OracleDB;
import interfaces.impl.PostgresSQL;

public class InterfaceMain {
    public static void main(String[] args) {

        MyDbDriver driver=new PostgresSQL();

        System.out.println(driver.getConnection());
        System.out.println(driver.getDbVersion());
        System.out.println("Driver Release No :"+MyDbDriver.releaseNo);
        System.out.println(driver.isClusterSupported());
    }
}
