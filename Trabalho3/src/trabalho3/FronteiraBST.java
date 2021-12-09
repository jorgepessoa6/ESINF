/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho3;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author jorgi
 */
public class FronteiraBST implements Comparable<FronteiraBST> {
    Pais pais;
    Set<Pais> fronteiras;
    int numeroFronteiras;
    
    /**
     * Construtor de fronteira
     * @param pais - pais
     * @param fronteira -  pais fronteira
     */
    public FronteiraBST(Pais pais, Pais fronteira) {
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
    public String toString(){
        return String.format("%s", pais.getNome());
    }

    @Override
    public int compareTo(FronteiraBST t) {
        int i;
        i = Integer.compare(this.getNumeroFronteiras(), t.getNumeroFronteiras());
        if (i != 0) {
            return -i;
        }
        i = Double.compare(this.getPais().getPopulacao(), t.getPais().getPopulacao());
        return i;
    }
    
}
