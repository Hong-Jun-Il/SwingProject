import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class PaymentFrame extends JFrame {
    private static final Color THEME_COLOR = new Color(135, 206, 235); // 스카이 블루 테마 색상
    private static final Color WHITE_COLOR = Color.WHITE; // 흰색
    private JPanel leftPanel;  // 주문 정보 패널
    private JPanel rightPanel; // 결제 단계 패널
    private int totalAmount;
    private Map<Integer, OrderPanel.OrderItem> orderItems;

    // 클릭한 버튼을 추적하기 위한 맵
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
        // 전체 배경색을 스카이 블루로 설정
        getContentPane().setBackground(THEME_COLOR);

        // 왼쪽 패널 (주문 정보)
        leftPanel = createLeftPanel();
        leftPanel.setBackground(THEME_COLOR);
        add(leftPanel);

        // 오른쪽 패널 (결제 단계)
        rightPanel = createRightPanel();
        rightPanel.setBackground(THEME_COLOR);
        add(rightPanel);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 주문 내역 테이블
        String[] columns = {"제품", "수량", "금액"};
        Object[][] data = new Object[orderItems.size() + 2][3];  // +2 for subtotal and total

        int row = 0;
        for (OrderPanel.OrderItem item : orderItems.values()) {
            data[row][0] = item.getMenu().getName();
            data[row][1] = item.getQuantity();
            data[row][2] = item.getMenu().getPrice() * item.getQuantity();
            row++;
        }

        // 구분선
        data[row][0] = "------------------------";
        data[row][1] = "-----";
        data[row][2] = "------------------------";
        row++;

        // 총액
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

        // Step 1: 포장/매장
        JPanel step1Panel = new JPanel(new BorderLayout());
        step1Panel.add(new JLabel("Step 1. 포장/매장을 선택해주세요."), BorderLayout.NORTH);
        JPanel buttonPanel1 = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel1.add(createStepButton("포장", "step1"));
        buttonPanel1.add(createStepButton("매장", "step1"));
        step1Panel.add(buttonPanel1, BorderLayout.CENTER);
        panel.add(step1Panel);

        // Step 2: 결제수단
        JPanel step2Panel = new JPanel(new BorderLayout());
        step2Panel.add(new JLabel("Step 2. 결제방법/선물을 선택해주세요."), BorderLayout.NORTH);
        JPanel buttonPanel2 = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel2.add(createStepButton("결제하기", "step2"));
        buttonPanel2.add(createStepButton("선물하기", "step2"));
        step2Panel.add(buttonPanel2, BorderLayout.CENTER);
        panel.add(step2Panel);

        // Step 3: 결제
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

        // 버튼 클릭 시 색상 변경 및 사운드 재생
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 이전에 선택된 버튼의 색상 원래대로 되돌리기
                JButton previousButton = stepButtonsMap.get(step);
                if (previousButton != null) {
                    previousButton.setBackground(WHITE_COLOR);
                }
                // 현재 클릭한 버튼 색상 변경
                button.setBackground(new Color(169, 169, 169));
                // 현재 버튼을 선택된 버튼으로 저장
                stepButtonsMap.put(step, button);

                // step3 버튼 클릭 시 사운드 재생
                if (step.equals("step3")) {
                    playSound(button.getText().equals("신용/체크") ? "sound/putTheCard.wav" : "sound/phone.wav");
                }
            }
        });

        return button;
    }

    private void playSound(String soundFilePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFilePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
