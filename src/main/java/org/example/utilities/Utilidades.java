package org.example.utilities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Utilidades {

    public static String pedirPalabra(String mensaje){
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        return sc.next();
    }


    public static String pedirString(String mensaje) {
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        return sc.nextLine();
    }

    public static BigDecimal pedirBigDecimal(String mensaje){
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        return sc.nextBigDecimal();
    }

    public static char pedirChar(String mensaje){
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        return (char) sc.nextInt();
    }

    public static int pedirInt(String mensaje){
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        return sc.nextInt();
    }

    public static LocalDate parsearFecha(String fecha){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(fecha,formatter);
    }

}
