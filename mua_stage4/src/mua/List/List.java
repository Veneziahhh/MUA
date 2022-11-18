package List;

import Const.Const;
import Data.Data;

public class List {
    public List() {

    };

    public Data first(Data origin) {
        Data tmp = new Data();
        if (origin.type == Const.WORD) {
            tmp.content = origin.content.substring(0, 1);
        } else if (origin.type == Const.LIST) {
            String s = origin.content;
            int x1 = 0, x2 = 0;
            while (s.charAt(x1) == ' ')
                x1++;
            x1++;
            while (s.charAt(x1) == ' ')
                x1++;
            if (s.charAt(x1) == '[') {
                int left = 1;
                x2 = x1 + 1;
                while (left != 0) {
                    if (s.charAt(x2) == '[')
                        left++;
                    else if (s.charAt(x2) == ']')
                        left--;
                    x2++;
                }
                tmp.type = Const.LIST;
                tmp.content = s.substring(x1, x2);
            } else {
                x2 = x1;
                while (s.charAt(x2) != ' ' && x2 < s.length() - 1)
                    x2++;
                s = s.substring(x1, x2);

                tmp.content = s;
                if (s.matches("[-]?[0-9][0-9]*[.]?[0-9]*")) {
                    tmp.num = Double.parseDouble(s);
                    tmp.type = Const.NUM;
                } else if (s.equals("true")) {
                    tmp.bool = true;
                    tmp.type = Const.BOOL;
                } else if (s.equals("false")) {
                    tmp.bool = false;
                    tmp.type = Const.BOOL;
                } else {
                    tmp.type = Const.WORD;
                }
            }
        }
        return tmp;
    }

    public Data last(Data origin) {
        Data tmp = new Data();
        if (origin.type == Const.WORD) {
            tmp.content = origin.content.substring(origin.content.length() - 1, origin.content.length());
        } else if (origin.type == Const.LIST) {
            String s = origin.content;
            int x1 = origin.content.length() - 1, x2;
            while (s.charAt(x1) == ' ')
                x1--;
            x1--;
            while (s.charAt(x1) == ' ')
                x1--;
            if (s.charAt(x1) == ']') {
                int right = 1;
                x2 = x1 - 1;
                while (right != 0) {
                    if (s.charAt(x2) == ']')
                        right++;
                    else if (s.charAt(x2) == '[')
                        right--;
                    x2--;
                }
                tmp.type = Const.LIST;
                tmp.content = s.substring(x2 + 1, x1 + 1);
            } else {
                x2 = x1;
                while (s.charAt(x2) != ' ' && x2 > 0)
                    x2--;
                s = s.substring(x2 + 1, x1 + 1);
                tmp.content = s;
                if (s.matches("[-]?[0-9][0-9]*[.]?[0-9]*")) {
                    tmp.num = Double.parseDouble(s);
                    tmp.type = Const.NUM;
                } else if (s.equals("true")) {
                    tmp.bool = true;
                    tmp.type = Const.BOOL;
                } else if (s.equals("false")) {
                    tmp.bool = false;
                    tmp.type = Const.BOOL;
                } else {
                    tmp.type = Const.WORD;
                }
            }
        }
        return tmp;
    }

    public Data butfirst(Data origin) {
        Data tmp = new Data();
        if (origin.type == Const.WORD) {
            tmp.content = origin.content.substring(1);
            return tmp;
        }
        String s = origin.content;
        List list = new List();
        tmp = list.first(origin);
        if (2 + tmp.content.length() != s.length()) {
            s = "[" + s.substring(2 + tmp.content.length());
        } else
            s = "[]";
        tmp.content = s;
        tmp.type = Const.LIST;
        return tmp;
    }

    public Data butlast(Data origin) {
        Data tmp = new Data();
        if (origin.type == Const.WORD) {
            tmp.content = origin.content.substring(0, origin.content.length() - 1);
            return tmp;
        }
        String s = origin.content;
        List list = new List();
        tmp = list.last(origin);
        if (2 + tmp.content.length() != s.length()) {
            s = s.substring(0, s.length() - tmp.content.length() - 2) + "]";
        } else
            s = "[]";
        tmp.content = s;
        tmp.type = Const.LIST;
        return tmp;
    }
}
