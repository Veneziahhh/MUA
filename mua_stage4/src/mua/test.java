import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class test {
    public static void main(String[] args) {

        HashMap<Integer, String> hash = new HashMap<Integer, String>();
        hash.put(0, "123");
        hash.put(11, "23");
        hash.put(2, "12");
        // String str = " a b c ";// "This is String , split by StringTokenizer, created
        // by runoob";
        // StringTokenizer st = new StringTokenizer(str);
        Iterator iter = hash.keySet().iterator();
        while (iter.hasNext())
            System.out.println(iter.next());
        // System.out.println("----- 通过空格分隔 ------");
        // while (st.hasMoreElements()) {
        // System.out.println(st.nextToken());

        // String a = "0.9";
        // double x = Double.parseDouble(a);
        // System.out.println(x);
        // string s = new string();
        // s.s = "abc";
        // // HashMap<Integer, string> a = new HashMap<Integer, string>();
        // man.addSpace(0, s.s);
        // System.out.println(man.contains(0));
        // System.out.println(man.contains(1));
        // System.out.println(man.getHere(0));
        // s.s = "abcd";
        // Manager man1 = new Manager();
        // man1.addSpace(1, s.s);
        // System.out.println(man.contains(0));
        // System.out.println(man.contains(1));
        // System.out.println(man.getHere(0));
        // System.out.println(man.getHere(1));
    }
}
