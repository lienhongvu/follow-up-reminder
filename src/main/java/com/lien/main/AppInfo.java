package com.lien.main;

import java.util.List;

public class AppInfo {
    private String name;
    private String lastNotify;
    private String status;
    private List<String> patients;
    private String lastFollowUp;

    public AppInfo() {
    }

    public AppInfo(String name, String lastNotify, String status, List<String> patients, String lastFollowUp) {
        this.name = name;
        this.lastNotify = lastNotify;
        this.status = status;
        this.patients = patients;
        this.lastFollowUp = lastFollowUp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastNotify() {
        return lastNotify;
    }

    public void setLastNotify(String lastNotify) {
        this.lastNotify = lastNotify;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getPatients() {
        return patients;
    }

    public void setPatients(List<String> patients) {
        this.patients = patients;
    }

    public String getLastFollowUp() {
        return lastFollowUp;
    }

    public void setLastFollowUp(String lastFollowUp) {
        this.lastFollowUp = lastFollowUp;
    }
}
