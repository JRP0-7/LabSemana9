package labsemana9;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class GUILogistica extends JFrame {

    private Logistica logistica = new Logistica();
    private Timer timer;

    private JLabel lblResumen;

    private JLabel lblRecepcion;
    private JProgressBar barRecepcion;
    private JTextArea areaRecepcion;

    private JLabel lblAlmacen;
    private JProgressBar barAlmacen;
    private JTextArea areaAlmacen;

    private JLabel lblClasificacion;
    private JProgressBar barClasificacion;
    private JTextArea areaClasificacion;

    private JLabel lblEmpaquetado;
    private JProgressBar barEmpaquetado;
    private JTextArea areaEmpaquetado;

    private JLabel lblExpedicion;
    private JProgressBar barExpedicion;
    private JTextArea[] areaRutas = new JTextArea[4];

    private JTextArea[] areaRepartidores = new JTextArea[4];
    private JTextArea areaRegistro;

    public GUILogistica() {
        setTitle("Centro Logístico");
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(construirPanelControles(), BorderLayout.NORTH);
        add(construirPanelCentral(), BorderLayout.CENTER);
        add(construirPanelRegistro(), BorderLayout.SOUTH);

        timer = new Timer(500, e -> {
            actualizar();
        });
        timer.start();
    }

    private JPanel construirPanelControles() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(20, 20, 35));

        JLabel lblTitulo = new JLabel("CENTRO LOGÍSTICO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel botones = new JPanel(new FlowLayout());
        botones.setOpaque(false);

        JButton btnIniciar = new JButton("INICIAR");
        btnIniciar.addActionListener(e -> {
            logistica.iniciar();
        });

        JButton btnPausar = new JButton("PAUSAR");
        btnPausar.addActionListener(e -> {
            logistica.pausar();
        });

        JButton btnReanudar = new JButton("REANUDAR");
        btnReanudar.addActionListener(e -> {
            logistica.reanudar();
        });

        // detener espera a los hilos, se hace aparte para no congelar la ventana
        JButton btnDetener = new JButton("DETENER");
        btnDetener.addActionListener(e -> {
            new Thread(() -> logistica.detener()).start();
        });

        JButton btnReiniciar = new JButton("REINICIAR");
        btnReiniciar.addActionListener(e -> {
            new Thread(() -> logistica.reiniciar()).start();
        });

        JButton btnEstadisticas = new JButton("ESTADÍSTICAS");
        btnEstadisticas.addActionListener(e -> {
            mostrarEstadisticas();
        });

        botones.add(btnIniciar);
        botones.add(btnPausar);
        botones.add(btnReanudar);
        botones.add(btnDetener);
        botones.add(btnReiniciar);
        botones.add(btnEstadisticas);

        lblResumen = new JLabel("Generados: 0 | Entregados: 0 | Devueltos: 0 | En proceso: 0");
        lblResumen.setForeground(Color.WHITE);
        lblResumen.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(botones, BorderLayout.CENTER);
        panel.add(lblResumen, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(235, 235, 235));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        lblRecepcion = new JLabel();
        barRecepcion = new JProgressBar();
        areaRecepcion = new JTextArea();

        lblAlmacen = new JLabel();
        barAlmacen = new JProgressBar();
        areaAlmacen = new JTextArea();

        lblClasificacion = new JLabel();
        barClasificacion = new JProgressBar();
        areaClasificacion = new JTextArea();

        lblEmpaquetado = new JLabel();
        barEmpaquetado = new JProgressBar();
        areaEmpaquetado = new JTextArea();

        JPanel zonas = new JPanel(new GridLayout(1, 4, 5, 5));
        zonas.setOpaque(false);
        zonas.add(construirPanelZona("RECEPCIÓN", lblRecepcion, barRecepcion, areaRecepcion));
        zonas.add(construirPanelZona("ALMACÉN", lblAlmacen, barAlmacen, areaAlmacen));
        zonas.add(construirPanelZona("CLASIFICACIÓN", lblClasificacion, barClasificacion, areaClasificacion));
        zonas.add(construirPanelZona("EMPAQUETADO", lblEmpaquetado, barEmpaquetado, areaEmpaquetado));

        JPanel abajo = new JPanel(new GridLayout(2, 1, 5, 5));
        abajo.setOpaque(false);
        abajo.setPreferredSize(new Dimension(0, 300));
        abajo.add(construirPanelExpedicion());
        abajo.add(construirPanelRepartidores());

        panel.add(zonas, BorderLayout.CENTER);
        panel.add(abajo, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirPanelZona(String titulo, JLabel lblOcupacion, JProgressBar barra, JTextArea area) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblOcupacion.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel encabezado = new JPanel(new GridLayout(3, 1));
        encabezado.setOpaque(false);
        encabezado.add(lblTitulo);
        encabezado.add(lblOcupacion);
        encabezado.add(barra);

        area.setEditable(false);

        panel.add(encabezado, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelExpedicion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel lblTitulo = new JLabel("EXPEDICIÓN");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        lblExpedicion = new JLabel();
        lblExpedicion.setHorizontalAlignment(SwingConstants.CENTER);
        barExpedicion = new JProgressBar();

        JPanel encabezado = new JPanel(new GridLayout(3, 1));
        encabezado.setOpaque(false);
        encabezado.add(lblTitulo);
        encabezado.add(lblExpedicion);
        encabezado.add(barExpedicion);

        JPanel rutas = new JPanel(new GridLayout(1, 4, 5, 5));
        rutas.setOpaque(false);
        for (int i = 0; i < areaRutas.length; i++) {
            areaRutas[i] = new JTextArea();
            areaRutas[i].setEditable(false);
            JScrollPane scroll = new JScrollPane(areaRutas[i]);
            scroll.setBorder(BorderFactory.createTitledBorder("Ruta" + (i + 1)));
            rutas.add(scroll);
        }

        panel.add(encabezado, BorderLayout.NORTH);
        panel.add(rutas, BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelRepartidores() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel lblTitulo = new JLabel("REPARTO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel repartidores = new JPanel(new GridLayout(1, 4, 5, 5));
        repartidores.setOpaque(false);
        for (int i = 0; i < areaRepartidores.length; i++) {
            areaRepartidores[i] = new JTextArea("Repartidor " + (i + 1) + "\nsin iniciar");
            areaRepartidores[i].setEditable(false);
            repartidores.add(areaRepartidores[i]);
        }

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(repartidores, BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelRegistro() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("REGISTRO DEL SISTEMA"));

        areaRegistro = new JTextArea(9, 80);
        areaRegistro.setEditable(false);

        panel.add(new JScrollPane(areaRegistro), BorderLayout.CENTER);
        return panel;
    }

    private void actualizar() {
        actualizarZona(logistica.getRecepcion(), lblRecepcion, barRecepcion, areaRecepcion);
        actualizarZona(logistica.getAlmacen(), lblAlmacen, barAlmacen, areaAlmacen);
        actualizarZona(logistica.getClasificacion(), lblClasificacion, barClasificacion, areaClasificacion);
        actualizarZona(logistica.getEmpaquetado(), lblEmpaquetado, barEmpaquetado, areaEmpaquetado);

        actualizarProcesando();
        actualizarExpedicion();
        actualizarRepartidores();
        actualizarRegistro();

        Estadisticas est = logistica.getEstadisticas();
        lblResumen.setText("Generados: " + est.getGenerados() + " | Entregados: " + est.getEntregados()
                + " | Devueltos: " + est.getDevueltos() + " | En proceso: " + est.getEnProceso());
    }

    private void actualizarZona(ListaSincronizada<Paquete> lista, JLabel lbl, JProgressBar barra, JTextArea area) {
        List<Paquete> paquetes = lista.snapshot();
        lbl.setText(paquetes.size() + " / " + lista.getSize() + " paquetes");
        barra.setMaximum(lista.getSize());
        barra.setValue(paquetes.size());

        String texto = "";
        for (Paquete p : paquetes) {
            texto += p.getCodigo() + "  " + p.getPrioridad() + "\n";
        }
        area.setText(texto);
    }

    private void actualizarProcesando() {
        String texto = "";
        for (Clasificador c : new ArrayList<>(logistica.getClasificadores())) {
            Paquete p = c.getPaqueteActual();
            if (p == null) {
                texto += "Clasificador " + c.getNumero() + " -> libre\n";
            } else {
                texto += "Clasificador " + c.getNumero() + " -> " + p.getCodigo() + "\n";
            }
        }
        texto += "--- en cola ---\n";
        for (Paquete p : logistica.getClasificacion().snapshot()) {
            texto += p.getCodigo() + "  " + p.getRuta() + "\n";
        }
        areaClasificacion.setText(texto);

        texto = "";
        for (Empaquetar e : new ArrayList<>(logistica.getEmpaquetadores())) {
            Paquete p = e.getPaqueteActual();
            if (p == null) {
                texto += "Empaquetador " + e.getNumero() + " -> libre\n";
            } else {
                texto += "Empaquetador " + e.getNumero() + " -> " + p.getCodigo() + "\n";
            }
        }
        texto += "--- en cola ---\n";
        for (Paquete p : logistica.getEmpaquetado().snapshot()) {
            texto += p.getCodigo() + "  " + p.getPrioridad() + "\n";
        }
        areaEmpaquetado.setText(texto);
    }

    private void actualizarExpedicion() {
        List<Paquete> paquetes = logistica.getExpedicion().snapshot();
        lblExpedicion.setText(paquetes.size() + " / " + logistica.getExpedicion().getSize() + " paquetes");
        barExpedicion.setMaximum(logistica.getExpedicion().getSize());
        barExpedicion.setValue(paquetes.size());

        for (int i = 0; i < areaRutas.length; i++) {
            String ruta = "Ruta" + (i + 1);
            String texto = "";
            for (Paquete p : paquetes) {
                if (ruta.equals(p.getRuta())) {
                    texto += p.getCodigo() + "  " + p.getPrioridad() + "\n";
                }
            }
            areaRutas[i].setText(texto);
        }
    }

    private void actualizarRepartidores() {
        List<Repartir> repartidores = new ArrayList<>(logistica.getRepartidores());
        for (int i = 0; i < areaRepartidores.length; i++) {
            if (i < repartidores.size()) {
                Repartir r = repartidores.get(i);
                String texto = r.getNombre() + " (" + r.getRuta() + ")\n";
                texto += "Estado: " + r.getEstado() + "\n";
                texto += "Capacidad: " + r.getCargaActual() + "/" + r.getCapacidad();
                if (r.getCargaActual() == r.getCapacidad()) {
                    texto += "  LLENO";
                }
                texto += "\nEntregados: " + r.getPaquetesEntregados();
                areaRepartidores[i].setText(texto);
            } else {
                areaRepartidores[i].setText("Repartidor " + (i + 1) + "\nsin iniciar");
            }
        }
    }

    private void actualizarRegistro() {
        String texto = "";
        for (String linea : logistica.getRegistro().obtenerLineas()) {
            texto += linea + "\n";
        }
        areaRegistro.setText(texto);
        areaRegistro.setCaretPosition(areaRegistro.getDocument().getLength());
    }

    private void mostrarEstadisticas() {
        Estadisticas est = logistica.getEstadisticas();
        String texto = "Paquetes generados: " + est.getGenerados() + "\n";
        texto += "Entregados: " + est.getEntregados() + "\n";
        texto += "Devueltos: " + est.getDevueltos() + "\n";
        texto += "En proceso: " + est.getEnProceso() + "\n";
        texto += "Tiempo promedio: " + String.format("%.1f", est.tiempoPromedio() / 1000.0) + " s\n\n";
        for (Repartir r : new ArrayList<>(logistica.getRepartidores())) {
            texto += r.getNombre() + ": " + r.getPaquetesEntregados() + "\n";
        }
        JOptionPane.showMessageDialog(this, texto, "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }
}
