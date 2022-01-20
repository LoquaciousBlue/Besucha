package besucha.backend.model.algorithm;

/**
 * Defines a Preference object for the algorithm.
 */
public class Preference {

	private Section section;
	private boolean isRequired;

	public Preference() {
	}

	public Preference(Section section, boolean isRequired) {
		this.setSection(section);
		this.setRequired(isRequired);
	}


	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}


}
