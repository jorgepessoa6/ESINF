/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esinf.tp1;

import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author jorgi
 */
public class main {

    public static void main(String[] args) throws IOException {
        Mundo es = new Mundo();

        final String menuContent = "\nEscolha uma opção.\n\n" + "1 - Visualizar lista dos países de um continente com mais de N milhões de habitantes, ordenada por ordem crescente de população\n"
                + "2 - Visualizar os países, de um respetivo continente, agrupados pelo mesmo número de países fronteiras, por ordem decrescente do número de fronteiras\n"
                + "3 - Visualizar o número mínimo de fronteiras que é necessário atravessar para chegar de um país origem a um país destino. a\n"
                + "0 - Encerrar programa.\n" + "Option: ";
        Formatter formatter = new Formatter(System.out);
        Scanner scanner = new Scanner(System.in);
        String op;
        do {
            formatter.format("%s", menuContent);
            op = scanner.nextLine();
            switch (op) {
                case "1":
                    System.out.println("Introduza o continente: ");
                    String continente = scanner.nextLine();
                    System.out.println("Introduza o numero minimo de habitantes: ");
                    double N = scanner.nextDouble();
                    List<Pais> listp = es.getListPopulacao(continente, N);
                    for (Pais pais : listp) {
                        System.out.println(pais.getPopulacao() + "," + pais.getNome());
                    }
                    scanner.nextLine();
                    break;
                case "2":
                    System.out.println("Introduza o continente: ");
                    String continente1 = scanner.nextLine();
                    Map<Integer, Set<Pais>> mapNumFronteiras = es.getNumeroFronteiras(continente1);
                    for (Map.Entry<Integer, Set<Pais>> entry : mapNumFronteiras.entrySet()) {
                        System.out.println(entry.getKey() + ":" + entry.getValue().toString());
                    }
                    break;
                case "3":
                    System.out.println("Introduza o primeiro pais: ");
                    String pais1 = scanner.nextLine();
                    System.out.println("Introduza o segundo pais: ");
                    String pais2 = scanner.nextLine();
                    int front = es.minimoFronteiras(pais1, pais2);
                    if(front == 0){
                        System.out.println("Não é possível chegar do primeiro ao segundo país.");
                    }else{
                        System.out.println("É preciso passar um minimo de " + front +" fronteiras para chegar do primeiro ao segundo país.");
                    }
                    break;
                case "0":
                    break;

                default:
                    formatter.format("Invalid option!");
            }
        } while (!"0".equals(op));
    }
}
