/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphbase;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author jorgi
 */
public class Fronteira {

    Pais pais;
    Set<Pais> fronteiras;
    
    /**
     * Construtor de fronteira
     * @param pais - pais
     * @param fronteira -  pais fronteira
     */
    public Fronteira(Pais pais, Pais fronteira) {
        this.fronteiras = new HashSet();
        this.pais = pais;
        this.fronteiras.add(fronteira);
    }

    /**
     * Adicionar pais fronteira
     * @param pais - pais a ser adicionado
     */
    public void addFronteira(Pais pais){
        if(!fronteiras.contains(pais))
            fronteiras.add(pais);
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
}
