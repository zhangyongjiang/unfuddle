package com.gaoshin.appbooster.service;

import com.gaoshin.appbooster.entity.Application;
import com.gaoshin.appbooster.entity.ApplicationType;

public interface AppResolver {
    Application getApplication(String id);
    ApplicationType getType();
}
