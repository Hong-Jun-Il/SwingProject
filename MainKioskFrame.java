import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.List;

public class MainKioskFrame extends JFrame {
    private OrderPanel orderPanel;
    private PaymentPanel paymentPanel;
    private JPanel menuPanel;
    private final List<Menu> menuList;
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private static final Color THEME_COLOR = new Color(135, 206, 235);

    public MainKioskFrame() {
        setTitle("카페 디버그(Cafe Debug)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);

        // 메뉴 데이터 초기화
        CreateMenus menuCreator = new CreateMenus();
        this.menuList = menuCreator.createMenus();

        // 전체 UI 구성
        initializeUI();
        playSound("sound/selectMenu.wav");
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        // 메인 패널 생성
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(THEME_COLOR);

        // 1. 상단 카테고리 패널 추가
        mainPanel.add(createCategoryPanel(), BorderLayout.NORTH);

        // 2. 중앙 컨테이너 패널 생성
        JPanel centerContainerPanel = new JPanel(new BorderLayout(10, 0));
        centerContainerPanel.setBackground(THEME_COLOR);
        centerContainerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 2-1. 중앙 메뉴 패널
        createMenuPanel();
        JScrollPane menuScrollPane = new JScrollPane(menuPanel);
        menuScrollPane.setBorder(BorderFactory.createEmptyBorder());
        centerContainerPanel.add(menuScrollPane, BorderLayout.CENTER);

        // 2-2. 오른쪽 통합 패널 (주문목록 + 결제정보)
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBackground(THEME_COLOR);

        // 주문 목록 패널
        orderPanel = new OrderPanel();
        rightPanel.add(orderPanel, BorderLayout.CENTER);

        // 결제 정보 패널
        paymentPanel = new PaymentPanel();
        paymentPanel.setOrderPanel(orderPanel);
        orderPanel.setPaymentPanel(paymentPanel);
        paymentPanel.setDeleteActionListener(e -> handleDeleteAction());
        rightPanel.add(paymentPanel, BorderLayout.SOUTH);

        centerContainerPanel.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(centerContainerPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private JPanel createCategoryPanel() {
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        categoryPanel.setBackground(THEME_COLOR);
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        String[] categories = {"전체", "커피", "디저트", "에이드", "티"};

        for (String category : categories) {
            JButton categoryBtn = createCategoryButton(category);
            categoryPanel.add(categoryBtn);
        }

        return categoryPanel;
    }

    private JButton createCategoryButton(String category) {
        JButton categoryBtn = new JButton(category);
        categoryBtn.setForeground(Color.WHITE);
        categoryBtn.setBackground(THEME_COLOR);
        categoryBtn.setBorderPainted(false);
        categoryBtn.setFocusPainted(false);
        categoryBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        categoryBtn.addActionListener(e -> filterMenusByCategory(category));
        categoryBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                categoryBtn.setBackground(THEME_COLOR.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                categoryBtn.setBackground(THEME_COLOR);
            }
        });

        return categoryBtn;
    }

    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 4, 10, 10));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        card.setLayout(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // 이미지 패널
        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setBackground(Color.WHITE);
        JLabel imageLabel = new JLabel();

        try {
            URL imageUrl = MainKioskFrame.class.getClassLoader().getResource(menu.getImagePath());
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
            } else {
                System.err.println("이미지를 찾을 수 없습니다: " + menu.getImagePath());
                imageLabel.setText("No Image");
            }
        } catch (Exception e) {
            System.err.println("이미지 로딩 에러: " + menu.getName());
            e.printStackTrace();
            imageLabel.setText("No Image");
        }

        // GridBagConstraints 설정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // 중앙 정렬
        imagePanel.add(imageLabel, gbc);

        card.add(imagePanel, BorderLayout.CENTER);

        // 정보 패널
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(menu.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        JLabel priceLabel = new JLabel(menu.getPrice() + "원", SwingConstants.CENTER);
        priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        card.add(infoPanel, BorderLayout.SOUTH);

        // 메뉴 선택 이벤트
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

        return card;
    }

    private void setComponentsBackground(Color color, JPanel... panels) {
        for (JPanel panel : panels) {
            panel.setBackground(color);
        }
    }

    private void filterMenusByCategory(String category) {
        List<Menu> filteredMenus;
        if ("전체".equals(category)) {
            filteredMenus = menuList;
        } else {
            filteredMenus = menuList.stream()
                    .filter(menu -> menu.getCategory().equals(category))
                    .toList();
        }
        displayMenus(filteredMenus);
    }

    private void selectMenuItem(Menu menu) {
        if (menu.getStock() > 0) {
            orderPanel.addMenuItem(menu);
            menu.decreaseStock(1);
            paymentPanel.updateItemCount(orderPanel.getItemCount());
        } else {
            JOptionPane.showMessageDialog(this,
                    "죄송합니다. 해당 메뉴는 품절되었습니다.",
                    "품절",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleDeleteAction() {
        int selectedOption = JOptionPane.showConfirmDialog(
                this,
                "선택한 메뉴를 삭제하시겠습니까?",
                "메뉴 삭제",
                JOptionPane.YES_NO_OPTION
        );

        if (selectedOption == JOptionPane.YES_OPTION) {
            orderPanel.clearAllItems();
            paymentPanel.updateItemCount(0);
            paymentPanel.updateTotalAmount(0);
        }
    }

    private void playSound(String soundFilePath) {
        try {
            InputStream soundStream = MainKioskFrame.class.getClassLoader().getResourceAsStream(soundFilePath);
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
}