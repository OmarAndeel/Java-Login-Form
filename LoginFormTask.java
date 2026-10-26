import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class User {
    private String username;
    private String password;
    private String role;

    User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

class WelcomePage extends Frame {
    WelcomePage(User user) {
        super("Welcome");

        setLayout(new GridLayout(3, 1, 10, 10));

        Label welcomeLabel = new Label("Hello, " + user.getUsername(), Label.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        Label roleLabel = new Label("Your role: " + user.getRole(), Label.CENTER);
        Button closeButton = new Button("Close");

        add(welcomeLabel);
        add(roleLabel);
        add(closeButton);

        closeButton.addActionListener(e -> dispose());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(350, 180);
        setLocationRelativeTo(null);
    }
}

public class LoginFormTask extends Frame {
    private List<User> users = new ArrayList<>();
    private int failedAttempts = 0;

    public LoginFormTask() {
        super("Login");
        loadUsers();

        setLayout(new GridBagLayout());
        Panel content = new Panel(new BorderLayout(10, 15));

        Label title = new Label("Welcome back", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));
        form.add(new Label("Username:", Label.RIGHT));
        TextField username = new TextField(18);
        form.add(username);

        form.add(new Label("Password:", Label.RIGHT));
        TextField password = new TextField(18);
        password.setEchoChar('*');
        form.add(password);

        form.add(new Label("Role:", Label.RIGHT));
        Choice role = new Choice();
        role.add("Student");
        role.add("TA");
        role.add("Professor");
        form.add(role);

        content.add(form, BorderLayout.CENTER);

        Label message = new Label(" ", Label.CENTER);
        // Keep the title visible above the status message.
        Panel heading = new Panel(new GridLayout(2, 1, 0, 5));
        heading.add(title);
        heading.add(message);
        content.add(heading, BorderLayout.NORTH);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        Button cancel = new Button("Cancel");
        Button login = new Button("Login");
        buttons.add(cancel);
        buttons.add(login);
        content.add(buttons, BorderLayout.SOUTH);

        add(content);

        login.addActionListener(e -> {
            String enteredUsername = username.getText().trim();
            String enteredPassword = password.getText();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                message.setText("Username and password cannot be empty.");
                return;
            }

            if (failedAttempts >= 3) {
                login.setEnabled(false);
                message.setText("Login disabled after 3 failed attempts.");
                return;
            }

            User user = findUser(enteredUsername);

            if (user == null
                    || !user.getPassword().equals(enteredPassword)
                    || !user.getRole().equals(role.getSelectedItem())) {
                failedAttempts++;

                if (failedAttempts >= 3) {
                    login.setEnabled(false);
                    message.setText("Login disabled after 3 failed attempts.");
                } else {
                    message.setText("Invalid username, password, or role. Attempts left: "
                            + (3 - failedAttempts));
                }
                return;
            }

            message.setText("Login successful!");
            new WelcomePage(user).setVisible(true);
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(420, 340);
        setLocationRelativeTo(null);
    }

    private void loadUsers() {
        users.add(new User("student1", "1234", "Student"));
        users.add(new User("ta1", "ta123", "TA"));
        users.add(new User("prof1", "prof123", "Professor"));
    }

    private User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        new LoginFormTask().setVisible(true);
    }
}
