package ui.data;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"classpath:authentication/user.properties",
        "system:properties"})
public interface User extends Config {
    @Key("login")
    String login();

    @Key("password")
    String password();

}

