package besucha.backend.model.algorithm;

import besucha.backend.exception.DuplicationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a SectionList object used by the CourseSystem.
 */
public class SectionList {

	private List<Section> sections;

	public SectionList() {
		this.sections = new ArrayList<>();
	}

	public SectionList(List<Section> sections) {
		this.sections = sections;
	}

	/**
	 * Add new section.
	 * @param s section to be added to the system
	 * @throws DuplicationException if section already contained within list
	 */
	public void addSection(Section s) throws DuplicationException {
		if (!sections.contains(s)) {
			sections.add(s);
		} else {
			throw new DuplicationException("Section ID " + s.getId() + " is a duplicate. Please remove duplicates from Excel spreadsheet and try again.");
		}
	}

	/**
	 * Get all sections.
	 * @return list of sections.
	 */
	public List<Section> getSections() {
		return this.sections;
	}

	/**
	 * Get the size of the list.
	 * @return integer size of list
	 */
	public int size() {
		return sections.size();
	}

	@Override
	public String toString() {
		return "SectionList{" +
				"sections=" + sections +
				'}';
	}
}

