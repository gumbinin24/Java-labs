import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


class Movie {
    private String title;
    private int duration;

    public Movie(String title, int duration) {
        this.title = title;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }
}

class Seat {
    private int row;
    private int number;
    private boolean isOccupied;

    public Seat(int row, int number) {
        this.row = row;
        this.number = number;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public String toString() {
        return "Row " + row + ", Seat " + number;
    }
}

class Hall {
    private String name;
    private List<List<Seat>> seats;
    private List<Screening> screenings;

    public Hall(String name, int rows, int seatsPerRow) {
        this.name = name;
        this.seats = new ArrayList<>();
        this.screenings = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            List<Seat> row = new ArrayList<>();
            for (int j = 0; j < seatsPerRow; j++) {
                row.add(new Seat(i + 1, j + 1));
            }
            seats.add(row);
        }
    }

    public String getName() {
        return name;
    }

    public List<List<Seat>> getSeats() {
        return seats;
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public void addScreening(Screening screening) {
        screenings.add(screening);
    }

    public void printSeatingPlan(Screening screening) {
        System.out.println("Seating Plan for " + screening.getMovie().getTitle());
        System.out.println("Hall: " + name);
        for(int i = 1; i < seats.getFirst().size() + 1; i++) {
            if (i < 10) {
                System.out.printf("   %d", i);
            } else {
                System.out.printf("  %d", i);
            }
        }
        System.out.println();
        int rov = 1;
        for (List<Seat> row : seats) {
            System.out.print(rov + " ");
            rov++;
            for (Seat seat : row) {
                System.out.print(seat.isOccupied() ? "[X] " : "[ ] ");
            }
            System.out.println();
        }
    }
}

class Cinema {
    private String name;
    private List<Hall> halls;

    public Cinema(String name) {
        this.name = name;
        this.halls = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addHall(Hall hall) {
        halls.add(hall);
    }

    public List<Hall> getHalls() {
        return halls;
    }
}

class Screening {
    private Movie movie;
    private LocalDateTime startTime;
    private Hall hall;

    public Screening(Movie movie, LocalDateTime startTime, Hall hall) {
        this.movie = movie;
        this.startTime = startTime;
        this.hall = hall;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Hall getHall() {
        return hall;
    }
}

class User {
    private String username;
    private String password;
    private boolean isAdmin;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

class CinemaSystem {
    private List<Cinema> cinemas;
    private List<Movie> movies;
    private List<User> users;
    private User currentUser;
    private Scanner scanner;

    public CinemaSystem() {
        this.cinemas = new ArrayList<>();
        this.movies = new ArrayList<>();
        this.users = new ArrayList<>();
        this.scanner = new Scanner(System.in);

        users.add(new User("admin", "admin123", true));
        users.add(new User("user", "user123", false));
    }

    public void start() {
        while (true) {
            if (currentUser == null) {
                System.out.println("\n1. Login\n2. Exit\nChoose option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) login();
                else if (choice == 2) break;
            } else {
                if (currentUser.isAdmin()) showAdminMenu();
                else showUserMenu();
            }
        }
    }

    private void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Login successful!");
                return;
            }
        }
        System.out.println("Invalid credentials!");
    }

    private void showAdminMenu() {
        System.out.println("\nAdmin Menu:\n1. Add Cinema\n2. Add Hall to Cinema\n3. Add Movie\n4. Add Screening\n5. View All Cinemas\n6. Logout");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1: addCinema(); break;
            case 2: addHall(); break;
            case 3: addMovie(); break;
            case 4: addScreening(); break;
            case 5: viewAllCinemas(); break;
            case 6: currentUser = null; break;
        }
    }

    private void showUserMenu() {
        System.out.println("\nUser Menu:\n1. View Available Movies\n2. Find Next Available Screening\n3. Book Ticket\n4. View Seating Plan\n5. Logout");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1: viewMovies(); break;
            case 2: findNextScreening(); break;
            case 3: bookTicket(); break;
            case 4: viewSeatingPlan(); break;
            case 5: currentUser = null; break;
        }
    }

