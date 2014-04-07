package common;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nncappelma on 4/7/14.
 */
public interface Constants {
    public static final String JAVA_FILE_EXTENSION = "js";
    public static final List<String> PHP_EXTENSION_TABLE = Arrays.asList("php", "php3", "phtml", JAVA_FILE_EXTENSION);
    public static final List<String> EXCLUSION_FILE_PATTERN = Arrays.asList("CVS", ".db");
}
