import java.io.File;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import IO.IO;
import Data.Data;
import Interpreter.Interpreter;
import Manager.Manager;
import Reader.Reader;

public class Main {
    public static void main(String[] args) {
        try {
            // IO.sc = new Scanner(System.in);
            IO.sc = new Scanner(new File("in"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Vector<Data> container = new Vector<Data>();
        Vector<String> list = new Vector<String>();
        Reader reader = new Reader();
        String line;

        while (IO.sc.hasNextLine()) {

            line = IO.sc.nextLine();
            if (line.length() == 0) {
                // System.out.println("- end of compiler -");
                continue;
            }

            StringTokenizer st = new StringTokenizer(line);

            while (st.hasMoreTokens()) {
                list.add(st.nextToken());
            }
        }
        IO.sc.close();

        container = reader.Read(list);
        Interpreter inter = new Interpreter(container, true);
        inter.setParent(null);
        Manager manager = new Manager();
        manager.addSpace(inter.getSpace());

        int[] idx = { 0 };
        inter.Exec(idx, false);
    }
}