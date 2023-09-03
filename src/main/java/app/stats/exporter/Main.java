package app.stats.exporter;

import app.stats.exporter.data.TelegramMessage;
import app.stats.exporter.db.DBHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import app.stats.exporter.data.TelegramExportData;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Main {

    static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {

        // init ObjectMapper
        objectMapper.registerModule(new JavaTimeModule());

        if (args.length <= 1) {
            singleExportingProcess(args);
        } else {
            batchExportingProcess(args);
        }

    }

    /**
     * Single exporting process.<br>
     * Filename could be unset (in this case will be used the default name <code>result.json</code>).
     *
     * @param args Arguments passed at runtime
     */
    private static void singleExportingProcess(String[] args) {

        // Init fileName and ConnectionString
        String fileName = args.length == 0 ? "result.json" : args[0];
        String connString = "jdbc:sqlite:" + fileName.replace(".json", "") + "-exported.db";

        try {

            log.info("Trying to parse {}...", fileName);
            log.info("This could take a while if the file is large!");
            TelegramExportData exportData = objectMapper.readValue(new File(fileName), TelegramExportData.class);
            log.info("Successfully parsed file");

            log.info("Exporting chat info to DB");
            DBHelper.initDB(connString);
            DBHelper.exportChatInfo(connString, exportData.getChatId(), exportData.getName(), exportData.getType());
            exportMessages(exportData.getMessages(), connString);
            log.info("Exporting complete!");

        } catch (Exception ex) {
            printException(ex);
        }

    }

    /**
     * Batch exporting process.<br>
     * Every input file will be processed individually.
     *
     * @param args Arguments passed at runtime
     */
    private static void batchExportingProcess(String[] args) {

        int i = 1;

        // checks if there is at least one filename that doesn't end with ".json"
        if (isThereAFilenameNotValid(args)) {
            printHelp();
            return;
        }

        for (String fileName : args) {

            // Init ConnectionString
            String connString = "jdbc:sqlite:" + fileName.replace(".json", "") + "-exported.db";

            try {

                log.info("Trying to parse file {}/{}: {}...", i, args.length, fileName);
                log.info("This could take a while if the file is large!");
                TelegramExportData exportData = objectMapper.readValue(new File(fileName), TelegramExportData.class);
                log.info("Successfully parsed file");

                log.info("Exporting chat info to DB");
                DBHelper.initDB(connString);
                DBHelper.exportChatInfo(connString, exportData.getChatId(), exportData.getName(), exportData.getType());
                exportMessages(exportData.getMessages(), connString);
                log.info("Exporting complete!");

                i++;

            } catch (Exception ex) {
                printException(ex);
            }

        }

    }

    /**
     * Checks if there is at least one filename that doesn't end with <code>.json</code>.
     *
     * @param fileNames Filenames to check
     * @return <code>true</code> if there is a filename that doesn't end with <code>.json</code>,
     * <code>false</code> otherwise.
     */
    private static boolean isThereAFilenameNotValid(String[] fileNames) {
        return Arrays.stream(fileNames).anyMatch(s -> !s.endsWith(".json"));
    }

    /**
     * Exports messages from JSON file to a SQLite DB
     * @param messagesList Messages obtained from JSON to process
     * @param connString Connection String needed to create and update the DB
     */
    private static void exportMessages(List<TelegramMessage> messagesList, String connString) {

        if (!messagesList.isEmpty()) {

            // Ignore telegram actions like adding/removing user to/from a group
            List<TelegramMessage> exportMessages = messagesList.stream()
                    .filter(m -> "".equals(m.getAction())).collect(Collectors.toList());

            int messageCount = exportMessages.size();
            int i = 1;

            for (TelegramMessage message : exportMessages) {

                log.info("Exporting message {}/{} to DB...", i, messageCount);
                DBHelper.exportMessageInfo(connString, message);
                i++;

            }

        } else {
            log.warn("Message list is empty. Messages will not be processed.");
        }

    }

    private static void printHelp() {
        System.out.println("\nSimple Telegram Stats Exporter, v1.0.2\n");
        System.out.println("Usage: TelegramStatsExporter [<file_names>...]\n");
        System.out.println("If filenames are unset, then will be used the default filename (result.json)\n");
    }

    private static void printException(Exception ex) {

        log.error("A {} occurred:\n", ex.getClass().getSimpleName());
        ex.printStackTrace();

    }
}