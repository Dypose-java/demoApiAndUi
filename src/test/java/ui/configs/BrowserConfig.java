package ui.configs;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties",
        "classpath:configs/${runIn}.properties"
})
public interface BrowserConfig extends Config{
    @Key("ui.url")
    String uiUrl();

    @Key("ui.browser")
    String browser();

    @Key("ui.browser.size")
    String size();

    @Key("ui.browser.timeOut")
    long timeOut();

    @Key("ui.pageLoadTimeout")
    long pageLoadTimeout();

    @Key("ui.headless")
    boolean headless();

    @Key("ui.browser.version")
    String version();

    @Key("ui.remote")
    String remote();

    @DefaultValue("browser_local")
    String runIn();
}
