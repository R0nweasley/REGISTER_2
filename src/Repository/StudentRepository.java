package Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import model.Student;

public class StudentRepository {

    private  static  String  FILE_PATH ="data/Student.csv";

    /*
    READ FILE STUDENT
    CHECK ID PASS
    RETURN current STUDENT 


    */
    // LOGIN // 
   public Student loginAndGetStudent(String studentId, String password) {
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            br.readLine(); // skip header
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // เช็กว่ามีข้อมูลครบ 4 ช่อง (ID, Name, Email, Password) ตามโครงสร้าง
                if (data.length >= 4) {
                    if (data[0].equals(studentId) && data[3].equals(password)) {
                        // ถ้าล็อกอินถูก สร้าง Object Student แล้วใส่ ID, Name, Email ลงไป
                        // index 0=ID, 1=Name, 2=Email
                        return new Student(data[0], data[1], data[2]); 
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // ล็อกอินไม่สำเร็จ
    }

    /*
    WRITE FILE STUDENT WHEN REGISTER
    GOTO FILE PATH WIRTE TEXT FROM LABEL.GETTEXT
    TO FILE
    StudentiD,  Name    ,EMail,  Password
    */
    // REGISTER //
    // REGISTER //
    public boolean register(String studentId, String name, String email, String password) {
        // ใช้ FileWriter พร้อมกำหนดค่า append เป็น true เพื่อเขียนข้อมูลต่อท้ายไฟล์เดิม
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            // นำข้อมูลมาต่อกันด้วยเครื่องหมายจุลภาค (,) ตามโครงสร้าง StudentID,Name,EMail,Password
            String dataLine = studentId + "," + name + "," + email + "," + password;
            
            // เขียนข้อมูลลงไฟล์พร้อมขึ้นบรรทัดใหม่
            out.println(dataLine);
            
            return true; // บันทึกสำเร็จ
            
        } catch (Exception e) {
            e.printStackTrace();
            return false; // เกิดข้อผิดพลาดในการบันทึก
        }
    }



}
