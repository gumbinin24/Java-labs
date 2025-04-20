import java.util.*;
import java.util.stream.Collectors;

class Book {
    private final String title;
    private final String author;
    private final int year;

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }

    @Override
    public String toString() {
        return String.format("Книга: '%s' (автор: %s, год: %d)", title, author, year);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return year == book.year &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, year);
    }
}

class Library {
    private final List<Book> books = new ArrayList<>();
    private final Set<String> authors = new HashSet<>();
    private final Map<String, Integer> authorStats = new HashMap<>();

    public void addBook(Book book) {
        books.add(book);
        authors.add(book.getAuthor());
        authorStats.put(book.getAuthor(),
                authorStats.getOrDefault(book.getAuthor(), 0) + 1);
    }

    public void removeBook(Book book) {
        if (books.remove(book)) {
            boolean authorHasBooks = books.stream()
                    .anyMatch(b -> b.getAuthor().equals(book.getAuthor()));

            if (!authorHasBooks) {
                authors.remove(book.getAuthor());
            }

            int count = authorStats.get(book.getAuthor());
            if (count == 1) {
                authorStats.remove(book.getAuthor());
            } else {
                authorStats.put(book.getAuthor(), count - 1);
            }
        }
    }

    public List<Book> findBooksByAuthor(String author) {
        return books.stream()
                .filter(b -> b.getAuthor().equals(author))
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByYear(int year) {
        return books.stream()
                .filter(b -> b.getYear() == year)
                .collect(Collectors.toList());
    }

    public void printAllBooks() {
        System.out.println("\n=== Все книги в библиотеке ===");
        if (books.isEmpty()) {
            System.out.println("Библиотека пуста");
        } else {
            books.forEach(System.out::println);
        }
    }

    public void printUniqueAuthors() {
        System.out.println("\n=== Уникальные авторы ===");
        if (authors.isEmpty()) {
            System.out.println("Нет авторов в библиотеке");
        } else {
            authors.forEach(System.out::println);
        }
    }

    public void printAuthorStatistics() {
        System.out.println("\n=== Статистика по авторам ===");
        if (authorStats.isEmpty()) {
            System.out.println("Нет данных для отображения");
        } else {
            authorStats.forEach((author, count) ->
                    System.out.printf("%s: %d книг(и)%n", author, count));
        }
    }
}

public class lab4 {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать в систему управления библиотекой!");

        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Добавить книгу");
            System.out.println("2. Удалить книгу");
            System.out.println("3. Найти книги по автору");
            System.out.println("4. Найти книги по году");
            System.out.println("5. Показать все книги");
            System.out.println("6. Показать уникальных авторов");
            System.out.println("7. Показать статистику по авторам");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Пожалуйста, введите число!");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Введите название книги: ");
                    String title = scanner.nextLine();
                    System.out.print("Введите автора: ");
                    String author = scanner.nextLine();
                    System.out.print("Введите год издания: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();
                    library.addBook(new Book(title, author, year));
                    System.out.println("Книга добавлена успешно!");
                    break;

                case 2:
                    System.out.print("Введите название книги для удаления: ");
                    title = scanner.nextLine();
                    System.out.print("Введите автора: ");
                    author = scanner.nextLine();
                    System.out.print("Введите год издания: ");
                    year = scanner.nextInt();
                    scanner.nextLine();
                    Book toRemove = new Book(title, author, year);
                    library.removeBook(toRemove);
                    System.out.println("Книга удалена (если она существовала)");
                    break;

                case 3:
                    System.out.print("Введите автора для поиска: ");
                    author = scanner.nextLine();
                    List<Book> authorBooks = library.findBooksByAuthor(author);
                    System.out.println("\nНайденные книги:");
                    if (authorBooks.isEmpty()) {
                        System.out.println("Книги не найдены");
                    } else {
                        authorBooks.forEach(System.out::println);
                    }
                    break;

                case 4:
                    System.out.print("Введите год для поиска: ");
                    year = scanner.nextInt();
                    scanner.nextLine();
                    List<Book> yearBooks = library.findBooksByYear(year);
                    System.out.println("\nНайденные книги:");
                    if (yearBooks.isEmpty()) {
                        System.out.println("Книги не найдены");
                    } else {
                        yearBooks.forEach(System.out::println);
                    }
                    break;

                case 5:
                    library.printAllBooks();
                    break;

                case 6:
                    library.printUniqueAuthors();
                    break;

                case 7:
                    library.printAuthorStatistics();
                    break;

                case 0:
                    System.out.println("Выход из системы...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Неверный выбор, попробуйте снова");
            }
        }
    }
}