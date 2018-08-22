package com.scholarcoder.chat.server.repository;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class EmbeddedDatabaseBuilder {
    private Connection connection;
    private List<File> updateScripts = new LinkedList<>();

    public void configureConnection(Connection connection) {
        this.connection = connection;
    }

    public void addUpdateScript(String scriptName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(scriptName).getFile());

        if (!file.exists()) {
            return;
        }
        updateScripts.add(file);
    }

    public Connection build() {
        if (connection == null) {
            this.connection = HsqldbConnection.getInstance().getInprocessConnection();
        }

        for (File updateScript : updateScripts) {
            String scriptFileContent = getFileContent(updateScript);
            System.out.println(scriptFileContent);
            executeUpdate(scriptFileContent);
        }

        return connection;
    }

    private void executeUpdate(String scriptFileContent) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(scriptFileContent);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getFileContent(File file) {
        try {
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
