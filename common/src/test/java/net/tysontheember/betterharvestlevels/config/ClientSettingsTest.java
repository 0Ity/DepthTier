package net.tysontheember.betterharvestlevels.config;

import net.tysontheember.betterharvestlevels.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientSettingsTest {
    @TempDir
    Path configDir;

    @Test
    void missingFileDefaultsToEnabledAndWritesFile() {
        ClientSettings settings = ClientSettings.load(configDir);
        assertTrue(settings.toolTierTooltip);
        assertTrue(Files.exists(configDir.resolve(Constants.CONFIG_FOLDER_NAME).resolve("client.json")));
    }

    @Test
    void disabledInFileIsRespected() throws Exception {
        Path folder = configDir.resolve(Constants.CONFIG_FOLDER_NAME);
        Files.createDirectories(folder);
        Files.writeString(folder.resolve("client.json"), "{\"toolTierTooltip\": false}");
        assertFalse(ClientSettings.load(configDir).toolTierTooltip);
    }

    @Test
    void malformedFileFallsBackToDefault() throws Exception {
        Path folder = configDir.resolve(Constants.CONFIG_FOLDER_NAME);
        Files.createDirectories(folder);
        Files.writeString(folder.resolve("client.json"), "not json");
        assertTrue(ClientSettings.load(configDir).toolTierTooltip);
    }
}
