package fatworm.meta;

import fatworm.type.Type;

import java.io.Serializable;
import java.util.*;

public class Schema implements Serializable {
	
	private List<Attribute> attrs;
	private Map<String, Integer> idx;
	private List<Integer> positions;
	private int length;
	private String primary;
	
	public Schema(String tblName) {
		attrs = new ArrayList<Attribute>();
		primary = null;
		length = 0;
		positions = new ArrayList<Integer>();
		idx = new HashMap<String, Integer>();
	}
	
	public List<Attribute> getAttributes() {
		return attrs;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getPos(int idx) {
		return positions.get(idx);
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
		positions.add(length);
		length += a.getType().getLength();
	}
	
	public void setPrimary(String attrName) {
		attrName = attrName.toLowerCase();
		for (Iterator<Attribute> iter = attrs.iterator(); iter.hasNext(); ) {
			Attribute attr = iter.next();
			if (attr.getAttrName().equals(attrName)) {
				attr.setPrimary();
				break;
			}
		}
		primary = attrName;
	}
	
	public String getPrimary() {
		return primary;
	}
	
}
