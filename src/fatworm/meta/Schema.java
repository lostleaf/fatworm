package fatworm.meta;

import fatworm.type.Type;

import java.io.Serializable;
import java.util.*;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Schema implements Serializable {

    private List<Attribute> attrs;
    private Map<String, Integer> idx;

    public Schema() {
        attrs = new ArrayList<Attribute>();
        idx = new HashMap<String, Integer>();
    }

    public List<Attribute> getAttributes() {
        return attrs;
    }

    public Type getType(int idx) {
        return attrs.get(idx).getType();
    }

    public int getIdx(String s) {
        if (idx.containsKey(s)) return idx.get(s);
        return -1;
    }

    public void addAttribute(Attribute a) {
        idx.put(a.getAttrName(), attrs.size());
        attrs.add(a);
    }

}
