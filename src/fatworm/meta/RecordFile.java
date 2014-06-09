package fatworm.meta;

import fatworm.constant.Const;

import java.util.List;

/**
 * Created by lostleaf on 14-6-5.
 */
public interface RecordFile {

    public void beforeFirst();

    public boolean next();

    public List<Const> getRecord();

    public Const getColumn(int idx);

    public void setValue(int idx, Const c);

    public void delete();

    public void insert(List<Const> values);

    public void init();

    public void close();


}
