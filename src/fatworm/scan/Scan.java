package fatworm.scan;

import fatworm.constant.Const;
import fatworm.expr.Expression;

import java.util.List;

/**
 * Created by lostleaf on 14-6-5.
 */
public interface Scan {
    public void beforeFirst();
    public boolean next();
    public int getColumnCount();
    public int getColumnType(int columnIndex);
    public int getColumnType(Expression expr, boolean findParent);
    public int getColumnIndex(Expression expr);

    public Const getColumn(int columnIndex);
    public Const getColumn(Expression expr, boolean findParent);
    public void close();
    public Expression getFieldName(int columnIndex);
    public String getTableName(int columnIndex);

    public int getOriginColNum();

    public List<Const> getNowRecord();
    public Const get(int columnIndex);
    public Const get(Expression expr, boolean findFather);

    static final int notFound = 1249327;
}
