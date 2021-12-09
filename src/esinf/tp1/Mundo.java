/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esinf.tp1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 *
 * @author jorgi
 */
public final class Mundo {

    public final List<Pais> list_paises;
    public final List<Fronteira> list_fronteiras;

    /**
     * Construtor de um mundo
     *
     * @throws IOException
     */
    public Mundo() throws IOException {
        list_paises = carregarPaises();
        list_fronteiras = carregarFronteiras();

    }

    /**
     * Carregar a lista de paises do ficheiro .txt
     *
     * @return A lista de paises criada
     * @throws FileNotFoundException
     * @throws IOException
     */
    public List<Pais> carregarPaises() throws FileNotFoundException, IOException {
        FileReader fr = new FileReader("paises.txt");
        BufferedReader br = new BufferedReader(fr);
        List<Pais> list = new ArrayList();
        String linha;
        while ((linha = br.readLine()) != null) {
            String[] temp = linha.trim().split(",");
            Pais pais = new Pais(temp[0].trim(), temp[1].trim(), Double.parseDouble(temp[2].trim()), temp[3].trim(), Double.parseDouble(temp[4]), Double.parseDouble(temp[5]));
            list.add(pais);
        }
        br.close();
        return list;
    }

    /**
     * Carregar a lista de fronteiras do ficheiro .txt
     *
     * @return lista de fronteiras criada
     * @throws FileNotFoundException
     * @throws IOException
     */
    public List<Fronteira> carregarFronteiras() throws FileNotFoundException, IOException {
        FileReader fr = new FileReader("fronteiras.txt");
        BufferedReader br = new BufferedReader(fr);

        List<Fronteira> list_fronteira = new ArrayList();
        Pais pais1;
        Pais pais2;
        String linha;
        Fronteira fronteira;
        Fronteira fronteira1;
        while ((linha = br.readLine()) != null) {
            String[] temp = linha.trim().split(",");
            pais1 = getPaisPorNome(temp[0]);
            pais2 = getPaisPorNome(temp[1]);
            if (pais1 != null && pais2 != null) {
                if (!check_fronteira(pais1, list_fronteira)) {
                    fronteira = new Fronteira(pais1, pais2);

                    list_fronteira.add(fronteira);
                } else {
                    fronteira = getFronteira(list_fronteira, pais1);
                    fronteira.addFronteira(pais2);
                }
                if (!check_fronteira(pais2, list_fronteira)) {
                    fronteira1 = new Fronteira(pais2, pais1);
                    list_fronteira.add(fronteira1);
                } else {
                    fronteira1 = getFronteira(list_fronteira, pais2);
                    fronteira1.addFronteira(pais1);
                }
            }
        }
        br.close();
        return list_fronteira;
    }

    /**
     * Obter instancia do pais pelo nome
     *
     * @param nome - nome do pais
     * @return a instancia do pais com o respetivo nome
     */
    public Pais getPaisPorNome(String nome) {
        for (Pais pais : list_paises) {
            if (pais.getNome().equals(nome.trim())) {
                return pais;
            }
        }
        return null;
    }

