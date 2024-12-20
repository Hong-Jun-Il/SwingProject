import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class OrderPanel extends JPanel {
    private Map<Integer, OrderItem> selectedMenus; // 메뉴 ID와 OrderItem(메뉴, 수량) 매핑
    private JPanel orderListPanel;
    private int totalAmount;

    public OrderPanel() {
        selectedMenus = new HashMap<>();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 0)); // 왼쪽 패널 너비 설정

        // 주문 목록 패널
        orderListPanel = new JPanel();
        orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(orderListPanel);

        add(scrollPane, BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder("주문 목록"));
    }

    public void addMenuItem(Menu menu) {
        if (selectedMenus.containsKey(menu.getId())) {
            // 이미 선택된 메뉴인 경우, 수량만 증가
            OrderItem orderItem = selectedMenus.get(menu.getId());
            orderItem.incrementQuantity();
        } else {
            // 새로운 메뉴 추가
            OrderItem orderItem = new OrderItem(menu);
            selectedMenus.put(menu.getId(), orderItem);

            // 주문 목록에 추가
            JPanel itemPanel = new JPanel(new BorderLayout());
            JLabel nameLabel = new JLabel(menu.getName());
            JLabel quantityLabel = new JLabel("1개");
            JLabel priceLabel = new JLabel(menu.getPrice() + "원");

            itemPanel.add(nameLabel, BorderLayout.WEST);
            itemPanel.add(quantityLabel, BorderLayout.CENTER);
            itemPanel.add(priceLabel, BorderLayout.EAST);

            orderListPanel.add(itemPanel);
            orderItem.setPanel(itemPanel);
            orderItem.setQuantityLabel(quantityLabel);
        }

        // 금액 및 UI 갱신
        totalAmount += menu.getPrice();
        revalidate();
        repaint();
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getItemCount() {
        return selectedMenus.size();
    }

    // 내부 클래스: 주문 항목 관리
    private static class OrderItem {
        private final Menu menu;
        private int quantity;
        private JPanel panel;
        private JLabel quantityLabel;

        public OrderItem(Menu menu) {
            this.menu = menu;
            this.quantity = 1;
        }

        public void incrementQuantity() {
            quantity++;
            if (quantityLabel != null) {
                quantityLabel.setText(quantity + "개");
            }
        }

        public Menu getMenu() {
            return menu;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setPanel(JPanel panel) {
            this.panel = panel;
        }

        public void setQuantityLabel(JLabel quantityLabel) {
            this.quantityLabel = quantityLabel;
        }
    }
}
