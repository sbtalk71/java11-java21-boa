package interfaces;

import interfaces.impl.MySqlDB;
import interfaces.impl.OracleDB;
import interfaces.impl.PostgresSQL;

import java.util.Optional;

public class InterfaceMain {
    public static void main(String[] args) {

        Optional<MyDbDriver> driverOptional=DriverFactory.create("oracles");

        if(driverOptional.isPresent()) {
            MyDbDriver driver=driverOptional.get();
            System.out.println(driver.getConnection());
            System.out.println(driver.getDbVersion());
            System.out.println("Driver Release No :" + MyDbDriver.releaseNo);
            System.out.println(driver.isClusterSupported());
        }else {
            System.out.println("Driver not supported..");
        }
    }
}
