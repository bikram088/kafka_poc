package com.kafka.connector;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class FileState {

    private static final Path INPUT_DIR = Paths.get("/Users/bikram_pradhan/Desktop/kafka_poc/src/main/input");
    private static final Path STATE_FILE = Paths.get("/Users/bikram_pradhan/Desktop/kafka_poc/src/main/state.properties");

    public static void main(String[] args) throws Exception {

        Properties state = loadState();

        Files.list(INPUT_DIR)
                .filter(Files::isRegularFile)
                .forEach(file -> processFile(file, state));

        saveState(state);

        System.out.println("Test run completed");
    }

    private static void processFile(Path file, Properties state) {

        try {
            String key = file.toAbsolutePath().toString();
            Instant fileInstant =
                    Files.getLastModifiedTime(file).toInstant();

            String storedValue = state.getProperty(key);

            if (storedValue != null) {
                OffsetDateTime storedTime =
                        OffsetDateTime.parse(storedValue);

                if (!fileInstant.isAfter(storedTime.toInstant())) {
                    System.out.println(
                            "SKIP (already processed): " + file.getFileName()
                    );
                    return;
                }
            }

            // ---- Simulate streaming to Kafka ----
            System.out.println(
                    "PROCESSING file: " + file.getFileName()
            );
            Thread.sleep(500); // simulate work

            // ---- Update state AFTER success ----
            OffsetDateTime newTime =
                    OffsetDateTime.ofInstant(
                            fileInstant,
                            ZoneId.systemDefault()
                    );

            state.setProperty(
                    key,
                    newTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Properties loadState() throws IOException {

        Properties props = new Properties();

        // Create state file if missing
        if (Files.notExists(STATE_FILE)) {
            Files.createFile(STATE_FILE);

            try (OutputStream out = Files.newOutputStream(STATE_FILE)) {
                props.store(out, "file ingestion state");
            }

            System.out.println(
                    "Created new state file: " + STATE_FILE.toAbsolutePath()
            );
            return props;
        }

        // Load existing state
        try (InputStream in = Files.newInputStream(STATE_FILE)) {
            props.load(in);
        }

        return props;
    }

    private static void saveState(Properties state) throws IOException {

        Path tmp = Paths.get("state.properties.tmp");

        try (OutputStream out = Files.newOutputStream(tmp)) {
            state.store(out, "file ingestion state");
        }

        Files.move(
                tmp,
                STATE_FILE,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE
        );
    }
}