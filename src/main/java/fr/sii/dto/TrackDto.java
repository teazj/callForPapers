package fr.sii.dto;

/**
 * Created by SGUERNIO on 29/11/2015.
 */
public class TrackDto {
    private int id;
    private String libelle;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
