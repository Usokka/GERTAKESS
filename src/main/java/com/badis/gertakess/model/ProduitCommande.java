package com.badis.gertakess.model;

public class ProduitCommande {

    private Produit produit;
    private Commande commande;
    private int quantiteProd;
    private double prixUniteProd;

    public ProduitCommande(){

    }
    public ProduitCommande(Produit produit, Commande commande, int quantiteProd, double prixUniteProd) {
        this.produit = produit;
        this.commande = commande;
        this.quantiteProd = quantiteProd;
        this.prixUniteProd = prixUniteProd;
    }

    public Produit getProduit() {
        return produit;
    }

    public double getTotal(){
        return quantiteProd * prixUniteProd;
    }
    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
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
