package com.badis.gertakess.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;

public class Panier {

    private int idPanier;
    private Vente vente;
    private List<LigneDeVente> lignesDeVentes;

    public Panier(){

    }

    public Panier(Vente vente, List<LigneDeVente> lignesDeVentes) {
        this.vente = vente;
        this.lignesDeVentes = lignesDeVentes;
    }

    public Panier(int idPanier, Vente vente, List<LigneDeVente> lignesDeVentes) {
        this.idPanier = idPanier;
        this.vente = vente;
        this.lignesDeVentes = lignesDeVentes;
    }

    public int getIdPanier() {
        return idPanier;
    }

    public void setIdPanier(int idPanier) {
        this.idPanier = idPanier;
    }

    public Vente getVente() {
        return vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public List<LigneDeVente> getLignesDeVentes() {
        return lignesDeVentes;
    }

    public void setLignesDeVentes(List<LigneDeVente> lignesDeVentes) {
        this.lignesDeVentes = lignesDeVentes;
    }

    public void imprimerPanier() {
        try {
            String cheminFichier = "src/main/resources/panier_" + this.getIdPanier() + ".pdf";
            System.out.printf(cheminFichier);
            PDDocument document = new PDDocument();

            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contenu = new PDPageContentStream(document, page);

            contenu.beginText();
            contenu.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contenu.newLineAtOffset(220, 750);
            contenu.showText("Détails de la Vente");
            contenu.endText();

            int yPosition = 700;
            int lineSpacing = 15;

            contenu.beginText();
            contenu.setFont(PDType1Font.HELVETICA, 12);
            contenu.newLineAtOffset(50, yPosition);

            contenu.showText("ID Panier : " + this.getIdPanier());
            contenu.newLineAtOffset(0, -lineSpacing);
            contenu.showText("Date de la Vente : " + this.getVente().getDateVente());
            contenu.newLineAtOffset(0, -lineSpacing);
            contenu.endText();

            yPosition -= 100; // Move down for the table
            contenu.beginText();
            contenu.newLineAtOffset(50, yPosition);
            contenu.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contenu.showText(String.format("%-25s %-20s %-10s %-15s %-15s",
                    "Nom du Produit", "Code Barre", "Quantité", "Prix Unitaire", "Total"));
            contenu.endText();

            yPosition -= 20;
            double totalVente = 0.0;

            for (LigneDeVente ldv : this.getLignesDeVentes()) {
                String nom = ldv.getProduit().getNomProd();
                String codeBarre = ldv.getProduit().getCodeBarProd() == null ? "" : ldv.getProduit().getCodeBarProd();
                String quantite = String.valueOf(ldv.getQuantiteProd());
                String prix = String.format("%.2f DZD", ldv.getPrixUniteProd());
                double totalProd = ldv.getTotal();
                String totalProdString = String.format("%.2f DZD", totalProd);

                // Print row content with adjusted spacing
                contenu.beginText();
                contenu.newLineAtOffset(50, yPosition);
                contenu.setFont(PDType1Font.HELVETICA, 12);
                contenu.showText(String.format("%-25s %-20s %10s %15s %15s",
                        nom, codeBarre, quantite, prix, totalProdString));
                contenu.endText();

                totalVente += totalProd;
                yPosition -= 20;
            }

            yPosition -= 40;
            contenu.beginText();
            contenu.newLineAtOffset(50, yPosition);
            contenu.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contenu.showText("Total de la Vente : " + String.format("%.2f DZD", totalVente));
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
