# 🎬 CinemaUQ – Sistema de Gestión de Cine

Proyecto final de **Programación I** – Universidad del Quindío.
Aplicación de escritorio en **Java 21 + JavaFX** para administrar un cine: cartelera, salas, funciones, venta de entradas con mapa de asientos, combos, membresías, pago con tarjeta (simulado) y reportes.

---

## 👥 Integrantes

| Nombre | Rama |
|---|---|
| Santiago Duque Martínez | `dev-santiagoDuqueMartinez` |
| _(Integrante 2)_ | |
| _(Integrante 3)_ | |

---

## 🛠️ Tecnologías

- **Java 21**
- **JavaFX 21** con **FXML** y Scene Builder
- **Maven** (incluye el wrapper `mvnw`)
- **JUnit 5** para las pruebas unitarias
- Persistencia con **serialización de Java** (`Serializable`)

---

## ▶️ Cómo ejecutarlo

1. Clonar el repositorio y abrirlo en **IntelliJ IDEA** (se importa como proyecto Maven).
2. Ejecutar la clase `HelloApplication` (o `Launcher`) del paquete `co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq`.

También desde la terminal:

```bash
./mvnw clean javafx:run
```

### Usuarios de prueba

| Usuario | Contraseña | Rol |
|---|---|---|
| `administrador` | `admin123` | Administrador |
| `operador` | `operador123` | Operador de taquilla |

La primera vez el programa crea datos de ejemplo (salas, películas, clientes y funciones).

---

## ✨ Funcionalidades

### Operador de taquilla
- **Generar venta:** película, función, cliente, tipo de entrada, combos y **mapa de asientos** (varios asientos por compra).
- **Pago con tarjeta (simulado):** débito o crédito con cuotas; detecta **Visa, Mastercard, American Express y Diners** por los primeros dígitos, valida vencimiento y CVV. Si el monto es menor → *saldo insuficiente*; si es mayor → se devuelve el restante.
- **Consultar películas:** cartelera en mosaico → detalle con banner, sinopsis, elenco, formatos y secciones informativas.
- **Consultar funciones:** horarios agrupados por día, formato e idioma, con asientos libres (estilo AMC).
- **Cancelar venta** (libera el asiento) y **registrar clientes** con plan de membresía.

### Administrador
- CRUD de **películas** (multiformato, carátula, banner, sinopsis, elenco, estudio y secciones del detalle).
- CRUD de **salas** (hasta 1000 asientos; las salas grandes usan filas más anchas).
- **Funciones:** programar, modificar y cancelar (no se borran: quedan en el historial).
- **Combos** con foto, incluidos combos **exclusivos de VIP Bistró**.
- **Tarifas** por tipo de entrada y **membresías** (Free, Básico, Estándar, Premium).
- **Empleados** y **reportes**: entradas vendidas, ingresos, película más vendida, ocupación de una función y funciones por fecha.

### Formatos de proyección
Signature, 3D, IMAX, IMAX 70 mm, Dolby Cinema, 4DX, ScreenX, Infinity Vision, Prime, XD y VIP Bistró — cada función se programa en un formato de la película y en idioma **doblado o subtitulado**.

---

## 📐 Reglas de negocio principales

- **Valor de la entrada** = (tarifa del tipo de entrada + valor base de la función) − descuento del plan del cliente.
- Un cliente puede comprar entrada **General** o la de **su tipo** (Estudiante, Niño, Adulto mayor).
- **No se cruzan funciones** en la misma sala: se respeta la duración de la película + **15 minutos de limpieza**.
- Una función **solo se cancela si no tiene entradas activas**; una venta cancelada libera su asiento.
- No se eliminan películas, salas, combos o clientes que ya tienen historial: se **deshabilitan**.
- Los **reportes** no cuentan las ventas canceladas.
- Del pago solo se guardan los **últimos 4 dígitos** de la tarjeta (nunca el número completo ni el CVV).

---

## 🏗️ Arquitectura (MVC)

```
src/main/java/co/edu/uniquindio/cineuq/proyectofinalprogramacion1cineuq/
├── model/            → Clases del dominio y reglas de negocio (Cine, Pelicula, Sala, Funcion, Venta, Pago…)
│   ├── interfaces/   → GestionPeliculas, GestionSalas, GestionFunciones, GestionVentas, Reportes…
│   └── exceptions/   → Excepciones propias (heredan de CinemaException)
├── controller/       → Puente entre la vista y el modelo (un controller por pantalla) + persistencia
└── viewController/   → Controladores de las pantallas FXML

src/main/resources/.../  → Archivos FXML, estilos CSS y recursos gráficos
src/test/java/...        → Pruebas unitarias JUnit 5
```

- `ModelFactoryController` (**Singleton**) comparte una única instancia de `Cine` entre todas las pantallas.
- Cada pantalla tiene su **ViewController** (FXML) y su **Controller**, que es el único que habla con el modelo.

---

## 💾 Persistencia

Los datos se guardan automáticamente después de cada cambio en la carpeta personal del usuario:

```
~/CinemaUQ/cinemauq.dat     → películas, salas, funciones, clientes, empleados, ventas, combos…
~/CinemaUQ/imagenes/        → copia de las imágenes que sube el administrador
```

Para volver a los datos de ejemplo: cerrar el programa y borrar `cinemauq.dat`.

---

## ✅ Pruebas unitarias

**174 pruebas con JUnit 5** sobre todas las clases del modelo y el conversor de datos.
Cada prueba crea un cine nuevo, así no dependen entre sí ni modifican los datos reales.

```bash
./mvnw test
```

```
Tests run: 174, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

En IntelliJ: clic derecho en `src/test/java` → **Run 'All Tests'**.
