package app.stats.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import app.stats.exporter.data.TelegramExportData;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException {

    // init ObjectMapper
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    try {

        log.info("Trying to open result.json file...");
        log.info("This could take a while if the file is large!");
        TelegramExportData exportData = objectMapper.readValue(new File("result.json"), TelegramExportData.class);

        log.info("Successfully retrieved JSON file");
        log.info("Trying exporting its contents to SQLite DB!");

    } catch (FileNotFoundException ex) {

        log.error("Cannot find result.json file. Exiting in 3 seconds...");
        Thread.sleep(3000);

    } catch (IOException ex){

        log.error("An IOException occurred. {}", ex.getMessage());
        ex.printStackTrace();
    }

    }
}