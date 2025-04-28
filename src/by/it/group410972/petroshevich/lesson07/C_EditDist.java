package by.it.group410972.petroshevich.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Итерационно вычислить алгоритм преобразования двух данных непустых строк
    Вывести через запятую редакционное предписание в формате:
     операция("+" вставка, "-" удаление, "~" замена, "#" копирование)
     символ замены или вставки

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    #,#,

    Sample Input 2:
    short
    ports
    Sample Output 2:
    -s,~p,#,#,#,+s,

    Sample Input 3:
    distance
    editing
    Sample Output 2:
    +e,#,#,-s,#,~i,#,-c,~g,


    P.S. В литературе обычно действия редакционных предписаний обозначаются так:
    - D (англ. delete) — удалить,
    + I (англ. insert) — вставить,
    ~ R (replace) — заменить,
    # M (match) — совпадение.
*/


public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();
        int[][] dp = new int[n + 1][m + 1];
        // Для восстановления пути
        char[][] op = new char[n + 1][m + 1];

        // Инициализация первой строки и столбца
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
            op[i][0] = '-'; // удаление
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
            op[0][j] = '+'; // вставка
        }
        op[0][0] = '#'; // старт

        // Заполнение таблицы DP и операций
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Копирование или замена
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                    op[i][j] = '#'; // копирование
                } else {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    op[i][j] = '~'; // замена
                }
                // Удаление
                if (dp[i][j] > dp[i - 1][j] + 1) {
                    dp[i][j] = dp[i - 1][j] + 1;
                    op[i][j] = '-';
                }
                // Вставка
                if (dp[i][j] > dp[i][j - 1] + 1) {
                    dp[i][j] = dp[i][j - 1] + 1;
                    op[i][j] = '+';
                }
            }
        }

        // Восстановление пути
        StringBuilder res = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            char curOp = op[i][j];
            if (curOp == '#') {
                res.insert(0, "#,");
                i--; j--;
            } else if (curOp == '~') {
                res.insert(0, "~" + two.charAt(j - 1) + ",");
                i--; j--;
            } else if (curOp == '-') {
                res.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            } else if (curOp == '+') {
                res.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
        }

        return res.toString();
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}