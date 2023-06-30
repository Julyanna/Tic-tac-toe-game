import java.util.Random;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class TicTacToe {
    /*
    блок настроек игры
     */
    private static char[][] map; // матрица игры
    private static int SIZE = 3;

    private static final char DOT_EMPTY = '.';
    private static final char DOT_X = 'X';
    private static final char DOT_O = 'O';

    private static final boolean SILLY_MODE = false;

    private static Scanner scanner = new Scanner(System.in);

    private static Random random = new Random();

    public static void main(String[] args) {
        initMap();
        printMap();

        while(true){
            humanTurn(); //Ход человека
            if (isEndGame(DOT_X)){
                break;
            };

            computerTurn(); //Ход компьютера
            if (isEndGame(DOT_O)){
                break;
            };
        }
    }
    /*
    Инициализация игры
     */
    private static void initMap(){

        map = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                map[i][j] = DOT_EMPTY;
            }
        }
    }
    /*
      Отрисовка поля для игры
     */
    private static void printMap(){
        for (int i = 0; i <= SIZE; i++){
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < SIZE; i++){
            System.out.print((i+1) + " ");
            for (int j = 0; j < SIZE; j++){
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    /*
    Ход человека
     */
    private static void humanTurn(){
        int x, y;
        do{
            System.out.println("Введите координаты ячейки через пробел: ");
            y = scanner.nextInt() - 1;
            x = scanner.nextInt() - 1;
        }while (!isCellValid(x,y));

        map[y][x] = DOT_X;


    }
    /*
        Ход компьютера
     */
    private static void computerTurn(){
        int y = -1;
        int x = -1;
        if(SILLY_MODE) {
            do {
                x = random.nextInt(SIZE);
                y = random.nextInt(SIZE);
            } while (!isCellValid(x, y));
        } else {
            //рассчитывание рейтинга ячеек

            int[][] arrayCellRating = new int[3][SIZE*SIZE];  // записываем в массив в 1-ую строчку - строку пустой клетки, во 2-ую - столбец, в 3-юю - числовой рейтинг ячейки
            int countEmptyDot = 0;
            int maxRating = -1;
            int numberOfBestCell = 0; //в каком столбце arrayCellRating находится лучшая ячейка для вставки нолика

            //вычисляем пустые ячейки и записываем их координаты в массив arrayCellRating
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (isCellValid(i,j)) {
                        arrayCellRating[0][countEmptyDot] = i;
                        arrayCellRating[1][countEmptyDot] = j;
                        arrayCellRating[2][countEmptyDot] = ratingEmptyDot(i, j);
                        countEmptyDot++;

                    }
                }
            }

            //вычисляем ячейку с лучшим рейтингом

            for (int i = 0; i < countEmptyDot; i++) {
                if (maxRating < arrayCellRating[2][i]) {
                    maxRating = arrayCellRating[2][i];
                    numberOfBestCell = i;
                }
            }

            x = arrayCellRating[0][numberOfBestCell];
            y = arrayCellRating[1][numberOfBestCell];

        }

        System.out.println("Компьютер выбрал ячейку " + (y+1) + " " + (x+1));
        map[y][x] = DOT_O;
    }

    private static int ratingEmptyDot(int x, int y) {
        int rating = 0;


        if (cellСontainO(x+1, y)) rating++;      //соседняя справа
        if (cellСontainO(x+1, y+1)) rating++; //соседняя справа по диагонали вниз
        if (cellСontainO(x, y+1)) rating++;      //соседняя внизу
        if (cellСontainO(x-1, y+1)) rating++; //соседняя вниз по диагонали влево
        if (cellСontainO(x-1, y)) rating++;      //соседняя слева
        if (cellСontainO(x-1, y-1)) rating++; //соседняя вверху по диагонали слева
        if (cellСontainO(x, y-1)) rating++;      //соседняя сверху
        if (cellСontainO(x+1, y-1)) rating++; //соседняя вверху по диагонали справа

        return rating;


    }

    /*
        Проверка на содержание ячейкой символа 'О'
     */

    private static boolean cellСontainO(int x, int y) {
        boolean result = false;

        if (cellExist(x,y)) {
            if (map[y][x] == DOT_O)
                result = true;
        }

        return result;
    }

    private static boolean cellExist(int x, int y) {
        boolean result = true;

        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            result = false;
        }

        return result;
    }
    /*
      Метод валидации запрашиваемой ячейки на корректность
      @param x - координаты по горизонтали
      @param y - координаты по вертикали
      @return boolean - признак валидности
     */
    private static boolean isCellValid(int x, int y){
        boolean result = true;
        // проверка координаты
        if (!cellExist(x,y)){
            System.out.println("Неверные координаты");
            result = false;
            return result;
        }
        //Проверка заполненности ячейки
        if (map[y][x] != DOT_EMPTY){
            result = false;
        }
        return result;

    }
    /*
      Метод проверки игры на завершение
      @param playerSymbol - символ, которым играет текущий игрок
      @return boolean - признак завершения игры
     */
    private static boolean isEndGame(char playerSymbol){
        boolean result = false;

        printMap();
        //Проверяем необходимость следующего хода
        if (checkWin(playerSymbol)){
            System.out.println("Победили " + playerSymbol);
            result = true;
            return result;
        }
        if (isMapFull()){
            System.out.println("Ничья");
            result = true;
        }
        return result;
    }
    /*
      Метод проверки на 100%-ю заполненность поля
      @return boolean - признак оптимальности
     */
    private static boolean isMapFull(){
        boolean result = true;

        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++){
                if (map[i][j] == DOT_EMPTY){
                    result = false;
                }
            }
        }

        return result;
    }

    private static boolean checkWin(char playerSymbol){
        boolean result = false;

        if(
                (map[0][0] == playerSymbol && map[0][1] == playerSymbol && map[0][2] == playerSymbol ) ||
                (map[1][0] == playerSymbol && map[1][1] == playerSymbol && map[1][2] == playerSymbol ) ||
                (map[2][0] == playerSymbol && map[2][1] == playerSymbol && map[2][2] == playerSymbol ) ||
                (map[0][0] == playerSymbol && map[1][0] == playerSymbol && map[2][0] == playerSymbol ) ||
                (map[0][1] == playerSymbol && map[1][1] == playerSymbol && map[2][1] == playerSymbol ) ||
                (map[0][2] == playerSymbol && map[1][2] == playerSymbol && map[2][2] == playerSymbol ) ||
                (map[0][0] == playerSymbol && map[1][1] == playerSymbol && map[2][2] == playerSymbol ) ||
                (map[2][0] == playerSymbol && map[1][1] == playerSymbol && map[0][2] == playerSymbol )
        ) {
            result = true;
        }

        return result;
    }
}