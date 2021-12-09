/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho3;

import trabalho3.Pais;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author jorgi
 */
public class Fronteira implements Comparable<Fronteira>{

    Pais pais;
    Set<Pais> fronteiras;
    int numeroFronteiras;
    
    /**
     * Construtor de fronteira
     * @param pais - pais
     * @param fronteira -  pais fronteira
     */
    public Fronteira(Pais pais, Pais fronteira) {
        this.fronteiras = new HashSet();
        this.pais = pais;
        addFronteira(fronteira);
    }

    /**
     * Adicionar pais fronteira
     * @param pais - pais a ser adicionado
     */
    public void addFronteira(Pais pais){
        if(!(fronteiras.contains(pais)) && pais != null){
            fronteiras.add(pais);
            this.numeroFronteiras = this.fronteiras.size();
        }
    }

    /**
     * Obter pais
     * @return -  pais
     */
    public Pais getPais(){
        return this.pais;
    }
    
    /**
     * Obter paises fronteira
     * @return - paises fronteira
     */
    public Set<Pais> getFronteiras(){
        return this.fronteiras;
    }
    
    public int getNumeroFronteiras(){
        return this.numeroFronteiras;
    }
    
    @Override
    public int compareTo(Fronteira t) {
        return this.getPais().getNome().compareTo(t.getPais().getNome());
    }
    
    @Override
    public String toString(){
        return String.format("%s", pais.getNome());
    }
}
