package org.example.utils.devices;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class DeviceType {
    String id;
    @SerializedName("device_name")
    String deviceName;
    @SerializedName("platform_name")
    String platformName;
    @SerializedName("platform_version")
    String platformVersion;
    String udid;
}
