package com.gmoi.directmessage.mail;

public class MailTemplates {

    private static final String BASE_HTML_TEMPLATE = """
        <html>
        <head>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 0;
                    background-color: #f4f7fa;
                }
                .email-card {
                    background-color: #ffffff;
                    width: 600px;
                    margin: 20px auto;
                    padding: 20px;
                    border-radius: 8px;
                    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                }
                .email-card h2 {
                    color: #333;
                }
                .email-card p {
                    font-size: 16px;
                    color: #555;
                }
                .email-card .button {
                    display: inline-block;
                    margin-top: 20px;
                    padding: 10px 20px;
                    background-color: #007bff;
                    color: white;
                    text-decoration: none;
                    font-size: 16px;
                    border-radius: 5px;
                    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
                }
                .email-card .button:hover {
                    background-color: #0056b3;
                }
            </style>
        </head>
        <body>
            <div class="email-card">
                <h2>{TITLE}</h2>
                <p>{BODY}</p>
                {BUTTON}
            </div>
        </body>
        </html>
        """;

    public static String friendRequestSent(String friendName) {
        return BASE_HTML_TEMPLATE
                .replace("{TITLE}", "New Friend Request")
                .replace("{BODY}", "You have received a friend request from " + friendName + ".")
                .replace("{BUTTON}", "");
    }

    public static String friendRequestAccepted(String friendName) {
        return BASE_HTML_TEMPLATE
                .replace("{TITLE}", "Friend Request Accepted")
                .replace("{BODY}", "Your friend request to " + friendName + " has been accepted!")
                .replace("{BUTTON}", "");
    }

    public static String registrationSuccess(String userName) {
        return BASE_HTML_TEMPLATE
                .replace("{TITLE}", "Registration Successful")
                .replace("{BODY}", "Welcome, " + userName + "! Your registration is complete.")
                .replace("{BUTTON}", "");
    }

    public static String emailVerification(String verificationLink) {
        return BASE_HTML_TEMPLATE
                .replace("{TITLE}", "Email Verification")
                .replace("{BODY}", "Please verify your email address by clicking the button below.")
                .replace("{BUTTON}", "<a href=\"" + verificationLink + "\" class=\"button\">Verify</a>");
    }
}

