package fatworm.memory;

import fatworm.constant.Const;
import fatworm.handler.Manager;
import fatworm.meta.RID;
import fatworm.meta.RecordFile;
import fatworm.meta.Schema;

import java.util.Iterator;
import java.util.List;

public class MemoryRecordFile implements RecordFile {
	
	private String tblName;
	private List<List<Const>> records;
	private Iterator<List<Const>> iter;
	private List<Const> nowRecord;
	private Schema schema;
	
	public MemoryRecordFile(String tblName) {
		this.tblName = tblName;
		records = Memory.records.get(tblName);
		schema = Manager.getDBManager().getCurrentDB().getTable(tblName).getSchema();
		iter = records.iterator();
	}

	@Override
	public void beforeFirst() {
		iter = records.iterator();
	}

	@Override
	public boolean next() {
		if (iter.hasNext()) {
			nowRecord = iter.next();
			return true;
		}
		return false;
	}

	@Override
	public List<Const> getRecord() {
		return nowRecord;
	}


	@Override
	public void setValue(int idx, Const val) {
		nowRecord.set(idx, val);
	}

	@Override
	public void insert(List<Const> values) {
		records.add(values);
	}

	@Override
	public void delete() {
		iter.remove();
	}

	@Override
	public Const getColumn(int idx) {
		return nowRecord.get(idx);
	}

	@Override
	public void close() {
	}

	@Override
	public void init() {
	}

	@Override
	public RID getCurrentRid() {
		return null;
	}

}
