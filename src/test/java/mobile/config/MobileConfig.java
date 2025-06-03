package mobile.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:configs/mobile.properties")
public interface MobileConfig extends Config {

    @Key("mobile.url")
    String mobileUrl();

    @Key("mobile.platform")
    String mobilePlatform(); // "android" или "ios"

    @Key("mobile.device")
    String mobileDevice();

    @Key("mobile.platformVersion")
    String mobilePlatformVersion();

    @Key("mobile.app.url")
    String mobileAppUrl();

    @Key("mobile.app.package")
    String mobileAppPackage();

    @Key("mobile.app.activity")
    String mobileAppActivity();
}
