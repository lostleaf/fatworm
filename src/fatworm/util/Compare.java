package fatworm.util;

import fatworm.expr.ColNameExpr;
import fatworm.expr.Expression;
import fatworm.expr.FuncExpr;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Compare {

    public static boolean equalCol(String tblName, Expression fldName, Expression colName) {
        return colName instanceof ColNameExpr ?
                ((ColNameExpr) colName).getTblName() == null ?
                    fldName instanceof ColNameExpr &&
                    ((ColNameExpr) colName).getFldName().equals(((ColNameExpr) fldName).getFldName())
                    :((ColNameExpr) colName).getTblName().equals(tblName) &&
                    fldName instanceof ColNameExpr &&
                    ((ColNameExpr) colName).getFldName().equals(((ColNameExpr) fldName).getFldName())

                :colName instanceof FuncExpr && fldName instanceof FuncExpr &&
                ((FuncExpr) fldName).getFunc() == ((FuncExpr) colName).getFunc() &&
                equalCol(((FuncExpr) fldName).getColName().getTblName(),
                        new ColNameExpr(null, ((FuncExpr) fldName).getColName().getFldName()),
                        ((FuncExpr) fldName).getColName());
    }

}
