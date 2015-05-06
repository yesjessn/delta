///////////////////////
// File:  DStudent.java
// Class: DStudent
//
// Description:
//	DStudent creates a database that maps the set of students to the set of trials.
//	A student can undergo multiple trials, but a trial can only have a single student.
//
//	DStudent is succinctly represented in mathematical notation as
//		DStudent: TRIAL --> STUDENT
//	where DStudent is a surjective function.
//
//	The csv file created by DStudent have the following format:
//		1: Student
//		2: Trial
//
// Public methods:
//	void addPairing(String student, String trial)
//		adds an entry of the form (student, trial).
//
// Usage:
//	Simple example
//	|	DStudent ds = new DStudent();
//	|	ds.addPairing("Stormtrooper", "TK421");
//	|	ds.addPairing("Preschooler", "1234");
//	|	ds.writeFile("Student.csv");
//

package delta.database;

public class DStudent extends DFile {
	// TODO: add a set of trials to ensure that one trial does not map to multiple students

	////////////////////////////////////////////////////
	public void addPairing(String student, String trial) {
	// adds a paring of student and trial,
	// represented mathematically as a 2-tuple (student, trial).
		String[] entry = {student, trial};
		this.addEntry(entry);
	} // public void addPairing(String student, String trial)
} // public class DStudent extends DFile
