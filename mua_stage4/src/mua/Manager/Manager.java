package Manager;

import java.util.HashMap;

import Space.Space;

public class Manager {
    private static HashMap<Integer, Space> SpaceManager = new HashMap<Integer, Space>();
    public static Integer idx = 1;

    public Manager() {

    };

    public Integer addSpace(Space space) {
        Integer tmp = idx;
        SpaceManager.put(idx, space);
        idx++;
        return tmp;
    };

    public boolean contains(Integer name) {
        return SpaceManager.containsKey(name);
    };

    public Space getHere(Integer id) {
        return SpaceManager.get(id);
    }
}
