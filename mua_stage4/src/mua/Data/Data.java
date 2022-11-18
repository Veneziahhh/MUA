package Data;

import Space.Space;

public class Data {
    public int type;
    public String content; // word or list
    public double num;
    public boolean bool;

    public boolean isFunc;
    public Space space;

    public Data() {
        isFunc = false;
        space = null;
    };
}
