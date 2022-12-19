package org.sid.metier;

public interface IMetierBanque {
    void addcompte(Compte cp);
    void verser(Long code,double montant);
    void retirer(Long code,double montant);
    Compte consulter(Long code);
}
