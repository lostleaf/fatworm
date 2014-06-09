package fatworm.plan;

import fatworm.scan.RenameScan;
import fatworm.scan.Scan;

import java.util.HashSet;

/**
 * Created by lostleaf on 14-6-7.
 */
public class RenamePlan implements Plan {
    private String newName;
    private Plan p;
    private Plan father;

    public RenamePlan(Plan p, String name, Plan father) {
        this.p = p;
        this.newName = name.toLowerCase();
        this.father = father;
    }

    public String getNewName() {
        return newName;
    }

    @Override
    public Scan open(Scan father) {
        Scan s = p.open(father);
        s = new RenameScan(s, newName, father);
        return s;
    }

    public String toString() {
        return "Rename plan ( " + "new name: ( " + newName + " ), " + "plan: ( " + p.toString() + ") )";
    }

    @Override
    public Plan getPlan() {
        return p;
    }

    @Override
    public void setPlan(Plan p) {
        this.p = p;
    }

    @Override
    public void renameTable(String from, String to) {
        p.renameTable(from, to);
    }

    @Override
    public HashSet<String> getAllTblNames() {
        HashSet<String> s = new HashSet<String>();
        s.add(newName);
        return s;
    }

    @Override
    public Plan getParentPlan() {
        return father;
    }
}
