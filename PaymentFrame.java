import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class PaymentFrame extends JFrame {
    private JPanel leftPanel;  // 주문 정보 패널
    private JPanel rightPanel; // 결제 단계 패널
    private int totalAmount;
    private Map<Integer, OrderPanel.OrderItem> orderItems;

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
        // 왼쪽 패널 (주문 정보)
        leftPanel = createLeftPanel();
        add(leftPanel);

        // 오른쪽 패널 (결제 단계)
        rightPanel = createRightPanel();
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
        buttonPanel1.add(createStepButton("포장", "/path/to/takeout/icon"));
        buttonPanel1.add(createStepButton("매장", "/path/to/store/icon"));
        step1Panel.add(buttonPanel1, BorderLayout.CENTER);
        panel.add(step1Panel);

        // Step 2: 결제수단
        JPanel step2Panel = new JPanel(new BorderLayout());
        step2Panel.add(new JLabel("Step 2. 결제방법/선물을 선택해주세요."), BorderLayout.NORTH);
        JPanel buttonPanel2 = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel2.add(createStepButton("결제하기", "/path/to/payment/icon"));
        buttonPanel2.add(createStepButton("선물하기", "/path/to/gift/icon"));
        step2Panel.add(buttonPanel2, BorderLayout.CENTER);
        panel.add(step2Panel);

        // Step 3: 결제
        JPanel step3Panel = new JPanel(new BorderLayout());
        step3Panel.add(new JLabel("Step 3. 결제를 선택하세요."), BorderLayout.NORTH);
        JPanel paymentPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        paymentPanel.add(createStepButton("신용/체크", "/path/to/card/icon"));
        paymentPanel.add(createStepButton("모바일페이", "/path/to/mobile/icon"));
        paymentPanel.add(createStepButton("간편결제", "/path/to/easy/icon"));
        step3Panel.add(paymentPanel, BorderLayout.CENTER);
        panel.add(step3Panel);

        return panel;
    }

    private JButton createStepButton(String text, String iconPath) {
        JButton button = new JButton(text);
        // 아이콘 설정 (실제 아이콘 경로 필요)
        // button.setIcon(new ImageIcon(iconPath));
        button.setPreferredSize(new Dimension(120, 80));
        return button;
    }
}