public class OrderPanel extends JPanel {
    private List<Menu> selectedMenus;
    private JPanel orderListPanel;
    private int totalAmount;

    public OrderPanel() {
        selectedMenus = new ArrayList<>();
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
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.add(new JLabel(menu.getName()), BorderLayout.WEST);
        itemPanel.add(new JLabel(String.valueOf(menu.getPrice()) + "원"), BorderLayout.EAST);

        orderListPanel.add(itemPanel);
        selectedMenus.add(menu);
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
}