package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/**
 * Formatos en los que se puede proyectar una película, con la información
 * que se muestra en la pantalla de detalle de cada formato.
 */
public enum Formato {
    DOS_D("2D",
            "La proyección digital tradicional: imagen nítida y sonido envolvente estándar. "
                    + "Sirve para cualquier película y es el formato más económico.",
            "Cualquier película y cualquier presupuesto",
            new String[]{"Proyección digital en alta definición",
                    "Sonido envolvente 7.1",
                    "El precio más económico de la cartelera"}),
    TRES_D("3D",
            "Con gafas especiales la imagen gana profundidad y los objetos parecen salir de la pantalla. "
                    + "Ideal para películas de animación, aventura y ciencia ficción.",
            "Películas de animación, aventura y ciencia ficción",
            new String[]{"Gafas 3D incluidas con la entrada",
                    "Imagen con profundidad real",
                    "Efectos que parecen salir de la pantalla"}),
    IMAX("IMAX",
            "Pantalla gigante de piso a techo, proyección láser de muy alta resolución y sonido de precisión "
                    + "calibrado para que sientas que estás dentro de la película.",
            "Grandes estrenos de acción y ciencia ficción",
            new String[]{"Pantalla gigante de piso a techo",
                    "Proyección láser en 4K",
                    "Sonido de 12 canales calibrado para la sala"}),
    DOLBY_CINEMA("Dolby Cinema",
            "Imagen Dolby Vision con colores vivos y negros profundos, más sonido Dolby Atmos que se mueve "
                    + "a tu alrededor, incluso por encima de ti. Sillas reclinables en toda la sala.",
            "Quienes buscan la mejor imagen y el mejor sonido",
            new String[]{"Dolby Vision: más contraste y colores más vivos",
                    "Dolby Atmos: el sonido también viene desde arriba",
                    "Sillas reclinables en toda la sala"}),
    CUATRO_DX("4DX",
            "Las sillas se mueven al ritmo de la acción y la sala suma efectos de viento, agua, aromas, "
                    + "vibraciones y luces. Una película para vivirla con todos los sentidos.",
            "Películas de acción y aventura",
            new String[]{"Sillas con movimiento sincronizado con la película",
                    "Efectos de viento, agua, aromas, vibraciones y luces",
                    "No recomendado para menores de 7 años ni mujeres embarazadas"}),
    VIP_BISTRO("VIP Bistró",
            "Sillas reclinables de cuero, mucho espacio entre filas y servicio de comida y bebidas "
                    + "a la silla durante toda la función.",
            "Planes especiales, citas y celebraciones",
            new String[]{"Sillas reclinables de cuero con mesa auxiliar",
                    "Menú gourmet y bebidas",
                    "Servicio a la silla durante toda la función"});

    private final String nombre;
    private final String descripcion;
    private final String idealPara;
    private final String[] caracteristicas;

    Formato(String nombre, String descripcion, String idealPara, String[] caracteristicas) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idealPara = idealPara;
        this.caracteristicas = caracteristicas;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getIdealPara() {
        return idealPara;
    }

    public String[] getCaracteristicas() {
        return caracteristicas;
    }

    // Así se muestra en los ComboBox y tablas
    @Override
    public String toString() {
        return nombre;
    }
}
