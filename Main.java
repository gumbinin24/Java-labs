import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main extends Application {
    private final String DB_URL = "jdbc:sqlite:calendar.sqlite";
    private final Map<LocalDate, List<String>> notesMap = new HashMap<>();
    private LocalDate selectedDate = LocalDate.now();
    private VBox noteBox;
    private VBox monthSummaryBox;
    private final ComboBox<String> monthBox = new ComboBox<>();
    private final ComboBox<Integer> yearBox = new ComboBox<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        createTables();
        loadNotesFromDB();

        monthBox.getItems().addAll(
                "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
                "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
        );
        monthBox.setValue(monthBox.getItems().get(selectedDate.getMonthValue() - 1));

        for (int y = 2020; y <= 2035; y++) {
            yearBox.getItems().add(y);
        }
        yearBox.setValue(selectedDate.getYear());

        Button showBtn = new Button("Показать");
        showBtn.setOnAction(e -> updateCalendar());

        noteBox = new VBox(5);
        monthSummaryBox = new VBox(5);
        noteBox.setPadding(new Insets(5));
        monthSummaryBox.setPadding(new Insets(5));

        GridPane calendarGrid = createCalendarGrid();

        VBox root = new VBox(10,
                createTopPanel(monthBox, yearBox, showBtn),
                calendarGrid,
                new Label("Выбрано: " + selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))),
                noteBox,
                new Label("Заметки за месяц:"),
                monthSummaryBox,
                createAddNoteButton()
        );
        root.setPadding(new Insets(10));

        updateCalendar();

        stage.setScene(new Scene(root, 550, 650));
        stage.setTitle("Ежедневник-Календарь");
        stage.show();
    }

    private HBox createTopPanel(ComboBox<String> monthBox, ComboBox<Integer> yearBox, Button showBtn) {
        HBox panel = new HBox(10,
                new Label("Месяц:"), monthBox,
                new Label("Год:"), yearBox,
                showBtn
        );
        panel.setAlignment(Pos.CENTER_LEFT);
        return panel;
    }

    private Button createAddNoteButton() {
        Button button = new Button("Добавить заметку");
        button.setOnAction(e -> openNoteDialog());
        return button;
    }

    private GridPane createCalendarGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));
        return grid;
    }

    private void updateCalendar() {
        GridPane calendarGrid = (GridPane) ((VBox) noteBox.getParent()).getChildren().get(1);
        calendarGrid.getChildren().clear();

        int month = monthBox.getItems().indexOf(monthBox.getValue()) + 1;
        int year = yearBox.getValue();
        YearMonth ym = YearMonth.of(year, month);
        LocalDate firstDay = LocalDate.of(year, month, 1);

        int firstDayOfWeek = (firstDay.getDayOfWeek().getValue() + 6) % 7;

        String[] days = {"Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};
        for (int i = 0; i < days.length; i++) {
            Label lbl = new Label(days[i]);
            lbl.setStyle("-fx-font-weight: bold;");
            calendarGrid.add(lbl, i, 0);
        }

        int row = 1, col = firstDayOfWeek;
        for (int day = 1; day <= ym.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(year, month, day);
            Button dayBtn = new Button(String.valueOf(day));
            dayBtn.setPrefWidth(40);
            dayBtn.setOnAction(e -> selectDate(date));
            calendarGrid.add(dayBtn, col++, row);
            if (col == 7) {
                col = 0;
                row++;
            }
        }

        selectDate(LocalDate.of(year, month, 1));
    }

    private void selectDate(LocalDate date) {
        selectedDate = date;
        ((Label) ((VBox) noteBox.getParent()).getChildren().get(2))
                .setText("Выбрано: " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        updateNoteBox();
        updateMonthSummary();
    }

    private void openNoteDialog() {
        TextField titleField = new TextField();
        titleField.setPromptText("Заголовок");

        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Содержание");

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Общее", "Работа", "Дом");
        categoryBox.setValue("Общее");

        Stage dialog = new Stage();
        dialog.setScene(new Scene(new VBox(10,
                new Label("Заголовок:"), titleField,
                new Label("Содержание:"), contentArea,
                new Label("Категория:"), categoryBox,
                new Button("Сохранить") {{
                    setOnAction(e -> saveNote(
                            titleField.getText().trim(),
                            contentArea.getText().trim(),
                            categoryBox.getValue(),
                            dialog
                    ));
                }}
        ) {{
            setPadding(new Insets(10));
        }}, 300, 400));
        dialog.setTitle("Новая заметка");
        dialog.showAndWait();
    }

    private void saveNote(String title, String content, String category, Stage dialog) {
        if (title.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Введите заголовок!").showAndWait();
            return;
        }
        insertNote(title, content, selectedDate.toString(), category);
        loadNotesFromDB();
        updateNoteBox();
        updateMonthSummary();
        dialog.close();
    }

    private void updateNoteBox() {
        noteBox.getChildren().clear();
        List<String> notes = notesMap.getOrDefault(selectedDate, Collections.emptyList());

        if (notes.isEmpty()) {
            noteBox.getChildren().add(new Label("Нет заметок на эту дату."));
        } else {
            notes.forEach(note -> noteBox.getChildren().add(new Label("• " + note) {{
                setWrapText(true);
            }}));
        }
    }

    private void updateMonthSummary() {
        monthSummaryBox.getChildren().clear();
        int month = monthBox.getItems().indexOf(monthBox.getValue()) + 1;
        int year = yearBox.getValue();
        String monthStr = String.format("%04d-%02d", year, month);

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT date, title, category FROM notes WHERE strftime('%Y-%m', date) = ?")) {

            pstmt.setString(1, monthStr);
            ResultSet rs = pstmt.executeQuery();

            Map<LocalDate, List<String>> summary = new TreeMap<>();

            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString("date"));
                String note = rs.getString("title") + " [" + rs.getString("category") + "]";
                summary.computeIfAbsent(date, k -> new ArrayList<>()).add(note);
            }

            if (summary.isEmpty()) {
                monthSummaryBox.getChildren().add(new Label("Нет заметок за выбранный месяц."));
            } else {
                summary.forEach((date, notes) -> monthSummaryBox.getChildren().add(
                        new Label(date.format(DateTimeFormatter.ofPattern("dd.MM")) + ": " +
                                String.join("; ", notes)) {{
                            setWrapText(true);
                        }}));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS notes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "content TEXT, " +
                    "date TEXT NOT NULL, " +
                    "category TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertNote(String title, String content, String date, String category) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO notes (title, content, date, category) VALUES (?, ?, ?, ?)")) {

            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, date);
            pstmt.setString(4, category);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadNotesFromDB() {
        notesMap.clear();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT title, content, date FROM notes")) {

            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString("date"));
                String note = rs.getString("title") +
                        (rs.getString("content").isEmpty() ? "" : ": " + rs.getString("content"));
                notesMap.computeIfAbsent(date, k -> new ArrayList<>()).add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}