package fatworm.planner;

import fatworm.constant.Const;
import fatworm.expr.Expr;
import fatworm.expr.FuncExpr;
import fatworm.handler.Manager;
import fatworm.meta.Attribute;
import fatworm.meta.Schema;
import fatworm.plan.Plan;
import fatworm.plan.TablePlan;
import fatworm.scan.Scan;
import fatworm.scan.TableScan;
import fatworm.util.ConstUtil;
import org.antlr.runtime.tree.CommonTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lostleaf on 14-6-7.
 */
public class InsertExecutor {
    public static void  insertValue(CommonTree tree, Plan parentPlan) {
        String tblName = tree.getChild(0).getText();
        Plan p = new TablePlan(tblName, parentPlan);
        TableScan s = (TableScan) p.open(null);
        CommonTree values = (CommonTree) tree.getChild(1);
        List<Attribute> attrs = Manager.getDBManager().getCurrentDB().getTable(tblName).
                getSchema().getAttributes();
        int i = 0;
        List<Const> insertVals = new ArrayList<Const>();
        for (Attribute attr : attrs) {
            String text = values.getChild(i).getText();
            if (text.toLowerCase().equals("default")
                    || attr.isAutoIncrement() && text.toLowerCase().equals("null")) {
                insertVals.add(attr.getDefaultConstant());
                ++i;
                continue;
            }
            Const c = attr.getNewConstant((CommonTree) values.getChild(i));
            insertVals.add(c);
            ++i;
        }
        s.insert(insertVals);
        s.close();
    }

    public static void insertColumn(CommonTree tree, Plan fatherPlan) {
        String tblName = tree.getChild(0).getText();
        Plan p = new TablePlan(tblName, fatherPlan);
        TableScan s = (TableScan) p.open(null);
        int childNum = tree.getChildCount();
        CommonTree values = (CommonTree) tree.getChild(childNum - 1);
        int colNum = values.getChildCount();
        List<Attribute> attrs = Manager.getDBManager().getCurrentDB().getTable(tblName).
                getSchema().getAttributes();
        Map<Integer, Const> vals = new HashMap<Integer, Const>();
        for (int i = 0; i < colNum; ++i) {
            Expr colNameExpr = ExprPlanner.getExpression((CommonTree) tree.getChild(i + 1),
                    upFuncs, p);
            int index = s.getColumnIndex(colNameExpr);
            if (values.getChild(i).getText().toLowerCase().equals("default")
                    || attrs.get(index).isAutoIncrement()
                    && values.getChild(i).getText().toLowerCase().equals("null")) {
                vals.put(index, attrs.get(index).getDefaultConstant());
            } else {
                Const c = attrs.get(index).getNewConstant((CommonTree) values.getChild(i));
                vals.put(index, c);
            }
        }
        List<Const> insertVals = new ArrayList<Const>();
        for (int i = 0; i < attrs.size(); ++i) {
            if (vals.containsKey(i)) {
                insertVals.add(vals.get(i));
            } else {
                insertVals.add(attrs.get(i).getDefaultConstant());
            }
        }
        s.insert(insertVals);
        s.close();
    }

    public static void insertSubquery(CommonTree tree, Plan fatherPlan) {
        String tblName = tree.getChild(0).getText();
        String tmpTable = getNewTableName();

        Schema schema = Manager.getDBManager().getCurrentDB().getTable(tblName).getSchema();
        List<Attribute> attrs = schema.getAttributes();
        Manager.getDBManager().getCurrentDB().addTable(tmpTable, schema);

        Plan newPlan = new TablePlan(tmpTable, fatherPlan);
        TableScan newScan = (TableScan) newPlan.open(null);

        Planner planner = Manager.createPlanner();
        planner.execute((CommonTree) tree.getChild(1), upFuncs, fatherPlan);
        Plan subPlan = planner.getQueryPlan();
        Scan subScan = subPlan.open(null);
        int colNum = subScan.getColumnCount();
        while (subScan.next()) {
            List<Const> insertVals = new ArrayList<Const>();
            for (int i = 0; i < colNum; ++i) {
                Const colC = subScan.get(i);
                colC = ConstUtil.changeToType(colC, attrs.get(i).getType());
                insertVals.add(colC);
            }
            newScan.insert(insertVals);
        }
        Plan p = new TablePlan(tblName, fatherPlan);
        TableScan s = (TableScan) p.open(null);
        newScan.beforeFirst();
        while (newScan.next()) {
            List<Const> insertVals = new ArrayList<Const>();
            for (int i = 0; i < colNum; ++i) {
                insertVals.add(newScan.get(i));
            }
            s.insert(insertVals);
        }

        newScan.close();
        s.close();
        Manager.getDBManager().getCurrentDB().dropTable(tmpTable);
    }
    private static List<FuncExpr> upFuncs = new ArrayList<FuncExpr>();
    private static int cnt = 0;

    private static String getNewTableName() {
        return "temporary_table_" + String.valueOf(cnt++);
    }
}
