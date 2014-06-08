package fatworm.handler;

import fatworm.constant.Const;
import fatworm.memory.Memory;
import fatworm.memory.MemoryRecordFile;
import fatworm.meta.RecordFile;
import fatworm.meta.Schema;
import fatworm.meta.Table;

import java.io.Serializable;
import java.util.*;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Database implements Serializable {
    private Map<String, Table> tables = new HashMap<String, Table>();

    public Table getTable(String name) {
        name = name.toLowerCase();
        if (tables.containsKey(name)) {
            return tables.get(name);
        }
        return null;
    }

    public void addTable(String name, Schema schema) {
        name = name.toLowerCase();
        Table table = new Table(name, schema);
        tables.put(name, table);
//        System.out.println(schema);
        Memory.records.put(name, new ArrayList<List<Const>>());
        RecordFile rf = new MemoryRecordFile(name);
        //table.getEmptyBlock();
        rf.init();
        rf.close();

        String primary = table.getSchema().getPrimary();
        if (primary != null) {
            table.createIndex(primary, "shit_primary");
        }

    }

    public void dropTable(String name) {
        name = name.toLowerCase();
        Table table = tables.remove(name);
        if(Memory.records != null && Memory.records.containsKey(name))
            Memory.records.remove(name);
    }

    public void dropAllTables() {
        ArrayList<String> list = new ArrayList<>(tables.keySet());
        for (String tblName : list)
            dropTable(tblName);
    }
}
