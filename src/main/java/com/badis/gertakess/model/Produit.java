package com.badis.gertakess.model;

public class Produit {

    private int idProd;
    private String nomProd;
    private String codeBarProd;
    private String categorieProd;
    private int quantiteStock;
    private double prixUniteProd;
    private boolean supprime;

    public Produit(){

    }

    public Produit(String nomProd, String codeBarProd, String categorieProd, int quantiteStock, double prixUniteProd, boolean supprime) {
        this.nomProd = nomProd;
        this.codeBarProd = codeBarProd;
        this.categorieProd = categorieProd;
        this.quantiteStock = quantiteStock;
        this.prixUniteProd = prixUniteProd;
        this.supprime = supprime;
    }

    public Produit(int idProd, String nomProd, String codeBarProd, String categorieProd, int quantiteStock, double prixUniteProd, boolean supprime) {
        this.idProd = idProd;
        this.nomProd = nomProd;
        this.codeBarProd = codeBarProd;
        this.categorieProd = categorieProd;
        this.quantiteStock = quantiteStock;
        this.prixUniteProd = prixUniteProd;
        this.supprime = supprime;
    }

    public double getTotal(){
        return prixUniteProd*quantiteStock;
    }

    public int getIdProd() {
        return idProd;
    }

    public void setIdProd(int idProd) {
        this.idProd = idProd;
    }

    public String getNomProd() {
        return nomProd;
    }

    public void setNomProd(String nomProd) {
        this.nomProd = nomProd;
    }

    public String getCodeBarProd() {
        return codeBarProd;
    }

    public void setCodeBarProd(String codeBarProd) {
        this.codeBarProd = codeBarProd;
    }

    public String getCategorieProd() {
        return categorieProd;
    }

    public void setCategorieProd(String categorieProd) {
        this.categorieProd = categorieProd;
    }

    public int getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public double getPrixUniteProd() {
        return prixUniteProd;
    }

    public void setPrixUniteProd(double prixUniteProd) {
        this.prixUniteProd = prixUniteProd;
    }

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }
}
