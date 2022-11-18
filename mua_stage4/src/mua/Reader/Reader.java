package Reader;

import Data.Data;

import java.util.Arrays;
import java.util.Vector;

import Const.Const;

public class Reader {

    Vector<Data> container = new Vector<Data>();
    Const Const = new Const();

    String line;
    String[] temp;
    Boolean nextIsRead = false;

    public Reader() {

    };

    public Vector<Data> Read(Vector<String> temp) {
        for (int i = 0; i < temp.size(); i++) {
            Data data = new Data();
            if (nextIsRead) {
                if (temp.get(i).matches("[-]?[0-9][0-9]*[.]?[0-9]*")) {
                    data.type = Const.NUM;
                    data.num = Double.parseDouble(temp.get(i));
                } else {
                    data.type = Const.WORD;
                }
                nextIsRead = false;
            } else if (Arrays.asList(Const.instructions).contains(temp.get(i))) {
                data.type = Const.INST;

                if (temp.get(i).equals("read"))
                    nextIsRead = true;
            } else if (Arrays.asList(Const.operators).contains(temp.get(i))) {
                data.type = Const.OPERATOR;
            } else {
                if (temp.get(i).matches("[-]?[0-9][0-9]*[.]?[0-9]*")) {
                    data.type = Const.NUM;
                    data.num = Double.parseDouble(temp.get(i));
                } else if (temp.get(i).charAt(0) == '"') {
                    data.type = Const.WORD;
                    temp.set(i, temp.get(i).substring(1));
                } else if (temp.get(i).charAt(0) == ':') {
                    Data thing = new Data();
                    thing.type = Const.INST;
                    thing.content = "thing";
                    container.addElement(thing);

                    data.type = Const.WORD;
                    temp.set(i, temp.get(i).substring(1));
                } else if (temp.get(i).equals("false") || temp.get(i).equals("true")) {
                    data.type = Const.BOOL;
                    if (temp.get(i).equals("false"))
                        data.bool = false;
                    else
                        data.bool = true;
                } else if (temp.get(i).charAt(0) == '[') {
                    int left = 0;
                    String listTemp = new String();
                    Boolean conti = true;
                    listTemp += temp.get(i);
                    left++;
                    int j;
                    for (j = 1; j < temp.get(i).length(); j++) {
                        if (temp.get(i).charAt(j) == '[')
                            left++;
                        else
                            break;
                    }
                    for (j = temp.get(i).length() - 1; j >= 0; j--) {
                        if (temp.get(i).charAt(j) == ']')
                            left--;
                        else
                            break;
                    }
                    if (left == 0) {
                        conti = false;
                    } else {
                        listTemp += " ";
                        i++;
                    }

                    while (conti) {
                        if (temp.get(i).charAt(0) == '[') {
                            left++;
                            for (j = 1; j < temp.get(i).length(); j++) {
                                if (temp.get(i).charAt(j) == '[')
                                    left++;
                                else
                                    break;
                            }
                            for (j = temp.get(i).length() - 1; j >= 0; j--) {
                                if (temp.get(i).charAt(j) == ']')
                                    left--;
                                else
                                    break;
                            }
                        } else if (temp.get(i).charAt(temp.get(i).length() - 1) == ']') {
                            left--;
                            for (j = temp.get(i).length() - 2; j >= 0; j--) {
                                if (temp.get(i).charAt(j) == ']')
                                    left--;
                                else
                                    break;
                            }
                        }
                        if (left == 0) {
                            listTemp += temp.get(i);
                            break;
                        } else {
                            listTemp += temp.get(i);
                            listTemp += " ";
                        }
                        i++;
                    }
                    temp.set(i, listTemp);
                    data.type = Const.LIST;
                } else {
                    data.type = Const.FUNC;
                }
            }
            data.content = temp.get(i);
            container.addElement(data);
        }
        return container;
    };
}
