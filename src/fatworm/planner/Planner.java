package fatworm.planner;

import fatworm.expr.FuncExpr;
import fatworm.parser.FatwormLexer;
import fatworm.parser.FatwormParser;
import fatworm.plan.Plan;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Planner {
    private QueryPlanner queryPlanner;
    private UpdatePlanner updatePlanner;
    public boolean isQuery;
    private Plan queryPlan;

    public Planner() {
        this(new QueryPlanner(), new UpdatePlanner());
    }

    public Planner(QueryPlanner queryPlanner, UpdatePlanner updatePlanner) {
        this.queryPlanner = queryPlanner;
        this.updatePlanner = updatePlanner;
    }

    public Plan getQueryPlan() {
        return queryPlan;
    }

    public void execute(String sql) {
        try {
            while (sql.endsWith(";"))
                sql = sql.substring(0, sql.length() - 1);

            ANTLRStringStream input = new ANTLRStringStream(sql);
            FatwormLexer lexer = new FatwormLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            FatwormParser parser = new FatwormParser(tokens);
            CommonTree t = (CommonTree) parser.statement().getTree();

            List<FuncExpr> funcs = new ArrayList<FuncExpr>();
            execute(t, funcs, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute(CommonTree t, List<FuncExpr> funcs, Plan plan) {
        isQuery = t.getType() == FatwormParser.SELECT || t.getType() == FatwormParser.SELECT_DISTINCT;
        if (isQuery) {
            queryPlan = queryPlanner.getPlan(t, funcs, plan);
//            System.out.println(queryPlan);
        }
        else
            updatePlanner.executeUpdate(t, plan);
    }
}
