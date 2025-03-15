package student_mangement_system;

public class Student {
	private String name;
	private int rollNo;
	private String course;
	private int marks;
	
	
	public Student(int rollNo, String name, String course, int marks) {
		this.name = name;
		this.rollNo = rollNo;
		this.course = course;
		this.marks = marks;
	}
	
	public String getName() {
		return name;
	}
	
	int getRollNo () {
		return rollNo;
	}
	
	public String getCourse() {
		return course;
	}
	
	int getMarks() {
		return marks;
	}
	
	
	// Setters to update values
	public void setName(String name) {
		this.name = name;
	}
	
	public void setRollNo(int rollNo) {
		this.rollNo = rollNo;
	}
	
	public void setCourse(String course) {
		this.course = course;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
}
