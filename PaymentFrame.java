import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;

public class PaymentFrame extends JFrame {
    private static final Color THEME_COLOR = new Color(135, 206, 235);
    private static final Color WHITE_COLOR = Color.WHITE;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private int totalAmount;
    private Map<Integer, OrderPanel.OrderItem> orderItems;

    private Map<String, JButton> stepButtonsMap = new HashMap<>();

    public PaymentFrame(Map<Integer, OrderPanel.OrderItem> orderItems, int totalAmount) {
        this.orderItems = orderItems;
        this.totalAmount = totalAmount;

        setTitle("결제하기");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));  // 2열 그리드 레이아웃

        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        getContentPane().setBackground(THEME_COLOR);

        leftPanel = createLeftPanel();
        leftPanel.setBackground(THEME_COLOR);
        add(leftPanel);

        rightPanel = createRightPanel();
        rightPanel.setBackground(THEME_COLOR);
        add(rightPanel);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"제품", "수량", "금액"};
        Object[][] data = new Object[orderItems.size() + 2][3];  // +2 for subtotal and total

        int row = 0;
        for (OrderPanel.OrderItem item : orderItems.values()) {
            data[row][0] = item.getMenu().getName();
            data[row][1] = item.getQuantity();
            data[row][2] = item.getMenu().getPrice() * item.getQuantity();
            row++;
        }

        data[row][0] = "------------------------";
        data[row][1] = "-----";
        data[row][2] = "------------------------";
        row++;

        data[row][0] = "결제 합계";
        data[row][1] = "";
        data[row][2] = totalAmount;

        JTable table = new JTable(data, columns);
        table.setEnabled(false);
        table.setBackground(WHITE_COLOR);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel step1Panel = new JPanel(new BorderLayout());
        step1Panel.add(new JLabel("Step 1. 포장/매장을 선택해주세요."), BorderLayout.NORTH);
        JPanel buttonPanel1 = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel1.add(createStepButton("포장", "step1"));
        buttonPanel1.add(createStepButton("매장", "step1"));
        step1Panel.add(buttonPanel1, BorderLayout.CENTER);
        panel.add(step1Panel);

        JPanel step2Panel = new JPanel(new BorderLayout());
        step2Panel.add(new JLabel("Step 2. 결제방법/선물을 선택해주세요."), BorderLayout.NORTH);
        JPanel buttonPanel2 = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel2.add(createStepButton("결제하기", "step2"));
        buttonPanel2.add(createStepButton("할인/적립하기", "step2"));
        step2Panel.add(buttonPanel2, BorderLayout.CENTER);
        panel.add(step2Panel);

        JPanel step3Panel = new JPanel(new BorderLayout());
        step3Panel.add(new JLabel("Step 3. 결제를 선택하세요."), BorderLayout.NORTH);
        JPanel paymentPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        paymentPanel.add(createStepButton("신용/체크", "step3"));
        paymentPanel.add(createStepButton("모바일페이", "step3"));
        step3Panel.add(paymentPanel, BorderLayout.CENTER);
        panel.add(step3Panel);

        return panel;
    }

    private JButton createStepButton(String text, String step) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 80));
        button.setBackground(WHITE_COLOR);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        button.setFocusPainted(false);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton previousButton = stepButtonsMap.get(step);
                if (previousButton != null) {
                    previousButton.setBackground(WHITE_COLOR);
                }
                button.setBackground(new Color(169, 169, 169));
                stepButtonsMap.put(step, button);

                if (step.equals("step3")) {
                    String soundFile = button.getText().equals("신용/체크") ? "sound/putTheCard.wav" : "sound/phone.wav";

                    playSound(soundFile);

                    Timer timer = new Timer(3000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            playSound("sound/pay.wav");

                            // 결제 완료 후 확인 메시지 표시
                            int option = JOptionPane.showOptionDialog(
                                    PaymentFrame.this,
                                    "결제가 완료되었습니다.\n영수증을 출력하시겠습니까?",
                                    "결제 완료",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,
                                    null, // 기본 아이콘
                                    new Object[] {"예", "아니요"}, // 버튼 옵션
                                    "예" // 기본 선택 버튼
                            );

                            // 사용자가 "예"를 클릭한 경우 영수증 출력
                            if (option == JOptionPane.YES_OPTION) {
                                saveReceiptToFile();
                            }

                            // 창 닫기
                            dispose();
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        });

        return button;
    }

    private void playSound(String soundFilePath) {
        try {
            InputStream soundStream = PaymentFrame.class.getClassLoader().getResourceAsStream(soundFilePath);
            if (soundStream == null) {
                System.err.println("Could not find sound file: " + soundFilePath);
                return;
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(soundStream)
            );
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveReceiptToFile() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("-------- 영수증 --------\n");
        receipt.append("주문 내역:\n");

        for (OrderPanel.OrderItem item : orderItems.values()) {
            receipt.append(item.getMenu().getName())
                    .append(" | 수량: ").append(item.getQuantity())
                    .append(" | 금액: ").append(item.getMenu().getPrice() * item.getQuantity())
                    .append("원\n");
        }

        receipt.append("-----------------------\n");
        receipt.append("결제 합계: ").append(totalAmount).append("원\n");
        receipt.append("-----------------------\n");
        receipt.append("감사합니다!\n");

        File receiptFile = new File("영수증.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(receiptFile))) {
            writer.write(receipt.toString());
            writer.flush();
            JOptionPane.showMessageDialog(
                    PaymentFrame.this,
                    "영수증이 저장되었습니다: " + receiptFile.getAbsolutePath(),
                    "영수증 저장",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    PaymentFrame.this,
                    "영수증 저장에 실패했습니다.",
                    "오류",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }
}