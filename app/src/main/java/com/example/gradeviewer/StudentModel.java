package com.example.gradeviewer;

public class StudentModel {
    String name,studentid, studentSubject,studenStCode ,uid;

    public StudentModel() {

    }

    public StudentModel(String name, String studentid, String studentSubject, String studenStCode, String uid) {
        this.name = name;
        this.studentid = studentid;
        this.studentSubject = studentSubject;
        this.studenStCode = studenStCode;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getStudentSubject() {
        return studentSubject;
    }

    public void setStudentSubject(String studentSubject) {
        this.studentSubject = studentSubject;
    }

    public String getStudenStCode() {
        return studenStCode;
    }

    public void setStudenStCode(String studenStCode) {
        this.studenStCode = studenStCode;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
