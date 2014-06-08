package fatworm.scan;

import fatworm.constant.Const;
import fatworm.constant.DoubleConst;
import fatworm.constant.IntegerConst;
import fatworm.constant.NullConst;
import fatworm.expr.ColNameExpr;
import fatworm.expr.Expression;
import fatworm.expr.FuncExpr;
import fatworm.expr.Function;
import fatworm.util.Compare;
import fatworm.util.ConstUtil;

import java.sql.Types;
import java.util.*;

/**
 * Created by lostleaf on 14-6-7.
 */
public class GroupScan implements Scan {

    private Scan s;
    private ColNameExpr colName;
    private List<FuncExpr> funcs;
    private List<List<Const>> funcResults;
    private int numFuncs;
    private List<List<Const>> allItem = null;
    private Iterator<List<Const>> iterItem;
    private Iterator<List<Const>> iterResult;
    private List<Const> nowItem;
    private List<Const> nowResult;
    private Scan father;

    private ArrayList<Const> nowRecord;
    private Map<String, Integer> idxMap;

    public GroupScan(Scan s, ColNameExpr colName, List<FuncExpr> funcs1, Scan father) {
        this.s = s;
        this.colName = colName;
        this.funcs = funcs1;
        this.father = father;
        numFuncs = funcs1.size();

        idxMap = new HashMap<String, Integer>();
        int columnCount = getColumnCount();
        for (int i = 0; i < columnCount; ++i) {
            String tbl = getTableName(i);
            Expression fld = getFieldName(i);
            if (fld instanceof ColNameExpr) {
                Expression exp = new ColNameExpr(tbl, ((ColNameExpr)fld).getFldName());
                idxMap.put(exp.toHashString(), i);
                exp = new ColNameExpr(null, ((ColNameExpr)fld).getFldName());
                idxMap.put(exp.toHashString(), i);
            } else {
                idxMap.put(fld.toHashString(), i);
            }
        }
//		int k = 0;
//		for (Iterator<FuncExpr> iter = funcs1.iterator(); iter.hasNext(); ) {
//			FuncExpr fe = iter.next();
//			idxMap.put(fe.toHashString(), columnCount + k);
//			++k;
//		}
    }

    private class MyCamparator implements Comparator<List<Const>> {

        private int compareIndex;

        public MyCamparator(int compareIndex) {
            if (compareIndex == -1) {
                this.compareIndex = 0;
            } else {
                this.compareIndex = compareIndex;
            }
        }

        @Override
        public int compare(List<Const> a1, List<Const> a2) {
            return a1.get(compareIndex).compareTo(a2.get(compareIndex));
        }

    }

    @Override
    public void beforeFirst() {
        iterItem = allItem.iterator();
        iterResult = funcResults.iterator();
    }

