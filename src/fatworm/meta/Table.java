package fatworm.meta;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Table {
    String name;
    Schema schema;

    public Table(String name, Schema schema) {
        this.name = name;
        this.schema = schema;
    }

    //TODO unimplemented
    public void createIndex(String primary, String indexName) {
    }


    public Schema getSchema() {
        return schema;
    }

}
