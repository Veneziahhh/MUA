package Interpreter;

import java.util.Iterator;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import java.io.*;

import javax.xml.stream.events.Namespace;

import Space.Space;
import Const.Const;
import Data.Data;
import List.List;
import Reader.Reader;

public class Interpreter {
    Vector<Data> words = new Vector<Data>();
    Stack<Data> names = new Stack<Data>();
    Stack<Data> values = new Stack<Data>();
    Stack<Data> ops = new Stack<Data>();
    Space nameSpace = new Space();
    boolean isGlobal, isFunc;

    public void setParent(Space parent) {
        nameSpace.setParent(parent);
    };

    public Interpreter(Vector<Data> words, boolean isGlobal, boolean isFunc) {
        this.words = words;
        this.isGlobal = isGlobal;
        this.isFunc = isFunc;
    };

    public Interpreter(Vector<Data> words, boolean isGlobal) {
        this.words = words;
        this.isGlobal = isGlobal;
        this.isFunc = false;
    };

    int GetLength() {
        return words.size();
    };

    public Space getSpace() {
        return nameSpace;
    }

    public Data Exec(int[] index, Boolean getValue) {

        Data tmp = new Data();
        Data op1 = new Data();
        Data op2 = new Data();
        String name;
        Vector<String> sonSpace = new Vector<String>();

        for (; index[0] < words.size(); index[0]++) {
            if (words.get(index[0]).type == Const.INST) {
                if (words.get(index[0]).content.equals("make")) {
                    ++index[0];
                    tmp = Exec(index, true);
                    name = tmp.content;

                    ++index[0];
                    tmp = Exec(index, true);
                    nameSpace.setHere(name, tmp);
                    if (isFunc)
                        sonSpace.add(name);
                } else if (words.get(index[0]).content.equals("erase")) {
                    name = words.get(++index[0]).content;
                    if (nameSpace.containsHere(name) == true) {
                        tmp = nameSpace.removeHere(name);
                    } else {
                        tmp = nameSpace.getGlobal().removeHere(name);
                    }
                } else if (words.get(index[0]).content.equals("isname")) {
                    ++index[0];
                    op1 = Exec(index, true);
                    tmp.type = Const.BOOL;
                    tmp.bool = nameSpace.containsHere(op1.content);
                    if (tmp.bool == false) {
                        tmp.bool = nameSpace.getGlobal().containsHere(op1.content);
                    }
                } else if (words.get(index[0]).content.equals("print")) {
                    ++index[0];
                    tmp = Exec(index, true);
                    if (tmp.type == Const.LIST)
                    {
                        String tmpContent = tmp.content.substring(1, tmp.content.length() - 1);

                        System.out.println(tmpContent);
                    } 
                    else if (tmp.type == Const.WORD)
                        System.out.println(tmp.content);
                    else if (tmp.type == Const.NUM)
                        System.out.println(tmp.num);
                    else if (tmp.type == Const.BOOL)
                        System.out.println(tmp.bool);
                } else if (words.get(index[0]).content.equals("thing")) {
                    ++index[0];
                    tmp = Exec(index, true);
                    name = tmp.content;
                    if (nameSpace.containsHere(name) == true) {
                        tmp = nameSpace.getHere(name);
                    } else if (nameSpace.getGlobal().containsHere(name) == true) {
                        tmp = nameSpace.getGlobal().getHere(name);
                    } else {
                        Space spaceTmp = new Space(nameSpace);
                        while (spaceTmp.containsHere(name) != true) {
                            spaceTmp = spaceTmp.getParent();
                        }
                        tmp = spaceTmp.getHere(name);
                    }
                } else if (words.get(index[0]).content.equals("read")) {
                    tmp = words.get(++index[0]);
                } else if (words.get(index[0]).content.equals("readlist")) {
                    tmp = words.get(++index[0]);
                    tmp.type = Const.LIST;
                } else if (words.get(index[0]).content.equals("not")) {
                    ++index[0];
                    tmp = Exec(index, true);
                    tmp.bool = !tmp.bool;
                } else if (words.get(index[0]).content.equals("isnumber")) {
                    ++index[0];
                    op1 = Exec(index, true);
                    tmp.type = Const.BOOL;
                    tmp.bool = (op1.type == Const.NUM) ? true
                            : (op1.type == Const.WORD && op1.content.matches("[-]?[0-9][0-9]*[.]?[0-9]*") ? true
                                    : false);
                } else if (words.get(index[0]).content.equals("isword")) {
                    ++index[0];
                    op1 = Exec(index, true);
                    tmp.type = Const.BOOL;
                    tmp.bool = (op1.type == Const.WORD) ? true : false;
                } else if (words.get(index[0]).content.equals("islist")) {
                    ++index[0];
                    op1 = Exec(index, true);
                    tmp.type = Const.BOOL;
                    tmp.bool = (op1.type == Const.LIST) ? true : false;
                } else if (words.get(index[0]).content.equals("isbool")) {
                    ++index[0];
                    op1 = Exec(index, true);
                    tmp.type = Const.BOOL;
                    tmp.bool = (op1.type == Const.BOOL) ? true
                            : (op1.type == Const.WORD && (op1.content.equals("true") || op1.content.equals("false"))
                                    ? true
                                    : false);
                } else if (words.get(index[0]).content.equals("isempty")) {
                    ++index[0];
                    op1 = Exec(index, true);
                    tmp.type = Const.BOOL;
                    if (op1.type == Const.WORD) {
                        tmp.bool = (op1.content.length() == 0) ? true : false;
                    } else if (op1.type == Const.LIST) {
                        tmp.bool = (op1.content.equals("[ ]") || op1.content.equals("[]")) ? true : false;
                    }
                } else if (words.get(index[0]).content.equals("run")) {
                    ++index[0];
                    op1 = Exec(index, true);
                    if (op1.content.equals("[]") || op1.content.equals("[ ]")) {
                        tmp.type = Const.LIST;
                        tmp.content = op1.content;
                    } else {
                        Vector<Data> subroutine = new Vector<Data>();
                        String op1Tmp = op1.content.substring(1, op1.content.length() - 1);

                        Vector<String> stringTmp = new Vector<String>();

                        StringTokenizer st = new StringTokenizer(op1Tmp);
                        while (st.hasMoreElements()) {
                            stringTmp.add(st.nextToken());
                        }

                        Reader subRead = new Reader();
                        subroutine = subRead.Read(stringTmp);

                        int[] indexSub = { 0 };

                        Interpreter interSub = new Interpreter(subroutine, false);
                        interSub.nameSpace = new Space(nameSpace);
                        tmp = interSub.Exec(indexSub, false);
                    }
                } else if (words.get(index[0]).content.equals("if")) {
                    // if <bool> <list1> <list2>
                    ++index[0];
                    op1 = Exec(index, true); // return bool value.
                    Boolean branch = op1.bool;
                    ++index[0];
                    op1 = Exec(index, true);
                    ++index[0];
                    op2 = Exec(index, true);
                    if (branch) {
                        // run list1
                        if (op1.content.equals("[]") || op1.content.equals("[ ]")) {
                            tmp.type = Const.LIST;
                            tmp.content = op1.content;
                        } else {
                            Vector<Data> sub = new Vector<Data>();
                            String op1Tmp = op1.content.substring(1, op1.content.length() - 1);
                            Vector<String> stringTmp = new Vector<String>();

                            StringTokenizer st = new StringTokenizer(op1Tmp);
                            while (st.hasMoreElements()) {
                                stringTmp.add(st.nextToken());
                            }

                            Reader subRead = new Reader();
                            sub = subRead.Read(stringTmp);
                            int[] idxSub = { 0 };
                            Interpreter ifSub = new Interpreter(sub, false);
                            ifSub.nameSpace = new Space(nameSpace);
                            tmp = ifSub.Exec(idxSub, false);
                        }
                    } else {
                        // run list2
                        if (op2.content.equals("[]") || op2.content.equals("[ ]")) {
                            tmp.type = Const.LIST;
                            tmp.content = op2.content;
                        } else {
                            Vector<Data> sub = new Vector<Data>();
                            String op2Tmp = op2.content.substring(1, op2.content.length() - 1);

                            Vector<String> stringTmp = new Vector<String>();

                            StringTokenizer st = new StringTokenizer(op2Tmp);
                            while (st.hasMoreElements()) {
                                stringTmp.add(st.nextToken());
                            }
                            Reader subRead = new Reader();
                            sub = subRead.Read(stringTmp);

                            int[] idxSub = { 0 };

                            Interpreter ifSub = new Interpreter(sub, false);
                            ifSub.nameSpace = new Space(nameSpace);
                            tmp = ifSub.Exec(idxSub, false);
                        }
                    }
                } else if (words.get(index[0]).content.equals("return")) {
                    ++index[0];
                    tmp = Exec(index, true);
                    tmp.isFunc = true;

                    for (int i = 0; i < sonSpace.size(); i++)
                        nameSpace.removeHere(sonSpace.get(i));

                    if (tmp.space == null)
                        tmp.space = nameSpace;
                    else {
                        tmp.space.coverSpace(nameSpace);
                    }
                } else if (words.get(index[0]).content.equals("export")) {
                    ++index[0];
                    tmp = Exec(index, true);

                    Data localValue = new Data();
                    localValue = nameSpace.getHere(tmp.content);

                    nameSpace.getGlobal().setHere(tmp.content, localValue);
                    tmp = localValue;
                } else if (words.get(index[0]).content.equals("word")) {
                    String content1 = new String();
                    String content2 = new String();
                    ++index[0];
                    op1 = Exec(index, true);
                    content1 = op1.content;
                    ++index[0];
                    op2 = Exec(index, true);
                    if (op2.type == Const.WORD)
                        content2 = op2.content;
                    else if (op2.type == Const.NUM)
                        content2 = Double.toString(op2.num);
                    else if (op2.type == Const.BOOL) {
                        if (op2.bool == true)
                            content2 = "true";
                        else
                            content2 = "false";
                    }
                    tmp.type = Const.WORD;
                    tmp.content = content1 + content2;
                } else if (words.get(index[0]).content.equals("sentence")) {
                    ++index[0];
                    op1 = Exec(index, true);
                    ++index[0];
                    op2 = Exec(index, true);
                    String op2Tmp = op2.content;
                    if (op2.type == Const.LIST)
                        op2Tmp = op2Tmp.substring(1, op2Tmp.length() - 1);
                    tmp.type = Const.LIST;
                    if (op1.type != Const.LIST)
                        tmp.content = "[" + op1.content + " " + op2Tmp + "]";
                    else 
                    {
                        tmp.content = op1.content.substring(0, op1.content.length() - 1) + " " + op2Tmp + "]";
                    }
                } else if (words.get(index[0]).content.equals("list")) {
                    ++index[0];
                    op1 = Exec(index, true);
                    ++index[0];
                    op2 = Exec(index, true);
                    tmp.type = Const.LIST;
                    tmp.content = "[" + op1.content + " " + op2.content + "]";
                } else if (words.get(index[0]).content.equals("join")) {
                    ++index[0];
                    op1 = Exec(index, true);
                    ++index[0];
                    op2 = Exec(index, true);

                    if (op1.content.equals("[]") || op1.content.equals("[ ]"))
                        tmp.content = "["+op2.content+"]";
                    else
                        tmp.content = op1.content.substring(0, op1.content.length() - 1) + " " + op2.content + "]";
                    tmp.type = Const.LIST;
                } else if (words.get(index[0]).content.equals("first")) {
                    ++index[0];
                    op1 = Exec(index, true);
                    if (op1.type == Const.LIST || op1.type == Const.WORD)
                    {
                        if (op1.content.equals("[]") || op1.content.equals("[ ]"))
                        tmp = op1;
                        else {
                            List list = new List();
                            tmp = list.first(op1);
                        }
                        if (op1.type == Const.WORD) tmp.type = Const.WORD;
                    }
                    else if (op1.type == Const.BOOL)
                    {
                        tmp.type = Const.WORD;
                        if (op1.bool == true)
                            tmp.content = "t";
                        else tmp.content = "f";
                    }
                    else if (op1.type == Const.NUM)
                    {
                        tmp.type = Const.WORD;
                        tmp.content = op1.content.substring(0, 1);
                    }
                    else tmp = op1;
                } else if (words.get(index[0]).content.equals("last")) {
                    ++index[0];
                    op1 = Exec(index, true);
                    if (op1.type == Const.LIST || op1.type == Const.WORD)
                    {
                        if (op1.content.equals("[]") || op1.content.equals("[ ]"))
                            tmp = op1;
                        else {
                            List list = new List();
                            tmp = list.last(op1);
                        }
                    }
                    else if (op1.type == Const.BOOL)
                    {
                        tmp.type = Const.WORD;
                        tmp.content = "e";
                    }
                    else if (op1.type == Const.NUM)
                    {
                        tmp.type = Const.WORD;
                        tmp.content = op1.content.substring(op1.content.length()-1);
                    }
                    else tmp = op1;
                } else if (words.get(index[0]).content.equals("butfirst")) {
                    ++index[0];
                    op1 = Exec(index, true);
                    if (op1.content.equals("[]") || op1.content.equals("[ ]"))
                        tmp = op1;

                    else {
                        List list = new List();
                        Data x = new Data();
                        x = op1;
                        tmp = list.butfirst(x);
                    }
                    if (op1.type == Const.WORD)
                        tmp.type = Const.WORD; 
                } else if (words.get(index[0]).content.equals("butlast")) {
                    ++index[0];
                    op1 = Exec(index, true);
                    if (op1.content.equals("[]") || op1.content.equals("[ ]"))
                        tmp = op1;
                    else {
                        List list = new List();
                        Data x = new Data();
                        x = op1;
                        tmp = list.butlast(x);
                    }
                } else if (words.get(index[0]).content.equals("random")) {
                    ++index[0];
                    tmp = Exec(index, true);
                    tmp.num = tmp.num * Math.random();
                } else if (words.get(index[0]).content.equals("int")) {
                    ++index[0];
                    tmp = Exec(index, true);
                    tmp.num = Math.floor(tmp.num);
                } else if (words.get(index[0]).content.equals("sqrt")) {
                    ++index[0];
                    tmp = Exec(index, true);
                    tmp.num = Math.sqrt(tmp.num);
                } else if (words.get(index[0]).content.equals("erall")) {
                    tmp.type = Const.BOOL;
                    tmp.content = "true";
                    tmp.bool = nameSpace.eraseAll();
                } else if (words.get(index[0]).content.equals("poall")) {
                    tmp.type = Const.LIST;
                    tmp.content = "[";
                    Iterator iter = nameSpace.getLocal().keySet().iterator();
                    while (iter.hasNext())
                        tmp.content += (iter.next() + " ");
                    if (tmp.content.length() > 1)
                        tmp.content = tmp.content.substring(0, tmp.content.length() - 1);
                    tmp.content += "]";
                } else if (words.get(index[0]).content.equals("save")) {
                    ++index[0];
                    tmp = Exec(index, true);

                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(tmp.content));
                        Iterator iterator = nameSpace.getLocal().keySet().iterator();
                        while (iterator.hasNext())
                        {
                            String key = iterator.next().toString();
                            Data data = nameSpace.getLocal().get(key);
                            out.write("make \"" + key + " " + data.content + "\n");
                        }
                        out.close();
                    } catch (IOException e) {

                    }
                } else if (words.get(index[0]).content.equals("load")) {
                    ++index[0];
                    tmp = Exec(index, true);
                    String runString = "";
                    File file = new File(tmp.content);
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new FileReader(file));
                        String tempString = null;
                        while ((tempString = reader.readLine()) != null) {
                            runString += tempString + " ";
                        }
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e1) {
                            }
                        }
                    }
                    
                    Vector<Data> loadRountine = new Vector<Data>();
                    Vector<String> loadStringTmp = new Vector<String>();

                    StringTokenizer st = new StringTokenizer(runString);
                    while (st.hasMoreElements())
                        loadStringTmp.add(st.nextToken());
                    
                    Reader loadRead = new Reader();
                    loadRountine = loadRead.Read(loadStringTmp);

                    int [] loadIndex = { 0 };

                    Interpreter interLoad = new Interpreter(loadRountine, false);
                    interLoad.nameSpace = new Space(nameSpace);
                    tmp = interLoad.Exec(loadIndex, false);
                    tmp.type = Const.BOOL;
                    tmp.bool = true;
                    tmp.content = "true";
                }
            } else if (words.get(index[0]).type == Const.OPERATOR) {
                name = words.get(index[0]).content;
                ++index[0];
                op1 = Exec(index, true);
                if (name.equals("add") || name.equals("sub") || name.equals("mul") || name.equals("div")
                        || name.equals("mod")) {
                    if (op1.type == Const.WORD) {
                        op1.num = Integer.parseInt(op1.content);
                    } else if (op1.type == Const.BOOL) {
                        if (op1.bool)
                            op1.num = 1;
                        else
                            op1.num = 0;
                    }
                }

                index[0]++;

                op2 = Exec(index, true);
                if (name.equals("add") || name.equals("sub") || name.equals("mul") || name.equals("div")
                        || name.equals("mod")) {
                    if (op2.type == Const.WORD) {
                        op2.num = Integer.parseInt(op2.content);
                    } else if (op2.type == Const.BOOL) {
                        if (op2.bool)
                            op2.num = 1;
                        else
                            op2.num = 0;
                    }
                }

                tmp.type = Const.NUM;
                if (name.equals("add"))
                    tmp.num = op1.num + op2.num;
                else if (name.equals("sub"))
                    tmp.num = op1.num - op2.num;
                else if (name.equals("mul"))
                    tmp.num = op1.num * op2.num;
                else if (name.equals("div"))
                    tmp.num = op1.num / op2.num;
                else if (name.equals("mod"))
                    tmp.num = op1.num % op2.num;
                else if (name.equals("eq")) {
                    tmp.type = Const.BOOL;
                    if (op1.type == Const.WORD)
                        tmp.bool = op1.content.equals(op2.content);
                    else
                        tmp.bool = (op1.num == op2.num);
                } else if (name.equals("gt")) {
                    tmp.type = Const.BOOL;
                    if (op1.type == Const.WORD) {
                        tmp.bool = op1.content.compareTo(op2.content) > 0 ? true : false;
                    } else
                        tmp.bool = (op1.num > op2.num);
                } else if (name.equals("lt")) {
                    tmp.type = Const.BOOL;
                    if (op1.type == Const.WORD) {
                        tmp.bool = op1.content.compareTo(op2.content) < 0 ? true : false;
                    } else
                        tmp.bool = (op1.num < op2.num);
                } else if (name.equals("and")) {
                    tmp.type = Const.BOOL;
                    tmp.bool = op1.bool && op2.bool;
                } else if (name.equals("or")) {
                    tmp.type = Const.BOOL;
                    tmp.bool = op1.bool || op2.bool;
                }
            } else if (words.get(index[0]).type == Const.LIST) {
                tmp.type = Const.LIST;
                tmp.content = words.get(index[0]).content;
            } else if (words.get(index[0]).type == Const.BOOL) {
                tmp.type = Const.BOOL;
                tmp.bool = words.get(index[0]).bool;
                tmp.content = words.get(index[0]).content;
            } else if (words.get(index[0]).type == Const.NUM) {
                tmp.type = Const.NUM;
                tmp.num = words.get(index[0]).num;
                tmp.content = words.get(index[0]).content;
            } else if (words.get(index[0]).type == Const.WORD) {
                tmp.type = Const.WORD;
                tmp.content = words.get(index[0]).content;
            } else if (words.get(index[0]).type == Const.FUNC) {
                String funcName = words.get(index[0]).content;

                Data funcData = new Data();

                // System.out.println(funcName);
                if (nameSpace.containsHere(funcName) == true) {
                    // local funtion
                    funcData = nameSpace.getHere(funcName);
                    // System.out.println("local-func:");
                    // System.out.println(funcName + " : " + funcData.content);
                } else {
                    Space spaceTmp = new Space();
                    spaceTmp = nameSpace;

                    while (spaceTmp.containsHere(funcName) == false) {
                        spaceTmp = spaceTmp.getParent();
                    }
                    funcData = spaceTmp.getHere(funcName);
                    // System.out.println("parent-func:");
                    // System.out.println(funcName + i + " : " + funcData.content);
                }
                // else if (nameSpace.getParent().containsHere(funcName)) {
                // // parent function
                // funcData = nameSpace.getParent().getHere(funcName);
                // System.out.println("parent-func:");
                // System.out.println(funcName + " : " + funcData.content);
                // } else if (nameSpace.getGlobal().containsHere(funcName)) {
                // // global function
                // funcData = nameSpace.getGlobal().getHere(funcName);
                // System.out.println("global-func:");
                // System.out.println(funcName + " : " + funcData.content);
                // }

                String funcList = funcData.content;

                int u1 = 1, u2;
                String argument, func;
                while (funcList.charAt(u1) == ' ')
                    u1++;
                u2 = u1;
                while (funcList.charAt(u2) != ']')
                    u2++;
                argument = funcList.substring(u1 + 1, u2);

                u1 = u2 + 1;
                while (funcList.charAt(u1) == ' ')
                    u1++;
                u2 = funcList.length() - 2;
                while (funcList.charAt(u2) == ' ')
                    u2--;
                func = funcList.substring(u1 + 1, u2);

                Vector<Data> funcSub = new Vector<Data>();

                Vector<String> nameTmp = new Vector<String>();
                StringTokenizer nameSt = new StringTokenizer(argument);
                while (nameSt.hasMoreElements())
                    nameTmp.add(nameSt.nextToken());

                Vector<String> funcTmp = new Vector<String>();
                StringTokenizer funcSt = new StringTokenizer(func);
                while (funcSt.hasMoreElements())
                    funcTmp.add(funcSt.nextToken());

                Reader funcRead = new Reader();
                funcSub = funcRead.Read(funcTmp);

                int[] funcIdx = { 0 };

                Interpreter funcInter = new Interpreter(funcSub, false, true);
                funcInter.nameSpace.setParent(nameSpace);

                for (int valueIdx = 0; valueIdx < nameTmp.size(); valueIdx++) {
                    ++index[0];
                    Data dataTmp = new Data();
                    dataTmp = Exec(index, true);
                    funcInter.nameSpace.setHere(nameTmp.get(valueIdx), dataTmp);
                }

                if (funcData.isFunc == true) {
                    funcInter.nameSpace.addSpace(funcData.space);
                }
                tmp = funcInter.Exec(funcIdx, false);
            }
            if (getValue) {
                return tmp;
            }
        }
        return tmp;
    };
}