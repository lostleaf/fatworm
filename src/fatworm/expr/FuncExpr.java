package fatworm.expr;

import fatworm.constant.Const;
import fatworm.plan.Plan;
import fatworm.scan.Scan;

import java.sql.Types;
import java.util.HashSet;

/**
 * Created by lostleaf on 14-6-5.
 */
public class FuncExpr implements Expression {

    private ColNameExpr colName;
    private int func;

    public FuncExpr(ColNameExpr colName, int func) {
        this.colName = colName;
        this.func = func;
    }

    public int getFunc() {
        return func;
    }

    public ColNameExpr getColName() {
        return colName;
    }

    @Override
    public Const getResult(Scan s) {
        return s.get(this, true);
    }

    @Override
    public int getType(Scan s) {
        switch (func) {
            case Function.AVG:
                return Types.DECIMAL;
            case Function.COUNT:
                return Types.INTEGER;
            case Function.MAX:
            case Function.MIN:
                return s.getColumnType(colName, true);
            case Function.SUM:
                return Types.DECIMAL;
        }
        return 0;
    }

    public String toString() {
        StringBuffer s = new StringBuffer("Function ( ");
        s.append("func: ");
        s.append(func);
        s.append(", ");
        s.append("colName: (");
        s.append(colName.toString());
        s.append(") )");
        return s.toString();
    }

    @Override
    public void renameTable(String from, String to) {
        colName.renameTable(from, to);
    }

    @Override
    public HashSet<String> getTblNames(Plan p) {
        return colName.getTblNames(p);
    }

    @Override
    public String toHashString() {
        StringBuffer s = new StringBuffer(String.valueOf(func));
        s.append(',');
        s.append(colName.toHashString());
        return s.toString();
    }

}
