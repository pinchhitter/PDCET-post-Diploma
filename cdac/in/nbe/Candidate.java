package cdac.in.nbe;

import java.util.Map; 
import java.util.TreeMap; 
import java.util.Iterator;
import java.util.Comparator;

class Candidate{

	String applicationId;
	String dob;
	double rank;
	double score;
	Map<Integer, String> choices;
	String category;
	String gender;
	String  specialCategory;
	boolean isPwD;
	boolean isAllocated;

	boolean isPwDQuotaAllocated;
	boolean isSpecialQuotaAllocated;

	Integer allocatedChoice;
	Quota allocatedQuota;
	Course allocatedCourse;
	int statusId;
	int actionId;

	static Map<String, String> CategoryMap = new TreeMap<String, String>();

	static{
		CategoryMap.put("1", "UR");
		CategoryMap.put("2", "OBC");
		CategoryMap.put("3", "SC");
		CategoryMap.put("4", "ST");
	}

	Candidate(String applicationId, String dob, String rank, String gender, String choiceList, String category, String specialCategory, String isPd, double score){

		try{
			this.applicationId = applicationId;
			this.dob = dob;
			this.gender = gender;
			this.category = CategoryMap.get( category.trim() );
			this.specialCategory = specialCategory;
			this.rank = Double.parseDouble( rank );
			this.score = score;

			if( isPd.trim().equals("t") ){
				this.isPwD = true;
			}else{
				this.isPwD = false;
			}

			this.choices = new TreeMap<Integer, String>();

			String[] tokens = choiceList.split("#");
			Integer number = 1;
			for(String token: tokens){
				String[] tk = token.split(":");
				this.choices.put( number, tk[1]+""+tk[2] );
				number++;
			}

			this.isAllocated = false;
			this.isPwDQuotaAllocated = false;
			this.isSpecialQuotaAllocated = false;
			this.allocatedChoice = null;
			this.allocatedQuota = null;
			this.allocatedCourse = null;

		}catch(Exception e){
			System.err.println( applicationId+", "+ dob+", "+ rank+", "+gender+", "+choiceList+", "+ category+", "+ specialCategory+", "+ isPd );
			e.printStackTrace();
		}
	}

	static void header(){
		System.out.println("ApplicationId, Gender, Category, specialCategory, isPwd, Rank");
	}
	static void allocHeader(){
		System.out.println("ApplicationId, Gender, Category, specialCategory, isPwd, Rank, allocateCourse, allocatedQuota, ChoiceNumber, Choices");
	}
	void printAllocation(){
		System.out.print( applicationId+", "+gender+", "+category+", "+specialCategory+", "+isPwD+", "+rank+", "+allocatedCourse.courseId+", "+allocatedQuota.name+", "+allocatedChoice+", ");
		for(Integer number: choices.keySet() ){
			System.out.print(choices.get( number ) +"-");
			if( number == allocatedChoice)
			    break;
		}
		System.out.println();
	}

	static void printAllocationTableHeader(){
		System.out.println("application_id, seat_allotted,quota, is_pwd_quota, is_spec_cat_quota,choice_no, round,status_id, action_id");
	}

	void printAllocationTable(String round){
		System.out.println( applicationId+", "+allocatedCourse.courseId+", "+allocatedQuota.name+", "+isPwDQuotaAllocated+", "+isSpecialQuotaAllocated+", "+allocatedChoice+", "+round+", "+statusId+", "+actionId);
	}

	void printNotAllocation(){
		System.out.print( applicationId+", "+gender+", "+category+", "+specialCategory+", "+isPwD+", "+rank+", "+allocatedCourse+", "+allocatedQuota+", "+allocatedChoice+", ");
		for(Integer number: choices.keySet() ){
			System.out.print(choices.get( number ) +"-");
		}
		System.out.println();
	}
	
	void print(){
		System.out.println( applicationId+", "+gender+", "+category+", "+specialCategory+", "+isPwD+", "+rank);
	}

	public void printDNBiew() {
		System.out.println(applicationId+", "+rank+", "+category+", "+allocatedQuota.name+", "+allocatedCourse.courseId+", "+allocatedCourse.getSpeciality()+", "+ allocatedCourse.getHospitalNameAddress()+", "+allocatedCourse.stateId);
	}

	public static void printAllocationDNBViewHeader() {
		System.out.println("Roll Number, Rank, Seat-Category, Allocated-Quota, Seat-Allocated, Specialty,  Name of the Institute/Hospital, State");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Candidate other = (Candidate) obj;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		return true;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
}

class CandidateSort implements Comparator<Candidate>{
        @Override
        public int compare(Candidate candidate1, Candidate candidate2) {
                if( candidate1.rank < candidate2.rank )
                        return -1;
                if( candidate1.rank > candidate2.rank )
                        return 1;

                long d1 = Double.doubleToLongBits(candidate1.rank );
                long d2 = Double.doubleToLongBits(candidate2.rank );

                return (d1 == d2 ?  0 : // Values are equal
                                (d1 < d2 ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
                                 1));                          // (0.0, -0.0) or (NaN, !NaN)
        }
}
