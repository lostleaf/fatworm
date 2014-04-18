package plan;

/**
 * Created by lostleaf on 14-4-18.
 */
public class InsertSubqueryPlan extends Plan {
    ExprPlan into;
    Plan value;

    public InsertSubqueryPlan(ExprPlan into, Plan value) {
        this.into = into;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Insert into {" + into + "} value {" + value + "}";
    }
}
