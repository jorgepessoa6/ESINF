package trabalho3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author jorgi
 */
public class main {

    public static void main(String[] args) throws IOException {
        Mundo es = new Mundo();

        final String menuContent = "\nEscolha uma opção.\n\n"
                + "1 -  Obter lista de países que fazem fronteira a um determinado país.\n"
                + "2 -  Obter lista ordenada dos países pertencentes a um determinado continente ordenada decrescentemente por número de fronteiras e crescentemente por valor de população. \n"
                + "3 -  Obter país correspondente a coordenadas da capital.\n"
                + "4 -  Obter país mais proximo de coordenadas.\n"
                + "5 -  Obter lista de países cuja capital está contida numa área dada por um rectângulo de coordenadas.\n"
                + "0 -  Encerrar programa.\n" + "Opção: ";
        Formatter formatter = new Formatter(System.out);
        Scanner scanner = new Scanner(System.in);

        String op;
        do {
            formatter.format("%s", menuContent);
            op = scanner.nextLine();
            switch (op) {
                case "1":
                    System.out.println("\nIntroduza um pais");
                    String pais = scanner.nextLine();
                    Set<Pais> list = es.findByNameFronteiras(pais);
                    if (list == null) {
                        System.out.println("Pais Inválido");
                    } else {
                        System.out.println("\nPaises que fazem fronteira com " + pais + ":");
                        for (Pais pais1 : list) {
                            System.out.println(pais1.toString());
                        }
                    }
                    break;
                case "2":
                    System.out.println("\nIntroduza um continente");
                    String continente = scanner.nextLine();
                    Iterable<FronteiraBST> listF = es.sortList(continente);
                    for (FronteiraBST fronteira : listF) {
                        System.out.println("\n" + fronteira.getPais().getNome() + "\nNúmero de fronteiras: " + fronteira.getNumeroFronteiras() + "\nValor de população: " + fronteira.getPais().getPopulacao());
                    }
                    break;
                case "3":
                    System.out.println("\nIntroduza a latitude");
                    double latitude = scanner.nextDouble();
                    System.out.println("\nIntroduza a longitude");
                    double longitude = scanner.nextDouble();
                    Fronteira fronteira = es.findByCoordinates(latitude, longitude);
                    if (fronteira == null) {
                        System.out.println("\nCoordenadas introduzidas não correspondem a nenhuma capital.");
                    } else {
                        System.out.println("\nCoordenadas correspondem a: " + fronteira.getPais().getNome());
                    }
                    scanner.nextLine();
                    break;
                case "4":
                    System.out.println("\nIntroduza a latitude");
                    double latitude1 = scanner.nextDouble();
                    System.out.println("\nIntroduza a longitude");
                    double longitude1 = scanner.nextDouble();
                    Fronteira fronteira1 = es.findNearestNeighbour(latitude1, longitude1);
                    System.out.println("\nPaís com capital mais próxima de coordenadas é: " + fronteira1.getPais().getNome());
                    scanner.nextLine();
                    break;
                case "5":
                    System.out.println("\nIntroduza a primeira latitude");
                    double lat1 = Double.parseDouble(scanner.nextLine());
                    System.out.println("Introduza a primeira longitude");
                    double long1 = Double.parseDouble(scanner.nextLine());
                    System.out.println("Introduza a segunda latitude");
                    double lat2 = Double.parseDouble(scanner.nextLine());
                    System.out.println("Introduza a segunda longitude");
                    double long2 = Double.parseDouble(scanner.nextLine());

                    List<Pais> foundCountries = es.findInGeographicArea(lat1, lat2, long1, long2);

                    if (foundCountries.size() == 0) {
                        System.out.println("Não há países dentro da área definida!");
                    } else {
                        System.out.println("Países na área definida:");
                        for (Pais country : foundCountries) {
                            System.out.println(country.getNome());
                        }
                    }
                    break;
                case "0":
                    break;

                default:
                    formatter.format("Opção inválida!");
            }
        } while (!"0".equals(op));
    }
}
