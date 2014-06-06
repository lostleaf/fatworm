package fatworm.plan;

import fatworm.handler.Manager;
import fatworm.meta.Schema;
import fatworm.scan.Scan;
import fatworm.scan.TableScan;

import java.util.HashSet;

public class TablePlan implements Plan {
	
	protected String tblName;
	protected Schema schema;
	protected Plan father;
	
	public TablePlan(String tblName, Plan father) {
		this.tblName = tblName.toLowerCase();
		schema = Manager.getDBManager().getCurrentDB().getTable(tblName).getSchema();
		this.father = father;
	}
	
	public String getTblName() {
		return tblName;
	}

	@Override
	public Scan open(Scan father) {
		Scan s = new TableScan(tblName, father);
		return s;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer("Table plan ( ");
		s.append("table name: ( "); s.append(tblName); s.append(" ) )"); 
		return s.toString();
	}
	
	@Override
	public Plan getPlan() {
		return null;
	}

	@Override
	public void setPlan(Plan p) {
		
	}

	@Override
	public Plan down() {
		return this;
	}

	@Override
	public void renameTable(String from, String to) {
	}

	@Override
	public HashSet<String> getAllTblNames() {
		HashSet<String> s = new HashSet<String>();
		s.add(tblName);
		return s;
	}

	@Override
	public String getTblName(String fldName, boolean findFather) {
		int idx = schema.getIdx(fldName);
		if (idx >= 0) return tblName;
		if (!findFather) return null;
		return father.getTblName(fldName, findFather);
	}

	@Override
	public HashSet<String> getAllUsedTblNames() {
		HashSet<String> s = new HashSet<String>();
		s.add(tblName);
		return s;
	}

	@Override
	public Plan getParentPlan() {
		return father;
	}
}
