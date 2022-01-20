package besucha.backend.dao;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SectionDao object that manages the "section" table in MySQL.
 */
@Entity
@Table(name = "section")
public class SectionDao {


	/**
	  int representing section ID; used as ID by MySQL.
	 */
	@Id
	private int sectionId;

	/**
	 * Title of class, as String.
	 */
	private String title;

	/**
	 * Capacity of class, as int. Max number of students who can be enrolled.
	 */
	private int capacity;

	/**
	 * A double representing the number of credits a class is worth.
	 */
	private double creditWeight;

	/**
	 * List of StudentDao objects who are enrolled in the class. In MySQL, this is part of a many-to-many relationship with StudentDao and is represented in the table "enrolled".
	 */
	@ManyToMany(mappedBy = "enrolled")
	private List<StudentDao> enrolled;

	/**
	 * List of StudentDao objects who are waitlisted in the class. In MySQL, this is part of a many-to-many relationship with StudentDao and is represented with the table "waitlist".
	 */
	@ManyToMany(mappedBy = "waitlist")
	private List<StudentDao> waitlist;

	/**
	 * List of PreferenceDao objects who want to take this class. In MySQL, this is part of a one-to-many relationship with PreferenceDao.
	 */
	@OneToMany(mappedBy = "section")
	private List<PreferenceDao> preferences;

	public SectionDao() {
		enrolled = new ArrayList<>();
		waitlist = new ArrayList<>();
		preferences = new ArrayList<>();
	}

	public SectionDao(String title) {
		this();
		this.title = title;
	}

	public SectionDao(int sectionId, String title, int capacity, double creditWeight) {
		this();
		this.sectionId = sectionId;
		this.title = title;
		this.capacity = capacity;
		this.creditWeight = creditWeight;
	}

	public SectionDao(int sectionId) {
		this.sectionId = sectionId;
	}

	/**
	 * Getter for section Id.
	 * @return int representing section Id
	 */
	public int getSectionId() {
		return sectionId;
	}


	/**
	 * Getter for section title.
	 * @return title as a String.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter for section title.
	 * @param title title as a String.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter for section capacity, or number of students.
	 * @return capacity as an int.
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Setter for section capacity, or number of students.
	 * @param capacity int representing class capacity.
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Getter for credit weight, as a double.
	 * @return double representing number of credits a section is worth.
	 */
	public double getCreditWeight() {
		return creditWeight;
	}

	/**
	 * Setter for credit weight, as a double.
	 * @param creditWeight double representing number of credits a section is worth.
	 */
	public void setCreditWeight(double creditWeight) {
		this.creditWeight = creditWeight;
	}


	/**
	 * Two sections are equal if their Id's match.
	 * @param o the other Object
	 * @return true if ids are a match
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SectionDao that = (SectionDao) o;

		return sectionId == that.sectionId;
	}

	@Override
	public int hashCode() {
		return sectionId;
	}
}
