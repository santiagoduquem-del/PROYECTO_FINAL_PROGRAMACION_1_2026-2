package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

/**
 * Entidad que puede informar cuántas entradas vendió (Pelicula, Funcion).
 * Solo se cuentan las ventas ACTIVAS (las canceladas no).
 */
public interface ContadorEntradas {

    int contarEntradasVendidas();

}
