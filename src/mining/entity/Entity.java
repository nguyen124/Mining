package mining.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Entity {
	private String title;
	/*
	 * private String text; private Set<Entity> disambiguationNames; private
	 * Set<Entity> redirectNames; private Set<Entity> entityPages; private
	 * Set<Entity> hyperLinks;
	 */
	private List<String> candidates;

	public Entity() {
		title = "";
		candidates = new ArrayList<String>();
	}

	public Entity(String title) { // TODO Auto-generated constructor stub
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * public String getText() { return text; }
	 * 
	 * public void setText(String text) { this.text = text; }
	 * 
	 * public Set<Entity> getDisambiguationNames() { return disambiguationNames;
	 * }
	 * 
	 * public void setDisambiguationNames(Set<Entity> disambiguationNames) {
	 * this.disambiguationNames = disambiguationNames; }
	 * 
	 * public Set<Entity> getRedirectNames() { return redirectNames; }
	 * 
	 * public void setRedirectNames(Set<Entity> redirectNames) {
	 * this.redirectNames = redirectNames; }
	 * 
	 * private int lowerBound; private int upperBound;
	 * 
	 * public String getName() { return title; }
	 * 
	 * public void setName(String name) { this.title = name; }
	 * 
	 * @Override public boolean equals(Object obj) { // TODO Auto-generated
	 * method stub String temp = ((Entity) obj).getName(); boolean tf =
	 * title.equals(temp); return tf; }
	 * 
	 * @Override public int hashCode() { // TODO Auto-generated method stub int
	 * hash = 7; for (int i = 0; i < title.length(); i++) { hash = hash * 31 +
	 * title.charAt(i); } return hash; }
	 * 
	 * public int getLowerBound() { return lowerBound; }
	 * 
	 * public void setLowerBound(int lowerBound) { this.lowerBound = lowerBound;
	 * }
	 * 
	 * public int getUpperBound() { return upperBound; }
	 * 
	 * public void setUpperBound(int upperBound) { this.upperBound = upperBound;
	 * }
	 * 
	 * public boolean isSameRange(Entity obj) { return ((this.lowerBound ==
	 * obj.getLowerBound()) && (this.upperBound == obj .getUpperBound())); }
	 * 
	 * public Set<Entity> getEntityPages() { return entityPages; }
	 * 
	 * public void setEntityPages(Set<Entity> entityPages) { this.entityPages =
	 * entityPages; }
	 * 
	 * public Set<Entity> getHyperLinks() { return hyperLinks; }
	 * 
	 * public void setHyperLinks(Set<Entity> hyperLinks) { this.hyperLinks =
	 * hyperLinks; }
	 */

	public List<String> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<String> candidates) {
		this.candidates = candidates;
	}
}
