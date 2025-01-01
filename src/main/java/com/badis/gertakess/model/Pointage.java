package com.badis.gertakess.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Pointage {
    private int idPoint;
    private LocalDate datePoint;
    private LocalTime heureEntreePoint;
    private LocalTime heureSortiePoint;
    private Employee employe;

    public Pointage(){

    }
    public Pointage(LocalDate datePoint, LocalTime heureEntreePoint, LocalTime heureSortiePoint, Employee employe) {
        this.datePoint = datePoint;
        this.heureEntreePoint = heureEntreePoint;
        this.heureSortiePoint = heureSortiePoint;
        this.employe = employe;
    }

    public Pointage(int idPoint, LocalDate datePoint, LocalTime heureEntreePoint, LocalTime heureSortiePoint, Employee employe) {
        this.idPoint = idPoint;
        this.datePoint = datePoint;
        this.heureEntreePoint = heureEntreePoint;
        this.heureSortiePoint = heureSortiePoint;
        this.employe = employe;
    }

    public int getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(int idPoint) {
        this.idPoint = idPoint;
    }

    public LocalDate getDatePoint() {
        return datePoint;
    }

    public void setDatePoint(LocalDate datePoint) {
        this.datePoint = datePoint;
    }

    public LocalTime getHeureEntreePoint() {
        return heureEntreePoint;
    }

    public void setHeureEntreePoint(LocalTime heureEntreePoint) {
        this.heureEntreePoint = heureEntreePoint;
    }

    public LocalTime getHeureSortiePoint() {
        return heureSortiePoint;
    }

    public void setHeureSortiePoint(LocalTime heureSortiePoint) {
        this.heureSortiePoint = heureSortiePoint;
    }

    public Employee getEmploye() {
        return employe;
    }

    public void setEmploye(Employee employe) {
        this.employe = employe;
    }
}
