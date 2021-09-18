package AppUtility.Config;

import AppUtility.Databases.InMemoryDatabaseServices;
import AppUtility.Interfaces.DatabaseServices;

public class Dependencies {
    public static DatabaseServices databaseServices = new InMemoryDatabaseServices();
}
