package fatworm.planner;

import fatworm.constant.Const;
import fatworm.constant.NullConst;
import fatworm.expr.Expr;
import fatworm.expr.FuncExpr;
import fatworm.handler.Fucker;
import fatworm.meta.Attribute;
import fatworm.meta.Schema;
import fatworm.parser.FatwormParser;
import fatworm.plan.Plan;
import fatworm.plan.SelectPlan;
import fatworm.plan.TablePlan;
import fatworm.scan.SelectScan;
import fatworm.scan.TableScan;
import fatworm.scan.UpdateScan;
import fatworm.type.*;
import fatworm.util.ConstUtil;
import org.antlr.runtime.tree.CommonTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lostleaf on 14-6-5.
 */
public class UpdateExecutor {
    public void executeUpdate(CommonTree tree, Plan parentPlan) {
//        System.out.println(tree.toStringTree());

        String name = tree.getChild(0).getText();
        switch (tree.getType()) {
            case FatwormParser.CREATE_DATABASE:
                Fucker.getDBManager().addDatabase(name);
                break;
            case FatwormParser.USE_DATABASE:
                Fucker.getDBManager().useDatabase(name);
                break;
            case FatwormParser.DROP_DATABASE:
                Fucker.getDBManager().dropDatabase(name);
                break;
            case FatwormParser.CREATE_TABLE:
                createTable(tree);
                break;
            case FatwormParser.DROP_TABLE:
                int count = tree.getChildCount();
                for (int i = 0; i < count; i++)
                    Fucker.getDBManager().getCurrentDB().dropTable(tree.getChild(i).getText());
                break;
            case FatwormParser.INSERT_VALUES:
                InsertExecutor.insertValue(tree, parentPlan);
                break;
            case FatwormParser.INSERT_COLUMNS:
                InsertExecutor.insertColumn(tree, parentPlan);
                break;
            case FatwormParser.INSERT_SUBQUERY:
                InsertExecutor.insertSubquery(tree, parentPlan);
                break;
            case FatwormParser.DELETE:
                delete(tree, parentPlan);
                break;
            case FatwormParser.UPDATE:
                update(tree, parentPlan);
                break;
        }
    }

    private void createTable(CommonTree tree) {
        int childNum = tree.getChildCount();
        String tblName = tree.getChild(0).getText();
        Schema schema = new Schema();
        for (int i = 1; i < childNum; ++i) {
            CommonTree treeChild = (CommonTree) tree.getChild(i);
            if (treeChild.getType() == FatwormParser.PRIMARY_KEY) continue;

            String attrName = treeChild.getChild(0).getText();
            int len1, len2;
            Type type = null;
            boolean autoInc = false;
            Const defaultValue = new NullConst();
            switch (treeChild.getChild(1).getType()) {
                case FatwormParser.INT:
                    type = new IntegerType();
                    break;
                case FatwormParser.FLOAT:
                    type = new FloatType();
                    break;
                case FatwormParser.CHAR:
                    len1 = Integer.valueOf(treeChild.getChild(1).getChild(0).getText());
                    type = new CharType(len1);
                    break;
                case FatwormParser.VARCHAR:
                    len1 = Integer.valueOf(treeChild.getChild(1).getChild(0).getText());
                    type = new VarcharType(len1);
                    break;
                case FatwormParser.DATETIME:
                case FatwormParser.TIMESTAMP:
                    type = new TimestampType();
                    break;
                case FatwormParser.BOOLEAN:
                    type = new BooleanType();
                    break;
                case FatwormParser.DECIMAL:
                    len1 = Integer.valueOf(treeChild.getChild(1).getChild(0).getText());
                    if (treeChild.getChild(1).getChildCount() > 1) {
                        len2 = Integer.valueOf(treeChild.getChild(1).getChild(1).getText());
                    } else {
                        len2 = 0;
                    }
                    type = new DecimalType(len1, len2);
                    break;
            }
            int defColNum = treeChild.getChildCount();
            for (int j = 2; j < defColNum; ++j) {
                CommonTree treeChildChild = (CommonTree) treeChild.getChild(j);
                switch (treeChildChild.getType()) {
                    case FatwormParser.NULL:
                        break;
                    case FatwormParser.AUTO_INCREMENT:
                        autoInc = true;
                        break;
                    case FatwormParser.DEFAULT:
                        defaultValue = type.toConst(treeChildChild.getChild(0).getText());
                        break;
                }
            }
            Attribute attr = new Attribute(attrName, type, defaultValue,
                    autoInc);
            schema.addAttribute(attr);
        }
//        if (primary_key != null) schema.setPrimary(primary_key);
        Fucker.getDBManager().getCurrentDB().addTable(tblName, schema);
    }


    private void delete(CommonTree tree, Plan fatherPlan) {
        String tblName = tree.getChild(0).getText();
        Plan p = new TablePlan(tblName, fatherPlan);
        if (tree.getChildCount() == 1) {
            TableScan s = (TableScan) p.open(null);
            while (s.next()) s.delete();
            s.close();
        } else {
            p = new SelectPlan(p,
                    PredParser.getPredicate((CommonTree) tree.getChild(1).getChild(0), upFuncs, p),
                    fatherPlan);
            SelectScan s = (SelectScan) p.open(null);
            while (s.next()) s.delete();
            s.close();
        }
    }

    private void update(CommonTree tree, Plan parentPlan) {
        String tblName = tree.getChild(0).getText();
        Plan p = new TablePlan(tblName, parentPlan);
        int childNum = tree.getChildCount();
        UpdateScan s;
        int totalChild = childNum;

        if (tree.getChild(childNum - 1).getType() == FatwormParser.WHERE) {
            p = new SelectPlan(p,
                    PredParser.getPredicate((CommonTree) tree.getChild(childNum - 1).getChild(0), upFuncs, p),
                    parentPlan
            );
            s = (SelectScan) p.open(null);
            totalChild--;
        } else
            s = (TableScan) p.open(null);
//        System.out.println(s.getClass() + " " + totalChild);
        while (s.next()) {
            for (int i = 1; i < totalChild; ++i) {
                CommonTree updatePair = (CommonTree) tree.getChild(i);
//                System.err.println(updatePair.toStringTree());
                Expr colName = ExprParser.getExpression(
                        (CommonTree) updatePair.getChild(0), upFuncs, p);
                Expr expr = ExprParser.getExpression(
                        (CommonTree) updatePair.getChild(1), upFuncs, p);
//                System.out.println(expr);
                Const c = expr.getResult(s);
                c = ConstUtil.changeToType(c, s.getColumnType(colName, true));
                s.setValue(colName, c);
            }
        }
        s.close();
    }

    private static List<FuncExpr> upFuncs = new ArrayList<FuncExpr>();
}
