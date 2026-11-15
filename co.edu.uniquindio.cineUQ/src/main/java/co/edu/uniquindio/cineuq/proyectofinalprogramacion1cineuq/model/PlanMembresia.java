package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/**
 * Planes de membresía del cliente. Cada plan da un descuento en entradas y en combos.
 * Los valores de aquí son solo los INICIALES: los vigentes están en la clase Membresia
 * y el administrador los cambia en "Gestionar membresías".
 */
public enum PlanMembresia {
    FREE("Free", 0, 0.00, 0.00,
            "Acumula puntos en cada compra"),
    BASICO("Básico", 14900, 0.10, 0.05,
            "1 bebida pequeña gratis al mes"),
    ESTANDAR("Estándar", 29900, 0.20, 0.15,
            "Preventa de estrenos 48 h antes"),
    PREMIUM("Premium", 49900, 0.30, 0.30,
            "2 entradas gratis al mes y acceso a VIP Bistró");

    private final String nombre;
    private final double precioMensual;
    private final double descuentoEntradas;   // 0.10 = 10 %
    private final double descuentoCombos;
    private final String beneficioExtra;

    PlanMembresia(String nombre, double precioMensual, double descuentoEntradas,
                  double descuentoCombos, String beneficioExtra) {
        this.nombre = nombre;
        this.precioMensual = precioMensual;
        this.descuentoEntradas = descuentoEntradas;
        this.descuentoCombos = descuentoCombos;
        this.beneficioExtra = beneficioExtra;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioMensual() {
        return precioMensual;
    }

    public double getDescuentoEntradas() {
        return descuentoEntradas;
    }

    public double getDescuentoCombos() {
        return descuentoCombos;
    }

    public String getBeneficioExtra() {
        return beneficioExtra;
    }

    // Así se muestra en los ComboBox y tablas
    @Override
    public String toString() {
        return nombre;
    }
}
