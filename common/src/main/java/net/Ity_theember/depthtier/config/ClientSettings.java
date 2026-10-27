package net.Ity_theember.depthtier.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.Ity_theember.depthtier.Constants;
import net.Ity_theember.depthtier.platform.Services;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ClientSettings {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static volatile ClientSettings loaded;

    boolean toolTierTooltip = true;

    public static boolean toolTierTooltip() {
        ClientSettings settings = loaded;
        if (settings == null) {
            settings = load(Services.PLATFORM.getConfigDir());
            loaded = settings;
        }
        return settings.toolTierTooltip;
    }

    public static void invalidate() {
        loaded = null;
    }

    static ClientSettings load(Path configDir) {
        Path file = configDir.resolve(Constants.CONFIG_FOLDER_NAME).resolve("client.json");
        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file)) {
                ClientSettings settings = GSON.fromJson(reader, ClientSettings.class);
                if (settings != null) return settings;
            } catch (Exception e) {
                Constants.LOG.error("Failed to read config file 'client.json': {}", e.getMessage());
            }
            return new ClientSettings();
        }
        ClientSettings settings = new ClientSettings();
        try {
            Files.createDirectories(file.getParent());
            try (Writer writer = Files.newBufferedWriter(file)) {
                GSON.toJson(settings, writer);
            }
        } catch (IOException e) {
            Constants.LOG.error("Failed to write config file 'client.json': {}", e.getMessage());
        }
        return settings;
    }
}
