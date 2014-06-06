package fatworm.plan;

import java.util.LinkedList;

/**
 * Created by lostleaf on 14-4-17.
 */
public class JoinPlan extends Plan {
    LinkedList<Plan> planList = new LinkedList<Plan>();

    public JoinPlan(LinkedList<Plan> l) {
        planList = l;
    }

    @Override
    public String toString() {
        if (planList.size() > 1) {
            String ret = "Join { ";
            for (Plan p : planList)
                ret = ret + p.toString() + " ";
            return ret + "}";
        } else
            return planList.get(0).toString();
    }
}
