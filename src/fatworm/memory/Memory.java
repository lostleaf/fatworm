package fatworm.memory;

import fatworm.constant.Const;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Memory {

    public static Map<String, List<List<Const>>> records = null;
    public static Map<String, Map<String, List<List<Const>>>> dbs = new HashMap<>();

}
