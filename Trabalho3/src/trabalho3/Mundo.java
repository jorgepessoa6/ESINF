package trabalho3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import trabalho3.BST.Node;
import trabalho3.Kdtree.KdNode;

/**
 *
 * @author jorgi
 */
public final class Mundo {

    public final BST<Fronteira> tree;
    public final Kdtree<Fronteira> tree_2d;

    /**
     * Construtor de um mundo
     *
     * @throws IOException
     */
    public Mundo() throws IOException {
        tree = new BST();
        tree_2d = new Kdtree();
        carregarPaises(tree, tree_2d);
        carregarFronteiras(tree, tree_2d);
    }

    /**
     * Carregar paises do ficheiro .txt
     *
     * @param tree
     * @param tree_2d
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void carregarPaises(BST tree, Kdtree tree_2d) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader("paises.txt");
        BufferedReader br = new BufferedReader(fr);
        String linha;
        while ((linha = br.readLine()) != null) {
            String[] temp = linha.trim().split(",");
            Pais pais = new Pais(temp[0].trim(), temp[1].trim(), Double.parseDouble(temp[2].trim()), temp[3].trim(), Double.parseDouble(temp[4]), Double.parseDouble(temp[5]));
            Fronteira fronteira = new Fronteira(pais, null);
            tree.insert(fronteira);
            tree_2d.insert(fronteira, fronteira.getPais().getLatitude(), fronteira.getPais().getLongitude());
        }
        br.close();
    }

    /**
     * Carregar fronteiras do ficheiro .txt
     *
     * @param tree
     * @param tree_2d
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void carregarFronteiras(BST tree, Kdtree tree_2d) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader("fronteiras.txt");
        BufferedReader br = new BufferedReader(fr);
        String linha;
        Fronteira fronteira;
        Fronteira fronteira1;
        while ((linha = br.readLine()) != null) {
            String[] temp = linha.trim().split(",");
            fronteira = findByNameBST(temp[0].trim());
            fronteira1 = findByNameBST(temp[1].trim());
            //add tree
            fronteira.addFronteira(fronteira1.getPais());
            fronteira1.addFronteira(fronteira.getPais());
            //add tree_2d
            fronteira = findByCoordinates(fronteira.getPais().getLatitude(), fronteira.getPais().getLongitude());
            fronteira1 = findByCoordinates(fronteira1.getPais().getLatitude(), fronteira1.getPais().getLongitude());
            fronteira.addFronteira(fronteira1.getPais());
            fronteira1.addFronteira(fronteira.getPais());
        }
        br.close();
    }

    /**
     * Obter pais pelo nome
     *
     * @param nome nome do pais
     * @return pais com o respetivo nome
     */
    public Fronteira findByNameBST(String nome) {
        return findByNameBST(nome, tree.root());
    }

    public Fronteira findByNameBST(String nome, Node<Fronteira> node) {
        if (node == null) {
            return null;
        }
        if (node.getElement().getPais().getNome().compareTo(nome) == 0) {
            return node.getElement();
        }
        if (node.getLeft() != null) {
            if (node.getElement().getPais().getNome().compareTo(nome) >= 0) {
                return findByNameBST(nome, node.getLeft());
            }
        }
        if (node.getRight() != null) {
            if (node.getElement().getPais().getNome().compareTo(nome) <= 0) {
                return findByNameBST(nome, node.getRight());
            }
        }
        return null;
    }

    /**
     * Obter fronteiras de pais
     *
     * @param nome nome do pais
     * @return fronteiras do pais
     */
    public Set<Pais> findByNameFronteiras(String nome) {
        return findByNameFronteiras(nome, tree.root());
    }

    /**
     * Obter fronteiras de pais
     *
     * @param nome nome do pais
     * @param node root
     * @return fronteiras de pais
     */
    public Set<Pais> findByNameFronteiras(String nome, Node<Fronteira> node) {
        if (node == null) {
            return null;
        }
        if (node.getElement().getPais().getNome().compareTo(nome) == 0) {
            return node.getElement().getFronteiras();
        }
        if (node.getLeft() != null) {
            if (node.getElement().getPais().getNome().compareTo(nome) >= 0) {
                return findByNameFronteiras(nome, node.getLeft());
            }
        }
        if (node.getRight() != null) {
            if (node.getElement().getPais().getNome().compareTo(nome) <= 0) {
                return findByNameFronteiras(nome, node.getRight());
            }
        }
        return null;
    }

