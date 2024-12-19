import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Layout extends JFrame {
    public Layout() {
        // JFrame 설정
        JFrame frame = new JFrame("Menu App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);

        // 메뉴 생성 및 패널에 추가
        CreateMenus menuCreator = new CreateMenus();
        List<Menu> menuList = menuCreator.createMenus(); // 메뉴 리스트 생성

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 5, 15, 20)); // GridLayout으로 5열 설정
        menuPanel.setBackground(new Color(240, 248, 255));

        for (Menu menu : menuList) {
            JPanel menuCard = createMenuCard(menu);
            menuCard.setPreferredSize(new Dimension(150, 150)); // 메뉴 카드 크기 설정
            menuPanel.add(menuCard); // menuCard 추가
        }

        frame.add(new JScrollPane(menuPanel)); // 스크롤 가능하게 설정
        frame.setVisible(true); // 프레임 표시
    }

    // 메뉴 카드 생성 메서드
    private JPanel createMenuCard(Menu menu) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // 이미지 추가
        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon(menu.getImagePath());
        Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);  // 수평 중앙 정렬
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);    // 수직 중앙 정렬
        panel.add(imageLabel, BorderLayout.CENTER);

        // 메뉴 정보 추가
        JLabel infoLabel = new JLabel("<html>" + menu.getName() + "<br>" + menu.getPrice() + "원" + "</html>", SwingConstants.CENTER);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);  // 수평 중앙 정렬
        infoLabel.setVerticalAlignment(SwingConstants.CENTER);    // 수직 중앙 정렬
        panel.add(infoLabel, BorderLayout.SOUTH);

        return panel;
    }
}
