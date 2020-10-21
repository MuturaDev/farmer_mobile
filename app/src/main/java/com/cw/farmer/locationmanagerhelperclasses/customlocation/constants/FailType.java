package com.cw.farmer.locationmanagerhelperclasses.customlocation.constants;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.FailType.UNKNOWN, com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.FailType.TIMEOUT, com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.FailType.PERMISSION_DENIED, com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.FailType.NETWORK_NOT_AVAILABLE,
      com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.FailType.GOOGLE_PLAY_SERVICES_NOT_AVAILABLE, com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.FailType.GOOGLE_PLAY_SERVICES_CONNECTION_FAIL,
      com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.FailType.GOOGLE_PLAY_SERVICES_SETTINGS_DIALOG, com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.FailType.GOOGLE_PLAY_SERVICES_SETTINGS_DENIED,
      com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.FailType.VIEW_DETACHED, com.cw.farmer.locationmanagerhelperclasses.customlocation.constants.FailType.VIEW_NOT_REQUIRED_TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface FailType {

    int UNKNOWN = -1;
    int TIMEOUT = 1;
    int PERMISSION_DENIED = 2;
    int NETWORK_NOT_AVAILABLE = 3;
    int GOOGLE_PLAY_SERVICES_NOT_AVAILABLE = 4;
    int GOOGLE_PLAY_SERVICES_CONNECTION_FAIL = 5;
    int GOOGLE_PLAY_SERVICES_SETTINGS_DIALOG = 6;
    int GOOGLE_PLAY_SERVICES_SETTINGS_DENIED = 7;
    int VIEW_DETACHED = 8;
    int VIEW_NOT_REQUIRED_TYPE = 9;

}