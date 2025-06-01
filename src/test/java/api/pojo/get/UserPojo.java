package api.pojo.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPojo {
    private long id;
    @JsonProperty("username")
    private String userName;
    private String firstName, lastName, email, password, phone;
    private int userStatus;

    public UserPojo(UserPojo original) {
        this.id = original.id;
        this.userName = original.userName;
        this.firstName = original.firstName;
        this.lastName = original.lastName;
        this.email = original.email;
        this.password = original.password;
        this.phone = original.phone;
        this.userStatus = original.userStatus;
    }

}
