package org.tkirill.teamcity.bisectPlugin;

import com.google.gson.*;
import jetbrains.buildServer.serverSide.CustomDataStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BisectRepository {

    private final TypeAdapter<Bisect> gson;
    private CustomDataStorage storage;

    public BisectRepository(CustomDataStorage storage) {
        this.storage = storage;
        gson = new Gson().getAdapter(Bisect.class);
    }

    public boolean exists(long buildId) {
        String value = storage.getValue(String.valueOf(buildId));
        return tryParse(value) != null;
    }

    public void create(long buildId) throws IOException {
        Bisect bisect = new Bisect(buildId);
        bisect.buildId = buildId;
        String json = gson.toJson(bisect);
        storage.putValue(String.valueOf(buildId), json);
    }

    public Bisect[] getAllNotFinished() {
        List<Bisect> result = new ArrayList<>();

        Map<String, String> values = storage.getValues();
        for (String key : values.keySet()) {
            Bisect bisect = tryParse(values.get(key));
            if (bisect != null && !bisect.isFinished) {
                result.add(bisect);
            }
        }

        return result.toArray(new Bisect[result.size()]);
    }

    private Bisect tryParse(String json) {
        if (json == null) {
            return null;
        }

        try {
            return gson.fromJson(json);
        } catch (Exception e) {
            return null;
        }
    }

    public void save(Bisect bisect) throws IOException {
        String json = gson.toJson(bisect);
        storage.putValue(String.valueOf(bisect.buildId), json);
    }
}