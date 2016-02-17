import java.sql.*;
import java.io.*;
import java.util.Enumeration;

public class university_program {
	public static void main(String args[]) throws SQLException {
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch(ClassNotFoundException e) {
			System.out.println("Could not load driver");
		}

		String user, pass;
		user = readEntry("userid : ");
		pass = readEntry("password: ");
		

		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@csoracle.utdallas.edu:1521:student", user,pass);
		boolean flag = false;
		while(flag == false){
		//Check if student data exists
		String stu_no = readEntry("\n\nEnter student number : ");
		String query = "SELECT Name FROM STUDENT WHERE Student_number = " + stu_no;
		Statement s = conn.createStatement();
		ResultSet r = s.executeQuery(query);

		if ( r.next()) {
			System.out.println("Student data found");
		}
		else {
			System.out.println("Student not found. Please try again");
		}
		s.close();

		System.out.println("\n\n\n\n\n------------------------Enrollment for Spring 2007--------------------------");
		System.out.println("1) Add a class");
		System.out.println("2) Drop a class");
		System.out.println("3) See my schedule");
		String ch = readEntry("Enter your choice : ");

		switch(ch.charAt(0))
		{
			case '1':
				System.out.println("\n\n--------Add a class--------");
				addCourse(conn, stu_no);
				break;
			case '2':
				System.out.println("\n\n--------Drop a class--------");
				dropCourse(conn,stu_no);
				break;
			case '3':
				System.out.println("\n\n--------Your Schedule--------");
				viewSchedule(conn, stu_no);
				break;
			default:
				flag = true;
		}
		}
		conn.close();
}
	static String readEntry(String prompt) {
		try {
			StringBuffer buffer = new StringBuffer();
			System.out.print(prompt);
			System.out.flush();
			int c = System.in.read();
			while(c != '\n' && c != -1) {
				buffer.append((char)c);
				c = System.in.read();
			}
			return buffer.toString().trim();
		} catch (IOException e) {
			return "";
		}
	}
	private static void addCourse(Connection conn, String stu_no) throws SQLException {
		String cno = readEntry("Course number : ");
		String sno = readEntry("Section number : ");

		String course_query = "SELECT Section_identifier FROM SECTION WHERE Section_identifier=" + sno + " AND Course_number='" + cno + "' AND Semester='Spring' AND YEAR=TO_DATE('2007','YYYY')";
		//System.out.println(course_query);
		Statement s1= conn.createStatement();
		ResultSet r1 = s1.executeQuery(course_query); 
		if(r1.next()){
			String addc = "INSERT INTO GRADE_REPORT (Student_number, Section_identifier, Grade) VALUES (" + stu_no + ", "+ sno + ", 'NULL')";
			Statement s2= conn.createStatement();
			System.out.println(addc);
			try{
				s2.executeUpdate(addc);
				System.out.println("Enrollment successfull");
			} catch (SQLException e) {
				System.out.println("Cannot add course into table");
			}
		}
		else {
			System.out.println("Course not found");
		}
		s1.close();
	}
	private static void dropCourse(Connection conn, String stu_no) throws SQLException {
		String cno = readEntry("Course number: ");
		String sno = readEntry("Section number : ");
		
		String chks = "SELECT g.Section_identifier FROM SECTION s, GRADE_REPORT g where s.Section_identifier=g.Section_identifier AND s.Course_number='" + cno + "' AND g.Section_identifier=" + sno + " AND s.Semester='Spring' AND s.Year=TO_DATE('2007','YYYY') AND g.Student_number=" + stu_no;
		Statement s3 = conn.createStatement();
		ResultSet r3 = s3.executeQuery(chks);

		if(r3.next()) {
			String del = "DELETE FROM GRADE_REPORT WHERE Student_number=" + stu_no + " AND Section_identifier=" + sno;
			Statement s4 = conn.createStatement();
			try{
				s4.executeUpdate(del);
				System.out.println("Course dropped");
			} catch (SQLException e) {
				System.out.println("Cannot drop a course from table");
			}
		}
		else {
			System.out.println("Enrollment data not found. Check the input and try again.");
		}
	}
	private static void viewSchedule(Connection conn, String stu_no) throws SQLException {
		String shd = "SELECT s.Course_number, s.Section_identifier, c.Course_name, s.Instructor FROM COURSE c, SECTION s, GRADE_REPORT t WHERE c.Course_number=s.Course_number AND s.Section_identifier=t.Section_identifier AND s.Semester='Spring' AND s.Year=TO_DATE('2007','YYYY') AND t.Student_number=" + stu_no;
		Statement s5 = conn.createStatement();
		ResultSet r4 = s5.executeQuery(shd);
		System.out.println("\n\nYour Spring 2007 class schedule");
		while(r4.next()){
			String cnum = r4.getString(1);
			int secnum = r4.getInt(2);
			String cname = r4.getString(3);
			String inst = r4.getString(4);

			System.out.println(cnum + " " + cname + " Section:" + secnum + " Instructor: " + inst);
		}
	}
}
