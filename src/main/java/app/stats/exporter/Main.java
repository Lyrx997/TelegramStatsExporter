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

        try {

            log.info("Trying to open result.json file...");
            log.info("This could take a while if the file is large!");
            TelegramExportData exportData = objectMapper.readValue(
                    new File("result.json"), TelegramExportData.class);

            log.info("Successfully retrieved JSON file");

            log.info("Exporting chat info to DB");
            DBHelper.initDB();
            DBHelper.exportChatInfo(exportData.getChatId(), exportData.getName(), exportData.getType());

            if (!exportData.getMessages().isEmpty()) {

                // Ignore telegram actions like adding/removing user to/from a group
                List<TelegramMessage> exportMessages = exportData.getMessages().stream()
                        .filter(m -> "".equals(m.getAction())).collect(Collectors.toList());

                int messageCount = exportMessages.size();
                int i = 1;

                for (TelegramMessage message : exportMessages) {

                    log.info("Exporting message {}/{} to DB...", i, messageCount);
                    DBHelper.exportMessageInfo(message);
                    i++;

                }

                log.info("Exporting complete!");

            } else {

                log.error("Message list in result.json is empty. Exiting in 3 seconds...");
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