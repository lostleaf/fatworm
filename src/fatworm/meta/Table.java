package fatworm.meta;

import java.io.Serializable;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Table implements Serializable {
    String name;
    Schema schema;

    public Table(String name, Schema schema) {
        this.name = name;
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }

}
