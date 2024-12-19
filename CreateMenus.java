import java.util.ArrayList;
import java.util.List;

public class CreateMenus {
    public List<Menu> createMenus() {
        List<Menu> menuList = new ArrayList<>();

        // 메뉴 생성
        menuList.add(new Menu(1, "아이스 아메리카노", 2000, "시원한 아메리카노", "images/IceAmericano.jpg", "커피", 20));
        menuList.add(new Menu(2, "카페 라떼", 3000, "부드러운 라떼", "images/CafeLatte.jpg", "커피", 15));
        menuList.add(new Menu(3, "초코 케이크", 5000, "달콤한 초코 케이크", "images/ChocoCake.jpg", "디저트", 10));
        menuList.add(new Menu(4, "오렌지 주스", 2500, "신선한 오렌지 주스", "images/OrangeJuice.jpg", "음료", 30));
        menuList.add(new Menu(5, "오렌지 주스", 2500, "신선한 오렌지 주스", "images/OrangeJuice.jpg", "음료", 30));
        menuList.add(new Menu(6, "오렌지 주스", 2500, "신선한 오렌지 주스", "images/OrangeJuice.jpg", "음료", 30));
        menuList.add(new Menu(7, "오렌지 주스", 2500, "신선한 오렌지 주스", "images/OrangeJuice.jpg", "음료", 30));


        return menuList;
    }
}
