package com.fstt.projectjava;

public class clientData {
    private int commade_ref;
    private String nom;
    private String prénom;
    private String adress;
    public clientData(int commade_id,String nom, String prénom, String adress) {
        this.commade_ref=commade_ref;
        this.nom = nom;
        this.prénom=prénom;
        this.adress=adress;

    }
    public Integer getCommande_id(){
        return commade_ref;
    }

    public String getNom() {
        return nom;
    }

    public String getPrénom() {
        return prénom;
    }



    public String getAdress() {
        return adress;
    }
}
