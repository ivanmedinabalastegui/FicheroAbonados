import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Abonados {

    protected Scanner teclado = new Scanner(System.in);
    protected FileOutputStream salida = null;
    protected DataOutputStream salida1 = null;
    protected RandomAccessFile fichero = null;
    protected String documento = "src/facturas_telf.dat";
    protected int número;
    protected String nombre;
    protected Float cantidad;

    public static void main(String[] args) {
        Abonados gt = new Abonados();
        System.out.println("\t\t\t\t\033[4m\033[1mPROGRAMA GESTIÓN COMPAÑÍA TELEFÓNICA\033[0m\033[2m");
        System.out.println("");
        Scanner sc = new Scanner(System.in);
        boolean menu = true;
        int opcion;
        while (menu) {
            System.out.println("\t\t\t\t\t\t\033[1mMenú de Opciones\033[0m");
            System.out.println("\t\t\t\t\t\t\033[1m================\033[0m");
            System.out.println();
            System.out.println("\t\t\t1) Alta de nuevas facturas");
            System.out.println("\t\t\t2) Modificación del valor de factura");
            System.out.println("\t\t\t3) Consulta del dato de facturación de un abonado");
            System.out.println("\t\t\t4) Consulta del dato de facturación total de la compañía");
            System.out.println("\t\t\t5) Eliminar el fichero");
            System.out.println("\t\t\t6) Salir");
            System.out.println();
            System.out.print("\t\t\t\t\t\tOpción: ");
            try {
                opcion = sc.nextInt();
                switch (opcion) {
                    case 1 -> gt.AltaAbonados();
                    case 2 -> gt.Modificar();
                    case 3 -> gt.Consultar();
                    case 4 -> gt.Total();
                    case 5 -> gt.Eliminar();
                    case 6 -> gt.Salir();
                    default -> System.out.println("\n\tDebes teclear un número entero entre 1 y 6\n");
                }
            } catch (InputMismatchException | IOException e) {
                System.out.println(e.getMessage());
                System.out.println("\n\tDebes teclear un número entero\n");
                sc.nextLine();
            }
        }
        sc.close();
    }

    protected void AltaAbonados() throws IOException {
        System.out.println("\033[4m\033[1mAlta de factura\033[0m\033[2m\n");
        System.out.print("Número del abonado: ");
        número = teclado.nextInt();
        teclado.nextLine();
        System.out.print("Nombre: ");
        nombre = teclado.nextLine();
        System.out.print("Valor de la factura: ");
        cantidad = teclado.nextFloat();
        try {
            salida = new FileOutputStream(documento, true);
            salida1 = new DataOutputStream(salida);
            salida1.writeInt(número);
            salida1.writeUTF(nombre);
            salida1.writeFloat(cantidad);
            System.out.println("\t\nDatos incorporados al fichero\n");
        } catch (IOException fnfe) {
            System.out.println(fnfe.getMessage());
        } finally {
            try {
                if (salida != null) {
                    salida.close();
                }
                if (salida1 != null) {
                    salida1.close();
                }
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }

    protected void Modificar() throws FileNotFoundException {
        System.out.println("\033[4m\033[1mModificación del valor de factura\033[0m\033[2m\n");
        System.out.print("Número del abonado: ");
        int num = teclado.nextInt();
        long position;
        boolean found = false;
        try {
            fichero = new RandomAccessFile(documento, "rw");
            fichero.seek(fichero.getFilePointer());
            while (true) {
                número = fichero.readInt();
                fichero.readUTF();
                if (num == número) {
                    position = fichero.getFilePointer();
                    System.out.print("\tValor de la factura: ");
                    cantidad = fichero.readFloat();
                    System.out.println(cantidad + "€");
                    System.out.print("Nuevo valor de la factura: ");
                    fichero.seek(position);
                    cantidad = teclado.nextFloat();
                    fichero.writeFloat(cantidad);
                    System.out.println("\n\tDato modificado en el fichero\n");
                    found = true;
                } else {
                    fichero.readFloat();
                }
            }
        } catch (EOFException eofe) {
        } catch (IOException fnfe) {
            System.out.println(fnfe.getMessage());
        } finally {
            try {
                if (fichero != null) {
                    fichero.close();
                }
            } catch (IOException eio) {
                System.out.println(eio.getMessage());
            }
        }
        if (!found) {
            System.out.println("\n\tAbonado no registrado");
        }
    }

    protected void Consultar() {
        System.out.println("\n\033[4m\033[1mConsulta facturación abonado\033[0m\033[2m\n");
        System.out.print("Número del abonado: ");
        int num = teclado.nextInt();
        boolean found = false;
        try {
            fichero = new RandomAccessFile(documento, "r");
            fichero.seek(fichero.getFilePointer());
            while (true) {
                número = fichero.readInt();
                fichero.readUTF();
                if (num == número) {
                    cantidad = fichero.readFloat();
                    System.out.println("\n\tValor de la factura: " + cantidad + "€\n");
                    found = true;
                } else {
                    fichero.readFloat();
                }
            }
        } catch (EOFException eofe) {
        } catch (IOException fnfe) {
            System.out.println(fnfe.getMessage());
        } finally {
            try {
                if (fichero != null) {
                    fichero.close();
                }
            } catch (IOException eio) {
                System.out.println(eio.getMessage());
            }
        }
        if (!found) {
            System.out.println("\nAbonado no registrado\n");
        }
    }

    protected void Total() {
        System.out.println("\033[4m\033[1mConsulta facturación total\033[0m\033[2m\n");
        float resultado = 0;
        try {
            fichero = new RandomAccessFile(documento, "r");
            fichero.seek(fichero.getFilePointer());
            while (true) {
                fichero.readInt();
                fichero.readUTF();
                resultado = resultado + fichero.readFloat();
            }
        } catch (EOFException eofe) {
        } catch (IOException fnfe) {
            System.out.println(fnfe.getMessage());
        } finally {
            try {
                if (fichero != null) {
                    fichero.close();
                }
            } catch (IOException eio) {
                System.out.println(eio.getMessage());
            }
        }
        System.out.println("\tFacturación total: " + resultado + "€\n");
    }

    protected void Eliminar() {
        System.out.println("\033[4m\033[1mEliminar fichero\033[0m\033[2m\n");
        try {
            boolean respuesta = Files.deleteIfExists(Paths.get(documento));
            if (respuesta) {
                System.out.println("\tFichero eliminado.\n");
            } else {
                System.out.println("\tNo se puede eliminar.\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void Salir() {
        System.exit(0);
    }
}