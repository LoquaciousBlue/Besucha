package besucha.backend.model.algorithm;

import besucha.backend.dao.SectionDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Section object for the algorithm.
 */
public class Section {

	private int id;
	private String title;
	private List<Student> waitlist;
	private List<Student> enrolled;
	private int capacity;
	private double creditWeight;

	public Section() {
		this.waitlist = new ArrayList<>();
		this.enrolled = new ArrayList<>();
	}

	/**
	 * Create a Section object using a SectionDao object.
	 * @param sectionDao sectionDao object
	 */
	public Section(SectionDao sectionDao) {
		this(sectionDao.getSectionId(),
				sectionDao.getTitle(),
				sectionDao.getCapacity(),
				sectionDao.getCreditWeight());
	}

	/**
	 * Initializes a Section with all its components. Set default creditWeight to 1.
	 * @param id the section ID; should be unique
	 * @param title the name of the section
	 * @param capacity the maximum enrollment
	 * @param creditWeight how many credits a section is worth (i.e. a lab is 0.5)
	 */
	public Section(int id, String title, int capacity, double creditWeight) {
		this();
		this.id = id;
		this.title = title;
		this.capacity = capacity;
		this.creditWeight = creditWeight;
	}


	/**
	 * Checks if the section has an open seat
	 * @return if there's an open seat
	 */
	public boolean hasOpenSeat() {
		return (this.openSeat() > 0);
	}

	/**
	 * returns the number of open seats remaining
	 * @return number of open seats
	 */
	public int openSeat() {
		return capacity - enrolled.size();
	}

	/**
	 * Getter for the section's id number
	 * @return the id of the section
	 */
	public int getId() {
		return id;
	}

	/**
	 * Getter for credit weight
	 * @return credit weight
	 */
	public double getCreditWeight() {
		return this.creditWeight;
	}

	/**
	 * Set the credit weight of a class.
	 * @param weight the creditWeight as a double
	 */
	public void setCreditWeight(double weight) {
		this.creditWeight = weight;
	}


	/**
	 * Getter for the title of the section
	 * @return the title of the section
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * Getter for the list of waitlisted students
	 * @return the students on the waiting list (as type List)
	 */
	public List<Student> getWaitlist() {
		return waitlist;
	}


	/**
	 * Getter for the list of enrolled students
	 * @return the enrolled students (as type List)
	 */
	public List<Student> getEnrolled() {
		return enrolled;
	}


	/**
	 * Getter for the capacity of the section
	 * @return the total number of seats the section offers
	 */
	public int getCapacity() {
		return capacity;
	}


	/**
	 * Formats the class into string representation
	 * @return name:gradYear as a string
	 */
	@Override
	public String toString(){
		return this.id + ":" + this.title;
	}

	/**
	 * Redefines the equals method to work specifically with Sections
	 * @return whether or not the argument is equal to invoking object
	 */
	@Override
	public boolean equals(Object o){
		if(o == this) return true;
		if (!(o instanceof Section)) return false;
		Section s = (Section)o;
		return s.toString().equals(this.toString());
	}

	/**
	 * Redefines the hashCode method to work specifically with Sections
	 */
	@Override
	public int hashCode(){
		return this.toString().hashCode();
	}
}
