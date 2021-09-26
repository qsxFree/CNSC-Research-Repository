package com.cnsc.research.misc.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ValidFields {

    public List<String> getFieldKeys() {
        List<String> keys = new ArrayList<>();
        getMap().forEach((k, v) -> keys.add(k));
        return keys;
    }

    public List<String> getFieldValues(String key) {
        return getMap().get(key);
    }

    protected abstract Map<String, List<String>> getMap();
}
