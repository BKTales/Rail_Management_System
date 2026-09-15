package org.dei.Repository;

import org.dei._Train.Freight;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FreightRepository {
    private final Map<String, Freight> freightMap;
    private static FreightRepository instance = null;

    public static FreightRepository getInstance() {
        if (instance == null) {
            instance = new FreightRepository();
        }
        return instance;
    }

    public FreightRepository() {
        freightMap = new HashMap<>();
    }

    public Collection<Freight> getAllFreights() {
        return freightMap.values();
    }

    public void addFreight(Freight freight) {
        freightMap.put(freight.getId(), freight);
    }

    public void removeFreight(Freight freight) {
        freightMap.remove(freight.getId());
    }

    public Freight getFreight(String id) {
        return freightMap.get(id);
    }

    public void clear() {
        freightMap.clear();
    }
}
