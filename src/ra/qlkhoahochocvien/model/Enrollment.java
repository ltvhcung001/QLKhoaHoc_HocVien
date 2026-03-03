package ra.qlkhoahochocvien.model;

import java.time.LocalDateTime;

public class Enrollment {
    private int id;
    private int student_id;
    private int course_id;
    private LocalDateTime registered_at;
    private StatusType status;

    public Enrollment(int id,
                      int student_id,
                      int course_id,
                      LocalDateTime registered_at,
                      StatusType status) {
        this.id = id;
        this.student_id = student_id;
        this.course_id = course_id;
        this.registered_at = registered_at;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public LocalDateTime getRegistered_at() {
        return registered_at;
    }

    public void setRegistered_at(LocalDateTime registered_at) {
        this.registered_at = registered_at;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }


}
