package com.example.gradeviewer;

import java.util.Comparator;

public class SubjectModel {

    String subjectName,subjectCode,subjectSched,subjectId;

    public SubjectModel(){

    }

    public SubjectModel(String subjectName, String subjectCode, String subjectSched, String subjectId) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.subjectSched = subjectSched;
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectSched() {
        return subjectSched;
    }

    public void setSubjectSched(String subjectSched) {
        this.subjectSched = subjectSched;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
