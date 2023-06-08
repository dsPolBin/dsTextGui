package org.example;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

public class Main extends JFrame {
    private int posX;
    private int posY;

    public Main() {
        setTitle("dsProgram - PolBin");
        setSize(600, 400);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
        getContentPane().setBackground(Color.DARK_GRAY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Dodanie obsługi zdarzeń myszy dla przeciągania okna
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                posX = e.getX();
                posY = e.getY();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int dx = e.getXOnScreen() - posX;
                int dy = e.getYOnScreen() - posY;
                setLocation(dx, dy);
            }
        });

        // Dodanie nazwy na górze okna
        JLabel titleLabel = new JLabel("dsProgram - Polbin");
        titleLabel.setForeground(Color.LIGHT_GRAY);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.DARK_GRAY.darker()); // Ciemniejsze tło
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.DARK_GRAY);

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.TRUETYPE_FONT, 20));
        textArea.setPreferredSize(new Dimension(450, 250));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setForeground(Color.LIGHT_GRAY);
        textArea.setBackground(Color.DARK_GRAY.darker());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.CENTER;

        centerPanel.add(textArea, gbc);

        // Utworzenie przycisku "Generuj"
        JButton generateButton = new RoundButton("Generuj");
        generateButton.setFont(new Font("Arial", Font.PLAIN, 14));
        generateButton.setBackground(Color.DARK_GRAY.darker());
        generateButton.setForeground(Color.LIGHT_GRAY); // Ustawienie jasnego koloru tekstu
        generateButton.setPreferredSize(new Dimension(120, 40));
        generateButton.addActionListener(e -> {
            // Tutaj umieść kod generujący wynik
            String text = textArea.getText();
            try {
                speakText(text);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(Main.this, "Błąd odczytu tekstu");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.add(generateButton);

        // Dodanie pustego komponentu Glue przed przyciskiem
        GridBagConstraints glueGbc = new GridBagConstraints();
        glueGbc.gridx = 0;
        glueGbc.gridy = 1;
        glueGbc.weighty = 0.5;
        centerPanel.add(Box.createGlue(), glueGbc);

        // Dodanie przycisku "Generuj"
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 2;
        buttonGbc.weighty = 0.9;
        buttonGbc.anchor = GridBagConstraints.NORTH; // Ustawienie przycisku do górnej krawędzi
        centerPanel.add(buttonPanel, buttonGbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void speakText(String text) throws IOException {
        // Wywołanie eSpeak z linii poleceń
        String command = "espeak -v pl \"" + text + "\" --stdout | aplay";
        Process process = Runtime.getRuntime().exec(command);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}

class RoundButton extends JButton {
    public RoundButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.setColor(getForeground());
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(getText(), x, y);
        g2.dispose();
    }
}