    /**
     * Averiguar se já existe uma instancia de fronteira com o respetivo pais
     *
     * @param pais - pais a ser verificado
     * @param fronteiras - lista de fronteiras
     * @return se já existe ou não a fronteira
     */
    public boolean check_fronteira(Pais pais, List<Fronteira> fronteiras) {
        for (Fronteira fronteira : fronteiras) {
            if (fronteira.getPais().getNome().equals(pais.getNome())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obter a instancia da fronteira com o respetivo pais
     *
     * @param fronteiras - lista de fronteiras
     * @param pais - pais com fronteiras
     * @return instancia de fronteira com o respetivo pais
     */
    public Fronteira getFronteira(List<Fronteira> fronteiras, Pais pais) {
        for (Fronteira fronteira : fronteiras) {
            if (fronteira.getPais().getNome().equals(pais.getNome())) {
                return fronteira;
            }
        }
        return null;
    }

    /**
     * Obter lista ordenada pelo numero de populacao de cada pais
     *
     * @param continente - continente a averiguar
     * @param N - numero a comparar com o numero de habitantes
     * @return - lista ordenada por ordem crescente de população num respetivo
     * continente
     */
    public List<Pais> getListPopulacao(String continente, double N) {
        List<Pais> listP = new ArrayList();
        for (Pais pais : list_paises) {
            if (pais.getPopulacao() > N && pais.getContinente().equals(continente)) {
                listP.add(pais);
            }
        }
        Collections.sort(listP, (Pais pais1, Pais pais2) -> Double.compare(pais1.getPopulacao(), pais2.getPopulacao()));

        return listP;
    }

    /**
     * Agrupar paises pelo numero de paises fronteira por ordem decrescente
     *
     * @param continente - continente a averiguar
     * @return estrutura de dados onde os paises estao agrupados pelo mesmo
     * número de países fronteiras, por ordem decrescente do número de
     * fronteiras.
     */
    public Map<Integer, Set<Pais>> getNumeroFronteiras(String continente) {
        Map<Integer, Set<Pais>> map = new TreeMap<>(Collections.reverseOrder());
        Set<Pais> setTemp;
        int size;
        int semFronteira = 0;
        Pais pais;

        for (Fronteira fronteira : list_fronteiras) {
            size = fronteira.getFronteiras().size();
            pais = fronteira.getPais();
            if (pais.getContinente().equals(continente)) {
                setTemp = map.get(size);
                if (setTemp == null) {
                    setTemp = new HashSet();
                    setTemp.add(pais);
                    map.put(size, setTemp);
                } else {
                    setTemp = map.get(size);
                    setTemp.add(pais);
                    map.put(size, setTemp);
                }
            }
        }
        if (!addPaisesSemFronteira(continente).isEmpty()) {
            map.put(semFronteira, addPaisesSemFronteira(continente));
        }
        return map;
    }

    /**
     * Adicionar paises sem fronteiras
     *
     * @param continente - continente a averiguar
     * @return estrutura de dados com paises, do respetivo continente, sem
     * paises fronteira
     */
    public Set<Pais> addPaisesSemFronteira(String continente) {
        Set<Pais> setTemp = new HashSet();
        int count;
        for (Pais pais : list_paises) {
            count = 0;
            for (Fronteira fronteira : list_fronteiras) {
                if (fronteira.getPais() == pais) {
                    count++;
                }
            }
            if (count == 0 && pais.getContinente().equals(continente)) {
                setTemp.add(pais);
            }
        }
        return setTemp;
    }

    /**
     * Obter número minimo de fronteiras
     * @param pais3 - pais de partida
     * @param pais4 - pais de chegada
     * @return minimo de fronteiras necessarias para chegar do pais de partida ao pais de chegada
     */
    public int minimoFronteiras(String pais3, String pais4) {
        Pais pais1 = getPaisPorNome(pais3);
        Pais pais2 = getPaisPorNome(pais4);
        if (pais1 == null || pais2 == null) {
            return 0;
        }
        Fronteira fronteira = getFronteira(list_fronteiras, pais1);
        if (fronteira.getFronteiras().contains(pais2)) {
            return 1;
        }
        if(!(pais1.getContinente().equals(pais2.getContinente()))){
            return 0;
        }
        int[][] matrix = createMatrix();
        return runMatrix(matrix, list_paises.indexOf(pais1), list_paises.indexOf(pais2));
    }

    public int minDistance(int dist[], Boolean sptSet[], int size) {
        int min = Integer.MAX_VALUE;
        int min_index = -1;

        for (int v = 0; v < size; v++) {
            if (sptSet[v] == false && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }
        }

        return min_index;
    }

    int runMatrix(int matrix[][], int start, int end) {
        int dist[] = new int[matrix.length];

        Boolean sptSet[] = new Boolean[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
        }

        dist[start] = 0;

        for (int count = 0; count < matrix.length - 1; count++) {

            int u = minDistance(dist, sptSet, matrix.length);

            sptSet[u] = true;

            for (int v = 0; v < matrix.length; v++){
                if (!sptSet[v] && matrix[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + matrix[u][v] < dist[v]) {
                    dist[v] = dist[u] + matrix[u][v];
                }
            }
        }
        if(dist[end] == Integer.MAX_VALUE){
            return 0;
        }
        return dist[end];
    }
    
    /**
     * Criar matriz de adjacencias das fronteiras
     * @return matriz
     */
    public int[][] createMatrix() {
        int i = 0;
        int j = 0;
        int size = list_paises.size();
        int[][] matrix = new int[size][size];
        for (Pais pais : list_paises) {
            i = list_paises.indexOf(pais);
            if (check_fronteira(pais, list_fronteiras)) {
                for (Pais pais1 : getFronteira(list_fronteiras, pais).getFronteiras()) {
                    j = list_paises.indexOf(pais1);
                    matrix[i][j] = 1;
                }
            }
        }
        return matrix;
    }

    /**
     * Obter lista de paises
     *
     * @return lista de paises
     */
    public List<Pais> getListPaises() {
        return list_paises;
    }

    /**
     * Obter lista de fronteiras
     *
     * @return lista de fronteiras
     */
    public List<Fronteira> getListFronteiras() {
        return list_fronteiras;
    }
}
