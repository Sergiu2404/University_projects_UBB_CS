<%@ page import="registration.models.Profile" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="registration.data_access_object.ProfileDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Profile Details</title>
    <style>
        .header {
            background-color: #333;
            color: #fff;
            padding: 10px;
            text-align: center;
            display: flex;
            flex-direction: row;
            align-items: center;
            justify-content: space-between;
        }

        .wlecome-message{
            color: #4CAF50;
            font-size: 16px;
        }

        .header button {
            vertical-align: middle;
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

        /* Added style for logout button */
        .logout-button {
            margin-left: 10px;
            margin-top: 10px;
        }

        /* Modal */
        .modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            z-index: 1; /* Sit on top */
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4);
        }

        .modal-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%; /* Could be more or less, depending on screen size */
            border-radius: 10px;
        }

        /* Close button */
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }

        .search-form {
            text-align: center;
            margin-top: 20px;
        }

        .search-form label {
            font-size: 16px;
            margin-right: 10px;
        }

        .search-form input[type="text"] {
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
            width: 200px;
        }

        .search-form .search-button {
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

        .search-form .search-button:hover {
            background-color: #45a049;
        }



        .profile-details {
            margin-top: 20px;
        }

        .profile-details table {
            width: 100%;
            border-collapse: collapse;
        }

        .profile-details table td,
        .profile-details table th {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        .profile-details table th {
            background-color: #f2f2f2;
        }

        .profile-details table tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .profile-details table tr:hover {
            background-color: #ddd;
        }



        /* Search results section */
        .search-results {
            margin-top: 20px;
        }

        .search-results h2 {
            font-size: 20px;
            margin-bottom: 10px;
        }

        .search-results table {
            width: 100%;
            border-collapse: collapse;
        }

        .search-results table th,
        .search-results table td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        .search-results table th {
            background-color: #f2f2f2;
        }

        .search-results table tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .search-results table tr:hover {
            background-color: #ddd;
        }

        .search-results p {
            margin-top: 10px;
            font-style: italic;
        }



        .profile-details ul {
            list-style-type: none;
            padding: 0;
            display: flex;
            flex-direction: column;
            text-align: center;
        }

        .profile-details li {
            margin-bottom: 10px;
            font-size: 20px;
            color: #4CAF50;

        }

        .profile-details li strong {
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="header">
    <form id="logoutForm" action="<%= request.getContextPath() %>/logoutServlet" method="post">
        <button class="logout-button" type="submit">Logout</button>
    </form>
    <%
        // Retrieve the Profile object from the session
        Profile loggedInProfile = (Profile) session.getAttribute("loggedInProfile");
    %>
    <span class="wlecome-message">Welcome, <%= loggedInProfile.getUsername() %>!</span>
</div>

<!-- Modal -->
<div id="successModal" class="modal">
    <!-- Modal content -->
    <div class="modal-content">
        <span class="close">&times;</span>
        <h2>Logged in successfully</h2>
    </div>
</div>




<!-- Edit Profile Modal -->
<div id="editProfileModal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <h2>Edit Profile</h2>
        <form id="editProfileForm" action="<%= request.getContextPath() %>/updateProfileServlet" method="post">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" value="<%= loggedInProfile.getEmail() %>" required>
            <br>
            <label for="image">Image:</label>
            <input type="text" id="image" name="image" value="<%= loggedInProfile.getImage() %>" required>
            <br>
            <label for="age">Age:</label>
            <input type="number" id="age" name="age" value="<%= loggedInProfile.getAge() %>" required>
            <br>
            <label for="home">Home:</label>
            <input type="text" id="home" name="home" value="<%= loggedInProfile.getHome() %>" required>
            <br>
            <button type="submit">Save Changes</button>
        </form>
    </div>
</div>



<div class="profile-details">
    <ul>
        <li><strong>Email:</strong> <%= loggedInProfile.getEmail() %></li>
        <li> <img src="<%= loggedInProfile.getImage() %>" alt="Profile Image" style="width:100px;height:100px;"></li>
        <li><strong>Age:</strong> <%= loggedInProfile.getAge() %></li>
        <li><strong>Home:</strong> <%= loggedInProfile.getHome() %></li>
    </ul>
</div>



<div class="search-form">
    <form action="" method="GET">
        <label for="username">Search by Username:</label>
        <input type="text" id="username" name="username">
        <input class="search-button" type="submit" value="Search">
    </form>
</div>


<div class="search-results">
    <%
        ProfileDAO profileDAO = new ProfileDAO();
        String username = request.getParameter("username");
        String email = request.getParameter("email");

        if ((username != null && !username.isEmpty()) || (email != null && !email.isEmpty())) {

            List<Profile> searchResults = null;
            try {
                //if (!username.isEmpty()) {
                searchResults = profileDAO.getProfilesByUsername(username);
//                } else if (!email.isEmpty()) {
//                    searchResults = profileDAO.getProfilesByEmail(email);
//                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

            if (searchResults != null && !searchResults.isEmpty()) {
    %>
    <h2>Search Results:</h2>
    <table>
        <tr>
            <th>Username</th>
            <th>Email</th>
            <th>Image</th>
            <th>Age</th>
            <th>Home</th>
        </tr>
        <% for (Profile profile : searchResults) { %>
        <tr>
            <td><%= profile.getUsername() %></td>
            <td><%= profile.getEmail() %></td>
            <td> <img src="<%= profile.getImage() %>" alt="Profile Image" style="width:100px;height:100px;"> </td>
            <td><%= profile.getAge() %></td>
            <td><%= profile.getHome() %></td>
        </tr>
        <% } %>
    </table>
    <%
    } else {
    %>
    <p>No matching profiles found.</p>
    <%
            }
        }
    %>
</div>

</body>
</html>
