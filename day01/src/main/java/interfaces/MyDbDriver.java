package interfaces;

public interface MyDbDriver {
    String releaseNo="1.0";

    String getConnection();
    String getDbVersion();
    default boolean isClusterSupported(){
        return false;
    }

}
