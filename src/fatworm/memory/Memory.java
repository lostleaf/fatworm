package fatworm.memory;

import fatworm.constant.Const;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Memory {

    public static Map<String, List<List<Const>>> records = null;
    public static Map<String, Map<String, List<List<Const>>>> dbs = new HashMap<>();

}
