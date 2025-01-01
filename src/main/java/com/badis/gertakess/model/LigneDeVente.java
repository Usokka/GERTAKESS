package com.badis.gertakess.model;

public class LigneDeVente {

    private Produit produit;
    private Panier panier;
    private int quantiteProd;
    private double prixUniteProd;

    public LigneDeVente(){

    }
    public LigneDeVente(Produit produit, Panier panier, int quantiteProd, double prixUniteProd) {
        this.produit = produit;
        this.panier = panier;
        this.quantiteProd = quantiteProd;
        this.prixUniteProd = prixUniteProd;
    }

    public double getTotal(){
        return quantiteProd * prixUniteProd;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    public int getQuantiteProd() {
        return quantiteProd;
    }

    public void setQuantiteProd(int quantiteProd) {
        this.quantiteProd = quantiteProd;
    }

    public double getPrixUniteProd() {
        return prixUniteProd;
    }

    public void setPrixUniteProd(double prixUniteProd) {
        this.prixUniteProd = prixUniteProd;
    }
}
