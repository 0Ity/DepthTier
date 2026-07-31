package net.tysontheember.betterharvestlevels.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigIOTest {
    @TempDir
    Path configDir;

    @Test
    void migratesLegacyConfigFolder() throws Exception {
        Path legacyFolder = configDir.resolve("betterharvestlevel");
        Files.createDirectories(legacyFolder);
        Files.writeString(legacyFolder.resolve("tiers.json"), tiersJson("legacy"));

        BHLConfig config = ConfigIO.load(configDir);
        assertEquals("legacy", config.getTiersConfig().getTiers().get(0).name);
        assertTrue(Files.exists(configDir.resolve("betterharvestlevels/tiers.json")));
        assertFalse(Files.exists(legacyFolder));
    }

    @Test
    void canonicalConfigFolderWinsWhenBothExist() throws Exception {
        Path legacyFolder = configDir.resolve("betterharvestlevel");
        Path canonicalFolder = configDir.resolve("betterharvestlevels");
        Files.createDirectories(legacyFolder);
        Files.createDirectories(canonicalFolder);
        Files.writeString(legacyFolder.resolve("tiers.json"), tiersJson("legacy"));
        Files.writeString(canonicalFolder.resolve("tiers.json"), tiersJson("canonical"));

        BHLConfig config = ConfigIO.load(configDir);

        assertEquals("canonical", config.getTiersConfig().getTiers().get(0).name);
        assertTrue(Files.exists(legacyFolder));
    }

    private static String tiersJson(String name) {
        return "{\"configVersion\":1,\"tiers\":[{\"name\":\"" + name + "\",\"level\":0}]}";
    }
}
