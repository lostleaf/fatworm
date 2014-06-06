package fatworm.scan;

import fatworm.constant.Const;
import fatworm.expr.Expression;

import java.util.List;

public interface UpdateScan extends Scan {
	
	public void setValue(Expression expr, Const val);
	public void setValue(int idx, Const val);
	public void insert(List<Const> values);
	public void delete();

}
