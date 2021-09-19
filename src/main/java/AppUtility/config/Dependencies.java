package AppUtility.config;

import AppUtility.databases.InMemoryDatabaseServices;
import AppUtility.interfaces.DatabaseServices;

public class Dependencies {
    public static DatabaseServices databaseServices = new InMemoryDatabaseServices();
}
