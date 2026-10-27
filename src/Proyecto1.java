import java.util.Scanner;

/**
 * Sistema de ventas y facturacion.
 * Archivo: Proyecto1.java
 */
public class Proyecto1 {

    private static final Scanner sc = new Scanner(System.in);

    // Variables globales para conservar las estadisticas de las ventas.
    private static int ventasRealizadas = 0;
    private static int productosVendidos = 0;
    private static double totalVendido = 0.0;
    private static int ventasEfectivo = 0;
    private static int ventasTarjeta = 0;
    private static int ventasTransferencia = 0;
    private static double ventaMayor = 0.0;
    private static double ventaMenor = 0.0;
    private static int ventasConDescuento = 0;
    private static int ventasMayores500 = 0;

    public static void main(String[] args) {
        iniciarSistema();
    }

    public static void iniciarSistema() {
        int opcion;

        do {
            System.out.println("\n======================================");
            System.out.println("       SISTEMA DE VENTAS Y FACTURACION");
            System.out.println("======================================");
            System.out.println("1. Registrar una venta");
            System.out.println("2. Consultar caja");
            System.out.println("3. Mostrar estadisticas");
            System.out.println("4. Salir");

            opcion = leerEnteroEnRango("Seleccione una opcion: ", 1, 4);

            switch (opcion) {
                case 1:
                    registrarVenta();
                    break;
                case 2:
                    consultarCaja();
                    break;
                case 3:
                    mostrarEstadisticas();
                    break;
                case 4:
                    int confirmar = leerEnteroEnRango(
                            "Desea salir? (1 = Si, 2 = No): ", 1, 2);
                    if (confirmar == 1) {
                        System.out.println("Saliendo del sistema...");
                    } else {
                        opcion = 0;
                    }
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 4);

        sc.close();
    }

    public static void registrarVenta() {
        int cantidadProductos = leerEnteroMinimo(
                "Cuantos productos diferentes tendra la compra? ", 1);
        double subtotal = 0.0;
        int unidadesVenta = 0;

        for (int i = 1; i <= cantidadProductos; i++) {
            System.out.println("\n--- Producto " + i + " ---");
            String codigo = leerTexto("Codigo del producto: ");
            String nombre = leerTexto("Nombre del producto: ");
            double precio = leerDoublePositivo("Precio (Q): ");
            int cantidad = leerEnteroMinimo("Cantidad: ", 1);

            double subProducto = precio * cantidad;
            subtotal += subProducto;
            unidadesVenta += cantidad;

            System.out.printf("Subtotal de %s (%s): Q%.2f%n",
                    nombre, codigo, subProducto);
        }

        double porcentaje;
        if (subtotal < 200.0) {
            porcentaje = 0.0;
        } else if (subtotal < 500.0) {
            porcentaje = 0.05;
        } else if (subtotal < 1000.0) {
            porcentaje = 0.08;
        } else {
            porcentaje = 0.10;
        }

        double descuento = subtotal * porcentaje;
        double montoConDescuento = subtotal - descuento;
        double iva = montoConDescuento * 0.12;
        double total = montoConDescuento + iva;

        System.out.printf("%nSubtotal: Q%.2f%n", subtotal);
        System.out.printf("Descuento (%.0f%%): Q%.2f%n",
                porcentaje * 100, descuento);
        System.out.printf("Monto con descuento: Q%.2f%n", montoConDescuento);
        System.out.printf("IVA (12%%): Q%.2f%n", iva);
        System.out.printf("Total a pagar: Q%.2f%n", total);

        System.out.println("\nForma de pago:");
        System.out.println("1. Efectivo");
        System.out.println("2. Tarjeta");
        System.out.println("3. Transferencia");
        int formaPago = leerEnteroEnRango("Seleccione forma de pago: ", 1, 3);

        String nombrePago;
        double recibido = 0.0;
        double cambio = 0.0;

        switch (formaPago) {
            case 1:
                nombrePago = "Efectivo";
                recibido = leerDoubleNoNegativo("Efectivo recibido (Q): ");
                while (recibido < total) {
                    System.out.printf(
                            "Efectivo insuficiente. Debe recibir al menos Q%.2f%n",
                            total);
                    recibido = leerDoubleNoNegativo(
                            "Ingrese nuevamente el efectivo recibido (Q): ");
                }
                cambio = recibido - total;
                break;
            case 2:
                nombrePago = "Tarjeta";
                break;
            case 3:
                nombrePago = "Transferencia";
                break;
            default:
                nombrePago = "No definida";
                break;
        }

        System.out.println("\n========== RESUMEN DE LA VENTA ==========");
        System.out.println("Productos diferentes: " + cantidadProductos);
        System.out.println("Unidades vendidas: " + unidadesVenta);
        System.out.printf("Subtotal: Q%.2f%n", subtotal);
        System.out.printf("Descuento: Q%.2f%n", descuento);
        System.out.printf("IVA: Q%.2f%n", iva);
        System.out.printf("Total: Q%.2f%n", total);
        System.out.println("Forma de pago: " + nombrePago);

        if (formaPago == 1) {
            System.out.printf("Efectivo recibido: Q%.2f%n", recibido);
            System.out.printf("Cambio: Q%.2f%n", cambio);
        }

        ventasRealizadas++;
        productosVendidos += unidadesVenta;
        totalVendido += total;

        switch (formaPago) {
            case 1:
                ventasEfectivo++;
                break;
            case 2:
                ventasTarjeta++;
                break;
            case 3:
                ventasTransferencia++;
                break;
            default:
                break;
        }

        if (porcentaje > 0.0) {
            ventasConDescuento++;
        }
        if (total > 500.0) {
            ventasMayores500++;
        }

        if (ventasRealizadas == 1) {
            ventaMayor = total;
            ventaMenor = total;
        } else {
            if (total > ventaMayor) {
                ventaMayor = total;
            }
            if (total < ventaMenor) {
                ventaMenor = total;
            }
        }

        System.out.println("Venta registrada correctamente.");
    }

    public static void consultarCaja() {
        System.out.println("\n========== CONSULTA DE CAJA ==========");
        System.out.println("Ventas realizadas: " + ventasRealizadas);
        System.out.println("Productos vendidos: " + productosVendidos);
        System.out.printf("Total vendido: Q%.2f%n", totalVendido);
        System.out.println("Ventas en efectivo: " + ventasEfectivo);
        System.out.println("Ventas con tarjeta: " + ventasTarjeta);
        System.out.println("Ventas por transferencia: " + ventasTransferencia);
    }

    public static void mostrarEstadisticas() {
        System.out.println("\n========== ESTADISTICAS ==========");

        if (ventasRealizadas == 0) {
            System.out.println("No existen ventas registradas.");
            return;
        }

        System.out.println("Numero total de ventas: " + ventasRealizadas);
        System.out.println("Cantidad total de productos vendidos: " + productosVendidos);
        System.out.printf("Total de dinero vendido: Q%.2f%n", totalVendido);
        System.out.printf("Promedio por venta: Q%.2f%n",
                totalVendido / ventasRealizadas);
        System.out.println("Ventas en efectivo: " + ventasEfectivo);
        System.out.println("Ventas con tarjeta: " + ventasTarjeta);
        System.out.println("Ventas por transferencia: " + ventasTransferencia);
        System.out.printf("Venta mayor: Q%.2f%n", ventaMayor);
        System.out.printf("Venta menor: Q%.2f%n", ventaMenor);
        System.out.println("Ventas con descuento: " + ventasConDescuento);
        System.out.println("Ventas mayores de Q500: " + ventasMayores500);
    }

    public static String leerTexto(String mensaje) {
        String texto;

        do {
            System.out.print(mensaje);
            texto = sc.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("El dato no puede quedar vacio.");
            }
        } while (texto.isEmpty());

        return texto.replace(",", " ");
    }

