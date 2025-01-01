package com.badis.gertakess.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Vente {

    private int idVente;
    private LocalDate dateVente;
    Employee employe;
    private List<Panier> paniers;
    public Vente(){

    }
    public Vente(LocalDate dateVente, Employee employe, List<Panier> paniers){
        this.dateVente = dateVente;
        this.employe = employe;
        this.paniers = paniers;
    }
    public Vente(int idVente, LocalDate dateVente, Employee employe, List<Panier> paniers) {
        this.idVente = idVente;
        this.dateVente = dateVente;
        this.employe = employe;
        this.paniers = paniers;
    }

    public int getIdVente() {
        return idVente;
    }

    public void setIdVente(int idVente) {
        this.idVente = idVente;
    }

    public LocalDate getDateVente() {
        return dateVente;
    }

    public void setDateVente(LocalDate dateVente) {
        this.dateVente = dateVente;
    }

    public Employee getEmploye() {
        return employe;
    }

    public void setEmploye(Employee employe) {
        this.employe = employe;
    }

    public List<Panier> getPaniers() {
        return paniers;
    }

    public void setPaniers(List<Panier> paniers) {
        this.paniers = paniers;
    }
}
