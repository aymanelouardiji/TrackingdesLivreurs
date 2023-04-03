package com.fstt.projectjava;

public class livData {
    private String nom;
    private String prénom;
    private String tel;
    private String adress;
    private int commande_ref;

    public livData(String nom, String prénom, String tel, String adress) {
        this.nom = nom;
        this.prénom=prénom;
        this.tel=tel;
        this.adress=adress;


    }
    public livData(String nom, String prénom, String adress, int commande_ref){
        this.nom = nom;
        this.prénom=prénom;
        this.adress=adress;
        this.commande_ref=commande_ref;
    }

    public String getNom() {
        return nom;
    }

    public String getPrénom() {
        return prénom;
    }

    public String getTel() {
        return tel;
    }

    public String getAdress() {
        return adress;
    }

    public int getCommande_ref() {
        return commande_ref;
    }
}
