package fatworm.util;

import fatworm.parser.FatwormParser;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Function {
	
	public static final int AVG     = 0;
	public static final int COUNT   = 1;
	public static final int MIN     = 2;
	public static final int MAX     = 3;
	public static final int SUM     = 4;
	
	public static final int CALAVG = -1;
	
	public static int getFuncFromType(int type) {
		switch (type) {
		case FatwormParser.AVG:
			return AVG;
		case FatwormParser.COUNT:
			return COUNT;
		case FatwormParser.MIN:
			return MIN;
		case FatwormParser.MAX:
			return MAX;
		case FatwormParser.SUM:
			return SUM;
		}
		return -1;
	}
}
