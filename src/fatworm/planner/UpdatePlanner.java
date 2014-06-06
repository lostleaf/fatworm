package fatworm.planner;

import fatworm.constant.Const;
import fatworm.handler.Manager;
import fatworm.meta.Attribute;
import fatworm.parser.FatwormParser;
import fatworm.plan.Plan;
import fatworm.plan.TablePlan;
import fatworm.scan.TableScan;
import org.antlr.runtime.tree.CommonTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lostleaf on 14-6-5.
 */
public class UpdatePlanner {
    public void executeUpdate(CommonTree tree, Plan parentPlan) {
        String name = tree.getChild(0).getText();
        switch (tree.getType()){
            case FatwormParser.CREATE_DATABASE:
                Manager.getDBManager().addDatabase(name);
                break;
            case FatwormParser.USE_DATABASE:
                Manager.getDBManager().useDatabase(name);
                break;
            case FatwormParser.INSERT_VALUES:
                insertValue(tree, parentPlan);
                break;
        }
    }

    private void insertValue(CommonTree tree, Plan parentPlan) {
        String tblName = tree.getChild(0).getText();
        Plan p = new TablePlan(tblName, parentPlan);
        TableScan s = (TableScan)p.open(null);
        CommonTree values = (CommonTree)tree.getChild(1);
        List<Attribute> attrs = Manager.getDBManager().getCurrentDB().getTable(tblName).
                getSchema().getAttributes();
        int i = 0;
        List<Const> insertVals = new ArrayList<Const>();
        for (Iterator<Attribute> iter = attrs.iterator(); iter.hasNext(); ) {
            Attribute attr = iter.next();
            String text = values.getChild(i).getText();
            if (text.toLowerCase().equals("default")
                    || attr.isAutoIncrement() && text.toLowerCase().equals("null")) {
                insertVals.add(attr.getDefaultConstant());
                ++i;
                continue;
            }
            Const c = attr.getNewConstant((CommonTree)values.getChild(i));
            insertVals.add(c);
            ++i;
        }
        s.insert(insertVals);
        s.close();
    }
}
