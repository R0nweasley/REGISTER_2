package model;

public class Course {

    private String courseID;
    private String name;
    private String credit;
     
    public Course(String courseID, String name, String credit){
        this.courseID = courseID;
        this.name = name;
        this.credit = credit;
    }
    public String getCourseID() {
        return courseID;
    }
    public String getCredit() {
        return credit;
    }
    public String getName() {
        return name;
    }
}
