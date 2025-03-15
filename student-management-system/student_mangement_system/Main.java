package student_mangement_system;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	private static ArrayList<Student> studentList = new ArrayList<Student>();
	static Scanner scan = new Scanner(System.in);
	public static void main(String[] args) {

		// TODO Auto-generated method stub
		System.out.println("===========================================");
		System.out.println("          STUDENT MANAGEMENT SYSTEM        ");
		System.out.println("===========================================\n\n");
		
		outer: while(true) {
			
			System.out.println("1. Add Student  ");
			System.out.println("2. Remove Student");
			System.out.println("3. Update Student");
			System.out.println("4. Search student by Roll No ");
			System.out.println("5. Display all student details ");
			System.out.println("6. Close Application");
			System.out.println();
			System.out.println("Enter the option: ");
			int option;
			try {
			option = scan.nextInt();
			}
			catch(InputMismatchException e) {
				System.out.println("Invalid Input!!\n");
				System.out.println("-------------------------");
				scan.nextLine(); // To prevent infinite loop d
				continue;
			}
			
			switch(option) {
			case 1:
				addStudent();
				break;
			case 2:
				removeStudent();
				break;
			case 3:
				updateStudent();
				break;
			case 4:
				searchStudent();
				break;
			case 5:
				displayAllStudents();
				break;
			case 6:
				break outer;
			default:
				System.out.println("Invalid choice ! try again...");
				System.out.println("-------------------------");
				System.out.println();

				
			}
			
			
			

		}
			System.out.println("Application stopped");

	}
	public static void addStudent() {
		scan.nextLine(); // to deal newline from last enter
		
		System.out.println("Enter name of student: ");
		String name = scan.nextLine();
		
		System.out.println("Enter the Roll no of Student ");
		int rollNo;
		while(true){
			try {
				rollNo = scan.nextInt();
				scan.nextLine();
				break;
			}
			catch(InputMismatchException e) {
				System.out.println("Enter a valid Integer Roll No");
				scan.nextLine();
				continue;
				}
			}
		
		System.out.println("Enter the course: ");
		String course = scan.nextLine();
		
		System.out.println("Enter Marks");
		int marks;
		
		while(true) {
			try {
		marks = scan.nextInt();
		break;
			}
			catch(InputMismatchException e) {
				System.out.println("Enter valid Integer marks");
				continue;
			}
		}
		
		Student student = new Student(rollNo, name, course, marks);// creating student object for new Student
		studentList.add(student);
		System.out.println("Added.. \n");
		System.out.println("-------------------------");
	}
	
	public static void removeStudent() {
		System.out.println("Enter Roll no of Student: ");
		int rollNo;
		while(true){
			try {
				rollNo = scan.nextInt();
				break;
			}
			catch(InputMismatchException e) {
				System.out.println("Enter a valid Integer Roll No");
				scan.nextLine();
				continue;
				}
			}		
		for(int i = 0; i <= studentList.size(); i++) {
			if(studentList.get(i).getRollNo() == rollNo) {
				studentList.remove(i);
				System.out.println("Student removed Successfully");
				System.out.println("-------------------------");
				System.out.println();

				return;
			}
		}
		System.out.println("Student record not found!!");
		System.out.println("-------------------------");
		System.out.println();

	}
	
	
	// Updating student details
	
	public static void updateStudent() {
		System.out.println("Enter Roll no: ");
		int rollNo;
		while(true){
			try {
				rollNo = scan.nextInt();
				break;
			}
			catch(InputMismatchException e) {
				System.out.println("Enter a valid Integer Roll No");
				scan.nextLine();
				continue;
				}
			}		
		scan.nextLine(); // to deal newline from last enter
		
		for(Student student: studentList) {
			if(student.getRollNo() == rollNo) {
				System.out.println("Enter the name to be updated ");
				String name = scan.nextLine();
				
				System.out.println("Enter New Course ");
				String course = scan.nextLine();
				
				System.out.println("Enter marks: ");
				int marks;
				while(true) {
					try {
				marks = scan.nextInt();
				break;
					}
					catch(InputMismatchException e) {
						System.out.println("Enter valid Integer marks");
						continue;
					}
				}
				
				student.setName(name);
				student.setCourse(course);
				student.setMarks(marks);
				System.out.println("Updated....");
				System.out.println("-------------------------");
				System.out.println();
				return;
				
			}
		}
		System.out.println("Record not found!!");
		System.out.println("-------------------------");
		System.out.println();

		
	}
	
	public static void searchStudent() {
		System.out.println("Enter Roll no of the student ");
		int rollNo;
		
		while(true){
			try {
				 rollNo = scan.nextInt();
				break;
			}
			catch(InputMismatchException e) {
				System.out.println("Enter a valid Integer Roll No");
				scan.nextLine();
				continue;
				}
			}		
		
		for(Student student: studentList) {
			if(student.getRollNo() == rollNo) {
				System.out.println();
				System.out.println("Record found ");
				System.out.println("Student Name: " + student.getName());
				System.out.println("Student Roll No: " + student.getRollNo());
				System.out.println("Student Marks: " + student.getMarks());
				System.out.println("course: " + student.getCourse());
				System.out.println("-------------------------");
				System.out.println();
				return;

			}
		}
		System.out.println("Student Not Found!!!!!");
		System.out.println("-------------------------");
		System.out.println();
		
		
	}
	
	public static void displayAllStudents() {
		System.out.println();
		System.out.println("No of Students: " + studentList.size());
		System.out.println();
		
		for(Student student: studentList) {

				System.out.println("Student Name: " + student.getName());
				System.out.println("Student Roll No: " + student.getRollNo());
				System.out.println("Student Marks: " + student.getMarks());
				System.out.println("course: " + student.getCourse());
				System.out.println("-------------------------");
				System.out.println();

			
		}
		
	}

}
