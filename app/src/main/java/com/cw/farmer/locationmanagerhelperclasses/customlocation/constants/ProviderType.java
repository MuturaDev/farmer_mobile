package com.cw.farmer.locationmanagerhelperclasses.customlocation.constants;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.ProviderType.NONE, com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.ProviderType.GOOGLE_PLAY_SERVICES,
      com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.ProviderType.GPS, com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.ProviderType.NETWORK, com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.ProviderType.DEFAULT_PROVIDERS})
@Retention(RetentionPolicy.SOURCE)
public @interface ProviderType {

    int NONE = 0;
    int GOOGLE_PLAY_SERVICES = 1;
    int GPS = 2;
    int NETWORK = 3;
    int DEFAULT_PROVIDERS = 4; // Covers both GPS and NETWORK
    
}