    /**
     * Criar nova bst e dar return de iterable com ordenação decrescente por
     * número de fronteiras e crescente por valor de população de um dado
     * continente
     *
     * @param continente continente
     * @return iterable com ordenação decrescente por número de fronteiras e
     * crescente por valor de população de um dado continente
     */
    public Iterable<FronteiraBST> sortList(String continente) {
        Iterable<Fronteira> it = tree.inOrder();
        BST<FronteiraBST> sortBST = new BST();
        for (Fronteira fronteira : it) {
            if (fronteira.getPais().getContinente().equals(continente)) {
                FronteiraBST fronteiraBST = new FronteiraBST(fronteira.getPais(), null);
                for (Pais pais : fronteira.getFronteiras()) {
                    fronteiraBST.addFronteira(pais);
                }
                sortBST.insert(fronteiraBST);
            }
        }
        return sortBST.inOrder();
    }

    /**
     * Obter fronteira com determinada latitude e longitude
     *
     * @param latitude latitude
     * @param longitude longitude
     * @return fronteira
     */
    public Fronteira findByCoordinates(double latitude, double longitude) {
        return findByCoordinates(latitude, longitude, tree_2d.root());
    }

    /**
     * Obter fronteira com determinada latitude e longitude
     *
     * @param latitude latitude
     * @param longitude longitude
     * @param node root
     * @return fronteira
     */
    public Fronteira findByCoordinates(double latitude, double longitude, KdNode<Fronteira> node) {
        if (node == null) {
            return null;
        }
        if (node.getLatitude() == latitude && node.getLongitude() == longitude) {
            return node.getElement();
        }
        if (node.getLeft() != null) {
            if (node.getLatitude() > latitude && node.getVertical() || node.getLongitude() > longitude && !node.getVertical()) {
                return findByCoordinates(latitude, longitude, node.getLeft());
            }
        }
        if (node.getRight() != null) {
            return findByCoordinates(latitude, longitude, node.getRight());
        }
        return null;
    }

    /**
     * Calcular distancia entre capitais
     *
     * @param lat1 latitude da primeira capital
     * @param lat2 latitude da segunda capital
     * @param long1 longitude da primeira capital
     * @param long2 longitude da segunda capital
     * @return distancia entre capitais
     */
    public double distance(double lat1, double lat2, double long1, double long2) {
        double radius = 6371e3;
        double a = Math.sin(Math.toRadians(lat2 - lat1) / 2) * Math.sin(Math.toRadians(lat2 - lat1) / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(Math.toRadians(long2 - long1) / 2) * Math.sin(Math.toRadians(long2 - long1) / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = radius * c;
        return d;
    }

    /**
     * Obter capital mais próxima de coordenadas
     *
     * @param x latitude
     * @param y longitude
     * @return capital mais proxima de coordenadas
     */
    public Fronteira findNearestNeighbour(double x, double y) {
        return findNearestNeighbour(tree_2d.root(), x, y, true);
    }

    /**
     * Obter capital mais próxima de coordenadas
     *
     * @param fromNode root
     * @param x latitude
     * @param y longitude
     * @param divX
     * @return capital mais próxima de coordenadas
     */
    private Fronteira findNearestNeighbour(KdNode<Fronteira> fromNode, final double x, final double y, boolean divX) {
        return new Object() {

            double closestDist = Double.POSITIVE_INFINITY;

            Fronteira closestNode = null;

            Fronteira findNearestNeighbour(KdNode<Fronteira> node, boolean divX) {
                if (node == null) {
                    return null;
                }
                double d = distance(node.getLatitude(), x, node.getLongitude(), y);
                if (closestDist > d) {
                    closestDist = d;
                    closestNode = node.getElement();
                }
                double delta = divX ? x - node.getLatitude() : y - node.getLongitude();
                double delta2 = delta * delta;
                KdNode<Fronteira> node1 = delta < 0 ? node.getLeft() : node.getRight();
                KdNode<Fronteira> node2 = delta < 0 ? node.getRight() : node.getLeft();
                findNearestNeighbour(node1, !divX);
                if (delta2 < closestDist) {
                    findNearestNeighbour(node2, !divX);
                }
                return closestNode;
            }
        }.findNearestNeighbour(fromNode, divX);
    }

    public List<Pais> findInGeographicArea(double lat1, double lat2, double long1, double long2) {

        List<Pais> inAreaCountries = new ArrayList<>();

        double menorLat = 0, menorLong = 0, maiorLat = 0, maiorLong = 0;

        if (lat1 > lat2) {
            maiorLat = lat1;
            menorLat = lat2;
        } else {
            maiorLat = lat2;
            menorLat = lat1;
        }

        if (long1 > long2) {
            maiorLong = long1;
            menorLong = long2;
        } else {
            maiorLong = long2;
            menorLong = long1;
        }

        for (Fronteira front : tree_2d.inOrder()) {
            Pais pais = front.getPais();
            if (pais.getLongitude() >= menorLong && pais.getLongitude() <= maiorLong && pais.getLatitude() >= menorLat && pais.getLatitude() <= maiorLat) {
                inAreaCountries.add(pais);
            }
        }

        return inAreaCountries;
    }
}