    private void addCinema() {
        System.out.print("Enter cinema name: ");
        cinemas.add(new Cinema(scanner.nextLine()));
        System.out.println("Cinema added successfully!");
    }

    private void addHall() {
        if (cinemas.isEmpty()) {
            System.out.println("No cinemas available. Please add a cinema first.");
            return;
        }

        System.out.println("Select cinema:");
        for (int i = 0; i < cinemas.size(); i++) {
            System.out.println((i + 1) + ". " + cinemas.get(i).getName());
        }

        int cinemaChoice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter hall name: ");
        String name = scanner.nextLine();
        System.out.print("Enter number of rows: ");
        int rows = scanner.nextInt();
        System.out.print("Enter seats per row: ");
        int seats = scanner.nextInt();

        cinemas.get(cinemaChoice - 1).addHall(new Hall(name, rows, seats));
        System.out.println("Hall added successfully!");
    }

    private void addMovie() {
        System.out.print("Enter movie title: ");
        String title = scanner.nextLine();
        System.out.print("Enter duration (minutes): ");
        int duration = scanner.nextInt();

        movies.add(new Movie(title, duration));
        System.out.println("Movie added successfully!");
    }

    private void addScreening() {
        if (movies.isEmpty() || cinemas.isEmpty()) {
            System.out.println("Movies or cinemas not available.");
            return;
        }

        System.out.println("Select movie:");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i).getTitle());
        }
        int movieChoice = scanner.nextInt();

        System.out.println("Select cinema:");
        for (int i = 0; i < cinemas.size(); i++) {
            System.out.println((i + 1) + ". " + cinemas.get(i).getName());
        }
        int cinemaChoice = scanner.nextInt();

        Cinema selectedCinema = cinemas.get(cinemaChoice - 1);
        System.out.println("Select hall:");
        for (int i = 0; i < selectedCinema.getHalls().size(); i++) {
            System.out.println((i + 1) + ". " + selectedCinema.getHalls().get(i).getName());
        }
        int hallChoice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter screening date and time (yyyy-MM-dd HH:mm): ");
        LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Hall selectedHall = selectedCinema.getHalls().get(hallChoice - 1);
        selectedHall.addScreening(new Screening(movies.get(movieChoice - 1), dateTime, selectedHall));

        System.out.println("Screening added successfully!");
    }

    private void viewMovies() {
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }

        System.out.println("\nAvailable Movies:");
        for (Movie movie : movies) {
            System.out.println("- " + movie.getTitle() + " (" + movie.getDuration() + " minutes)");
        }
    }

    private void findNextScreening() {
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }

        System.out.println("Select movie:");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i).getTitle());
        }
        int choice = scanner.nextInt();
        Movie selectedMovie = movies.get(choice - 1);

        LocalDateTime now = LocalDateTime.now();
        Screening nextScreening = null;
        Cinema nextCinema = null;

        for (Cinema cinema : cinemas) {
            for (Hall hall : cinema.getHalls()) {
                for (Screening screening : hall.getScreenings()) {
                    if (screening.getMovie().getTitle().equals(selectedMovie.getTitle()) &&
                            screening.getStartTime().isAfter(now)) {
                        if (nextScreening == null || screening.getStartTime().isBefore(nextScreening.getStartTime())) {
                            nextScreening = screening;
                            nextCinema = cinema;
                        }
                    }
                }
            }
        }

        if (nextScreening != null) {
            System.out.println("\nNext available screening:");
            System.out.println("Movie: " + selectedMovie.getTitle());
            System.out.println("Cinema: " + nextCinema.getName());
            System.out.println("Hall: " + nextScreening.getHall().getName());
            System.out.println("Time: " + nextScreening.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        } else {
            System.out.println("No upcoming screenings found for this movie.");
        }
    }

    private void bookTicket() {
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }

        System.out.println("Select movie:");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i).getTitle());
        }
        int movieChoice = scanner.nextInt();

        List<Screening> availableScreenings = new ArrayList<>();
        for (Cinema cinema : cinemas) {
            for (Hall hall : cinema.getHalls()) {
                for (Screening screening : hall.getScreenings()) {
                    if (screening.getMovie().equals(movies.get(movieChoice - 1))) {
                        availableScreenings.add(screening);
                    }
                }
            }
        }

        if (availableScreenings.isEmpty()) {
            System.out.println("No available screenings for this movie.");
            return;
        }

        System.out.println("\nAvailable screenings:");
        for (int i = 0; i < availableScreenings.size(); i++) {
            Screening screening = availableScreenings.get(i);
            System.out.println((i + 1) + ". " + screening.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }

        System.out.print("Select screening: ");
        int screeningChoice = scanner.nextInt();
        Screening selectedScreening = availableScreenings.get(screeningChoice - 1);

        selectedScreening.getHall().printSeatingPlan(selectedScreening);

        System.out.print("Enter row number: ");
        int row = scanner.nextInt();
        System.out.print("Enter seat number: ");
        int seatNum = scanner.nextInt();

        List<List<Seat>> seats = selectedScreening.getHall().getSeats();
        if (row <= seats.size() && seatNum <= seats.get(0).size()) {
            Seat selected = seats.get(row - 1).get(seatNum - 1);
            if (!selected.isOccupied()) {
                selected.setOccupied(true);
                System.out.println("Ticket booked successfully!");
                printTicket(selectedScreening, row, seatNum);
            } else {
                System.out.println("This seat is already occupied. Please choose another seat.");
            }
        } else {
            System.out.println("Invalid seat selection. Please try again.");
        }
    }

    private void viewSeatingPlan() {
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }

        System.out.println("Select movie:");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i).getTitle());
        }
        int movieChoice = scanner.nextInt();

        List<Screening> availableScreenings = new ArrayList<>();
        for (Cinema cinema : cinemas) {
            for (Hall hall : cinema.getHalls()) {
                for (Screening screening : hall.getScreenings()) {
                    if (screening.getMovie().equals(movies.get(movieChoice - 1))) {
                        availableScreenings.add(screening);
                    }
                }
            }
        }

        if (availableScreenings.isEmpty()) {
            System.out.println("No available screenings for this movie.");
            return;
        }

        System.out.println("\nAvailable screenings:");
        for (int i = 0; i < availableScreenings.size(); i++) {
            Screening screening = availableScreenings.get(i);
            System.out.println((i + 1) + ". " + screening.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }

        System.out.print("Select screening: ");
        int screeningChoice = scanner.nextInt();
        Screening selectedScreening = availableScreenings.get(screeningChoice - 1);

        selectedScreening.getHall().printSeatingPlan(selectedScreening);
    }

    private void viewAllCinemas() {
        if (cinemas.isEmpty()) {
            System.out.println("No cinemas available.");
            return;
        }

        for (Cinema cinema : cinemas) {
            System.out.println("\nCinema: " + cinema.getName());
            List<Hall> halls = cinema.getHalls();
            if (halls.isEmpty()) {
                System.out.println("No halls available in this cinema.");
            } else {
                for (Hall hall : halls) {
                    System.out.println("\nHall: " + hall.getName());
                    System.out.println("Screenings:");
                    List<Screening> screenings = hall.getScreenings();
                    if (screenings.isEmpty()) {
                        System.out.println("No screenings scheduled.");
                    } else {
                        for (Screening screening : screenings) {
                            System.out.println("- " + screening.getMovie().getTitle() + " at " +
                                    screening.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                        }
                    }
                }
            }
        }
    }

    private void printTicket(Screening screening, int row, int seatNum) {
        System.out.println("\n=================================");
        System.out.println("           TICKET                ");
        System.out.println("=================================");
        System.out.println("Movie: " + screening.getMovie().getTitle());
        System.out.println("Time: " + screening.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("Hall: " + screening.getHall().getName());
        System.out.println("Seat: Row " + row + ", Seat " + seatNum);
        System.out.println("=================================\n");
    }
}

public class lab3 {
    public static void main(String[] args) {
        CinemaSystem system = new CinemaSystem();
        system.start();
    }
}