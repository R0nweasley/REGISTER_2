package model;

public class Student {

    public String name;
    public String ID;
    public  String email;

    public Student(String ID, String name,String email){
        this.ID = ID;
        this.name = name;
        this.email = email;
    }

    public String getEmail() { return email;}
    public String getID()   { return ID; }
    public String getName() {  return name; }

}
