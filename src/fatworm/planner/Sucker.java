package fatworm.planner;

import fatworm.expr.FuncExpr;
import fatworm.handler.Fucker;
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
public class Sucker {
    private QueryPlanner queryPlanner;
    private UpdateExecutor updateExecutor;
    public boolean isQuery;
    private Plan queryPlan;

    public Sucker() {
        this(new QueryPlanner(), new UpdateExecutor());
    }

    public Sucker(QueryPlanner queryPlanner, UpdateExecutor updateExecutor) {
        this.queryPlanner = queryPlanner;
        this.updateExecutor = updateExecutor;
    }

    public Plan getQueryPlan() {
        return queryPlan;
    }

    public void doExecute(String sql) {
        try {
            while (sql.endsWith(";"))
                sql = sql.substring(0, sql.length() - 1);

            ANTLRStringStream input = new ANTLRStringStream(sql);
            FatwormLexer lexer = new FatwormLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            FatwormParser parser = new FatwormParser(tokens);
            CommonTree t = (CommonTree) parser.statement().getTree();

            doExecute(t, new ArrayList<FuncExpr>(), null);
        } catch (Exception e) {
            isQuery = false;
            e.printStackTrace();
        }
    }

    public void execute(String sql){
        doExecute(sql);
        if (isQuery)
            Fucker.storeSQL(sql);
    }

    public void doExecute(CommonTree t, List<FuncExpr> funcs, Plan plan) {
        isQuery = t.getType() == FatwormParser.SELECT || t.getType() == FatwormParser.SELECT_DISTINCT;
        if (isQuery) {
            queryPlan = queryPlanner.getPlan(t, funcs, plan);
//            System.out.println(queryPlan);
        }
        else
            updateExecutor.executeUpdate(t, plan);
    }
}
