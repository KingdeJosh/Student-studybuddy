package com.example.akujobijoshua.StudentBuddy.library;

/**
 * Created by Akujobi Joshua on 4/3/2017.
 */

public class courses{
    private String CourseId;
    private String CourseName;
    private String Lecturer;
    private String LecturerEmail;
    private String LecturerContact;
    public courses(){

    }
    public courses(String id, String name, String Lecturer, String LecturerEmail, String LecturerContact) {
        this.CourseId = id;
        this.CourseName = name;
        this.Lecturer = Lecturer;
        this.LecturerEmail = LecturerEmail;
        this.LecturerContact = LecturerContact;
    }
    public String getCourseId() {
        return CourseId;
    }

    public void setCourseId(String CourseId) {
        this.CourseId = CourseId;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String CourseName) {
        this.CourseName = CourseName;
    }


    public String getLecturerContact() {
        return LecturerContact;
    }

    public void setLecturerContact(String LecturerContact) {
        this.LecturerContact = LecturerContact;
    }


    public String getLecturerEmail() {
        return LecturerEmail;
    }

    public void setLecturerEmail(String LecturerEmail) {
        this.LecturerEmail = LecturerEmail;
    }

    public String getLecturer() {
        return Lecturer;
    }

    public void setLecturer(String Lecturer) {
        this.Lecturer = Lecturer;
    }
}

