package mining.graph;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Entity {
	private String name;
	private Set<Entity> refs;
	private int lowerBound;
	private int upperBound;

	public Entity() {
		name = "";
		refs = new HashSet<Entity>();
	}

	public Entity(String title) {
		// TODO Auto-generated constructor stub
		this.name = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Entity> getRefs() {
		return refs;
	}

	public void setRefs(Set<Entity> refs) {
		this.refs = refs;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		String temp = ((Entity) obj).getName();
		boolean tf = name.equals(temp);
		return tf;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int hash = 7;
		for (int i = 0; i < name.length(); i++) {
			hash = hash * 31 + name.charAt(i);
		}
		return hash;
	}

	public int getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}

	public int getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}

	public boolean isSameRange(Entity obj) {
		return ((this.lowerBound == obj.getLowerBound()) && (this.upperBound == obj
				.getUpperBound()));
	}
}
