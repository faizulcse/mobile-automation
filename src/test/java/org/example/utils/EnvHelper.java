package org.example.utils;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnvHelper {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
}
