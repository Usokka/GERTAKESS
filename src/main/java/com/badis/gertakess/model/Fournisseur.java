package com.badis.gertakess.model;

import java.time.LocalDate;
import java.util.List;

public class Fournisseur {

    private int idFourn;
    private String nomFourn;
    private String emailFourn;
    private String numTelFourn;
    private String adresseFourn;
    private boolean supprimeFourn;
    private List<Commande> commandes ;
    private String categoriesFourn;

    public Fournisseur(){

    }
    public Fournisseur(String nomFourn, String emailFourn, String numTelFourn, String adresseFourn, boolean supprimeFourn, List<Commande> commandes, String categoriesFourn) {
        this.nomFourn = nomFourn;
        this.emailFourn = emailFourn;
        this.numTelFourn = numTelFourn;
        this.adresseFourn = adresseFourn;
        this.supprimeFourn = supprimeFourn;
        this.commandes = commandes;
        this.categoriesFourn = categoriesFourn;
    }

    public Fournisseur(int idFourn, String nomFourn, String emailFourn, String numTelFourn, String adresseFourn, boolean supprimeFourn, List<Commande> commandes, String categoriesFourn) {
        this.idFourn = idFourn;
        this.nomFourn = nomFourn;
        this.emailFourn = emailFourn;
        this.numTelFourn = numTelFourn;
        this.adresseFourn = adresseFourn;
        this.supprimeFourn = supprimeFourn;
        this.commandes = commandes;
        this.categoriesFourn = categoriesFourn;
    }

    public int getIdFourn() {
        return idFourn;
    }

    public void setIdFourn(int idFourn) {
        this.idFourn = idFourn;
    }

    public String getNomFourn() {
        return nomFourn;
    }

    public void setNomFourn(String nomFourn) {
        this.nomFourn = nomFourn;
    }

    public String getEmailFourn() {
        return emailFourn;
    }

    public void setEmailFourn(String emailFourn) {
        this.emailFourn = emailFourn;
    }

    public String getNumTelFourn() {
        return numTelFourn;
    }

    public void setNumTelFourn(String numTelFourn) {
        this.numTelFourn = numTelFourn;
    }

    public String getAdresseFourn() {
        return adresseFourn;
    }

    public void setAdresseFourn(String adresseFourn) {
        this.adresseFourn = adresseFourn;
    }

    public boolean isSupprimeFourn() {
        return supprimeFourn;
    }

    public void setSupprimeFourn(boolean supprimeFourn) {
        this.supprimeFourn = supprimeFourn;
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
    }

    public String getCategoriesFourn() {
        return categoriesFourn;
    }

    public void setCategoriesFourn(String categoriesFourn) {
        this.categoriesFourn = categoriesFourn;
    }
}
