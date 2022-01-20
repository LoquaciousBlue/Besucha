package besucha.backend.model.algorithm;

import java.util.*;

/**
 * Enrollment Algorithm designed around dealing with potentially
 * over-enrolling/conflicting classes, while ensuring no criterion
 * is given special weight when determining priority
 */
public class BalancedConflictAlgorithm extends ConflictAlgorithm {
	private ConflictAlgorithm.StudentPriorityList priorityChecker;
	private CourseSystem cs;
	private HashMap<Student, Integer> priorityOffset;
	private HashMap<Section, List<Student>> preferenceMap;

	/**
	 * Constructs the algorithm around the given course system.
	 * @param cs the course system to evaluate with the algorithm
	 */
	public BalancedConflictAlgorithm(CourseSystem cs){
		Student s = (cs == null ? null : cs.getAllStudents().get(0));

		this.cs = cs;
		this.priorityChecker = new ConflictAlgorithm.StudentPriorityList(s, false);
		this.priorityOffset = new HashMap<>();
		this.preferenceMap = new HashMap<>();
	}

	/**
	 * Runs the algorithm on the provided course system.
	 */
	public Void runAlgorithm(){
		this.fillPreferenceAndPriorityMap();
		this.performEnrollment();
		return null;
	}

	/**
	 * Determines the enrollment order of students wanting to get into a class
	 * @param sect the section to enroll into
	 * @param sl the list of students wishing to enroll
	 */
	@Override
	protected Queue<Student> calculateStudentOrder(Section sect, List<Student> sl){
		HashMap<Integer, HashMap<Integer, List<Student>>> enrollmentOrder = new HashMap<>(); //Convert this to tree?
		List<Integer> priorityList = new ArrayList<>(), creditList = new ArrayList<>();

		this.populateEnrollmentOrder(sect, sl, enrollmentOrder, priorityList, creditList);

		this.specialSortHelperLists(priorityList, creditList);

		return this.buildProperlyOrderedQueue(priorityList, creditList, enrollmentOrder);
	}

	/**
	 * Populates and initializes the necessary preference and priority offset maps
	 */
	private void fillPreferenceAndPriorityMap(){
		for (Student stud : this.cs.getAllStudents()){
			this.priorityOffset.put(stud, 0);
			for (Preference preference : stud.getPreferences()){
				// Need to make sure we initialize each new mapping
				this.preferenceMap.computeIfAbsent(preference.getSection(), k -> new ArrayList<>());
				this.preferenceMap.get(preference.getSection()).add(stud);
			}
		}
	}

	/**
	 * Entry point for enrollment
	 */
	private void performEnrollment(){
		for (int currentPreferenceLevel : priorityChecker.getAllPriorities()){
			this.performRoundOfEnrollment(currentPreferenceLevel);
		}
	}

	/**
	 * Perform a round of enrollment, using the provided preference level
	 * @param priorityLevel the priority to enroll at or above
	 */
	private void performRoundOfEnrollment(int priorityLevel){
		boolean didEnrollmentChange, OVER_ENROLLED = true, NORMAL = false;
		do didEnrollmentChange = processEnrollmentIntoClasses(
				this.preferenceMap.keySet(), priorityLevel, OVER_ENROLLED);
		while (didEnrollmentChange);

		processEnrollmentIntoClasses(this.preferenceMap.keySet(), priorityLevel, NORMAL);
	}

	// Only meant to improve visiblity; look at implementation before using
	private void specialSortHelperLists(List<Integer> priorityList, List<Integer> creditList){
		Collections.sort(priorityList);
		Collections.sort(creditList);
		Collections.reverse(priorityList); // Put high priorities first
	}

	/**
	 * Creates a queue with the proper enrollment order of the students
	 * @param priorityList the list of priorities to enroll with
	 * @param creditList the list of credits to enroll with
	 * @param enrollmentOrder the source of all enrollment stats
	 */
	private Queue<Student> buildProperlyOrderedQueue(List<Integer> priorityList, List<Integer> creditList,
													 HashMap<Integer, HashMap<Integer, List<Student>>> enrollmentOrder){
		Queue<Student> finalList = new LinkedList<>();

		for (Integer i : priorityList){
			for (Integer j : creditList){
				List<Student> currentList = enrollmentOrder.get(i).get(j);
				if (currentList == null) continue; // This commbination doesnt exist here
				Collections.shuffle(currentList); // we have no more metrics; random is the last resort
				finalList.addAll(currentList);
			}
		}
		return finalList;
	}

	/**
	 * Prepares the enrollment map for each future enrollment pass
	 */
	private void populateEnrollmentOrder(Section sect, List<Student> sl, HashMap<Integer, HashMap<Integer, List<Student>>> enrollmentOrder,
										 List<Integer> priorityList, List<Integer> creditList){

		for (Student s : sl) {
			int finalPriority = getFinalPriority(s, sect);
			int creditLoad = cs.getNumEnrolledCredits(s);

			this.placeStudentInEnrollmentMap(s, enrollmentOrder, finalPriority, creditLoad);
			this.updateAuxiliaryLists(priorityList, creditList, finalPriority, creditLoad);
		}
	}

	/**
	 * Returns the weighted priority value of the student in the provided section
	 * @param s the student to equate the priority to
	 * @param sect the section the student is trying to enroll in
	 * @return the priority level as an integer
	 */
	private int getFinalPriority(Student s, Section sect){
		return this.priorityChecker.calculatePriority(s.getPreferenceRank(sect), s, true) + this.priorityOffset.get(s);
	}