    public boolean next() {
        if (allItem == null) {
            allItem = new ArrayList<List<Const>>();
            while (s.next()) {
                List<Const> newItem = new ArrayList<Const>();
                newItem.addAll(s.getNowRecord());
                allItem.add(newItem);
            }
//			if (allItem.isEmpty()) {
//				iterItem = allItem.iterator();
//				return ;
//			}
            int index;
            if (colName == null) {
                index = -1;
            } else {
                index = getColumnIndex(colName);
            }
            Collections.sort(allItem, new MyCamparator(index));
            ArrayList<Integer> funcTypes = new ArrayList<Integer>();
            ArrayList<Integer> funcIndex = new ArrayList<Integer>();
            ArrayList<Integer> resultTypes = new ArrayList<Integer>();
            funcResults = new ArrayList<List<Const>>();
            for (Iterator<FuncExpr> iter = funcs.iterator(); iter.hasNext(); ) {
                Expression e = iter.next();
                int type = ((FuncExpr)e).getFunc();
                ColNameExpr col = ((FuncExpr)e).getColName();
                int idx = getColumnIndex(col);
                if (idx == notFound) {
                    iter.remove();
                    continue;
                }
                funcTypes.add(type);
                funcIndex.add(idx);
                resultTypes.add(getColumnType(idx));
            }
            numFuncs = funcTypes.size();
            Const last = null;
            Iterator<List<Const>> iter = allItem.iterator();
            List<Const> result = null;
            int num = 0;
            while (iter.hasNext()) {
                List<Const> now = iter.next();
                if (last != null && (index == -1 || now.get(index).equals(last))) {
                    for (int i = 0; i < numFuncs; ++i) {
                        Const c = now.get(funcIndex.get(i));
                        result.set(i, result.get(i).operate(c, funcTypes.get(i)));
                    }
                    ++num;
                    iter.remove();
                } else {
                    if (result != null) {
                        for (int i = 0; i < numFuncs; ++i) {
                            if (funcTypes.get(i) == Function.AVG) {
                                result.set(i,
                                        result.get(i).operate(new DoubleConst(num), Function.CALAVG));
                            }
                        }
                        funcResults.add(result);
                    }
                    result = new ArrayList<Const>();
                    num = 0;
                    for (int i = 0; i < numFuncs; ++i) {
                        Const c = now.get(funcIndex.get(i));
                        if (c instanceof NullConst) {
                            result.add(new NullConst());
                            continue;
                        }
                        switch (funcTypes.get(i)) {
                            case Function.COUNT:
                                result.add(new IntegerConst(1));
                                break;
                            case Function.MAX:
                            case Function.MIN:
                                c = ConstUtil.changeToType(c, resultTypes.get(i));
                                if (resultTypes.get(i) == Types.FLOAT) c = c.toDoubleConst();
                                result.add(c);
                                break;
                            case Function.AVG:
                            case Function.SUM:
                                result.add(c.toDecimalConst());
                                break;
                        }
                    }
                    if (index != -1) {
                        last = now.get(index);
                    } else {
                        last = new IntegerConst(0);
                    }
                    num = 1;
                }
            }
            if (result != null) {
                for (int i = 0; i < numFuncs; ++i) {
                    if (funcTypes.get(i) == Function.AVG) {
                        result.set(i, result.get(i).operate(new DoubleConst(num), Function.CALAVG));
                    }
                }
                funcResults.add(result);
            } else {
                result = new ArrayList<Const>();
                for (int i = 0; i < numFuncs; ++i) {
                    switch (funcTypes.get(i)) {
                        case Function.COUNT:
                            result.add(new IntegerConst(0));
                            break;
                        case Function.AVG:
                        case Function.MAX:
                        case Function.MIN:
                        case Function.SUM:
                            result.add(new NullConst());
                    }
                }
                funcResults.add(result);
            }

            iterItem = allItem.iterator();
            iterResult = funcResults.iterator();
        }

        if (iterResult.hasNext()) {
            nowRecord = new ArrayList<Const>();
            if (iterItem.hasNext()) {
                nowItem = iterItem.next();
                nowRecord.addAll(nowItem);
            } else {
                for (int i = 0; i < s.getColumnCount(); ++i) {
                    nowRecord.add(new NullConst());
                }
            }
            nowResult = iterResult.next();
            nowRecord.addAll(nowResult);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getColumnCount() {
        return s.getColumnCount() + numFuncs;
    }

    @Override
    public int getColumnType(int columnIndex) {
        int colCount = s.getColumnCount();
        if (columnIndex < colCount) return s.getColumnType(columnIndex);
        return funcs.get(columnIndex - colCount).getType(s);
    }

    @Override
    public Const getColumn(int columnIndex) {
        int colCount = s.getColumnCount();
        if (columnIndex < colCount) return nowItem.get(columnIndex);
        return nowResult.get(columnIndex - colCount);
    }

    @Override
    public void close() {
        s.close();
    }

    @Override
    public Expression getFieldName(int columnIndex) {
        int colCount = s.getColumnCount();
        if (columnIndex < colCount) return s.getFieldName(columnIndex);
        return funcs.get(columnIndex - colCount);
    }

    @Override
    public String getTableName(int columnIndex) {
        int colCount = s.getColumnCount();
        if (columnIndex < colCount) return s.getTableName(columnIndex);
        return null;
    }

    @Override
    public Scan getParent() {
        return father;
    }

    @Override
    public Const getColumn(Expression expr, boolean findFather) {
        int idx = getColumnIndex(expr);
        if (idx != notFound) return getColumn(idx);
        if (father == null || !findFather) return null;
        return father.getColumn(expr, true);
//		if (father == null || !(s instanceof TableScan)) return null;
//		return father.getColumn(expr);
    }

    @Override
    public int getColumnType(Expression expr, boolean findFather) {
        int idx = getColumnIndex(expr);
        if (idx != notFound) return getColumnType(idx);
        if (father == null || !findFather) return notFound;
        return father.getColumnType(expr, true);
//		if (!(s instanceof TableScan)) return notFound;
//		if (father == null) return notFound;
//		return father.getColumnType(expr);
    }

    @Override
    public int getColumnIndex(Expression expr) {
        int idx = s.getColumnIndex(expr);
        if (idx != notFound) return idx;
        for (int i = 0; i < numFuncs; ++i) {
            if (Compare.equalCol(null, funcs.get(i), expr)) {
                return i + s.getColumnCount();
            }
        }
        return notFound;
    }

    @Override
    public int getOriginColNum() {
        return s.getOriginColNum();
    }

    @Override
    public List<Const> getNowRecord() {
        return nowRecord;
    }

    @Override
    public Const get(int columnIndex) {
        return nowRecord.get(columnIndex);
    }

    @Override
    public Const get(Expression expr, boolean findFather) {
        if (idxMap.containsKey(expr.toHashString())) {
            return nowRecord.get(idxMap.get(expr.toHashString()));
        }
        if (!findFather || father == null) return null;
        return father.get(expr, true);
    }

}
