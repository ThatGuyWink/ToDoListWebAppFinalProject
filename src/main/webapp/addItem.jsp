<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Add Item</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="container">
    <h1>Add New Item</h1>
    <div class="form-container">
        <form action="addItem" method="post">
            <div class="form-group">
                <label for="task">Task:</label>
                <input type="text" id="task" name="task" required>
            </div>
            <button type="submit" class="submit-button">Add Item</button>
        </form>
        <a href="index.jsp" class="back-link">Back to To Do List</a>
    </div>
</div>
</body>
</html>
