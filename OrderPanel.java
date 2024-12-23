import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class OrderPanel extends JPanel {
    private Map<Integer, OrderItem> selectedMenus;
    private JPanel orderListPanel;
    private int totalAmount;
    private PaymentPanel paymentPanel;

    public OrderPanel() {
        selectedMenus = new HashMap<>();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 400));
        setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(135, 206, 235));
        JLabel titleLabel = new JLabel("선택한 메뉴", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        orderListPanel = new JPanel();
        orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));
        orderListPanel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(orderListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setPaymentPanel(PaymentPanel paymentPanel) {
        this.paymentPanel = paymentPanel;
    }

    public void addMenuItem(Menu menu) {
        if (selectedMenus.containsKey(menu.getId())) {
            OrderItem orderItem = selectedMenus.get(menu.getId());
            orderItem.incrementQuantity();
            updateOrderItem(orderItem);
        } else {
            OrderItem orderItem = new OrderItem(menu);
            selectedMenus.put(menu.getId(), orderItem);
            addNewOrderItemPanel(orderItem);
        }

        updateTotalAmount();
        revalidate();
        repaint();
    }

    private void addNewOrderItemPanel(OrderItem orderItem) {
        JPanel itemPanel = new JPanel(new BorderLayout(5, 0));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        itemPanel.setPreferredSize(new Dimension(280, 40));

        JLabel nameLabel = new JLabel(orderItem.getMenu().getName());
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        nameLabel.setPreferredSize(new Dimension(100, 30));
        itemPanel.add(nameLabel, BorderLayout.WEST);

        JPanel quantityPanel = new JPanel(new GridBagLayout()); // GridBagLayout으로 변경
        quantityPanel.setBackground(Color.WHITE);

        JButton minusBtn = createQuantityButton("-");
        JLabel quantityLabel = new JLabel("1", SwingConstants.CENTER);
        JButton plusBtn = createQuantityButton("+");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5); // 버튼 간격
        gbc.anchor = GridBagConstraints.CENTER; // 중앙 정렬

        gbc.gridx = 0;
        gbc.gridy = 0;
        quantityPanel.add(minusBtn, gbc);

        gbc.gridx = 1;
        quantityPanel.add(quantityLabel, gbc);

        gbc.gridx = 2;
        quantityPanel.add(plusBtn, gbc);

        itemPanel.add(quantityPanel, BorderLayout.CENTER);

        JLabel priceLabel = new JLabel(orderItem.getMenu().getPrice() + "원", SwingConstants.RIGHT);
        priceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        priceLabel.setPreferredSize(new Dimension(80, 30));
        itemPanel.add(priceLabel, BorderLayout.EAST);

        minusBtn.addActionListener(e -> {
            int quantity = orderItem.getQuantity();
            if (quantity > 1) {
                orderItem.setQuantity(quantity - 1);
                quantityLabel.setText(String.valueOf(quantity - 1));
                priceLabel.setText((orderItem.getMenu().getPrice() * (quantity - 1)) + "원");
                updateTotalAmount();
            }
        });

        plusBtn.addActionListener(e -> {
            int quantity = orderItem.getQuantity();
            orderItem.setQuantity(quantity + 1);
            quantityLabel.setText(String.valueOf(quantity + 1));
            priceLabel.setText((orderItem.getMenu().getPrice() * (quantity + 1)) + "원");
            updateTotalAmount();
        });

        orderListPanel.add(itemPanel);
        orderItem.setPanel(itemPanel);
        orderItem.setQuantityLabel(quantityLabel);
        orderItem.setPriceLabel(priceLabel);

        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }


    private JButton createQuantityButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(45, 30));
        button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBackground(new Color(135, 206, 235));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        return button;
    }

    private void updateOrderItem(OrderItem orderItem) {
        orderItem.getQuantityLabel().setText(String.valueOf(orderItem.getQuantity()));
        int newPrice = orderItem.getMenu().getPrice() * orderItem.getQuantity();
        orderItem.getPriceLabel().setText(newPrice + "원");
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        totalAmount = 0;
        for (OrderItem item : selectedMenus.values()) {
            totalAmount += item.getMenu().getPrice() * item.getQuantity();
        }
        if (paymentPanel != null) {
            paymentPanel.updateTotalAmount(totalAmount);
        }
    }

//    public void removeSelectedItem(Menu menu) {
//        OrderItem orderItem = selectedMenus.get(menu.getId());
//        if (orderItem != null) {
//            orderListPanel.remove(orderItem.getPanel());
//            selectedMenus.remove(menu.getId());
//            updateTotalAmount();
//            revalidate();
//            repaint();
//        }
//    }

    public void clearAllItems() {
        selectedMenus.clear();
        orderListPanel.removeAll();
        totalAmount = 0;
        updateTotalAmount();
        revalidate();
        repaint();
    }

//    public int getTotalAmount() {
//        return totalAmount;
//    }

    public int getItemCount() {
        return selectedMenus.size();
    }

    public Map<Integer, OrderItem> getSelectedMenus() {
        return selectedMenus;
    }

    public static class OrderItem {
        private final Menu menu;
        private int quantity;
        private JPanel panel;
        private JLabel quantityLabel;
        private JLabel priceLabel;

        public OrderItem(Menu menu) {
            this.menu = menu;
            this.quantity = 1;
        }

        public void incrementQuantity() {
            quantity++;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
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

        public void setPriceLabel(JLabel priceLabel) {
            this.priceLabel = priceLabel;
        }

        public JPanel getPanel() {
            return panel;
        }

        public JLabel getQuantityLabel() {
            return quantityLabel;
        }

        public JLabel getPriceLabel() {
            return priceLabel;
        }
    }
}