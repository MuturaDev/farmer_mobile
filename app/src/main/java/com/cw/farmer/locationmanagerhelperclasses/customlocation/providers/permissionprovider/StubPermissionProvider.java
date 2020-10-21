package com.cw.farmer.locationmanagerhelperclasses.customlocation.providers.permissionprovider;

import androidx.annotation.NonNull;

import com.cw.farmer.locationmanagerhelperclasses.customlocation.configuration.Defaults;
import com.cw.farmer.locationmanagerhelperclasses.customlocation.providers.permissionprovider.PermissionProvider;

public class StubPermissionProvider extends PermissionProvider {

    public StubPermissionProvider() {
        super(Defaults.LOCATION_PERMISSIONS, null);
    }

    @Override
    public boolean requestPermissions() {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
    }
}
