package org.tkirill.teamcity.bisectPlugin;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import jetbrains.buildServer.serverSide.CustomDataStorage;

public class BisectRepository {

    private CustomDataStorage storage;

    public BisectRepository(CustomDataStorage storage) {
        this.storage = storage;
    }

    public boolean exists(long buildId) {
        String value = storage.getValue(String.valueOf(buildId));
        if (value == null || value.isEmpty()) {
            return false;
        }

        try {
            new Gson().fromJson(value, Bisect.class);
        } catch (JsonParseException e) {
            return false;
        }
        return true;
    }

    public void create(long buildId) {
    }
}