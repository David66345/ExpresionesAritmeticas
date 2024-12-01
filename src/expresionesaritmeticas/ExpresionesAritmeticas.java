/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package expresionesaritmeticas;

import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author david
 */
public class ExpresionesAritmeticas {

    /**
     * @param args the command line arguments
     */
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_COLOR = "\u001B[41m";
    
    public static void main(String[] args) {
        ExpresionesAritmeticas ea = new ExpresionesAritmeticas();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingresa una expresión matemática: ");
        String expresion = scanner.nextLine();
        double resultado = ea.evaluarExpresion(expresion, scanner);
        System.out.println("El resultado es: " + resultado);
    }
    
    public double evaluarExpresion(String expresion, Scanner scanner) {
        expresion = expresion.replaceAll("\\s+", ""); // Eliminar espacios en blanco

        Stack<Double> numeros = new Stack<>();
        Stack<Character> operadores = new Stack<>();
        String[][] variables = new String[26][2]; // Arreglo para almacenar nombres y valores de variables (a-z)

        for (int i = 0; i < expresion.length(); i++) {
            char caracter = expresion.charAt(i);
            // Verificar si hay un número seguido de una letra sin el operador de multiplicación
            if (i + 1 < expresion.length() && Character.isDigit(expresion.charAt(i)) && Character.isLetter(expresion.charAt(i + 1))) {
                System.out.println("Error: Falta signo de multiplicación (*) en la expresión '"+ANSI_COLOR + expresion.substring(i, i + 2) + ANSI_RESET+"'");
                System.exit(0);
            } else if (Character.isDigit(caracter)) {
                StringBuilder numero = new StringBuilder();
                while (i < expresion.length() && (Character.isDigit(expresion.charAt(i)) || expresion.charAt(i) == '.')) {
                    numero.append(expresion.charAt(i));
                    i++;
                }
                i--;
                numeros.push(Double.parseDouble(numero.toString()));
            } else if (Character.isLetter(caracter)) {
                
                
                StringBuilder variable = new StringBuilder();
                while (i < expresion.length() && Character.isLetter(expresion.charAt(i))) {
                    variable.append(expresion.charAt(i));
                    i++;
                }
                i--;
                
                String nombreVariable = variable.toString().toLowerCase();
                double valorVariable;
                if (variables[nombreVariable.charAt(0) - 'a'][0] == null) {
                    System.out.println("¿Cuánto vale la variable '" + nombreVariable + "'?");
                    String valor = scanner.nextLine();
                    if (!esNumero(valor)) {
                        System.out.println("Error: Solo se pueden introducir valores numéricos en la variable '" +ANSI_COLOR + nombreVariable +ANSI_RESET+ "'");
                        System.exit(0);
                    }
                    
                    valorVariable = Double.parseDouble(valor);
                    variables[nombreVariable.charAt(0) - 'a'][0] = nombreVariable;
                    variables[nombreVariable.charAt(0) - 'a'][1] = Double.toString(valorVariable);
                } else {
                    valorVariable = Double.parseDouble(variables[nombreVariable.charAt(0) - 'a'][1]);
                }
                numeros.push(valorVariable);
                
            } else if (caracter == '(') {
                operadores.push(caracter);
            } else if (caracter == ')') {
                while (!operadores.isEmpty() && operadores.peek() != '(') {
                    aplicarOperacion(numeros, operadores.pop());
                }
                operadores.pop(); // Sacar el '('
            } else if (esOperador(caracter)) {
                while (!operadores.isEmpty() && precedencia(operadores.peek()) >= precedencia(caracter)) {
                    aplicarOperacion(numeros, operadores.pop());
                }
                operadores.push(caracter);
            } else {
                System.out.println("Error: El caracter "+ANSI_COLOR +caracter+ANSI_RESET+" es invalido");
                System.exit(0);
            }
        }

        while (!operadores.isEmpty()) {
            aplicarOperacion(numeros, operadores.pop());
        }

        return numeros.pop();
    }

    private void aplicarOperacion(Stack<Double> numeros, char operador) {
        double num2 = numeros.pop();
        double num1 = numeros.pop();
        double resultado = 0;
        switch (operador) {
            case '+':
                resultado = num1 + num2;
                break;
            case '-':
                resultado = num1 - num2;
                break;
            case '*':
                resultado = num1 * num2;
                break;
            case '/':
                if (num2 != 0) {
                    resultado = num1 / num2;
                } else {
                    System.out.println("No se puede dividir entre cero, denominador: "+ANSI_COLOR +num2+ANSI_RESET);
                    System.exit(0);
                }
                break;
            case '^':
                resultado = Math.pow(num1, num2);
                break;
        }
        numeros.push(resultado);
    }

    private boolean esOperador(char caracter) {
        return caracter == '+' || caracter == '-' || caracter == '*' || caracter == '/' || caracter == '^';
    }
    
    private boolean esNumero(String cadena) {
        try {
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int precedencia(char operador) {
        switch (operador) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }
}
