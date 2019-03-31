import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Snake extends JPanel implements KeyListener, ActionListener {
    private JFrame frame = new JFrame("Snake");
    private JLabel score = new JLabel("Score: 0 ");
    private int grid = 25;
    private int size;
    private ArrayList<Dimension> snake;
    private Timer tm = new Timer(175, this);
    private int vecX, vecY;
    private Dimension appleXY;
    private int points;

    private Snake() {
        score.setOpaque(true);
        score.setBackground(Color.LIGHT_GRAY);
        score.setHorizontalAlignment(SwingConstants.RIGHT);
        frame.getContentPane().add(score, BorderLayout.NORTH);
        setPreferredSize(new Dimension(700, 700));
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createRaisedBevelBorder());
        frame.getContentPane().add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth()) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight()) / 2);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
        size = getWidth() / grid;
        reset();
    }

    private void reset() {
        points = 0;
        vecX = 1;
        vecY = 0;
        snake = new ArrayList<>();
        snake.add(new Dimension(2, 0));
        snake.add(new Dimension(1, 0));
        snake.add(new Dimension(0, 0));
        genApple();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            updateSnake(vecX = 0, vecY = -1);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            updateSnake(vecX = 0, vecY = 1);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            updateSnake(vecX = -1, vecY = 0);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            updateSnake(vecX = 1, vecY = 0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    private void genApple() {
        boolean onSnake = false;
        do {
            appleXY = new Dimension((int)(Math.random() * grid), (int)(Math.random() * grid));
            for (Dimension d : snake) {
                onSnake = d.equals(appleXY);
                if (onSnake) break;
            }
        } while (onSnake);
    }

    private boolean snakeOnSnake() {
        boolean onSnake = false;
        for (int i = 1; i < snake.size(); i++) {
            onSnake = snake.get(0).width + vecX == snake.get(i).width && snake.get(0).height + vecY == snake.get(i).height;
            if (onSnake) break;
        }
        return onSnake;
    }

    private void updateSnake(int x, int y) {
        tm.stop();
        Dimension tail = snake.get(snake.size() - 1);
        if (snake.get(0).width + x < grid && snake.get(0).width + x >= 0 && snake.get(0).height + y < grid && snake.get(0).height + y >= 0 && !snakeOnSnake()) {
            for (int i = snake.size() - 1; i > 0; i--) {
                snake.set(i, snake.get(i - 1));
            }
            snake.set(0, new Dimension(snake.get(0).width + x, snake.get(0).height + y));
        } else {
            int resetOrExit = JOptionPane.showConfirmDialog(frame, "Your Score: " + points + "\nPlay again?", "You DEAD!", JOptionPane.YES_NO_OPTION);
            if (resetOrExit == 0) {
                reset();
            } else {
                System.exit(0);
            }
        }
        if (snake.get(0).equals(appleXY)) {
            score.setText("Score: " + ++points + " ");
            snake.add(tail);
            genApple();
        }
        repaint();
    }

    private void paintSnake(Graphics g) {
        for (Dimension d : snake) {
            g.setColor(Color.GREEN);
            paintBox(g, d);
        }
    }

    private void paintApple(Graphics g) {
        g.setColor(Color.RED);
        paintBox(g, appleXY);
    }

    private void paintBox(Graphics g, Dimension d) {
        g.fillRect(d.width * size, d.height * size, size, size);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(d.width * size, d.height * size, size, size);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintApple(g);
        paintSnake(g);
        tm.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateSnake(vecX, vecY);
    }

    public static void main(String[] args) {
        new Snake();
    }
}