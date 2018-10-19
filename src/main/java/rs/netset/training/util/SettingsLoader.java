package rs.netset.training.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsLoader {

	private static SettingsLoader instance;
	private Properties properties;

	private SettingsLoader() throws IOException {
		loadProperties();

	}

	private void loadProperties() throws IOException {
		FileInputStream fis = new FileInputStream("C:\\Users\\petar.NETSETGLOBAL\\eclipse-workspace\\training-parent\\training-ejb\\conf.properties");
		properties = new Properties();
		properties.load(fis);

	}

	public static SettingsLoader getInstance() throws IOException {
		if (instance == null) {
			instance = new SettingsLoader();
		}
		return instance;
	}

	public String getValue(String key) {
		return properties.getProperty(key, "N/A");
	}

	public void setValue(String key, String value) {
		properties.put(key, value);
	}

}
