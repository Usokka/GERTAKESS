CREATE DATABASE gestion_caisse_db;
use gestion_caisse_db;

DROP TABLE IF EXISTS `commande`;
CREATE TABLE IF NOT EXISTS `commande` (
                                          `idCmd` int NOT NULL AUTO_INCREMENT,
                                          `datePrevueCmd` date DEFAULT NULL,
                                          `dateCmd` date NOT NULL,
                                          `dateLivrCmd` date DEFAULT NULL,
                                          `statutCmd` tinyint(1) DEFAULT NULL,
                                          `idFourn` int DEFAULT NULL,
                                          PRIMARY KEY (`idCmd`),
                                          KEY `idFourn` (`idFourn`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



DROP TABLE IF EXISTS `contrat`;
CREATE TABLE IF NOT EXISTS `contrat` (
                                         `idContr` int NOT NULL AUTO_INCREMENT,
                                         `typeContr` enum('CDD','CDI') DEFAULT NULL,
                                         `dateDebContr` date NOT NULL,
                                         `dateFinContr` date DEFAULT NULL,
                                         `posteContr` varchar(50) DEFAULT NULL,
                                         `dateQuitPoste` date DEFAULT NULL,
                                         `salaireContr` decimal(10,2) DEFAULT NULL,
                                         `idEmpl` int DEFAULT NULL,
                                         PRIMARY KEY (`idContr`),
                                         KEY `idEmpl` (`idEmpl`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `employe`;
CREATE TABLE IF NOT EXISTS `employe` (
                                         `idEmpl` int NOT NULL AUTO_INCREMENT,
                                         `nomEmpl` varchar(50) DEFAULT NULL,
                                         `prenomEmpl` varchar(50) DEFAULT NULL,
                                         `sexeEmpl` enum('HOMME','FEMME') DEFAULT NULL,
                                         `dateNaissEmpl` date DEFAULT NULL,
                                         `adresseEmpl` varchar(255) DEFAULT NULL,
                                         `SituationEmpl` enum('DIVORCE','MARIE','CELIBATAIRE') DEFAULT NULL,
                                         `emailEmpl` varchar(100) DEFAULT NULL,
                                         `numTelEmpl` varchar(10) DEFAULT NULL,
                                         `mdpCptEmpl` varchar(25) DEFAULT NULL,
                                         `permissionCptEmpl` enum('ADMIN','MODERATEUR','UTILISATEUR') DEFAULT NULL,
                                         `dateCptEmpl` date DEFAULT NULL,
                                         `etatEmpl` tinyint(1) DEFAULT NULL,
                                         PRIMARY KEY (`idEmpl`),
                                         UNIQUE KEY `emailEmpl` (`emailEmpl`),
                                         UNIQUE KEY `numTelEmpl` (`numTelEmpl`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `fournisseur`;
CREATE TABLE IF NOT EXISTS `fournisseur` (
                                             `idFourn` int NOT NULL AUTO_INCREMENT,
                                             `nomFourn` varchar(100) DEFAULT NULL,
                                             `adresseFourn` varchar(255) DEFAULT NULL,
                                             `emailFourn` varchar(100) DEFAULT NULL,
                                             `numTelFourn` varchar(10) DEFAULT NULL,
                                             `categoriesFourn` varchar(255) DEFAULT NULL,
                                             `supprime` tinyint(1) DEFAULT NULL,
                                             PRIMARY KEY (`idFourn`),
                                             UNIQUE KEY `emailFourn` (`emailFourn`),
                                             UNIQUE KEY `numTelFourn` (`numTelFourn`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `lignedevente`;
CREATE TABLE IF NOT EXISTS `lignedevente` (
                                              `idPan` int NOT NULL,
                                              `idProd` int NOT NULL,
                                              `qteProdVente` int DEFAULT NULL,
                                              `prixUniteVente` double DEFAULT NULL,
                                              PRIMARY KEY (`idPan`,`idProd`),
                                              KEY `idProd` (`idProd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `panier`;
CREATE TABLE IF NOT EXISTS `panier` (
                                        `idPan` int NOT NULL AUTO_INCREMENT,
                                        `idVente` int DEFAULT NULL,
                                        PRIMARY KEY (`idPan`),
                                        KEY `panier_vente_idVente_fk` (`idVente`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `pointage`;
CREATE TABLE IF NOT EXISTS `pointage` (
                                          `idPoint` int NOT NULL AUTO_INCREMENT,
                                          `datePoint` date NOT NULL,
                                          `heureEntreePoint` time DEFAULT NULL,
                                          `heureSortiePoint` time DEFAULT NULL,
                                          `idEmpl` int DEFAULT NULL,
                                          PRIMARY KEY (`idPoint`),
                                          KEY `idEmpl` (`idEmpl`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



DROP TABLE IF EXISTS `produit`;
CREATE TABLE IF NOT EXISTS `produit` (
                                         `idProd` int NOT NULL AUTO_INCREMENT,
                                         `codeBarreProd` varchar(50) DEFAULT NULL,
                                         `nomProd` varchar(100) DEFAULT NULL,
                                         `categorieProd` varchar(50) DEFAULT NULL,
                                         `qteStockProd` int DEFAULT NULL,
                                         `prixUniteProd` double DEFAULT NULL,
                                         `supprime` tinyint(1) DEFAULT NULL,
                                         PRIMARY KEY (`idProd`),
                                         UNIQUE KEY `codeBarreProd` (`codeBarreProd`),
                                         UNIQUE KEY `nomProd` (`nomProd`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `produitcommande`;
CREATE TABLE IF NOT EXISTS `produitcommande` (
                                                 `idCmd` int NOT NULL,
                                                 `idProd` int NOT NULL,
                                                 `qteProdCmd` int DEFAULT NULL,
                                                 `prixUniteCmd` double DEFAULT NULL,
                                                 PRIMARY KEY (`idCmd`,`idProd`),
                                                 KEY `idProd` (`idProd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `vente`;
CREATE TABLE IF NOT EXISTS `vente` (
                                       `idVente` int NOT NULL AUTO_INCREMENT,
                                       `dateVente` date DEFAULT NULL,
                                       `idEmpl` int DEFAULT NULL,
                                       PRIMARY KEY (`idVente`),
                                       KEY `idEmpl` (`idEmpl`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE `commande`
    ADD CONSTRAINT `commande_ibfk_1` FOREIGN KEY (`idFourn`) REFERENCES `fournisseur` (`idFourn`);

ALTER TABLE `contrat`
    ADD CONSTRAINT `contrat_ibfk_1` FOREIGN KEY (`idEmpl`) REFERENCES `employe` (`idEmpl`);

ALTER TABLE `lignedevente`
    ADD CONSTRAINT `lignedevente_ibfk_1` FOREIGN KEY (`idPan`) REFERENCES `panier` (`idPan`) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `lignedevente_ibfk_2` FOREIGN KEY (`idProd`) REFERENCES `produit` (`idProd`);

ALTER TABLE `panier`
    ADD CONSTRAINT `panier_vente_idVente_fk` FOREIGN KEY (`idVente`) REFERENCES `vente` (`idVente`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `pointage`
    ADD CONSTRAINT `pointage_ibfk_1` FOREIGN KEY (`idEmpl`) REFERENCES `employe` (`idEmpl`);

ALTER TABLE `produitcommande`
    ADD CONSTRAINT `produitcommande_ibfk_1` FOREIGN KEY (`idCmd`) REFERENCES `commande` (`idCmd`) ON DELETE CASCADE,
    ADD CONSTRAINT `produitcommande_ibfk_2` FOREIGN KEY (`idProd`) REFERENCES `produit` (`idProd`);

ALTER TABLE `vente`
    ADD CONSTRAINT `vente_ibfk_1` FOREIGN KEY (`idEmpl`) REFERENCES `employe` (`idEmpl`);

INSERT INTO `employe` (`nomEmpl`, `prenomEmpl`, `sexeEmpl`, `dateNaissEmpl`, `adresseEmpl`, `SituationEmpl`, `emailEmpl`, `numTelEmpl`, `mdpCptEmpl`, `permissionCptEmpl`, `dateCptEmpl`, `etatEmpl`) VALUES
    ('AOURTILANE', 'Badis', 'HOMME', '2004-11-24', 'Tinebdar', 'MARIE', 'badisaourtilane@gmail.com', '0500000000', 'badiss', 'ADMIN', '2024-12-11', 1);

INSERT INTO `contrat` (`typeContr`, `dateDebContr`, `dateFinContr`, `posteContr`, `dateQuitPoste`, `salaireContr`, `idEmpl`) VALUES
    ('CDI', '2024-12-01', NULL, 'Gérant', NULL, 10000.00, 1)


