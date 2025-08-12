package org.example.utils.allure;



import org.example.utils.LogUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import static org.example.utils.FileUtils.createDirectoryIfNotExist;


public class AllureEnvironmentWriter {
    private AllureEnvironmentWriter() {
        super();
    }

    public static void writeEnvironmentProperties(Properties props, String outputDirectory) {



        createDirectoryIfNotExist(outputDirectory);
        File envFile = new File(outputDirectory + "/environment.properties");

        try (FileWriter writer = new FileWriter(envFile)) {
            props.store(writer, "Allure Environment Properties");
        } catch (IOException e) {
            LogUtils.logError(Arrays.toString(e.getStackTrace()));
        }
    }
}