package cdac.in.nbe;

import java.util.List;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Quota{

	String name;
	Integer size;
	Integer allocated;
	

	Integer superNumeri;
	double closingRank;  
	double openingRank;  
	boolean isSpecial;
	boolean isPwD;
	List<Candidate> candidates;

	Quota(String name, Integer size, boolean isSpecial, boolean isPwD){
		this.name = name;
		this.size = size;
		this.allocated = 0;
		this.superNumeri = 0;
		this.closingRank = 0;
		this.candidates = new ArrayList<Candidate>();
		this.isSpecial = isSpecial;
		this.isPwD = isPwD;
	}
	
	void allocate(Candidate candidate){

		this.candidates.add( candidate );
		this.updateOpeningClosingRank();
		this.allocated++;
		Allocator.allocated++;
		if( this.allocated > this.size)
			this.superNumeri++;
	}
	void free(Candidate candidate){

		this.candidates.remove(candidate);
		this.allocated--;
		Allocator.allocated--;
		this.updateOpeningClosingRank();
		System.out.println("candidate freed seat  : "+candidate.applicationId+":"+ candidate.rank+" : "+candidate.allocatedCourse.courseId);
	}
	public String getStatus(){
		return size+","+allocated;
	}
	
	

	void updateOpeningClosingRank(){

		if( candidates.size() > 0){
			
			Collections.sort( candidates,  new CandidateSort() );
			
			this.openingRank = candidates.get(0).rank;
			this.closingRank = candidates.get( candidates.size() - 1).rank;
			

		}else{

			this.openingRank = 0;
			this.closingRank = 0;
			
		}
	}
	Candidate getClosingCandidate(){
		return candidates.get( candidates.size() - 1);
	}
	String getOpeningClosingRank(){
		return this.openingRank+","+this.closingRank;
	}
	
	int getRemainingSeat(){
	 return size-allocated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Quota other = (Quota) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
