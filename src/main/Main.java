package main;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import parser.FatwormLexer;
import parser.FatwormParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by lostleaf on 14-4-17.
 */
public class Main {
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

        FatwormLexer lexer = new FatwormLexer(new ANTLRStringStream(sql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FatwormParser parser = new FatwormParser(tokens);
        FatwormParser.statement_return rs = parser.statement();
        CommonTree ct = (CommonTree) rs.getTree();
        System.out.println(ct.toStringTree());

        System.out.println(planParser.parse(ct));
    }

}
