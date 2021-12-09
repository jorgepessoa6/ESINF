/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphbase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author jorgi
 */
public final class Mundo {

    public final List<Pais> list_paises;
    public final List<Fronteira> list_fronteiras;
    public final Graph<Pais, Double> graph;
    public final AdjacencyMatrixGraph<Pais, Double> adjGraph;

    /**
     * Construtor de um mundo
     *
     * @throws IOException
     */
    public Mundo() throws IOException {
        list_paises = carregarPaises();
        list_fronteiras = carregarFronteiras();
        graph = createGraph();
        adjGraph = createMatrixGraph();

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
     * Criar grafo
     *
     * @return matriz
     */
    public Graph<Pais, Double> createGraph() {
        double lat1, lat2, long1, long2, d;
        Graph<Pais, Double> graph1 = new Graph<>(false);
        for (Pais pais : list_paises) {
            graph1.insertVertex(pais);
        }
        for (Fronteira fronteira : list_fronteiras) {
            lat1 = fronteira.getPais().getLatitude();
            long1 = fronteira.getPais().getLongitude();
            for (Pais pais : fronteira.getFronteiras()) {
                lat2 = pais.getLatitude();
                long2 = pais.getLongitude();
                d = distance(lat1, lat2, long1, long2);
                // d em metros
                graph1.insertEdge(fronteira.getPais(), pais, d / 1000, d / 1000);
            }

        }
        return graph1;
    }

    public double distance(double lat1, double lat2, double long1, double long2) {
        double radius = 6371e3;
        double a = Math.sin(Math.toRadians(lat2 - lat1) / 2) * Math.sin(Math.toRadians(lat2 - lat1) / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(Math.toRadians(long2 - long1) / 2) * Math.sin(Math.toRadians(long2 - long1) / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = radius * c;
        return d;
    }

    /**
     * Criar grafo de matriz de adjacencias
     *
     * @return matriz
     */
    public AdjacencyMatrixGraph<Pais, Double> createMatrixGraph() {
        double lat1, lat2, long1, long2, d;
        AdjacencyMatrixGraph<Pais, Double> graph1 = new AdjacencyMatrixGraph<>();
        for (Pais pais : list_paises) {
            graph1.insertVertex(pais);
        }
        for (Fronteira fronteira : list_fronteiras) {
            lat1 = fronteira.getPais().getLatitude();
            long1 = fronteira.getPais().getLongitude();
            for (Pais pais : fronteira.getFronteiras()) {
                lat2 = pais.getLatitude();
                long2 = pais.getLongitude();
                d = distance(lat1, lat2, long1, long2);
                // d em metros
                graph1.insertEdge(fronteira.getPais(), pais, d / 1000);
            }

        }
        return graph1;
    }

    /**
     * Mostrar caminho mais curto e indicar as capitais incluídas no caminho e a
     * respetiva distância em kms
     *
     * @param pais pais de partida
     * @param pais2 pais de chegada
     */
    public void getPathByCitie(String pais, String pais2) {
        Pais pais3 = getPaisPorNome(pais);
        Pais pais4 = getPaisPorNome(pais2);

        if (graph.validVertex(pais3) && graph.validVertex(pais4)) {
            LinkedList<Pais> shortPath = new LinkedList();
            double sizeMin = GraphAlgorithms.shortestPath(graph, pais3, pais4, shortPath);
            boolean firstLoop = true;
            for (Pais pais5 : shortPath) {
                if (!firstLoop) {
                    Edge edge = graph.getEdge(pais3, pais5);
                    System.out.println("\n" + pais3.getCapital() + "-->" + pais5.getCapital() + "-->" + edge.getWeight() + " kms");
                    pais3 = pais5;
                } else {
                    firstLoop = false;
                }
            }
            System.out.println("\nDistancia total: " + sizeMin + " kms");
        } else {
            System.out.println("Países Inválidos");
        }
    }

    /**
     * Mostrar caminho mais curto com passagem em paises obrigatorios
     *
     * @param pais pais de partida
     * @param pais2 pais de chegada
     */
    public void getPathPassingCapital(String pais, String pais2) {
        Pais pais3 = getPaisPorNome(pais);
        Pais pais4 = getPaisPorNome(pais2);
        if (graph.validVertex(pais3) && graph.validVertex(pais4)) {
            List<Pais> listPaises = getCapitaisObrigatorias();
            int[] arrayPermut = createArray(listPaises.size());
            List<List<Integer>> list = permute(arrayPermut);
            LinkedList<Pais> path = getShortestPathByList(list, listPaises, pais3, pais4);
            System.out.println(path);
        } else {
            System.out.println("Países Inválidos");
        }

    }

    /**
     * Verificar dos caminhos possiveis o mais curto
     *
     * @param arrayPermut permutacoes possiveis entre a visita dos paises
     * obrigatorios
     * @param listPaises lista de paises obrigatorios
     * @param vOrig pais de partida
     * @param vDest pais de chegada
     * @return caminho possivel de menor distancia
     */
    public LinkedList<Pais> getShortestPathByList(List<List<Integer>> arrayPermut, List<Pais> listPaises, Pais vOrig, Pais vDest) {
        double shortestPathSize = Double.MAX_VALUE;
        double size;
        LinkedList<Pais> finalPath = new LinkedList();
        LinkedList<Pais> path = new LinkedList();
        LinkedList<Pais> shortPath = new LinkedList();
        Pais pais;
        Pais pais2 = null;
        Pais paisDest = vDest;
        for (List<Integer> list : arrayPermut) {
            pais = vOrig;
            path.clear();
            size = 0;
            for (int i = 0; i < list.size(); i++) {
                pais2 = listPaises.get(list.get(i));
                size += GraphAlgorithms.shortestPath(graph, pais, pais2, shortPath);
                shortPath.removeLast();
                path.addAll(shortPath);
                shortPath.clear();
                pais = pais2;
            }
            size += GraphAlgorithms.shortestPath(graph, pais2, paisDest, shortPath);
            path.addAll(shortPath);
            shortPath.clear();
            if (size < shortestPathSize) {
                shortestPathSize = size;
                finalPath.clear();
                finalPath = (LinkedList) path.clone();
                path.clear();
            }
        }
        System.out.println("\nDistancia total: " + shortestPathSize);
        return finalPath;
    }

    /**
     * Obter paises de passagem obrigatoria
     *
     * @return paises de passagem obrigatoria
     */
    public List<Pais> getCapitaisObrigatorias() {
        Scanner scanner = new Scanner(System.in);
        List<Pais> listCapitais = new ArrayList();
        String op;
        String paisS;
        Pais pais;
        do {
            System.out.println("Deseja introduzir um pais obrigatorio?\n 1 - Sim \n 2 - Não");
            op = scanner.nextLine();
            switch (op) {
                case "1":
                    System.out.println("Introduza um pais: ");
                    paisS = scanner.nextLine();
                    pais = getPaisPorNome(paisS);
                    if (adjGraph.checkVertex(pais) && !listCapitais.contains(pais)) {
                        listCapitais.add(pais);
                        System.out.println("Pais obrigatorio adicionado com sucesso.");
                    } else {
                        System.out.println("Pais Inválido.");
                    }
                    break;
                case "2":
                    break;

                default:
                    System.out.println("Invalid option!");
            }

        } while (!"2".equals(op));
        return listCapitais;
    }

    /**
     * Criar array de inteiros em função da quantidade de paises de passagem
     * obrigatoria
     *
     * @param size quantidade de paises de passagem obrigatoria
     * @return array de inteiros
     */
    public int[] createArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    /**
     * Obter possiveis permutacoes entre o array de inteiros
     *
     * @param arr array de inteiros
     * @return lista com possiveis permutacoes entre os do array
     */
    public List<List<Integer>> permute(int[] arr) {
        List<List<Integer>> list = new ArrayList<>();
        permuteHelper(list, new ArrayList<Integer>(), arr);
        return list;
    }

    /**
     * Obter permutacoes possiveis entre os valores
     *
     * @param list lista a guardar as permutacoes possiveis
     * @param resultList lista com permutacao possivel
     * @param arr valores para a permutacao
     */
    private void permuteHelper(List<List<Integer>> list, List<Integer> resultList, int[] arr) {

        if (resultList.size() == arr.length) {
            list.add(new ArrayList<>(resultList));
        } else {
            for (int i = 0; i < arr.length; i++) {

                if (resultList.contains(arr[i])) {
                    continue;
                }
                resultList.add(arr[i]);
                permuteHelper(list, resultList, arr);
                resultList.remove(resultList.size() - 1);
            }
        }
    }

    /*
     * Colorir o mapa de modo a que países vizinhos nao partilhem a mesma cor
     *
     * @return mapa com o pais e a cor
     */
    public Map<Pais, Integer> colorMap() {

        int result[] = new int[graph.numVertices()];
        Map<Pais, Integer> colorful = new HashMap<>();

        // Inicializa os vertices como unassigned 
        Arrays.fill(result, -1);

        // Atribui a primeira cor ao primeiro vertice 
        result[0] = 0;

        // Array temporario para armazenar as cores disponiveis. False 
        // Se for falso significa que a cor ja esta atribuida aos
        // vertices adjacentes
        boolean available[] = new boolean[graph.numVertices()];

        // Inicializa o array de disponibilidade de modo a que todas as cores estejam disponiveis 
        Arrays.fill(available, true);

        // Atribui cores aos restantes vertices
        for (int i = 1; i < graph.numVertices(); i++) {
            // Percorre pelos vertices adjacentes e marca as suas cores como indisponiveis
            Iterator<Pais> it = graph.adjVertices(list_paises.get(i)).iterator();
            while (it.hasNext()) {
                int a = graph.getKey(it.next());
                if (result[a] != -1) {
                    available[result[a]] = false;
                }
            }

            // Encontra a primeira cor disponivel
            int cr;
            for (cr = 0; cr < graph.numVertices(); cr++) {
                if (available[cr]) {
                    break;
                }
            }

            // Atribui a cor encontrada
            result[i] = cr;
            colorful.put(list_paises.get(i), cr);

            // Restabelece as cores como disponiveis, para a proxima iteracao 
            Arrays.fill(available, true);
        }

        // imprime o resultado 
        for (int u = 0; u < graph.numVertices(); u++) {
            System.out.format("%s ---> %d\n", list_paises.get(u).toString(), result[u]);
        }

        return colorful;
    }
    
    /**
     * Determinar o circuito de menor comprimento que parte de uma capital origem e visita outras capitais uma única vez, voltando à capital inicial usando a heurística do vizinho mais próximo.
     * @param pais pais origem 
     */
    public void getCircuitUsingHeuristic(String pais) {
        Pais pais1 = getPaisPorNome(pais);
        if(graph.validVertex(pais1)){
            LinkedList<Pais> path = new LinkedList();
            boolean[] visited = new boolean[graph.numVertices()];
            path.add(pais1);
            LinkedList<Pais> finalPath = nearestNeighbourHeuristic(pais1,pais1, visited, path);
            System.out.println(finalPath);
        }else{
            System.out.println("Pais Inválido!");
        }
    }

    /**
     * Calcular o caminho possível de realizar usando a heurística do vizinho mais próximo
     * @param vOrig pais origem
     * @param paisSeguinte
     * @param visited array de booleans
     * @param path circuito
     * @return caminho possível de realizar usando a heurística do vizinho mais próximo
     */
    public LinkedList<Pais> nearestNeighbourHeuristic(Pais vOrig, Pais paisSeguinte, boolean[] visited, LinkedList<Pais> path) {
        int vkey = graph.getKey(paisSeguinte);

        double distMin = Double.MAX_VALUE;

        Pais pais = null;
       
        int vkeyAdjNext = -1;
        
        visited[vkey] = true;

        for (Pais vAdj : graph.adjVertices(paisSeguinte)) {
            int vkeyAdj = graph.getKey(vAdj);

            Edge<Pais, Double> edge = graph.getEdge(paisSeguinte, vAdj);

            double dist = edge.getWeight();

            if (!visited[vkeyAdj] && distMin > dist) {
                distMin = dist;
                vkeyAdjNext = vkeyAdj;
                pais = vAdj;
            }
        }
        if (distMin == Double.MAX_VALUE || pais == vOrig) {
            return path;
        } else {
            visited[vkeyAdjNext] = true;
            path.add(pais);
            return nearestNeighbourHeuristic(vOrig, pais, visited, path);
        }
    }
    
}
