package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.legado;

/**
 * SOLO PARA COMPATIBILIDAD: así eran los combos en la versión anterior (fijos, en un enum).
 * Se usa únicamente al abrir un archivo de datos guardado con esa versión, para pasar sus
 * ventas a los combos nuevos (clase model.Combo). No se usa en ninguna otra parte del programa.
 */
public enum Combo {
    PERSONAL("Combo Personal", "Crispetas medianas + gaseosa mediana", 18000),
    PAREJA("Combo Pareja", "Crispetas grandes + 2 gaseosas medianas", 32000),
    FAMILIAR("Combo Familiar", "2 crispetas grandes + 4 gaseosas + chocolatina", 52000),
    NACHOS("Combo Nachos", "Nachos con queso cheddar + gaseosa grande", 24000),
    PERRO("Combo Perro Caliente", "Perro caliente + crispetas pequeñas + gaseosa", 26000),
    INFANTIL("Combo Infantil", "Crispetas pequeñas + jugo + juguete sorpresa", 16000);

    private final String nombre;
    private final String descripcion;
    private final double precio;

    Combo(String nombre, String descripcion, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
