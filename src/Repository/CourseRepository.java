package Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository {
    private static final String FILE_PATH = "data/Course.csv";

    // ฟังก์ชันดึงรายวิชาทั้งหมด (เพื่อเอาไปโชว์ใน JList)
    public List<String> getAllCourses() {
        List<String> courseList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // ข้าม Header
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) {
                    // นำ รหัสวิชา และ ชื่อวิชา มาต่อกันให้สวยงาม
                    // เช่น "WIZ001 - WIZARD"
                    courseList.add(data[0] + " - " + data[1]); 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }
    // ฟังก์ชันนี้รับ CourseID แล้วไปหาว่าวิชานี้ชื่ออะไร (ใช้แสดงในฝั่งขวา)
    public String getCourseNameById(String courseId) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // ข้าม Header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // โครงสร้าง Course.csv: CourseID(0), CourseName(1)
                if (data.length >= 2 && data[0].trim().equals(courseId.trim())) {
                    return data[1].trim(); // เจอแล้ว คืนค่าชื่อวิชากลับไป
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown Course"; // ถ้าหาไม่เจอ
    }
}
