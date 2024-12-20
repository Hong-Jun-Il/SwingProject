public class PaymentPanel extends JPanel {
    private JLabel timeLabel;
    private JLabel itemCountLabel;
    private JButton paymentButton;
    private Timer timer;
    private int timeRemaining;

    public PaymentPanel() {
        setLayout(new GridLayout(3, 1, 0, 10));
        setPreferredSize(new Dimension(200, 0)); // 오른쪽 패널 너비 설정

        // 시간 표시
        timeLabel = new JLabel("남은시간: 119초", SwingConstants.CENTER);
        timeRemaining = 119;

        // 선택한 상품 개수
        itemCountLabel = new JLabel("선택한 상품: 0개", SwingConstants.CENTER);

        // 결제 버튼
        paymentButton = new JButton("0원 결제하기");
        paymentButton.setBackground(Color.RED);
        paymentButton.setForeground(Color.WHITE);

        add(timeLabel);
        add(itemCountLabel);
        add(paymentButton);

        startTimer();
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            timeRemaining--;
            timeLabel.setText("남은시간: " + timeRemaining + "초");
            if (timeRemaining <= 0) {
                ((Timer)e.getSource()).stop();
                JOptionPane.showMessageDialog(null, "시간이 초과되었습니다.");
                resetTimer();
            }
        });
        timer.start();
    }

    public void updateItemCount(int count) {
        itemCountLabel.setText("선택한 상품: " + count + "개");
    }

    public void updateTotalAmount(int amount) {
        paymentButton.setText(amount + "원 결제하기");
    }

    private void resetTimer() {
        timeRemaining = 119;
        timeLabel.setText("남은시간: " + timeRemaining + "초");
        timer.restart();
    }
}