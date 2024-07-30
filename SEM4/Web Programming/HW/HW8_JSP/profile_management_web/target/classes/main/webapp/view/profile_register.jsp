<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
    <script>
        function displayAlert(message) {
            alert(message);
        }

        function validateForm() {
            var username = document.forms["registerForm"]["username"].value;
            var password = document.forms["registerForm"]["password"].value;
            var email = document.forms["registerForm"]["email"].value;
            var image = document.forms["registerForm"]["image"].value;
            var age = document.forms["registerForm"]["age"].value;
            var home = document.forms["registerForm"]["home"].value;
            var valid = true;
            var errorMsg = "";

            if (username == null || username == "") {
                errorMsg += "Username must be filled out.\\n";
                valid = false;
            }
            if (password == null || password == "") {
                errorMsg += "Password must be filled out.\\n";
                valid = false;
            }
            if (email == null || email == "") {
                errorMsg += "Email must be filled out.\\n";
                valid = false;
            } else {
                var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
                if (!emailPattern.test(email)) {
                    errorMsg += "Invalid email format.\\n";
                    valid = false;
                }
            }
            if (image == null || image == "") {
                errorMsg += "Image URL must be filled out.\\n";
                valid = false;
            }
            if (age == null || age == "") {
                errorMsg += "Age must be filled out.\\n";
                valid = false;
            } else {
                if (isNaN(age) || age <= 0) {
                    errorMsg += "Age must be a positive number.\\n";
                    valid = false;
                }
            }
            if (home == null || home == "") {
                errorMsg += "Home must be filled out.\\n";
                valid = false;
            }
            if (!valid) {
                displayAlert(errorMsg);
            }
            return valid;
        }
    </script>
    <style>
        /* styles.css */
        .header {
            background-color: #333;
            color: #fff;
            padding: 10px;
            text-align: center;
        }

        .header button {
            background-color: #4CAF50;
            border: none;
            color: white;
            padding: 10px 20px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            cursor: pointer;
            border-radius: 5px;
            transition-duration: 0.4s;
        }

        .header button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<div class="header">
    <button onclick="window.location.href='<%= request.getContextPath() %>/login'">Go to Login</button>
</div>
<div class="container">
    <h3>Register</h3>
    <form name="registerForm" action="<%= request.getContextPath() %>/register" method="post" onsubmit="return validateForm()">
        <table style="width: 80%">
            <tr>
                <td>Username: </td>
                <td><input type="text" name="username"/></td>
            </tr>
            <tr>
                <td>Password: </td>
                <td><input type="password" name="password"/></td>
            </tr>
            <tr>
                <td>Email: </td>
                <td><input type="text" name="email"/></td>
            </tr>
            <tr>
                <td>Image: </td>
                <td><input type="text" name="image"/></td>
            </tr>
            <tr>
                <td>Age: </td>
                <td><input type="text" name="age"/></td>
            </tr>
            <tr>
                <td>Home: </td>
                <td><input type="text" name="home"/></td>
            </tr>
        </table>
        <input type="submit" value="Submit"/>
    </form>
</div>

<% if (request.getAttribute("usernameExistsMessage") != null) { %>
<script>
    displayAlert("<%= request.getAttribute("usernameExistsMessage") %>");
</script>
<% } %>

</body>
</html>
