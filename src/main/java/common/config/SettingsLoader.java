package common.config;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by howor on 28.01.2017.
 */
public class SettingsLoader {
    public static Settings load(File file) throws JAXBException {
        Settings settings = null;
        JAXBContext context = JAXBContext.newInstance(Settings.class);
        Unmarshaller um = context.createUnmarshaller();
        settings = (Settings) um.unmarshal(file);

        return settings;
    }
}