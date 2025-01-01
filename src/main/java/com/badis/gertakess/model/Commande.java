package com.badis.gertakess.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;
import java.time.LocalDate;
import java.util.Objects;

public class Commande {
    private int idCmd;
    private LocalDate dateCmd;
    private LocalDate datePrevCmd;
    private LocalDate dateLivrCmd;
    private boolean statutCmd;
    private Fournisseur fournisseur;
    private List<ProduitCommande> produitsCommandes;

    public Commande(){

    }
    public Commande(int idCmd, LocalDate dateCmd, LocalDate datePrevCmd, LocalDate dateLivrCmd, boolean statutCmd, Fournisseur fournisseur, List<ProduitCommande> produitsCommandes) {
        this.idCmd = idCmd;
        this.dateCmd = dateCmd;
        this.datePrevCmd = datePrevCmd;
        this.dateLivrCmd = dateLivrCmd;
        this.statutCmd = statutCmd;
        this.fournisseur = fournisseur;
        this.produitsCommandes = produitsCommandes;
    }
    public Commande(LocalDate dateCmd, LocalDate datePrevCmd, LocalDate dateLivrCmd, boolean statutCmd, Fournisseur fournisseur, List<ProduitCommande> produitsCommandes) {
        this.idCmd = idCmd;
        this.dateCmd = dateCmd;
        this.datePrevCmd = datePrevCmd;
        this.dateLivrCmd = dateLivrCmd;
        this.statutCmd = statutCmd;
        this.fournisseur = fournisseur;
        this.produitsCommandes = produitsCommandes;
    }

    public int getIdCmd() {
        return idCmd;
    }

    public void setIdCmd(int idCmd) {
        this.idCmd = idCmd;
    }

    public LocalDate getDateCmd() {
        return dateCmd;
    }

    public void setDateCmd(LocalDate dateCmd) {
        this.dateCmd = dateCmd;
    }

    public LocalDate getDatePrevCmd() {
        return datePrevCmd;
    }

    public void setDatePrevCmd(LocalDate datePrevCmd) {
        this.datePrevCmd = datePrevCmd;
    }

    public LocalDate getDateLivrCmd() {
        return dateLivrCmd;
    }

    public void setDateLivrCmd(LocalDate dateLivrCmd) {
        this.dateLivrCmd = dateLivrCmd;
    }

    public boolean getStatutCmd() {
        return statutCmd;
    }

    public void setStatutCmd(boolean statutCmd) {
        this.statutCmd = statutCmd;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public List<ProduitCommande> getProduitsCommandes() {
        return produitsCommandes;
    }

    public void setProduitsCommandes(List<ProduitCommande> produitsCommandes) {
        this.produitsCommandes = produitsCommandes;
    }

    public void imprimerCommande() {
        try {
            String cheminFichier = "src/main/resources/commande_" + this.getIdCmd() + ".pdf";
            System.out.printf(cheminFichier);
            PDDocument document = new PDDocument();

            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contenu = new PDPageContentStream(document, page);

            contenu.beginText();
            contenu.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contenu.newLineAtOffset(220, 750);
            contenu.showText("Détails de la Commande");
            contenu.endText();

            int yPosition = 700;
            int lineSpacing = 15;

            contenu.beginText();
            contenu.setFont(PDType1Font.HELVETICA, 12);
            contenu.newLineAtOffset(50, yPosition);

            contenu.showText("ID Commande : " + this.getIdCmd());
            contenu.newLineAtOffset(0, -lineSpacing);
            contenu.showText("Nom du Fournisseur : " + this.getFournisseur().getNomFourn());
            contenu.newLineAtOffset(0, -lineSpacing);
            contenu.showText("Date de la Commande : " + this.getDateCmd());
            contenu.newLineAtOffset(0, -lineSpacing);
            contenu.showText("Date prévue de Livraison : " + this.getDatePrevCmd());
            contenu.newLineAtOffset(0, -lineSpacing);
            contenu.showText("Date de Livraison : " + (this.getDateLivrCmd() == null ? "" : this.getDateLivrCmd()));
            contenu.endText();

            yPosition -= 100; // Move down for the table
            contenu.beginText();
            contenu.newLineAtOffset(50, yPosition);
            contenu.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contenu.showText(String.format("%-25s %-20s %-10s %-15s %-15s",
                    "Nom du Produit", "Code Barre", "Quantité", "Prix Unitaire", "Total"));
            contenu.endText();

            yPosition -= 20;
            double totalCommande = 0.0;

            for (ProduitCommande pc : this.getProduitsCommandes()) {
                String nom = pc.getProduit().getNomProd();
                String codeBarre = pc.getProduit().getCodeBarProd() == null ? "" : pc.getProduit().getCodeBarProd();
                String quantite = String.valueOf(pc.getQuantiteProd());
                String prix = String.format("%.2f DZD", pc.getPrixUniteProd());
                double totalProd = pc.getTotal();
                String totalProdString = String.format("%.2f DZD", totalProd);

                // Print row content with adjusted spacing
                contenu.beginText();
                contenu.newLineAtOffset(50, yPosition);
                contenu.setFont(PDType1Font.HELVETICA, 12);
                contenu.showText(String.format("%-25s %-20s %10s %15s %15s",
                        nom, codeBarre, quantite, prix, totalProdString));
                contenu.endText();

                totalCommande += totalProd;
                yPosition -= 20;
            }

            yPosition -= 40;
            contenu.beginText();
            contenu.newLineAtOffset(50, yPosition);
            contenu.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contenu.showText("Total de la Commande : " + String.format("%.2f DZD", totalCommande));
            contenu.endText();

            contenu.close();

            document.save(cheminFichier);
            document.close();

            System.out.println("PDF généré avec succès : " + cheminFichier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
