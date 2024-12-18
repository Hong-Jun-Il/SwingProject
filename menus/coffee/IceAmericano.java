package menus.coffee;
import javax.swing.*;

public class IceAmericano {
    private String name = "아이스 아메리카노";          // 메뉴 이름
    private int price = 2000;         // 가격
    private String description = "한성커피만의 특별한 로스팅을 거친 아이스 아메리카노입니다.";   // 설명
    private String imagePath = "images/IceAmericano.jpg";     // 이미지 경로
    private String category = "커피";      // 카테고리
    private int id = 1;               // 메뉴 ID (고유 식별자)
    private int stock = 20;            // 재고 수량

    // 생성자
    public IceAmericano(int id, String name, int price, String description, String imagePath, String category, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
        this.category = category;
        this.stock = stock;
    }

    //가격 반환
    public double getPrice() {
        return price;
    }

    //이름 반환
    public String getName() {
        return name;
    }

    //설명 반환
    public String getDescription() {
        return description;
    }

    //이미지 파일 반환
    public String getImagePath() {
        return imagePath;
    }

    //카테고리 반환
    public String getCategory() {
        return category;
    }

    //식별 아이디 반환
    public int getId() {
        return id;
    }

    //재고 반환
    public int getStock() {
        return stock;
    }

    //판매 시 재고 빼기
    public void decreaseStock(int quantity) {
        if (this.stock - quantity < 0) {
            // 재고 부족 시 메시지 팝업을 띄움
            JOptionPane.showMessageDialog(null,
                    "재고가 부족합니다. 현재 재고는 " + this.stock + "개입니다.",
                    "재고 부족",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.stock -= quantity;
    }
}
