package by.it.group410972.petroshevich.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].

    (Такая пара элементов называется инверсией массива.
    Количество инверсий в массиве является в некотором смысле
    его мерой неупорядоченности: например, в упорядоченном по неубыванию
    массиве инверсий нет вообще, а в массиве, упорядоченном по убыванию,
    инверсию образуют каждые (т.е. любые) два элемента.
    )

Sample Input:
5
2 3 9 2 9
Sample Output:
2

Головоломка (т.е. не обязательно).
Попробуйте обеспечить скорость лучше, чем O(n log n) за счет многопоточности.
Докажите рост производительности замерами времени.
Большой тестовый массив можно прочитать свой или сгенерировать его программно.
*/


public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int result = 0;
        int[] temp = new int[n];
        result = mergeSortAndCount(a, temp, 0, n - 1);
        return result;
    }
    int mergeSortAndCount(int[] a, int[] temp, int left, int right) {
        int inv_count = 0;
        if (left < right) {
            int mid = (left + right) / 2;
            inv_count += mergeSortAndCount(a, temp, left, mid);
            inv_count += mergeSortAndCount(a, temp, mid + 1, right);
            inv_count += mergeAndCount(a, temp, left, mid, right);
        }
        return inv_count;
    }

    int mergeAndCount(int[] a, int[] temp, int left, int mid, int right) {
        int i = left, j = mid + 1, k = left;
        int inv_count = 0;
        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
                inv_count += (mid - i + 1);
            }
        }
        while (i <= mid)
            temp[k++] = a[i++];
        while (j <= right)
            temp[k++] = a[j++];
        for (i = left; i <= right; i++)
            a[i] = temp[i];
        return inv_count;
    }
}
