package app.stats.exporter;

import app.stats.exporter.data.TelegramMessage;
import app.stats.exporter.db.DBHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import app.stats.exporter.data.TelegramExportData;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException {

        // init ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Init fileName and ConnectionString
        String fileName = args.length == 0 ? "result" : args[0];
        if (fileName.endsWith(".json")){
            fileName = fileName.replace(".json", "");
        }
        String connString = "jdbc:sqlite:" + fileName + "-exported.db";

        try {

            log.info("Trying to open {}.json...", fileName);
            log.info("This could take a while if the file is large!");
            TelegramExportData exportData = objectMapper.readValue(
                    new File(fileName + ".json"), TelegramExportData.class);
            log.info("Successfully parsed file");

            log.info("Exporting chat info to DB");
            DBHelper.initDB(connString);
            DBHelper.exportChatInfo(connString, exportData.getChatId(), exportData.getName(), exportData.getType());

            if (!exportData.getMessages().isEmpty()) {

                // Ignore telegram actions like adding/removing user to/from a group
                List<TelegramMessage> exportMessages = exportData.getMessages().stream()
                        .filter(m -> "".equals(m.getAction())).collect(Collectors.toList());

                int messageCount = exportMessages.size();
                int i = 1;

                for (TelegramMessage message : exportMessages) {

                    log.info("Exporting message {}/{} to DB...", i, messageCount);
                    DBHelper.exportMessageInfo(connString, message);
                    i++;

                }

                log.info("Exporting complete!");

            } else {

                log.error("Message list in {}.json is empty. Exiting in 3 seconds...", fileName);
                Thread.sleep(3000);
            }

        } catch (Exception ex) {
            printException(ex);
        }
    }

    private static void printException(Exception ex) throws InterruptedException {

        log.error("A {} occurred:\n", ex.getClass().getSimpleName());
        ex.printStackTrace();
        log.info("Exiting in 3 seconds...");
        Thread.sleep(3000);

    }
}