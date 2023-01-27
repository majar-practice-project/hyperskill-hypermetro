package metro;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private static final Properties properties = new Properties();

    static {
        try (FileReader reader = new FileReader("metro.config")) {
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException("Broken config file");
        }
    }

    public static int getTransferTime() {
        return Integer.parseInt(properties.getProperty("transferTime", "5"));
    }

    public static String getTimeUnit() {
        return properties.getProperty("timeUnit", "minutes");
    }
}
