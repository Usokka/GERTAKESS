package com.badis.gertakess.model;

import java.time.LocalDate;

public class Contrat {

    private int idContr;
    private String typeContr;
    private LocalDate dateDebContr;
    private LocalDate dateFinContr;
    private LocalDate dateQuitEmploi;
    private String posteContr;
    private double salaireJourContr;
    private Employee employeContr;

    public Contrat(){

    }
    public Contrat(String typeContr, LocalDate dateDebContr, LocalDate dateFinContr, LocalDate dateQuitEmploi, String posteContr, double salaireJourContr, Employee employeContr) {
        this.typeContr = typeContr;
        this.dateDebContr = dateDebContr;
        this.dateFinContr = dateFinContr;
        this.dateQuitEmploi = dateQuitEmploi;
        this.posteContr = posteContr;
        this.salaireJourContr = salaireJourContr;
        this.employeContr = employeContr;
    }

    public Contrat(int idContr, String typeContr, LocalDate dateDebContr, LocalDate dateFinContr, LocalDate dateQuitEmploi, String posteContr, double salaireJourContr, Employee employeContr) {
        this.idContr = idContr;
        this.typeContr = typeContr;
        this.dateDebContr = dateDebContr;
        this.dateFinContr = dateFinContr;
        this.dateQuitEmploi = dateQuitEmploi;
        this.posteContr = posteContr;
        this.salaireJourContr = salaireJourContr;
        this.employeContr = employeContr;
    }
    public int getIdContr() {
        return idContr;
    }

    public void setIdContr(int idContr) {
        this.idContr = idContr;
    }

    public String getTypeContr() {
        return typeContr;
    }

    public void setTypeContr(String typeContr) {
        this.typeContr = typeContr;
    }

    public LocalDate getDateDebContr() {
        return dateDebContr;
    }

    public void setDateDebContr(LocalDate dateDebContr) {
        this.dateDebContr = dateDebContr;
    }

    public LocalDate getDateFinContr() {
        return dateFinContr;
    }

    public void setDateFinContr(LocalDate dateFinContr) {
        this.dateFinContr = dateFinContr;
    }

    public LocalDate getDateQuitEmploi() {
        return dateQuitEmploi;
    }

    public void setDateQuitEmploi(LocalDate dateQuitEmploi) {
        this.dateQuitEmploi = dateQuitEmploi;
    }

    public String getPosteContr() {
        return posteContr;
    }

    public void setPosteContr(String posteContr) {
        this.posteContr = posteContr;
    }

    public double getSalaireJourContr() {
        return salaireJourContr;
    }

    public void setSalaireJourContr(double salaireJourContr) {
        this.salaireJourContr = salaireJourContr;
    }

    public Employee getEmployeContr() {
        return employeContr;
    }

    public void setEmployeContr(Employee employeContr) {
        this.employeContr = employeContr;
    }
}
