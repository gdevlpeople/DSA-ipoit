package by.it.group410972.petroshevich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        quickSort3Way(segments);

        // Для каждой точки ищем количество подходящих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int idx = binarySearchFirst(segments, point);
            if (idx == -1) {
                result[i] = 0;
                continue;
            }
            // Считаем все подряд идущие подходящие отрезки
            int count = 0;
            int j = idx;
            while (j < n && segments[j].start <= point && point <= segments[j].stop) {
                count++;
                j++;
            }
            result[i] = count;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Object o) {
            Segment other = (Segment) o;
            if (this.start != other.start) {
                return Integer.compare(this.start, other.start);
            }
            return Integer.compare(this.stop, other.stop);
        }
    }
    private void quickSort3Way(Segment[] a) {
        int[] stack = new int[a.length * 2];
        int top = 0;
        stack[top++] = 0;
        stack[top++] = a.length - 1;
        while (top > 0) {
            int right = stack[--top];
            int left = stack[--top];
            if (left >= right) continue;
            int[] mid = partition3(a, left, right);
            // Сначала меньшую часть в стек (для уменьшения глубины)
            if (mid[0] - left < right - mid[1]) {
                stack[top++] = left;
                stack[top++] = mid[0] - 1;
                stack[top++] = mid[1] + 1;
                stack[top++] = right;
            } else {
                stack[top++] = mid[1] + 1;
                stack[top++] = right;
                stack[top++] = left;
                stack[top++] = mid[0] - 1;
            }
        }
    }

    private int[] partition3(Segment[] a, int left, int right) {
        Segment pivot = a[right];
        int lt = left, gt = right, i = left;
        while (i <= gt) {
            int cmp = a[i].compareTo(pivot);
            if (cmp < 0) swap(a, lt++, i++);
            else if (cmp > 0) swap(a, i, gt--);
            else i++;
        }
        return new int[]{lt, gt};
    }

    private void swap(Segment[] a, int i, int j) {
        Segment tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    private int binarySearchFirst(Segment[] a, int point) {
        int left = 0, right = a.length - 1, result = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (a[mid].start <= point && point <= a[mid].stop) {
                result = mid;
                right = mid - 1; // ищем первый слева
            } else if (a[mid].start > point) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return result;
    }


}
