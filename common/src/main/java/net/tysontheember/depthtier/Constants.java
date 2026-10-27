package net.tysontheember.depthtier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class Constants {
    public static final String MOD_ID = "depthtier";
    public static final String MOD_NAME = "DepthTier";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final String CONFIG_FOLDER_NAME = "depthtier";
    /** Legacy config folders migrated in order (first existing wins). */
    public static final List<String> LEGACY_CONFIG_FOLDER_NAMES = List.of(
            "betterharvestlevels", "betterharvestlevel");

    private Constants() {}
}
