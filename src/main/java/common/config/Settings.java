package common.config;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by howor on 28.01.2017.
 */
@XmlRootElement(name = "settings")
public class Settings {
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
