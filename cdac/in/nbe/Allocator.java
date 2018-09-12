package cdac.in.nbe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Allocator{

	List<Candidate> candidates;
	Map<String, Course> courses;
	Map<String,Course> lastRoundAllocation ;
	Map<String,Integer> noFoundCourses = new TreeMap<String,Integer>();
	static int allocated;
	static int totalSeats;
	static List<String> priority;
	static Map<String,Integer> quotaPriority;

	static double URCuttOff = 371.00;
	static double PwDCuttOff = 353.00;
	static double OBCSCSTCuttOff = 334.00;

	Allocator(){

		candidates = new ArrayList<Candidate>();
		courses = new TreeMap<String, Course>();
		priority = new ArrayList<String>();

		priority.add("UR");
		priority.add("URWo");
		priority.add("PWD");
		priority.add("SC");
		priority.add("SCWo");
		priority.add("ST");
		priority.add("STWo");
		priority.add("OBC");
		priority.add("OBC-A");
		priority.add("OBC-B");
		priority.add("MBC");
		priority.add("BC");
		priority.add("BCA");
		priority.add("BCAWo");
		priority.add("BCBWo");
		priority.add("SCA");
		priority.add("MBC/DC");
			
		lastRoundAllocation = new HashMap<>();
		allocated = 0;
		totalSeats = 0;
		
		quotaPriority = new HashMap<>();
		quotaPriority.put("UR", 1);
		quotaPriority.put("URWo", 2);
		quotaPriority.put("PWD", 3);
		quotaPriority.put("SC", 4);
		quotaPriority.put("SCWo", 5);
		quotaPriority.put("ST", 5);
		quotaPriority.put("STWo", 5);
		quotaPriority.put("OBC", 6);
		quotaPriority.put("OBC-A", 7);
		quotaPriority.put("OBC-B", 7);
		quotaPriority.put("MBC", 7);
		quotaPriority.put("BC", 7);
		quotaPriority.put("BCA", 7);
		quotaPriority.put("BCAWo", 8);
		quotaPriority.put("BCBWo", 8);
		quotaPriority.put("SCA", 8);
		quotaPriority.put("MBC/DC", 8);

	}
	
	void readCourses(String filename, boolean header){

		BufferedReader br = null; 
		int count = 0;
		String line = null;

		try{
			br = new BufferedReader(new FileReader( new File(filename) ) );
			while( (line = br.readLine() ) != null ){
				if( header ){
					header = false;
					continue;
				}
				count++;
				String []token = line.trim().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

				String courseId = token[0].trim().substring(1);
				String hospitalCode = token[1].trim();
				String specialityCode = token[2].trim();
				String nameAddress = token[3].trim();
				String state = token[4].trim();
				String speciality = token[5].trim();
				String total = token[6].trim();

				int ur = Integer.parseInt( token[7].trim() );
				int sc = Integer.parseInt( token[8].trim() );
				int st = Integer.parseInt( token[9].trim() );
				int obc = Integer.parseInt( token[10].trim() );
				int obca = Integer.parseInt( token[11].trim() );
				int obcb = Integer.parseInt( token[12].trim() );
				int pwd = Integer.parseInt( token[13].trim() );
				int mbc = Integer.parseInt( token[14].trim() );
				int bc = Integer.parseInt( token[15].trim() );
				int bca = Integer.parseInt( token[16].trim() );
				int sca = Integer.parseInt( token[17].trim() );
				int mbcdc = Integer.parseInt( token[18].trim() );
				int urWom = Integer.parseInt( token[19].trim() );
				int scWom = Integer.parseInt( token[20].trim() );
				int stWom = Integer.parseInt( token[21].trim() );
				int bcaWom = Integer.parseInt( token[22].trim() );
				int bcbWom = Integer.parseInt( token[23].trim() );

				Course course = new Course( courseId, hospitalCode, state, specialityCode, total, ur, sc, st, obc, obca, obcb, pwd, mbc, bc, bca, sca, mbcdc, urWom, scWom, stWom, bcaWom, bcbWom );

				course.setHospitalNameAddress(nameAddress);
				course.setSpeciality(speciality);
				courses.put( courseId.trim(), course);
				totalSeats=totalSeats+Integer.parseInt(total);
			}
			System.err.println("Total Courses Read: "+(count));
			//printCourse();

		}catch(Exception e){
			System.err.println("Course File reading error at "+count+"line");
			System.err.println("Line: "+line);
			e.printStackTrace();
		} 
	}

	void readCandidate(String filename, boolean header){
		BufferedReader br = null; 
		int count = 0;
		try{
			br = new BufferedReader(new FileReader( new File(filename) ) );
			String line = null;
			while( (line = br.readLine() ) != null ){
				if( header ){
					header = false;
					continue;
				}

				String []token = line.trim().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				String applicantId = token[0].trim();
				String dob = token[1].trim();
				String categoryId = token[2].trim();
				String isPwD = token[3].trim();
				String gender = token[4].trim();
				double score = Double.parseDouble( token[5].trim() );
				String air = token[6].trim();
				String prefer = token[7].trim();
				String statusId = token[8].trim();
				String specialCatopted = token[9].trim();
				String specialCat = token[10].trim();
				Candidate candidate = null;

				if( specialCatopted.equals("t") ){
					candidate = new Candidate( applicantId, dob, air, gender, prefer, categoryId, specialCat, isPwD, score);
				}else{
					candidate = new Candidate( applicantId, dob, air, gender, prefer, categoryId, null, isPwD, score);
				}
				candidate.setActionId(1);
				candidate.setStatusId(1);
				candidates.add( candidate );
				count++;
			}
			System.err.println("Total Applicant Read: "+count);
		}catch(Exception e){
			e.printStackTrace();
		} 
	}

	boolean isValidQuotaAllocation(Quota quota, Candidate candidate){

		if( quota == null)
			return false;
		if( quota.name.equals("UR") )
			return true;
		if( quota.name.equals(candidate.category) )
			return true;
		if( quota.name.equals("PWD") && candidate.isPwD )
			return true;
		if( quota.name.equals( candidate.specialCategory ) )
			return true;

		if( candidate.gender.equals("Female") ){

			if( quota.name.equals("URWo") ){
				return true;
			}else if( quota.name.equals("PWDWo") && candidate.isPwD ){
				return true;	
			}else if( quota.name.equals("SCWo") && candidate.category.equals("SC") ){
				return true;
			}else if( quota.name.equals("STWo") && candidate.category.equals("ST") ){
				return true;
			}else if( quota.name.equals("OBCWo") && candidate.category.equals("OBC") ){
				return true;
			}else if( quota.name.equals("BCAWo") && candidate.specialCategory != null && candidate.specialCategory.equals("BCA") ){
				return true;
			}else if( quota.name.equals("BCBWo") && candidate.specialCategory != null && candidate.specialCategory.equals("BCB") ){
				return true;
			}
		}
		return false;	
	}	

	boolean allocate(Candidate candidate, Course course, Quota quota, Integer number){


		if ( isValidQuotaAllocation(  quota, candidate )  &&  quota.allocated < quota.size ){

			if( candidate.isAllocated ){

				int allocatedQuotaPriority = course.getQuotaPriority(candidate.allocatedQuota.name);
                        	int newQuotaPriority = course.getQuotaPriority(quota.name);

				if( (!course.equals(candidate.allocatedCourse) || newQuotaPriority < allocatedQuotaPriority) ){

					candidate.allocatedQuota.free(candidate);
					candidate.allocatedCourse.allocated--;
					candidate.isAllocated = true;
					candidate.allocatedChoice = number;
					candidate.allocatedQuota = quota;
					candidate.isSpecialQuotaAllocated = quota.isSpecial;	
					candidate.isPwDQuotaAllocated = quota.isPwD;
					candidate.allocatedCourse = course;
					quota.allocate(candidate);
					course.allocated++;
					
					Course lastRoundCourse = lastRoundAllocation.get(candidate.applicationId);
					if (lastRoundCourse != null && lastRoundCourse.courseId.equals(course.courseId)) {
						candidate.setStatusId(4);
						candidate.setActionId(3);
					} else if (lastRoundCourse != null) {
						candidate.setStatusId(3);
						candidate.setActionId(3);
					}
					return true;
				}else{
					return false;
				} 
			}else{
				candidate.isAllocated = true;
				candidate.allocatedChoice = number;
				candidate.allocatedQuota = quota;
				candidate.isSpecialQuotaAllocated = quota.isSpecial;	
				candidate.isPwDQuotaAllocated = quota.isPwD;
				candidate.allocatedCourse = course;
				quota.allocate( candidate );	
				course.allocated++;
				candidate.setStatusId(1);
				candidate.setActionId(1);
				return true;
			}
		}
	return false;
	}

	boolean allocate(Candidate candidate, Course course, Integer number){

		for(String quota: priority){

			if( course.quotas.get( quota ) != null ){
				if ( allocate( candidate, course, course.quotas.get( quota ), number ) ){
					return true;
				}
			}
		}
	return false;
	}
	
	boolean isValidMarks(Candidate candidate){
	
		if ( candidate.category.equals("OBC") || candidate.category.equals("SC") || candidate.category.equals("ST") ){
			if( candidate.score < OBCSCSTCuttOff ){
				return false;
			}
		}else if ( candidate.isPwD && candidate.score < PwDCuttOff ){
				return false;
		}else if( candidate.score < URCuttOff){
			return false;
		}
	return true;
	}

	boolean allocate(Candidate candidate){

		if( !isValidMarks(candidate ) ){
			System.err.println(candidate.applicationId+", "+candidate.score+" NO enough to Qualify "+candidate.category+", "+candidate.isPwD+", "+candidate.score);
			return false;
		}
		
		boolean allocated = false;
		int lastChoiceNumber=0;

		Integer allocatedChoiceNumber = candidate.allocatedChoice;
			
		if(allocatedChoiceNumber == null || allocatedChoiceNumber == 0) {
			lastChoiceNumber = candidate.choices.size();
		}else{
			lastChoiceNumber = allocatedChoiceNumber;
		}

		
		for (int number = 1; number <= lastChoiceNumber; number++) {

			String courseId = candidate.choices.get( number );
			if( courses.get( courseId.trim() ) != null ){
				Course course = courses.get( courseId.trim() );

				if( allocate( candidate, course, number ) ){
					allocated = true;
					break;
				}
			}else{
				Integer count = noFoundCourses.get( courseId.trim() );
				if( count == null)
					count = new Integer(0);
				count++;
				noFoundCourses.put(courseId.trim(), count);
			}
		}
		
	return allocated;
	}
	
	
	private void lastRound(String filename, boolean header) {

		// TODO Auto-generated method stub
	
		BufferedReader br = null; 
		int count = 0;
		try{
			br = new BufferedReader(new FileReader( new File(filename) ) );
			String line = null;
			while( (line = br.readLine() ) != null ){
				if( header ){
					header = false;
					continue;
				}
				//application_id,dob,category_id,is_pwd,gender,seat_preference,status_id,has_opted_special_cat,opted_special_cat_id
				//application_id,dob,category_id,is_pwd,gender,score,air,seat_preference,status_id,has_opted_special_cat,opted_special_cat_id
				String []token = line.trim().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				//application_id,seat_allotted,quota,choice_no,round,action_id
				String allocatedApplicationId= token[0];
				String allocatedSeat= token[1];
				String allocatedQuota= token[2];
				try{

				Candidate allocatedCandidate = candidates.stream().filter(candidate ->candidate.applicationId.equals(allocatedApplicationId)). findFirst().get();
				
				Course course = courses.get( allocatedSeat );
				Quota quota = course.quotas.get(allocatedQuota);
				Integer choiceNo = allocatedCandidate.choices.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), allocatedSeat)).map(Map.Entry::getKey).findFirst().get();

				allocate(allocatedCandidate,course,quota,choiceNo);
				allocatedCandidate.setStatusId(4);
				allocatedCandidate.setActionId(3);
				lastRoundAllocation.put(allocatedApplicationId, course);
				count++;
				}
				catch(Exception e){
					e.printStackTrace();
					System.err.println("Choice not found  "+allocatedApplicationId+":"+allocatedSeat);
				}
			}
			System.out.println("Last Round Allocated "+count);
		}catch(Exception e){
			System.err.println("lastRound allocation error at "+count+" candidate");
			e.printStackTrace();
		}
	}

	void allocate(){

		Collections.sort( candidates, new CandidateSort() );
		boolean allocation= true;
		int iteration = 0;
		for (int i = 0; i < candidates.size(); i++) {
			allocate( candidates.get(i) );
		}
		for(String course: noFoundCourses.keySet() ){
			System.err.println(course+" <> "+noFoundCourses.get( course ) );
		}
	}

	
	void allocattionVerification(){
			
		for(Candidate candidate: candidates){
			Integer allocatedChoice = candidate.allocatedChoice;
			if(allocatedChoice  == null){
				verifiyChoices(1,candidate.choices.size(),candidate);
			}else
				verifiyChoices(1,allocatedChoice,candidate);
			
		}	
		
		System.out.println("Verification Done: ");
	}
	private void verifiyChoices(int lIndex, int uIndex, Candidate candidate) {

		if( !isValidMarks(candidate ) ){
			System.err.println(candidate.applicationId+", "+candidate.score+" NO enough to Qualify "+candidate.category+", "+candidate.isPwD+", "+candidate.score);
			return;
		}

		for(int i=lIndex ;i <uIndex;i++){
			String courseId = candidate.choices.get( i );
			Course course = courses.get( courseId );
			if( course != null ){
				for(String key: priority){
					Quota quota = course.quotas.get( key );
					if( isValidQuotaAllocation(quota, candidate ) ){
						if( quota.closingRank >= candidate.rank ){
							System.out.println("Rank Violation for: "+course.courseId+", Quota: "+quota.name+", closing candidate "+quota.getClosingCandidate().applicationId+", closing-rank:"+quota.closingRank+", Applicant Rank: "+candidate.rank+" Applicant: "+candidate.applicationId)	;

						}	
					}
				}
			}
		}
	}

	void printCourse(){
		boolean isFirst = true;
		for(String courseId: courses.keySet() ){
			if( isFirst ){
				courses.get( courseId ).header();
				isFirst = false;
			}
			courses.get( courseId ).print();
		}	
	}

	void printCourseStatus(){
		System.out.println("-------------------- Course Status -------------------");
		boolean isFirst = true;
		for(String courseId: courses.keySet() ){
			if( isFirst ){
				courses.get( courseId ).headerCourseStatus();
				isFirst = false;
			}
			courses.get( courseId ).printStatus();
		}	
	}
	
	void printCourseSeatMatrix(){
		System.out.println("-------------------- Course Seat Matrix -------------------");
		boolean isFirst = true;
		for(String courseId: courses.keySet() ){
			if( isFirst ){
				courses.get( courseId ).headerCourseSeatMatrix();
				isFirst = false;
			}
			courses.get( courseId ).printSeatMatrixStatus();
		}	
	}
	
	void printNotAllocatedCourseStatus(){
		System.out.println("-------------------- Not Allocated Course Status -------------------");
		boolean isFirst = true;
		for(String courseId: courses.keySet() ){
			if( isFirst ){
				courses.get( courseId ).headerCourseStatus();
				isFirst = false;
			}
			courses.get( courseId ).printNotAllocatedStatus();;
		}	
	}
	
	void printOpeningClosingRankStatus(){
		System.out.println("-------------------- Not Allocated Course Status -------------------");
		boolean isFirst = true;
		for(String courseId: courses.keySet() ){
			if( isFirst ){
				courses.get( courseId ).headerCourseOpeningClosingRankStatus();
				isFirst = false;
			}
			courses.get( courseId ).printOpeningClosingRankStatus();
		}	
	}
	void printOpeningClosingRankStatus(String applicationId){
		System.out.println("-------------------- Not Allocated Course Status for "+applicationId+" -------------------");
		
		Candidate candidateCheck = candidates.stream().filter(candidate ->candidate.applicationId.equals(applicationId)). findFirst().get();
		
		boolean isFirst = true;
		for (int i = 1; i <= candidateCheck.choices.size(); i++) {
			if( isFirst ){
				courses.get( candidateCheck.choices.get(i) ).headerCourseOpeningClosingRankStatus();
				isFirst = false;
			}
			courses.get( candidateCheck.choices.get(i) ).printOpeningClosingRankStatus();
		}
		
	}
	
	void tableView(String round){
		System.out.println("---------- Allocated ---------");
		Candidate.printAllocationTableHeader();
		for(Candidate candidate: candidates){
			if( candidate.isAllocated ){
				candidate.printAllocationTable( round );
			}
		}
	}
	
	private void printDNBView() {

		System.out.println("---------- printDNBView ---------");
		Candidate.printAllocationDNBViewHeader();
		for(Candidate candidate: candidates){
			if( candidate.isAllocated ){
				candidate.printDNBiew( );
			}
		}
	}

	void print(){
		System.out.println("---------- Allocated ---------");
		Candidate.allocHeader();	
		for(Candidate candidate: candidates){
			if( candidate.isAllocated )
				candidate.printAllocation();
		}
		System.out.println("---------- Not Allocated ---------");
		for(Candidate candidate: candidates){
			if( !candidate.isAllocated )
				candidate.printNotAllocation();
		}
		//printCourse();
	}

	public static void main(String[] args){

		try{
			String courseFile = null;
			String candidateFile = null;
			String LastRoundFile = null;
			String round = null;
			boolean tableView = false;
			boolean DNBView = false;
			boolean courseStatus= false;
			boolean remaningSeatMatrix = false;
			boolean stats= false;
			boolean applicantDetail= false;
			String applicationId = null;
			
			int i = 0;
			while( i < args.length ){
				if( args[i].equals("-c") || args[i].equals("-C") ){
					courseFile = args[i+1].trim();
					i++;
				}else if( args[i].equals("-a") || args[i].equals("-A") ){
					candidateFile = args[i+1].trim();
					i++;
				}else if( args[i].equals("-r") || args[i].equals("-R") ){
					round = args[i+1].trim();
					i++;
				}else if( args[i].equals("-lr") || args[i].equals("-LR") ){
					LastRoundFile = args[i+1].trim();
					i++;
				}else if( args[i].equals("-tv") || args[i].equals("-tv") ){
					tableView = true;
				}
				else if( args[i].equals("-dnb") || args[i].equals("-DNB") ){
					DNBView = true;
				}
				else if( args[i].equals("-cs") || args[i].equals("-CS") ){
					courseStatus = true;
				}
				else if( args[i].equals("-stats") || args[i].equals("-STATS") ){
					stats = true;
				}
				
				else if( args[i].equals("-applicant") || args[i].equals("-APPLICANT") ){
					applicantDetail= true;
					applicationId = args[i+1].trim();
				}
				else if( args[i].equals("-rs") || args[i].equals("-rs") ){
					remaningSeatMatrix = true;
				}
				
			i++;
			}			
			if( courseFile == null || candidateFile == null || round == null ){
				
				System.err.println("Course File , Candidate File and Round value are compulsory to run the program. Please provide the same");
				
				optionsOfTheProgram();
				
				
				System.exit(0);
			}

			Allocator allocator = new Allocator();
			allocator.readCourses(courseFile, true);
			allocator.readCandidate(candidateFile, true);
			
			if(LastRoundFile != null)
			{
				allocator.lastRound(LastRoundFile,true);
				if(remaningSeatMatrix)
					allocator.printCourseSeatMatrix();
			}
			
			
			allocator.allocate();
			allocator.allocattionVerification();
			
			System.out.println("Total Seats: "+totalSeats);
			System.out.println("Total Allocated: "+allocated);
			
			//allocator.print();
			
			
			/*
			 * Applicant Choices Details View
			 * Not Allocated Course Status for applicant.
			 * Prints opening closing of the course he opted.
			*/
			if(applicantDetail){
				allocator.printOpeningClosingRankStatus(applicationId);
			}
			
			
			
			///////////////////////////////////////////////

			
			
			
			/*
			 * Opening Closing Rank Details of each course.
			*/
			if(stats)
			{
				allocator.printNotAllocatedCourseStatus();
				allocator.pritntNotAllocatedCandidates();
				allocator.printOpeningClosingRankStatus();
			}
			
			
			///////////////////////////////////////////////
			
			
			/*
			 * Course Status View
			 * 	Prints status of each course with Quota wise capacity and allocated count.
			*/
			if(courseStatus)
			{
				allocator.printCourseStatus();	
			}
			///////////////////////////////////////////////
			
			
			/*DNB View
			 * 	Print the DNB specified view of the applicant
			 * 	It gives all the information about allocated candidate like allocated course, quota etc
			*/
			if(DNBView)
			{
				allocator.printDNBView();
			}
			///////////////////////////////////////////////
			
			
			
			/*Database Table View
			 *Prints Database table CSV with appropriate status and action id for provided round 
			*/
			if(tableView)
			{
				allocator.tableView( round );
				
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public static void optionsOfTheProgram() {
		System.out.println("Following are the options of the Allocation Application : ");
		System.out.println("-[c,C] <courseFile>  : Course seat matrix file path");
		System.out.println("-[a,A] <applicantFile> : Applicant data with choices file path");
		System.out.println("-r [roundVal] : round value");
		System.out.println("-lr <LastRounFile> : Last Round details file path");
		System.out.println("-rs : Remaning seat matrix after last round allocation (-lr is compulsory for this view)");
		System.out.println("-stats (stats of Not allocated course,not allocated candidate,opening closing Rank)");
		System.out.println("-cs (Course Status View)");
		System.out.println("-dnb (DNB-specified view analysis)");
		System.out.println("-tv (Database Table View analysis)");
		System.out.println("-applicant <application Id> (Applicant Choices Details View)");
		
	}

	private void pritntNotAllocatedCandidates() {
		// TODO Auto-generated method stub
		for(Candidate candidate: candidates){
			if(! candidate.isAllocated ){
				candidate.print();
			}
		}
	}
	
}
