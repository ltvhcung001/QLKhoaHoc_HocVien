package ra.qlkhoahochocvien.model;

import java.util.Date;

public class Course {
    private int id;
    private String name;
    private int duration;
    private String instructor;
    private Date create_at;

    public Course(int id,
                  String name,
                  int duration,
                  String instructor,
                  Date create_at) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.instructor = instructor;
        this.create_at = create_at;
    }
}
