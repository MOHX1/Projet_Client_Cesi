package com.example.mybeautybooking.activity;

import java.io.Serializable;

public class Professionnel implements Serializable {

private String Nom;
private String Ville ;
private String Num_tel;
private String Specialite;
    public Professionnel() {
    }
    public Professionnel(String nom, String ville, String num_tel, String specialite) {
        Nom = nom;
        Ville = ville;
        Num_tel = num_tel;
        Specialite = specialite;
    }
    public String getSpecialite() {
        return Specialite;
    }

    public void setSpecialite(String specialite) {
        Specialite = specialite;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public void setVille(String ville) {
        Ville = ville;
    }

    public void setNum_tel(String num_tel) {
        Num_tel = num_tel;
    }

    public String getNom() {
        return Nom;
    }

    public String getVille() {
        return Ville;
    }

    public String getNum_tel() {
        return Num_tel;
    }
}
