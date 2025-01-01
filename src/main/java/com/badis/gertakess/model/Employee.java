package com.badis.gertakess.model;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Employee {
    private int idEmpl;
    private String nomEmpl;
    private String prenomEmpl;
    private String sexeEmpl;
    private LocalDate dateNaissEmpl;
    private String adresseEmpl;
    private String numTelEmpl;
    private String emailEmpl;
    private String situationEmpl;
    private String mdpCptEmpl;
    private String permCptEmpl;
    private LocalDate dateCptEmpl;
    private boolean etatCptEmpl;
    private List<Pointage> poitagesEmpl;
    private List<Contrat> contratsEmpl;

    public Employee(){

    }

    public Employee(String nomEmpl, String prenomEmpl, String sexeEmpl, LocalDate dateNaissEmpl,
                    String adresseEmpl, String numTelEmpl, String emailEmpl, String situationEmpl,
                    String mdpCptEmpl, String permCptEmpl, LocalDate dateCptEmpl, boolean etatCptEmpl,
                    List<Pointage> poitagesEmpl, List<Contrat> contratsEmpl) {
        this.nomEmpl = nomEmpl;
        this.prenomEmpl = prenomEmpl;
        this.sexeEmpl = sexeEmpl;
        this.dateNaissEmpl = dateNaissEmpl;
        this.adresseEmpl = adresseEmpl;
        this.numTelEmpl = numTelEmpl;
        this.emailEmpl = emailEmpl;
        this.situationEmpl = situationEmpl;
        this.mdpCptEmpl = mdpCptEmpl;
        this.permCptEmpl = permCptEmpl;
        this.dateCptEmpl = dateCptEmpl;
        this.etatCptEmpl = etatCptEmpl;
        this.poitagesEmpl = poitagesEmpl;
        this.contratsEmpl = contratsEmpl;
    }

    public Employee(int idEmpl, String nomEmpl, String prenomEmpl, String sexeEmpl, LocalDate dateNaissEmpl, String adresseEmpl, String numTelEmpl, String emailEmpl, String situationEmpl, String mdpCptEmpl, String permCptEmpl, LocalDate dateCptEmpl, boolean etatCptEmpl, List<Pointage> poitagesEmpl, List<Contrat> contratsEmpl) {
        this.idEmpl = idEmpl;
        this.nomEmpl = nomEmpl;
        this.prenomEmpl = prenomEmpl;
        this.sexeEmpl = sexeEmpl;
        this.dateNaissEmpl = dateNaissEmpl;
        this.adresseEmpl = adresseEmpl;
        this.numTelEmpl = numTelEmpl;
        this.emailEmpl = emailEmpl;
        this.situationEmpl = situationEmpl;
        this.mdpCptEmpl = mdpCptEmpl;
        this.permCptEmpl = permCptEmpl;
        this.dateCptEmpl = dateCptEmpl;
        this.etatCptEmpl = etatCptEmpl;
        this.poitagesEmpl = poitagesEmpl;
        this.contratsEmpl = contratsEmpl;
    }

    public int getIdEmpl() {
        return idEmpl;
    }

    public void setIdEmpl(int idEmpl) {
        this.idEmpl = idEmpl;
    }

    public String getNomEmpl() {
        return nomEmpl;
    }

    public void setNomEmpl(String nomEmpl) {
        this.nomEmpl = nomEmpl;
    }

    public String getPrenomEmpl() {
        return prenomEmpl;
    }

    public void setPrenomEmpl(String prenomEmpl) {
        this.prenomEmpl = prenomEmpl;
    }

    public String getSexeEmpl() {
        return sexeEmpl;
    }

    public void setSexeEmpl(String sexeEmpl) {
        this.sexeEmpl = sexeEmpl;
    }

    public LocalDate getDateNaissEmpl() {
        return dateNaissEmpl;
    }

    public void setDateNaissEmpl(LocalDate dateNaissEmpl) {
        this.dateNaissEmpl = dateNaissEmpl;
    }

    public String getAdresseEmpl() {
        return adresseEmpl;
    }

    public void setAdresseEmpl(String adresseEmpl) {
        this.adresseEmpl = adresseEmpl;
    }

    public String getNumTelEmpl() {
        return numTelEmpl;
    }

    public void setNumTelEmpl(String numTelEmpl) {
        this.numTelEmpl = numTelEmpl;
    }

    public String getEmailEmpl() {
        return emailEmpl;
    }

    public void setEmailEmpl(String emailEmpl) {
        this.emailEmpl = emailEmpl;
    }

    public String getSituationEmpl() {
        return situationEmpl;
    }

    public void setSituationEmpl(String situationEmpl) {
        this.situationEmpl = situationEmpl;
    }

    public String getMdpCptEmpl() {
        return mdpCptEmpl;
    }

    public void setMdpCptEmpl(String mdpCptEmpl) {
        this.mdpCptEmpl = mdpCptEmpl;
    }

    public String getPermCptEmpl() {
        return permCptEmpl;
    }

    public void setPermCptEmpl(String permCptEmpl) {
        this.permCptEmpl = permCptEmpl;
    }

    public LocalDate getDateCptEmpl() {
        return dateCptEmpl;
    }

    public void setDateCptEmpl(LocalDate dateCptEmpl) {
        this.dateCptEmpl = dateCptEmpl;
    }

    public boolean isEtatCptEmpl() {
        return etatCptEmpl;
    }

    public void setEtatCptEmpl(boolean etatCptEmpl) {
        this.etatCptEmpl = etatCptEmpl;
    }

    public List<Pointage> getPoitagesEmpl() {
        return poitagesEmpl;
    }

    public void setPoitagesEmpl(List<Pointage> poitagesEmpl) {
        this.poitagesEmpl = poitagesEmpl;
    }

    public List<Contrat> getContratsEmpl() {
        return contratsEmpl;
    }

    public void setContratsEmpl(List<Contrat> contratsEmpl) {
        this.contratsEmpl = contratsEmpl;
    }
}
