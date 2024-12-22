import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PaymentPanel extends JPanel {
    private JLabel timeLabel;
    private JLabel itemCountLabel;
    private JButton paymentButton;
    private JButton deleteButton;
    private Timer timer;
    private int timeRemaining;
    private ActionListener deleteListener;
    private OrderPanel orderPanel;
    private int totalAmount;

    public PaymentPanel() {
        setLayout(new GridLayout(4, 1, 5, 5));
        setPreferredSize(new Dimension(300, 150));
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        timeLabel = new JLabel("남은시간: 119초", SwingConstants.CENTER);
        timeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        timeRemaining = 119;

        itemCountLabel = new JLabel("선택한 상품: 0개", SwingConstants.CENTER);
        itemCountLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));

        deleteButton = new JButton("선택 삭제");
        deleteButton.setBackground(new Color(135, 206, 235));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);

        paymentButton = new JButton("0원 결제하기");
        paymentButton.setBackground(new Color(135, 206, 235));
        paymentButton.setForeground(Color.WHITE);
        paymentButton.setFocusPainted(false);
        paymentButton.setBorderPainted(false);

        paymentButton.addActionListener(e -> {
            new PaymentFrame(orderPanel.getSelectedMenus(), totalAmount);
        });

        add(timeLabel);
        add(itemCountLabel);
        add(deleteButton);
        add(paymentButton);

        startTimer();
    }

    public void setOrderPanel(OrderPanel orderPanel) {
        this.orderPanel = orderPanel;
    }

    private void startTimer() {
        if (timer != null && timer.isRunning()) {
            return;
        }

        timer = new Timer(1000, e -> {
            if (timeRemaining > 0) {
                timeRemaining--;
                timeLabel.setText("남은시간: " + timeRemaining + "초");
            } else {
                int option = JOptionPane.showOptionDialog(
                        SwingUtilities.getWindowAncestor(this),
                        "주문 시간이 만료되었습니다.",
                        "시간 초과",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        new Object[] { "확인" },
                        "확인"
                );

                if (option == JOptionPane.OK_OPTION) {
                    updateItemCount(0);
                    resetTimer();
                    if (orderPanel != null) {
                        orderPanel.clearAllItems();
                    }
                }
            }
        });
        timer.start();
    }

    public void setDeleteActionListener(ActionListener listener) {
        if (deleteListener != null) {
            deleteButton.removeActionListener(deleteListener);
        }
        deleteListener = listener;
        deleteButton.addActionListener(listener);
    }

    public void updateItemCount(int count) {
        itemCountLabel.setText("선택한 상품: " + count + "개");
    }

    public void updateTotalAmount(int amount) {
        this.totalAmount = amount;
        paymentButton.setText(amount + "원 결제하기");
    }

    private void resetTimer() {
        timeRemaining = 119;
        timeLabel.setText("남은시간: " + timeRemaining + "초");
        startTimer();
    }
}
