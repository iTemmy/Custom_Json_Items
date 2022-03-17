package com.temmy.json_items_test_1.util;

import org.jetbrains.annotations.Nullable;

public class Database {
    public String host;
    public Integer port;
    public String databaseName;
    public String user;
    public String password;

    /**
     * Creates a database with the parsed variables
     * @param host hostname of the database e.g. localhost, 123.123.123.123
     * @param port port number of the database most defaults at 3306
     * @param databaseName database name !Not the username!
     * @param user username for the database
     * @param password password for the database
     */
    public Database(@Nullable String host, @Nullable Integer port, @Nullable String databaseName, @Nullable String user, @Nullable String password){
        if (host != null)this.host = host;
        if (port != null)this.port = port;
        if (databaseName != null)this.databaseName = databaseName;
        if (user != null)this.user = user;
        if (password != null)this.password = password;
    }

}
