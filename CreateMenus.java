import java.util.ArrayList;
import java.util.List;

public class CreateMenus {
    public List<Menu> createMenus() {
        List<Menu> menuList = new ArrayList<>();

        menuList.add(new Menu(1, "아이스 아메리카노", 2000, "시원한 아메리카노", "images/IceAmericano.jpg", "커피", 20));
        menuList.add(new Menu(2, "카페 라떼", 3000, "부드러운 라떼", "images/CafeLatte.jpg", "커피", 15));
        menuList.add(new Menu(3, "바닐라 라떼", 3500, "달콤한 바닐라 라떼", "images/VanillaLatte.jpg", "커피", 10));
        menuList.add(new Menu(4, "콜드 브루", 4000, "진한 콜드 브루", "images/Coldbrew.jpg", "커피", 25));
        menuList.add(new Menu(5, "카푸치노", 3200, "거품 가득 카푸치노", "images/Cappuccino.jpg", "커피", 12));
        menuList.add(new Menu(6, "모카", 3700, "달콤한 모카", "images/Mocha.jpg", "커피", 18));
        menuList.add(new Menu(7, "더블 에스프레소", 2500, "진한 에스프레소", "images/DoubleEspresso.jpg", "커피", 22));
        menuList.add(new Menu(8, "헤이즐넛 라떼", 3600, "고소한 헤이즐넛 라떼", "images/HazelnutLatte.jpg", "커피", 10));

        menuList.add(new Menu(9, "레몬 에이드", 3000, "상큼한 레몬 에이드", "images/LemonAde.jpg", "에이드", 20));
        menuList.add(new Menu(10, "자몽 에이드", 3500, "달콤쌉싸름한 자몽 에이드", "images/GrapefruitAde.jpg", "에이드", 15));
        menuList.add(new Menu(11, "청포도 에이드", 3000, "달콤한 청포도 에이드", "images/GreenGrapeAde.jpg", "에이드", 18));
        menuList.add(new Menu(12, "오렌지 에이드", 3200, "신선한 오렌지 에이드", "images/OrangeAde.jpg", "에이드", 25));
        menuList.add(new Menu(13, "블루베리 에이드", 3300, "달콤한 블루베리 에이드", "images/BlueberryAde.jpg", "에이드", 15));
        menuList.add(new Menu(14, "복숭아 에이드", 3100, "향긋한 복숭아 에이드", "images/PeachAde.jpg", "에이드", 12));
        menuList.add(new Menu(15, "키위 에이드", 3400, "상큼한 키위 에이드", "images/KiwiAde.jpg", "에이드", 18));
        menuList.add(new Menu(16, "체리 에이드", 3500, "달콤한 체리 에이드", "images/CherryAde.jpg", "에이드", 10));

        menuList.add(new Menu(17, "초코 케이크", 5000, "달콤한 초코 케이크", "images/ChocoCake.jpg", "디저트", 10));
        menuList.add(new Menu(18, "치즈 케이크", 5500, "부드러운 치즈 케이크", "images/CheeseCake.jpg", "디저트", 8));
        menuList.add(new Menu(19, "마카롱", 2000, "알록달록한 마카롱", "images/Macarons.jpg", "디저트", 12));
        menuList.add(new Menu(20, "티라미수", 6000, "부드러운 티라미수", "images/Tiramisu.jpg", "디저트", 7));
        menuList.add(new Menu(21, "애플 파이", 4500, "달콤한 애플 파이", "images/ApplePie.jpg", "디저트", 9));
        menuList.add(new Menu(22, "브라우니", 4000, "진한 초콜릿 브라우니", "images/Brownie.jpg", "디저트", 11));
        menuList.add(new Menu(23, "크로와상", 3500, "바삭한 크로와상", "images/Croissant.jpg", "디저트", 15));
        menuList.add(new Menu(24, "푸딩", 3000, "부드러운 푸딩", "images/Pudding.jpg", "디저트", 13));

        menuList.add(new Menu(25, "허브 티", 2500, "향긋한 허브 티", "images/HerbTea.jpg", "티", 20));
        menuList.add(new Menu(26, "얼그레이 티", 3000, "고급스러운 얼그레이 티", "images/EarlGreyTea.jpg", "티", 15));
        menuList.add(new Menu(27, "레몬 진저 티", 3500, "상큼한 레몬 진저 티", "images/LemonGingerTea.jpg", "티", 18));
        menuList.add(new Menu(28, "캐모마일 티", 2700, "부드러운 캐모마일 티", "images/ChamomileTea.jpg", "티", 22));
        menuList.add(new Menu(29, "페퍼민트 티", 2800, "시원한 페퍼민트 티", "images/PeppermintTea.jpg", "티", 12));
        menuList.add(new Menu(30, "루이보스 티", 2900, "향긋한 루이보스 티", "images/RooibosTea.jpg", "티", 14));
        menuList.add(new Menu(31, "그린 티", 2600, "상쾌한 녹차", "images/GreenTea.jpg", "티", 18));
        menuList.add(new Menu(32, "자스민 티", 3000, "향기로운 자스민 티", "images/JasmineTea.jpg", "티", 16));

        return menuList;
    }
}
