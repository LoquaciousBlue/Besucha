package besucha.backend.model.algorithm;

import besucha.backend.dao.StudentDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Student object used by the algorithm.
 */
public class Student {

	private int studentId;
	private String name;
	private List<Preference> preferences;
	private Seniority seniority;

	public Student() {
		this.preferences = new ArrayList<>();
	}



	public Student(int studentId, String name, Seniority seniority, List<Preference> preferences) {
		this();
		this.studentId = studentId;
		this.name = name;
		this.preferences = preferences;
		this.seniority = seniority;
	}

	public Student(StudentDao studentDao) {
		this();
		this.studentId = studentDao.getStudentId();
		this.name = studentDao.getName();
		this.seniority = studentDao.getSeniority();
	}

	/** If section is on a student's preference list, return its rank. Otherwise, return -1.
	 * @param section the section to look for
	 * @return its index on the preference list
	 */
	public int getPreferenceRank(Section section) {
		for (Preference p  : preferences){
			if (section == p.getSection()){
				return preferences.indexOf(p);
			}
		}
		return -1;
	}

	/** If section is on a student's preference list, return its rank. Otherwise, return -1.
	 * @param p the preference pair to find the preference score of
	 * @return its index on the preference list
	 */
	public int getPreferenceRank(Preference p ){
		int result = preferences.indexOf(p);
		if (result == -1){ // no such section "exists", let's manually check
			return this.getPreferenceRank(p);
		}
		return result;
	}


	/**
	 * Adds a section to a student's preferences list
	 * @param section the section the student wants to enroll into
	 * @param isNeeded whether or not the student needs the class to graduate
	 */
	public void addPreference(Section section, boolean isNeeded) {
		Preference preference = new Preference(section, isNeeded);
		addPreference(preference);
	}

	/**
	 * Adds a section to a student's preferences list
	 * @param preference pair the section and need of the preference.
	 */
	public void addPreference(Preference preference) {
		preferences.add(preference);
	}

	/**
	 * Getter for the name of the student
	 * @return the name of the student
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for the student name.
	 * @param name the name of the student as a string.
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Getter for the ID of the student
	 * @return the ID of the student
	 */
	public int getId() {
		return this.studentId;
	}

	/**
	 * Setter for the student's ID
	 * @param idNum the student's ID number
	 */
	public void setId(int idNum) {
		this.studentId = idNum;
	}



	/**
	 * Getter for the class year of the student
	 * @return the class year as type seniority
	 */
	public Seniority getSeniority(){
		return this.seniority;
	}

	/**
	 * Setter for the class year of the student
	 * @param seniority set Seniority
	 */
	public void setSeniority(Seniority seniority){
		this.seniority = seniority;
	}

	/**
	 * Getter for the preferences of the student
	 * @return the preferred sections as type List
	 */
	public List<Preference> getPreferences() {
		return preferences;
	}

	/**
	 * Setter for the preferences of the student
	 * @param preferences a list of sections to make preferred sections
	 */
	public void setPreferences(List<Preference> preferences) {
		this.preferences = preferences;
	}




	@Override
	public String toString() {
		return "Student{" +
				"studentId=" + studentId +
				", name='" + name + '\'' +
				", seniority=" + seniority +
				'}';
	}


	/**
	 * Redefines the equals method to work specifically with Students
	 * @return whether or not the argument is equal to invoking object
	 */
	@Override
	public boolean equals(Object o){
		if(o == this) return true;
		if (!(o instanceof Student)) return false;
		Student s = (Student)o;
		return s.toString().equals(this.toString());
	}

	/**
	 * Overrides hashCode to work with Students
	 * @return the hash code
	 */
	@Override
	public int hashCode(){
		return this.toString().hashCode();
	}





}