    public static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    public static int leerEnteroMinimo(String mensaje, int minimo) {
        int valor;

        do {
            valor = leerEntero(mensaje);
            if (valor < minimo) {
                System.out.println("El valor minimo es " + minimo + ".");
            }
        } while (valor < minimo);

        return valor;
    }

    public static int leerEnteroEnRango(
            String mensaje, int minimo, int maximo) {
        int valor;

        do {
            valor = leerEntero(mensaje);
            if (valor < minimo || valor > maximo) {
                System.out.println("Seleccione una opcion entre "
                        + minimo + " y " + maximo + ".");
            }
        } while (valor < minimo || valor > maximo);

        return valor;
    }

    public static double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim().replace(",", ".");

            try {
                double valor = Double.parseDouble(entrada);
                if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                    System.out.println("Ingrese un numero valido.");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
            }
        }
    }

    public static double leerDoubleNoNegativo(String mensaje) {
        double valor;

        do {
            valor = leerDouble(mensaje);
            if (valor < 0.0) {
                System.out.println("El monto no puede ser negativo.");
            }
        } while (valor < 0.0);

        return valor;
    }

    public static double leerDoublePositivo(String mensaje) {
        double valor;

        do {
            valor = leerDouble(mensaje);
            if (valor <= 0.0) {
                System.out.println("El precio debe ser mayor que cero.");
            }
        } while (valor <= 0.0);

        return valor;
    }
}
