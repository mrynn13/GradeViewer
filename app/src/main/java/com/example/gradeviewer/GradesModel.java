package com.example.gradeviewer;

public class GradesModel {
    String studentName,subjectCode,subjectName,prelim,midterm,finals;
    public GradesModel(){

    }

    public GradesModel(String studentName,String subjectCode,String subjectName, String prelim, String midterm, String finals) {
        this.studentName = studentName;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.prelim = prelim;
        this.midterm = midterm;
        this.finals = finals;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getPrelim() {
        return prelim;
    }

    public void setPrelim(String prelim) {
        this.prelim = prelim;
    }

    public String getMidterm() {
        return midterm;
    }

    public void setMidterm(String midterm) {
        this.midterm = midterm;
    }

    public String getFinals() {
        return finals;
    }

    public void setFinals(String finals) {
        this.finals = finals;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
}
