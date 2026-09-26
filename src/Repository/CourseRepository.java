package Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository {

    private static final String FILE_PATH = "data/Course.csv";

    // ดึงรายวิชาทั้งหมดในรูปแบบ "CourseID - CourseName"
    public List<String> getAllCourses() {
        List<String> courseList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // ข้าม Header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);

                if (data.length >= 2) {
                    String courseId = data[0].trim();
                    String courseName = data[1].trim();

                    if (!courseId.isEmpty()) {
                        courseList.add(courseId + " - " + courseName);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Cannot read Course.csv: " + FILE_PATH);
            e.printStackTrace();
        }

        return courseList;
    }

    // รับ CourseID แล้วคืนชื่อวิชา
    public String getCourseNameById(String courseId) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // ข้าม Header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);

                if (data.length >= 2 && data[0].trim().equals(courseId.trim())) {
                    return data[1].trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Unknown Course";
    }

    // รับ CourseID แล้วคืนจำนวนหน่วยกิต
    public int getCourseCreditById(String courseId) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // ข้าม Header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);

                if (data.length >= 3 && data[0].trim().equals(courseId.trim())) {
                    return Integer.parseInt(data[2].trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
