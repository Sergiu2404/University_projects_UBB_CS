<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
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

        .error {
            color: red;
        }
    </style>
    <script>
        function validateForm() {
            var username = document.forms["loginForm"]["username"].value;
            var password = document.forms["loginForm"]["password"].value;
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
            if (!valid) {
                alert(errorMsg);
            }

            return valid;
        }
    </script>
</head>
<body>
<div class="header">
    <button onclick="window.location.href='<%= request.getContextPath() %>/register'">Go to Register</button>
</div>
<h3>Login</h3>
<form name="loginForm" action="<%= request.getContextPath() %>/login" method="post" onsubmit="return validateForm()">
    <table>
        <tr>
            <td>Username:</td>
            <td><input type="text" name="username"></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password" name="password"></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="Login"></td>
        </tr>
    </table>
</form>
</body>
</html>
