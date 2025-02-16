import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class lab2{
    public static void main(String[] args) {
        Scanner inter = new Scanner(System.in);
        System.out.println("Enter the number of task");
        int n = inter.nextInt();
        switch(n){
            case 1:
                task1.func1();
                break;
            case 2:
                task2.func2();
                break;
            case 3:
                task3.func3();
                break;
            case 4:
                task4.func4();
            case 5:
                task5.func5();
            case 6:
                task6.func6();
            case 7:
                task7.func7();
            case 8:
                task8.func8();
        }
    }
}
class task1{
    public static Boolean isUnique(String str) {
        HashMap<Character, Boolean> uniqueChars = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            if (uniqueChars.containsKey(str.charAt(i))) {
                return false;
            }
            uniqueChars.put(str.charAt(i), true);
        }
        return true;
    }

    public static void func1() {
        Scanner inter = new Scanner(System.in);
        String[] arr = inter.nextLine().split(" ");
        if (arr.length == 0) {
            System.out.println("empty string");
            return;
        }
        int maxLen = 0;
        int maxIndex = 0;

        for (int i = 0; i < arr.length; i++) {
            if(isUnique(arr[i]) && arr[i].length() > maxLen) {
                maxLen = arr[i].length();
                maxIndex = i;
            }
        }
        System.out.println(arr[maxIndex]);
    }
}

class task2{
    public static void func2(){
        Scanner sc = new Scanner(System.in);
        List<Integer> arr1 = Arrays.stream(sc.nextLine().split(" ")).map(Integer::parseInt).toList();
        List<Integer> arr2 = Arrays.stream(sc.nextLine().split(" ")).map(Integer::parseInt).toList();
        List<Integer> c = Stream.concat(arr1.stream(), arr2.stream()).sorted().collect(Collectors.toList());
        System.out.println(c);
    }
}
class task3{
    public static void func3(){
        Scanner sc = new Scanner(System.in);
        List<Integer> arr1 = Arrays.stream(sc.nextLine().split(" ")).map(Integer::parseInt).toList();
        int MaxSum = 0;
        for (int i = 1; i < arr1.size(); i++) {
            if (arr1.get(i) + arr1.get(i-1) > MaxSum) {
                MaxSum = arr1.get(i) + arr1.get(i-1);
            }
        }
        System.out.println(MaxSum);
    }
}
class task4{
    public static void func4() {
        Scanner inter = new Scanner(System.in);
        System.out.println("Enter size of matrix 'x' 'y'");
        int x = inter.nextInt(), y = inter.nextInt();
        int[][] srcMatrix = new int[x][y];
        int[][] dstMatrix = new int[y][x];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                srcMatrix[i][j] = inter.nextInt();
            }
        }
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                dstMatrix[i][j] = srcMatrix[x - 1 - j][i];
                System.out.print(dstMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
class task5{
    public static void func5(){
        Scanner inter = new Scanner(System.in);
        List<Integer> list = Arrays.stream(inter.nextLine().split(" ")).map(Integer::parseInt).toList();

        if (list.toArray().length == 0) {
            System.out.println(0);
            return;
        }
        int k = inter.nextInt();

        HashMap<Integer, Integer> seen = new HashMap<>();

        for (int i = 0; i < list.toArray().length; i++) {
            if (!(seen.containsKey(list.get(i)))) {
                seen.put(k - list.get(i), i);
            } else {
                int first = list.get(seen.get(list.get(i))), second = list.get(i);
                System.out.println(first);
                System.out.println(second);
                return;
            }
        }
        System.out.println("null");
    }
}
class task6{
    public static void func6(){
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        int y = sc.nextInt();
        int[][] twoDimArray = new int[x][y];
        int g = twoDimArray[0].length;
        int h = twoDimArray.length;
        int sum = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < g; j++) {
                twoDimArray[i][j] = sc.nextInt();
                sum += twoDimArray[i][j];
            }
        }
        System.out.println(sum);
    }
}
class task7{
    public static void func7(){
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        int y = sc.nextInt();
        int[][] twoDimArray = new int[x][y];
        int g = twoDimArray.length;
        int h = twoDimArray[0].length;
        int [] result = new int[g];
        for (int i = 0; i < g; i++) {
            int MaxNumber = 0;
            for (int j = 0; j < h; j++) {
                twoDimArray[i][j] = sc.nextInt();
                if(twoDimArray[i][j] > MaxNumber){
                    MaxNumber = twoDimArray[i][j];
                }
            }
            result[i] = MaxNumber;
        }
        System.out.println(Arrays.toString(result));
    }
}
class task8{
    public static void func8() {
        Scanner inter = new Scanner(System.in);
        System.out.println("Enter size of matrix 'x' 'y'");
        int x = inter.nextInt(), y = inter.nextInt();
        int[][] srcMatrix = new int[x][y];
        int[][] dstMatrix = new int[y][x];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                srcMatrix[i][j] = inter.nextInt();
            }
        }
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                dstMatrix[i][j] = srcMatrix[j][y-i-1];
                System.out.print(dstMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}