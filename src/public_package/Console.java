package public_package;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

class Console {
    private final JFrame frame = new JFrame();
    public Console() {
        JTextArea textArea = new JTextArea(24, 80);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.LIGHT_GRAY);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                textArea.append(String.valueOf((char) b));
            }
        }));
        frame.add(textArea);
        init();
    }
    private void init() {
        frame.pack();
        frame.setVisible(true);
    }
    public JFrame getFrame() {
        return frame;
    }
}