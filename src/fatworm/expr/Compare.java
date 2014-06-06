package fatworm.expr;

public class Compare {

	public static boolean equalCol(String tblName, Expression fldName, Expression colName) {
		if (colName instanceof ColNameExpr) {
			if (((ColNameExpr) colName).getTblName() == null) {
				return fldName instanceof ColNameExpr &&
						((ColNameExpr)colName).getFldName().equals(((ColNameExpr)fldName).getFldName());
			} else {
				return ((ColNameExpr)colName).getTblName().equals(tblName) &&
						fldName instanceof ColNameExpr &&
						((ColNameExpr)colName).getFldName().equals(((ColNameExpr)fldName).getFldName());
			}
		}
		if (colName instanceof FuncExpr) {
			return fldName instanceof FuncExpr &&
					((FuncExpr)fldName).getFunc() == ((FuncExpr)colName).getFunc() && 
					equalCol(((FuncExpr)fldName).getColName().getTblName(), 
							new ColNameExpr(null, ((FuncExpr)fldName).getColName().getFldName()),
							((FuncExpr)fldName).getColName());
		}
		return false;
	}
	
}
