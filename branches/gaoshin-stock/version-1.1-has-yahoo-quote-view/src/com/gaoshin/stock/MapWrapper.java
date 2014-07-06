package com.gaoshin.stock;

import java.util.Map;

public class MapWrapper {
    private Map<String, Object> map;
    
    public MapWrapper() {
    }

    public MapWrapper(Map map) {
        this.map = map;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}