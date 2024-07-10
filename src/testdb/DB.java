/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testdb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class DB {

    private static final String URL = "jdbc:sqlite:tasks.db";

    public static void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(URL)) {
            Statement statement = connection.createStatement();
            String sqlCreateTable = "CREATE TABLE IF NOT EXISTS tasks ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "description TEXT, "
                    + "due_date TEXT, "
                    + "priority TEXT, "
                    + "status TEXT)";
            statement.execute(sqlCreateTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addTask(Task task) {
        String sqlInsert = "INSERT INTO tasks(name, description, due_date, priority, status) VALUES(?, ?, ?, ?, ?)";
        try (
            Connection connection = DriverManager.getConnection(URL); PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getDueDate());
            preparedStatement.setString(4, task.getPriority());
            preparedStatement.setString(5, task.getStatus());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1); // Obtener el ID generado
                task.setId(generatedId); // Establecer el ID generado en el objeto Task
            } else {
                throw new SQLException("No se pudo obtener el ID generado para la tarea.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTask(Task task) {
        String sqlUpdate = "UPDATE tasks SET name = ?, description = ?, due_date = ?, priority = ?, status = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL); PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getDueDate());
            preparedStatement.setString(4, task.getPriority());
            preparedStatement.setString(5, task.getStatus());
            preparedStatement.setInt(6, task.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTask(int taskId) {
        String sqlDelete = "DELETE FROM tasks WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL); PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)) {
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sqlSelect = "SELECT * FROM tasks";
        try (
                Connection connection = DriverManager.getConnection(URL); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sqlSelect)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String dueDate = resultSet.getString("due_date");
                String priority = resultSet.getString("priority");
                String status = resultSet.getString("status");
                tasks.add(new Task(id, name, description, dueDate, priority, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }
}
