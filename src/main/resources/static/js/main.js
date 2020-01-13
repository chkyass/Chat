/**
*
* register link
*/
function registerLink() {
    $(".mb2").before("<div class=\"input_outer\" id=\"confirm-container\"><input id=\"confirm-pass\" name=\"pass\" class=\"text\"type=\"password\" placeholder=\"Confirm password\"></div>");
    $("#register-btn").text("Register");
    $("#register-login-link").text("Already registered ? Login");
    $("#register-login-link").attr("href", "javascript:loginLink()");
    $("#login").attr("action", "javascript:register()");
}

/**
*
* login link
*/
function loginLink() {
    $("#confirm-container").remove();
    $("#register-btn").text("Login");
    $("#register-login-link").text("Not registered ? Create an account");
    $("#register-login-link").attr("href", "javascript:registerLink()");
    $("#login").attr("action", "login");
}

/**
 * register
 */
function register() {
    $("#user-exist").remove();
    $("#password-mismatch").remove();

    if($("#confirm-pass").val() !== $("#pass").val()) {
        $("#pass").after(" <span id=\"password-mismatch\" style=\"color:red\">Password mismatch</span>");
        $("#confirm-pass").val("")
        $("#pass").val("")
    } else {      
          $.post( "/register", { user: $("#user").val(), pass: $("#pass").val()},
            function (data) {
                var message = JSON.parse(data);
                if(message.status === "success") {
                    window.location.replace("/chat");
                } else {
                    $("#user").after(" <span id=\"user-exist\" style=\"color:red\">" + message.message + "</span>");
                }
            });
    }
}

/**
 * Enter to login.
 */
document.onkeydown = function (event) {
    var e = event || window.event || arguments.callee.caller.arguments[0];
    e.keyCode === 13 && login();
};