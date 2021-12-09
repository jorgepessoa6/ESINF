/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphbase;

import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;

/**
 *
 * @author jorgi
 */
public class main {

    public static void main(String[] args) throws IOException {
        Mundo es = new Mundo();

        final String menuContent = "\nEscolha uma opção.\n\n"
                + "1 - Visualizar o mapa colorido.\n"
                + "2 - Visualizar caminho mais curto entre dois países e indicar as capitais incluídas no caminho e a respetiva distância em kms.\n"
                + "3 - Visualizar o caminho mais curto entre duas capitais passando obrigatoriamente por outras capitais indicadas.\n"
                + "4 - Determinar o circuito de menor comprimento que parte de uma capital origem e visita outras capitais uma única vez, voltando à capital inicial usando a heurística do vizinho mais próximo.\n"
                + "0 - Encerrar programa.\n" + "Option: ";
        Formatter formatter = new Formatter(System.out);
        Scanner scanner = new Scanner(System.in);

        String op;
        do {
            formatter.format("%s", menuContent);
            op = scanner.nextLine();
            switch (op) {
                case "1":
                    es.colorMap();
                    break;
                case "2":
                    System.out.println("Introduza um pais");
                    String pais = scanner.nextLine();
                    System.out.println("Introduza outro pais");
                    String pais2 = scanner.nextLine();
                    es.getPathByCitie(pais, pais2);
                    break;
                case "3":
                    System.out.println("Introduza um pais");
                    String pais3 = scanner.nextLine();
                    System.out.println("Introduza outro pais");
                    String pais4 = scanner.nextLine();
                    es.getPathPassingCapital(pais3, pais4);
                    break;
                case "4":
                    System.out.println("Introduza um pais");
                    String pais5 = scanner.nextLine();
                    es.getCircuitUsingHeuristic(pais5);
                    break;
                case "0":
                    break;

                default:
                    formatter.format("Invalid option!");
            }
        } while (!"0".equals(op));
    }
}
