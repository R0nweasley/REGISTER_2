package Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentRepository {

    // กำหนดที่อยู่ไฟล์ (เปลี่ยน Path ให้ตรงกับโฟลเดอร์ data ของคุณ)
    private static final String FILE_PATH = "data/Enrollment.csv";


    

    // ==========================================
    // 1. ฟังก์ชัน: ลงทะเบียนเรียน (เพิ่มข้อมูลต่อท้ายไฟล์)
    // ==========================================
    public boolean enrollCourse(String studentId, String courseId) {
        // ใช้ FileWriter(..., true) เพื่อเขียนต่อท้ายไฟล์ (Append)
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            // สร้าง EnrollmentID จำลองด้วยเวลาปัจจุบัน (หรือจะรันเลขเองก็ได้)
            String enrollId = "ENR" + System.currentTimeMillis();
            
            // เขียนข้อมูลลงไฟล์
            out.println(enrollId + "," + studentId + "," + courseId + ",ENROLLED");
            return true; // บันทึกสำเร็จ
            
        } catch (Exception e) {
            e.printStackTrace();
            return false; // บันทึกไม่สำเร็จ
        }
    }

    // ==========================================
    // 2. ฟังก์ชัน: ถอนรายวิชา (ลบข้อมูลบรรทัดนั้นออกไปเลย)
    // ==========================================
    public boolean withdrawCourse(String studentId, String courseId) {
        List<String> linesToKeep = new ArrayList<>();
        boolean isDeleted = false;
        
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        // 2.1 อ่านข้อมูลทั้งหมด และคัดกรองบรรทัดที่ไม่ต้องการลบเก็บไว้ใน List
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String header = br.readLine();
            if (header != null) linesToKeep.add(header); // เก็บ Header ไว้เสมอ

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // เช็กว่าเป็น ID นักศึกษาและ ID วิชานี้หรือไม่
                if (data.length >= 4 && data[1].equals(studentId) && data[2].equals(courseId)) {
                    // ถ้า "เจอ" -> ไม่ต้องเพิ่มลง List (เสมือนการลบทิ้ง)
                    isDeleted = true; 
                } else {
                    // ถ้า "ไม่ใช่" -> เก็บไว้เหมือนเดิม
                    linesToKeep.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // 2.2 ถ้ามีการค้นหาเจอและลบออกไป ค่อยเขียนข้อมูลใหม่ทับไฟล์เดิมทั้งหมด
        if (isDeleted) {
            // ใช้ FileWriter(..., false) เพื่อเขียนทับ (Overwrite)
            try (PrintWriter out = new PrintWriter(new FileWriter(file, false))) {
                for (String lineToWrite : linesToKeep) {
                    out.println(lineToWrite);
                }
                return true; // ถอนสำเร็จ
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return false; // หาวิชาที่ต้องการถอนไม่เจอ
    }

    // ==========================================
    // 3. ฟังก์ชัน: ดึงรหัสวิชาทั้งหมดที่นักศึกษาคนนี้ลงทะเบียนไว้
    // ==========================================
    public List<String> getEnrolledCourseIds(String studentId) {
        List<String> myCourses = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // ข้าม Header
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                
                // เช็กว่า StudentID ตรงกับคนที่ล็อกอินหรือไม่ และต้องสถานะ ENROLLED
                if (data.length >= 4 && data[1].equals(studentId) && data[3].equals("ENROLLED")) {
                    myCourses.add(data[2]); // เก็บ CourseID (ช่องที่ 3 หรือ index 2) ใส่ List
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return myCourses; // คืนค่ารายการรหัสวิชาทั้งหมดกลับไป
    }





}
