package ui.configs;

import org.aeonbits.owner.ConfigFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BrowserConfigFactory {
    public static final BrowserConfig PROP = ConfigFactory.create(BrowserConfig.class);

    static {
        String tag = System.getProperty("tag");
        if (tag == null || tag.isEmpty()) {
            throw new IllegalArgumentException("tag must be specified (UI or API)");
        }

        assertPropValue(PROP.uiUrl(), "UiURL");

        switch (PROP.runIn()) {
            case "browser_local":
                assertPropValue(PROP.browser(), "browser");
                break;
            case "browser_selenoid":
                assertPropValue(PROP.remote(), "remote");
                assertPropValue(PROP.version(), "version");
                break;
            default:
                throw new IllegalArgumentException("tag runIn incorrect name\nSee the readme file");

        }

    }

    public static void assertPropValue(String propValue, String nameProp) {
        assertThat(propValue)
                .withFailMessage(nameProp + ": is empty or null")
                .isNotEmpty().isNotNull();
    }
}