package com.example.gradeviewer;

public class StudentListModel {
    String studentName,studentId,studentCode;
    public StudentListModel() {
    }

    public StudentListModel(String studentName, String studentId, String studentCode) {
        this.studentName = studentName;
        this.studentId = studentId;
        this.studentCode = studentCode;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }
}