	/**
	 * Places student into enrollment map, initializing empty fields as necessary
	 */
	private void placeStudentInEnrollmentMap(Student s, HashMap<Integer, HashMap<Integer, List<Student>>> enrollmentOrder,
											 int finalPriority, int creditLoad){
		// Initialize sub maps if the don't exist yet
		if (!enrollmentOrder.containsKey(finalPriority))
			enrollmentOrder.put(finalPriority, new HashMap<>());

		if (!enrollmentOrder.get(finalPriority).containsKey(creditLoad))
			enrollmentOrder.get(finalPriority).put(creditLoad, new ArrayList<>());

		enrollmentOrder.get(finalPriority).get(creditLoad).add(s);
	}

	/**
	 * Updates auxiliary lists to help with enrollment
	 */
	private void updateAuxiliaryLists(List<Integer> priorityList,
									  List<Integer> creditList, int priority, int creditLoad){

		if (!priorityList.contains(priority)) priorityList.add(priority);
		if (!creditList.contains(creditLoad)) creditList.add(creditLoad);

	}

	// This should only be called if it's absolutely confirmed the student
	// should enroll in the course
	private void enrollStudent(Student stud, Section sect){
		this.cs.enroll(stud, sect);
	}

	/**
	 * Performs the enrollment of students into open sections.
	 */
	private boolean processEnrollmentIntoClasses(Set<Section> openSections, int priority, boolean selectOverwantedClasses){
		List<Section> sectionsToRemove = new ArrayList<>();
		for (Section sect : openSections){
			boolean sectionShouldBeRemoved = this.performEnrollmentRoundForSection(sect, priority, selectOverwantedClasses);
			if (sectionShouldBeRemoved) sectionsToRemove.add(sect);
		}

		for (Section sect : sectionsToRemove) this.makeSectionClosed(sect);
		return sectionsToRemove.size() > 0;
	}

	private boolean performEnrollmentRoundForSection(Section sect, int priority, boolean selectOverwantedClasses ){
		boolean shouldRemove = false;
		Queue<Student> enrollmentQueue = calculateStudentOrder(sect, preferenceMap.get(sect));
		boolean isOverRequested = sect.openSeat() < enrollmentQueue.size();

		if (selectOverwantedClasses == isOverRequested){
			shouldRemove = this.restartEnrollment(sect, enrollmentQueue, priority);
			this.updatePreferenceMap(sect, enrollmentQueue);
		}
		return shouldRemove;
	}

	/**
	 * Performs the prioritized enrollment process on a given queue of students into a section,
	 * so long as the next student has the proper priority
	 * @param sect the section to enroll into
	 * @param enrolleeQueue the queue of students wanting to enroll into the class, sorted by priority
	 * @param priority the necessary priority to enroll into the class.
	 * @return if the section was filled by the enrollment
	 */
	private boolean restartEnrollment(Section sect, Queue<Student> enrolleeQueue, int priority){
		boolean isSectionClosed = false;
		List<Student> studentsToRemove = new ArrayList<>();

		// Start iterating through queue
		while (enrolleeQueue.size() > 0 && sect.hasOpenSeat()){
			if (this.cs.hasMaxCredits(enrolleeQueue.peek())){
				studentsToRemove.add(enrolleeQueue.remove());
			} else if (this.hasInsignificantPriority(sect, enrolleeQueue, priority)) {
				break;
			} else {
				enrollStudent(enrolleeQueue.remove(), sect);
			}
		}

		if (!sect.hasOpenSeat()) isSectionClosed = true;

		for (Student stud : studentsToRemove) this.removeStudentFromEnrollment(stud);

		return isSectionClosed;
	}

	/**
	 * Checks if a given student in the enrollment queue is not eligible for enrollment in this round
	 */
	private boolean hasInsignificantPriority(Section sect, Queue<Student> enrolleeQueue, int priority){
		// Priority Check (assume: below priority = end enrollment for now)
		int basePriority = this.priorityChecker.calculatePriority(
				enrolleeQueue.peek().getPreferenceRank(sect), enrolleeQueue.peek());
		return (basePriority + this.priorityOffset.get(enrolleeQueue.peek()) < priority);
	}

	/**
	 * deletes the section from the enrollment map, while updating the priority
	 * of the students who didn't manage to enroll in the section
	 * @param sect the section that has been filled
	 */
	private void makeSectionClosed(Section sect){
		// Potentially add exception to throw in case not empty?
		for (Student stud : this.preferenceMap.get(sect)){
			this.cs.addToWaitlist(stud, sect);
			this.priorityOffset.put(stud, this.priorityOffset.get(stud) - 1);
		}
		this.preferenceMap.remove(sect);
	}

	/**
	 * Removes no longer eligible students from the enrollment map by
	 * replacing the existing list with the post-enrollment queue
	 * @param sect the section that was enrolled into
	 * @param remainingQueue the students left to be enrolled
	 */
	private void updatePreferenceMap(Section sect, Queue<Student> remainingQueue){
		List<Student> newList = this.preferenceMap.get(sect);
		newList.clear();
		while(remainingQueue.size() > 0){
			newList.add(remainingQueue.remove());
		}
	}

	/** Removes student from the enrollment list */
	private void removeStudentFromEnrollment(Student stud){
		for (List<Student> enrolleeList : this.preferenceMap.values()){
			enrolleeList.remove(stud);
		}
	}


}