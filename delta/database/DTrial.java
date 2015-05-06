/////////////////////
// File:  DTrial.java
// Class: DTrial
//
// Description:
//	DTrial records the decisions made by a student during a trial.
//	It contains no information on the identity
//	of the student -- this is the job of DStudent.
//
//	The csv files created by DTrial have the following format:
//		1: Event ID
//		2: Wait period
//		3: Decision
//		4: Elapsed time in milliseconds
//	The Event ID is necessary because DFile automatically sorts the rows.
//
// Public methods:
//	void recordDelay(int elapsed)
//		records a delay decision made by student.
//	void recordNow(int elapsed)
//		records a immediate decision made by student.
//	void setWait(int wait)
//		sets the waiting period for the delay decision.
//
// Usage:
//	Sample trial
//	|	DTrial dt = new DTrial();
//	|	dt.setWait(5);
//	|
//	|	// Assume that getTime() is a method
//	|	// that returns the elapsed time
//	|	dt.recordNow  (getTime());
//	|	dt.recordDelay(getTime());
//	|	dt.recordDelay(getTime());
//	|	dt.recordNow  (getTime());
//	|	dt.recordDelay(getTime());
//	|
//	|	dt.writeFile("TK421.csv");
//

package delta.database;

public class DTrial extends DFile {
	private int eventId = 0;
	private int waitPeriod = 0;

	////////////////////////////////////
	public void recordDelay(int elapsed) {
	// adds a tuple of the form (eventId, waitPeriod, "DELAY", elapsed).
		String[] entry = {idToString(), String.valueOf(waitPeriod),
			"DELAY", String.valueOf(elapsed)};
		this.addEntry(entry);
	} // public void recordDelay(int elapsed)

	//////////////////////////////////
	public void recordNow(int elapsed) {
	// adds a tuple of the form (eventId, waitPeriod, "NOW").
		String[] entry = {idToString(), String.valueOf(waitPeriod),
			"NOW", String.valueOf(elapsed)};
		this.addEntry(entry);
	} // public void recordNow(int elapsed)

	/////////////////////////////
	public void setWait(int wait) {
	// sets the waiting period to be recorded
	// into the database.
		this.waitPeriod = wait;
	} // public void setWait(int wait)

	///////////////////////////
	private String idToString() {
	// returns a string form of the eventId.
	// The number is padded with zeros to prevent sorting issues.
	// For example, "14" would be sorted before "2",
	// but "02" would be sorted before "14".
		return String.format("%03d", eventId++);
	} // private String idToString()

} // public class DTrial extends DFile
