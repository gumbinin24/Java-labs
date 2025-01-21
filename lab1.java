import java.util.Arrays;
import java.util.Scanner;

class lab1 {
    public static void main(String[] args) {
        Scanner inter = new Scanner(System.in);
        System.out.println("Enter the number of task");
        int n = inter.nextInt();
        switch(n)
        {
            case 1:
                System.out.println(task1.func(inter));
                break;
            case 2:
                System.out.println(task2.func(inter));
                break;
            case 3:
                System.out.println(task3.func(inter));
                break;
            case 4:
                task4.func();
                break;
            case 5:
                task5.func();
                break;
            default:
                break;
        }
        inter.close();
    }
}
class task1{
    public static int func(Scanner inter)
    {
        int i = inter.nextInt();
        inter.close();
        int iter = 0;
        while(i != 1)
        {
            if(i%2 == 0)
            {
                i = i/2;
            }
            else
            {
                i = 3*i+1;
            }
            iter += 1;
        }
        return iter;
    }
}
class task2{
    public static int func(Scanner inter){
        int n1 = inter.nextInt();
        int sum = 0;
        boolean flag = true;
        for (int i = n1; i > 0; i--){
            Scanner sc2 = new Scanner(System.in);
            if (flag){
                sum += sc2.nextInt();
                flag = false;
            } else {
                sum -= sc2.nextInt();
                flag = true;
            }

        }
        return sum;
    }
}
class task3{
    public static int func(Scanner inter){
        int n1 = inter.nextInt();
        int n2 = inter.nextInt();
        int myCoordinates[] = { 0, 0 };
        int coordinates[] = { n1, n2 };
        String str = inter.next().toLowerCase();
        int counter = 0;
        int result = 0;
        while(!str.equals("стоп")){
            int cor = inter.nextInt();
            switch (str){
                case "север":
                    myCoordinates[1] += cor;
                    break;
                case "юг":
                    myCoordinates[1] -= cor;
                    break;
                case "запад":
                    myCoordinates[0] -= cor;
                    break;
                case "восток":
                    myCoordinates[0] += cor;
                    break;
            }
            str = inter.next().toLowerCase();
            counter += 1;
            if (Arrays.equals(coordinates, myCoordinates)){
                result = counter;
            }

        }
        return result;
    }
}
class task4{
    public static void func() {
        Scanner sc = new Scanner(System.in);
        int counter_of_road = sc.nextInt();
        int result = 0;
        int max_height = Integer.MIN_VALUE;
        for(int i = 1; i <= counter_of_road; i++){
            int counter_of_tunnels = sc.nextInt();
            int min_height = Integer.MAX_VALUE;
            for (int j = 0; j < counter_of_tunnels; j++){
                int height = sc.nextInt();
                min_height = Math.min(min_height, height);
            }
            if(min_height > max_height){
                max_height = min_height;
                result = counter_of_tunnels;
            }
        }
        System.out.println(result);
        System.out.println(max_height);
    }
}
class task5{
    public static void func() {
        Scanner inter = new Scanner(System.in);
        int number = inter.nextInt();
        int sum = 0;
        int mult = 1;
        while (number != 0) {
            sum += number % 10;
            mult *= number % 10;
            number /= 10;
        }
        if (sum % 2 == 0 && mult % 2 == 0) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }

    }
}

