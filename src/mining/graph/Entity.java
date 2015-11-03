package mining.graph;

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
		String temp1 = name.toLowerCase();
		String temp2 = ((Entity) obj).getName();
		return temp1.equals(temp2);
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
