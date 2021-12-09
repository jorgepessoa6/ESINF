package graphbase;

/**
 *
 * @author jorgi
 */
public class Pais {
    
    private final String nome;
    private final String continente;
    private final double populacao;
    private final String capital;
    private final double latitude;
    private final double longitude;
    
    /**
     * Construtor de um pais
     * @param nome - nome do pais
     * @param continente - continente em que o pais se situa
     * @param populacao - populacao do pais
     * @param capital - capital do pais
     * @param latitude - latitude do pais
     * @param longitude - longitude do pais
     */
    public Pais(String nome, String continente, double populacao, String capital, double latitude, double longitude){
        this.nome = nome;
        this.continente = continente;
        this.populacao = populacao;
        this.capital = capital;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    /**
     * Obter nome do pais
     * @return - nome do pais
     */
    public String getNome(){
        return this.nome;
    }
    
    /**
     * Obter continente do pais
     * @return - continente do pais
     */
    public String getContinente(){
        return this.continente;
    }
    
    /**
     * Obter populacao do pais
     * @return populacao do pais
     */
    public double getPopulacao(){
        return this.populacao;
    }
    
    /**
     * Obter capital do pais
     * @return capital do pais
     */
    public String getCapital(){
        return this.capital;
    }
    
    /**
     * Obter latitude do pais
     * @return latitude do pais
     */
    public double getLatitude(){
        return this.latitude;
    }
    
    /**
     * Obter longitude do pais
     * @return longitude do pais
     */
    public double getLongitude(){
        return this.longitude;
    }
    
    @Override
    public String toString(){
        return String.format("%s", nome);
    }
}
