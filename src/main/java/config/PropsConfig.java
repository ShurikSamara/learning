package config;

import org.aeonbits.owner.ConfigFactory;

public class PropsConfig {

    public static final String USER_HOME = System.getProperty("user.home");
    public static final String STAND = System.setProperty("env", getProps().env());

    public static Props getProps() {
        return ConfigFactory.create(Props.class);
    }
}