package besucha.backend.model.algorithm;

import java.util.*;

public abstract class ConflictAlgorithm implements Algorithm {
	protected abstract Queue<Student> calculateStudentOrder(Section sect, List<Student> sl);

	protected class StudentPriorityList {

		private HashMap<StudentDetails, Integer> priorityLevelMap;
		private final int MAX_PREFERENCES = 10;
		private List<Integer> allPriorities;

		private class StudentDetails{
			private int studentSeniority;
			private int preferenceRank;
			private boolean isNecessaryCourse;

			@SuppressWarnings("all") // Remove if this constructor is ever used.
			public StudentDetails(int preferenceRank, Student s){
				this(preferenceRank, s.getSeniority(), false);
			}

			public StudentDetails(int preferenceRank, Seniority s){
				this(preferenceRank, s, false);
			}

			public StudentDetails(int preferenceRank, Student s, boolean iNC){
				this(preferenceRank, s.getSeniority(), iNC);
			}

			public StudentDetails(int preferenceRank, Seniority s, boolean iNC){
				this.studentSeniority = s.ordinal();
				this.preferenceRank = preferenceRank;
				this.isNecessaryCourse = iNC;
			}

			@Override
			public boolean equals(Object o){
				if(o == this) return true;
				if (!(o instanceof StudentDetails)) return false;
				StudentDetails sd = (StudentDetails)o;
				return (sd.studentSeniority == this.studentSeniority
						&& sd.preferenceRank == this.preferenceRank
						&& sd.isNecessaryCourse == this.isNecessaryCourse);
			}

			@Override
			public String toString(){
				return String.format("(%d, %d, %s)", this.studentSeniority, this.preferenceRank,
						this.isNecessaryCourse? "needed" : "elective");
			}
		}


		private void populatePriorityMap(Student s, boolean isJagged){
			Seniority[] seniorities = Seniority.values();
			int maxPreferences = MAX_PREFERENCES;
			int maxSeniority = seniorities.length;

			if (s != null) // Correct maxPreference size based on provided student
				maxPreferences = s.getPreferences().size();

			if (isJagged){
				jaggedPriorityAssignment(maxPreferences, maxSeniority);
			} else {
				balancedPriorityAssignment(maxPreferences, maxSeniority);
			}
			determineNumberOfPriorities();
		}

		private void jaggedPriorityAssignment(int maxPreferences, int maxSeniority){
			Seniority[] seniorities = Seniority.values();
			int rounds = maxPreferences + maxSeniority - 1;
			for(int i = 0; i < rounds; i++){
				for (int k = 0; k <= i && k < maxSeniority; k++){
					if (i-k < maxSeniority){
						this.priorityLevelMap.put(new StudentDetails(i-k, seniorities[k]), rounds - i);
					}
				}
			}
		}

		private void balancedPriorityAssignment(int maxPreferences, int maxSeniority){
			Seniority[] seniorities = Seniority.values();
			boolean isNeeded = true;
			for (int b = 0; b < 2; b++, isNeeded = false){
				for(int i = 0; i < maxPreferences; i++){
					for (int k = 0; k < maxSeniority; k++){
						int value = 2 * maxPreferences * maxSeniority -
								((maxPreferences * maxSeniority * b) + (maxSeniority * i) + k + 1);
						this.priorityLevelMap.put(new StudentDetails(i, seniorities[k], isNeeded), value);
					}
				}
			}
		}

		private void determineNumberOfPriorities(){
			Collection<Integer> vals = this.priorityLevelMap.values();
			for (Integer i : vals){
				if (!this.allPriorities.contains(i)){
					allPriorities.add(i);
				}
			}
			Collections.sort(this.allPriorities);
			Collections.reverse(this.allPriorities); // Largest Priority First
		}

		/////////////////////////////////////////////////////////////////////////
		// PUBLIC METHODS

		public StudentPriorityList(){
			this(null);
		}

		public StudentPriorityList(Student s){
			this(s, false);
		}

		public StudentPriorityList(Student s, boolean useJaggedPriorities){
			this.priorityLevelMap = new HashMap<>();
			this.allPriorities = new ArrayList<>();
			this.populatePriorityMap(s, useJaggedPriorities);
		}

		/**
		 * Calculates what the priority would be of a given student.
		 * @param preferenceRank the nth (starting from 0) pick section of the student
		 * @param s the student to prioritize
		 * @return the priority rating
		 */
		public int calculatePriority(int preferenceRank, Student s){
			return this.calculatePriority(preferenceRank, s, false);
		}

		/**
		 * Calculates what the priority would be of a given student.
		 * @param preferenceRank the nth (starting from 0) pick section of the student
		 * @param s the student to prioritize
		 * @param isNeeded a boolean representing whether or not student needs teh class
		 * @return the priority rating
		 */
		public int calculatePriority(int preferenceRank, Student s, boolean isNeeded){
			StudentDetails selectedSd = new StudentDetails(preferenceRank, s, isNeeded);
			for (StudentDetails sd : priorityLevelMap.keySet()){
				if ((sd.toString()).equals((selectedSd).toString())){
					return priorityLevelMap.get(sd);
				}
			}
			System.out.printf("%s's #%d preference deranked\n", s.getName(), preferenceRank);
			return -1;
		}

		/**
		 * Provides the number of priorities calculated
		 * @return size of the priority list.
		 */
		public int getNumberOfPriorities(){
			return this.allPriorities.size();
		}

		/**
		 * Provides the list of all priorities calculated by the class
		 * @return sorted list of all priorities
		 */
		public List<Integer> getAllPriorities(){
			return this.allPriorities;
		}
	}
}