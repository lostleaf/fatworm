package fatworm.memory;

import fatworm.constant.Const;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Memory {

    public static HashMap<String, List<List<Const>>> records = null;
    public static HashMap<String, HashMap<String, List<List<Const>>>> dbs = new HashMap<>();

}
