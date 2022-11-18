package Space;

import java.util.HashMap;

import Const.Const;
import Data.Data;

public class Space {
    private HashMap<String, Data> nameSpace = new HashMap<String, Data>();
    private Space parent;

    public Space() {
        Data value = new Data();
        value.type = Const.NUM;
        value.content = "3.14159";
        value.num = 3.14159;

        nameSpace.put("pi", value);
        parent = null;
    }

    public Space(Space nameSpace) {
        this.nameSpace = nameSpace.getLocal();
        this.parent = nameSpace.getParent();
    }

    public void addSpace(Space space) {
        HashMap<String, Data> newSpace = space.nameSpace;
        newSpace.putAll(nameSpace);
        nameSpace = newSpace;
    }

    public void coverSpace(Space space) {
        if (space.nameSpace != null)
            nameSpace.putAll(space.nameSpace);
    }

    public void setParent(Space parent) {
        this.parent = parent;
    }

    public HashMap<String, Data> getLocal() {
        return this.nameSpace;
    }

    public Space getGlobal() {
        if (parent == null)
            return this;
        else
            return parent.getGlobal();
    }

    public Space getParent() {
        if (parent == null)
            return this;
        else
            return parent;
    }

    public void setHere(String name, Data value) {
        nameSpace.put(name, value);
    }

    public Data removeHere(String name) {
        return nameSpace.remove(name);
    }

    public boolean containsHere(String name) {
        return nameSpace.containsKey(name);
    }

    public Data getHere(String name) {
        return nameSpace.get(name);
    }

    public int getLength() {
        return nameSpace.size();
    }

    public boolean eraseAll() {
        nameSpace.clear();
        return true;
    }
}
