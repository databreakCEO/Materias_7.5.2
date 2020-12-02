/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package materias;

/**
 *
 * @author aniel
 */
public class Alumno {

    static String numControl, carrera, apPat, apMat, nombres, genero, activo, reticula;
    static int creditos, creditosAcumulados;

    public static void Alumno(String numControl, String carrera, String apPat, String apMat, String nombres, String genero, String activo, String reticula, int creditos, int creditosAcumulados) {
        Alumno.numControl = numControl;
        Alumno.carrera = carrera;
        Alumno.apPat = apPat;
        Alumno.apMat = apMat;
        Alumno.nombres = nombres;
        Alumno.genero = genero;
        Alumno.activo = activo;
        Alumno.reticula = reticula;
        Alumno.creditos = creditos;
        Alumno.creditosAcumulados = creditosAcumulados;

    }

}
