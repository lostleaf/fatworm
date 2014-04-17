package parser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by lostleaf on 14-4-17.
 */
public class Test {
    public static void main(String[] args) throws RecognitionException {
        File input = new File("test.sql");
        Scanner scanner;
        try {
            scanner = new Scanner(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        String sql = "";
        while (scanner.hasNextLine()) sql += scanner.nextLine();
        parse(sql);
    }

    private static void parse(String sql) throws RecognitionException {
        FatwormLexer lexer = new FatwormLexer(new ANTLRStringStream(sql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FatwormParser parser = new FatwormParser(tokens);
        FatwormParser.statement_return rs = parser.statement();
        CommonTree t = (CommonTree) rs.getTree();
        System.out.println(t.toStringTree());
    }
}
