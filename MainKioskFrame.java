import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainKioskFrame extends JFrame {
    private OrderPanel orderPanel;
    private PaymentPanel paymentPanel;
    private JPanel menuPanel;
    private final List<Menu> menuList;
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;

    public MainKioskFrame() {
        setTitle("Easy KIOSK");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);

        // 메뉴 데이터 초기화
        CreateMenus menuCreator = new CreateMenus();
        this.menuList = menuCreator.createMenus();

        // 전체 UI 구성
        initializeUI();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        // 메인 패널 생성
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 1. 상단 카테고리 패널 추가
        mainPanel.add(createCategoryPanel(), BorderLayout.NORTH);

        // 2. 중앙 컨테이너 패널 생성
        JPanel centerContainerPanel = new JPanel(new BorderLayout());
        centerContainerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 2-1. 왼쪽 주문 패널
        orderPanel = new OrderPanel();
        centerContainerPanel.add(orderPanel, BorderLayout.WEST);

        // 2-2. 중앙 메뉴 패널
        createMenuPanel();
        JScrollPane menuScrollPane = new JScrollPane(menuPanel);
        menuScrollPane.setBorder(BorderFactory.createEmptyBorder());
        centerContainerPanel.add(menuScrollPane, BorderLayout.CENTER);

        // 2-3. 오른쪽 결제 패널
        paymentPanel = new PaymentPanel();
        centerContainerPanel.add(paymentPanel, BorderLayout.EAST);

        // 메인 패널에 중앙 컨테이너 추가
        mainPanel.add(centerContainerPanel, BorderLayout.CENTER);

        // 메인 패널을 프레임에 추가
        setContentPane(mainPanel);
    }

    private JPanel createCategoryPanel() {
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        categoryPanel.setBackground(new Color(255, 102, 0));
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        String[] categories = {"세트메뉴", "추천(음료)", "추천(디저트)", "커피(HOT)", "커피(ICE)"};

        for (String category : categories) {
            JButton categoryBtn = createCategoryButton(category);
            categoryPanel.add(categoryBtn);
        }

        return categoryPanel;
    }

    private JButton createCategoryButton(String category) {
        JButton categoryBtn = new JButton(category);
        categoryBtn.setForeground(Color.WHITE);
        categoryBtn.setBackground(new Color(255, 102, 0));
        categoryBtn.setBorderPainted(false);
        categoryBtn.setFocusPainted(false);
        categoryBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        // 버튼 이벤트
        categoryBtn.addActionListener(e -> filterMenusByCategory(category));

        // 마우스 오버 효과
        categoryBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                categoryBtn.setBackground(new Color(255, 140, 0));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                categoryBtn.setBackground(new Color(255, 102, 0));
            }
        });

        return categoryBtn;
    }

    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 3, 20, 20));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        displayMenus(menuList);
    }

    private void displayMenus(List<Menu> menus) {
        menuPanel.removeAll();
        for (Menu menu : menus) {
            menuPanel.add(createMenuCard(menu));
        }
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private JPanel createMenuCard(Menu menu) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // 이미지 패널
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(Color.WHITE);
        JLabel imageLabel = new JLabel();

        try {
            ImageIcon icon = new ImageIcon(menu.getImagePath());
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Error loading image for menu item: " + menu.getName());
            imageLabel.setText("No Image");
        }

        imagePanel.add(imageLabel);
        card.add(imagePanel, BorderLayout.CENTER);

        // 정보 패널
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(menu.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        JLabel priceLabel = new JLabel(String.format("%,d원", menu.getPrice()), SwingConstants.CENTER);
        priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        card.add(infoPanel, BorderLayout.SOUTH);

        // 카드 선택 이벤트
        addMenuCardListeners(card, imagePanel, infoPanel, menu);

        return card;
    }

    private void addMenuCardListeners(JPanel card, JPanel imagePanel, JPanel infoPanel, Menu menu) {
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(menu);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setComponentsBackground(new Color(245, 245, 245), card, imagePanel, infoPanel);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setComponentsBackground(Color.WHITE, card, imagePanel, infoPanel);
            }
        });
    }

    private void setComponentsBackground(Color color, JPanel... panels) {
        for (JPanel panel : panels) {
            panel.setBackground(color);
        }
    }

    private void filterMenusByCategory(String category) {
        List<Menu> filteredMenus = menuList.stream()
                .filter(menu -> menu.getCategory().equals(category))
                .toList();
        displayMenus(filteredMenus);
    }

    private void selectMenuItem(Menu menu) {
        if (menu.getStock() > 0) {
            orderPanel.addMenuItem(menu);
            menu.decreaseStock(1);

            // 결제 패널 업데이트
            paymentPanel.updateItemCount(orderPanel.getItemCount());
            paymentPanel.updateTotalAmount(orderPanel.getTotalAmount());
        } else {
            JOptionPane.showMessageDialog(this,
                    "죄송합니다. 해당 메뉴는 품절되었습니다.",
                    "품절",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Error setting look and feel: " + e.getMessage());
            }
            new MainKioskFrame();
        });
    }
}
