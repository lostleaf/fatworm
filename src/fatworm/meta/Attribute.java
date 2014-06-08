package fatworm.meta;

import fatworm.constant.Const;
import fatworm.constant.DecimalConst;
import fatworm.constant.IntegerConst;
import fatworm.constant.NullConst;
import fatworm.expr.Expression;
import fatworm.expr.FuncExpr;
import fatworm.expr.Function;
import fatworm.expr.Operator;
import fatworm.planner.ExprPlanner;
import fatworm.type.DecimalType;
import fatworm.type.Type;
import org.antlr.runtime.tree.CommonTree;

import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Attribute implements Serializable {
    private String tblName, attrName;
    private Const defaultValue;
    private boolean autoIncrement;
    private Type type;

    public Attribute(String tblName, String attrName, Type type, Const defaultValue,
                     boolean mustNull, boolean mustNotNull, boolean autoIncre) {
        this.tblName = tblName.toLowerCase();
        this.attrName = attrName.toLowerCase();
        this.type = type;
        this.defaultValue = defaultValue;
        this.autoIncrement = autoIncre;
        if (autoIncre) this.defaultValue = new IntegerConst(0);
    }

    public String getTblName() {
        return tblName;
    }

    public String getAttrName() {
        return attrName;
    }

    public Type getType() {
        return type;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public Const getDefaultConstant() {
        if (autoIncrement) defaultValue = defaultValue.binaryOp(new IntegerConst(1), Operator.PLUS);
        return defaultValue.copy();
    }

    public Const getNewConstant(CommonTree tree) {
        if (tree.getText().toLowerCase().equals("null")) return new NullConst();
        Expression expr = ExprPlanner.getExpression(tree, new ArrayList<FuncExpr>(), null);
        Const c = expr.getResult(null);
        switch (type.getType()) {
            case Types.INTEGER:
                c = c.toIntegerConst();
                if (autoIncrement) defaultValue = defaultValue.operate(c, Function.MAX);
                return c;
            case Types.CHAR:
            case Types.VARCHAR:
                return c.toStringConst();
            case Types.DECIMAL:
                c = c.toDecimalConst();
                ((DecimalConst) c).setScale(((DecimalType) type).getScale());
                return c;
            case Types.TIMESTAMP:
                return c.toTimestampConst();
            case Types.DOUBLE:
                return c.toDoubleConst();
            case Types.FLOAT:
                return c.toFloatConst();
            case Types.BOOLEAN:
                return c.toBooleanConst();
            default:
                return new NullConst();
        }
    }
}
