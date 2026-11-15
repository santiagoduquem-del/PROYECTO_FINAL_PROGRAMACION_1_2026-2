package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import java.io.Serializable;

/**
 * Valores VIGENTES de un plan de membresía. El administrador los puede modificar
 * (igual que la clase Tarifa con los tipos de entrada).
 * Los valores iniciales salen del enum PlanMembresia.
 */
public class Membresia implements Serializable {

    private static final long serialVersionUID = 1L;

    private PlanMembresia plan;
    private double precioMensual;
    private double descuentoEntradas;   // 0.10 = 10 %
    private double descuentoCombos;
    private String beneficioExtra;

    public Membresia(PlanMembresia plan, double precioMensual, double descuentoEntradas,
                     double descuentoCombos, String beneficioExtra) {
        this.plan = plan;
        this.precioMensual = precioMensual;
        this.descuentoEntradas = descuentoEntradas;
        this.descuentoCombos = descuentoCombos;
        this.beneficioExtra = beneficioExtra;
    }

    /** Crea la membresía con los valores por defecto del plan. */
    public Membresia(PlanMembresia plan) {
        this(plan, plan.getPrecioMensual(), plan.getDescuentoEntradas(),
                plan.getDescuentoCombos(), plan.getBeneficioExtra());
    }

    public PlanMembresia getPlan() {
        return plan;
    }

    public double getPrecioMensual() {
        return precioMensual;
    }

    public void setPrecioMensual(double precioMensual) {
        this.precioMensual = precioMensual;
    }

    public double getDescuentoEntradas() {
        return descuentoEntradas;
    }

    public void setDescuentoEntradas(double descuentoEntradas) {
        this.descuentoEntradas = descuentoEntradas;
    }

    public double getDescuentoCombos() {
        return descuentoCombos;
    }

    public void setDescuentoCombos(double descuentoCombos) {
        this.descuentoCombos = descuentoCombos;
    }

    public String getBeneficioExtra() {
        return beneficioExtra;
    }

    public void setBeneficioExtra(String beneficioExtra) {
        this.beneficioExtra = beneficioExtra;
    }

    @Override
    public String toString() {
        return plan.getNombre();
    }
}
