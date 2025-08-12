package org.example.utils.allure;


import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Result;
import io.cucumber.plugin.event.TestStepFinished;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Parameter;
import org.example.drivers.DriverManager;
import org.example.utils.CommandExecuter;
import org.example.utils.FileUtils;
import org.example.utils.LogUtils;
import org.example.utils.bots.ScreenShotsActions;
import org.example.utils.properties.EnvManager;

import java.io.*;
import java.nio.file.Files;




public class AllureUtils {
    private static final String ALLURE_REPORT_PATH = System.getProperty("user.dir") + File.separator +"reports"+ File.separator +"OMS_ALLURE_REPORT";
    private static final String ALLURE_RESULT_PATH = System.getProperty("user.dir") + File.separator + "allure-results";

    private static final String[] GENERATE_COMMAND = {"allure", "generate", "--clean", "--single-file","-o",ALLURE_REPORT_PATH, ALLURE_RESULT_PATH};
    private static final String[] SERVE_COMMAND = { "allure","serve", ALLURE_RESULT_PATH, "-h", "localhost"};

private AllureUtils(){
    super();
}
    private static void getAllureExecutable() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            GENERATE_COMMAND[0] = "allure.cmd";
            SERVE_COMMAND[0] = "allure.cmd";
        }
    }
    public static void generateReport() {
        LogUtils.logInfo("=============================== Generating Allure Report =============================== ");
        getAllureExecutable();
        CommandExecuter.executeCommand(GENERATE_COMMAND);

    }

    public static void serverReport() {
        LogUtils.logInfo("=============================== Serving Allure Report =============================== ");
        getAllureExecutable();
        CommandExecuter.executeCommand(SERVE_COMMAND);
    }

    public static void cleanOldResults() {
        LogUtils.logInfo("=============================== Cleaning Old Allure Reports =============================== ");
        FileUtils.cleanDirectory(ALLURE_RESULT_PATH);
    }

    public static void attachImage(String name,byte[] image) {
        LogUtils.logInfo("Attaching image to allure report");
        Allure.addAttachment(name+".png",new ByteArrayInputStream(image));

    }

    public static void attachImage(File file) {
        LogUtils.logInfo("Attaching image to allure report");
        try {
            Allure.addAttachment("Screenshot.png", new FileInputStream(file));
        }catch (NullPointerException | FileNotFoundException e){
            LogUtils.logWarn("File not found or null: " + (file != null ? file.getAbsolutePath() : "null"));
        }

    }

    public static void attachLogFile() {
        try {
            File logFile = FileUtils.getLatestFile(LogUtils.LOGS_PATH);
            if (logFile == null|| !logFile.exists()) {
                LogUtils.logWarn("Log file not found");
                return;
            }
            LogUtils.logInfo("Attaching Log File");
            Allure.getLifecycle().addAttachment("log", "text/plain",".log" ,Files.readAllBytes( logFile.toPath()));
        } catch (IOException e) {
            LogUtils.logWarn("failed to attach file to allure report", e.getMessage());
        }

    }

    public static void setSuiteName(String name) {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.getLabels().removeIf(label -> "suite".equals(label.getName()));
            testResult.getLabels().add(new io.qameta.allure.model.Label().setName("suite").setValue(name));
        });
    }

    public static void allureAfterStepAction(TestStepFinished event) {
        Result result = event.getResult();
        if (event.getTestStep() instanceof PickleStepTestStep step) {
            try {
                if (!result.getStatus().isOk())
                    AllureUtils.attachImage(result.getStatus() + " " + step.getStep().getText(), new ScreenShotsActions(DriverManager.getDriver()).takeScreenShotByte());
            } catch (Exception e) {
                LogUtils.logError("error on attaching Screen Shot to Allure:", e.getMessage());
            }
        }
    }

    public static void setEnvironment(String uuid) {
        Allure.getLifecycle().updateTestCase(uuid, r -> {
            r.getParameters().removeIf(param -> "Environment".equals(param.getName()));
            r.getParameters().add(new Parameter().setName("Environment").setValue(EnvManager.getEnv()));
        });
    }
}
