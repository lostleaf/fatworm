package fatworm.memory;

import fatworm.constant.Const;
import fatworm.meta.RecordFile;

import java.util.Iterator;
import java.util.List;

/**
 * Created by lostleaf on 14-6-5.
 */
public class MemoryRecordFile implements RecordFile {

    private List<List<Const>> records;
    private Iterator<List<Const>> iter;
    private List<Const> nowRecord;

    public MemoryRecordFile(String tblName) {
        records = Memory.records.get(tblName);
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



}
