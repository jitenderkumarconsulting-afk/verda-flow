<html>
  <head>
    <title>Reset Password</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
      href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <link
      href="https://fonts.googleapis.com/css?family=Lato:300,400,700"
      rel="stylesheet"
      type="text/css"
    />
    <style>
      h1,
      h2,
      h3,
      h4,
      h5 {
        color: #0e132a;
        margin-bottom: 40px;
      }

      .error,
      #error-div {
        color: red;
      }

      .form-control {
        height: 50px;
      }

      .form-horizontal .control-label {
        padding-top: 15px;
      }

      .btn_default,
      .btn_default:hover,
      .btn_default:focus,
      .btn_default:active,
      .btn_default:active:focus {
        background-color: #d27611;
        color: #fff;
        height: 50px;
      }

      /*
                    .form-control:focus {
                        border-color: #D27611;
                        -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(210, 118, 17, 0.6);
                        box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(210, 118, 17, 0.6);
                    }
        */
      input:-webkit-autofill,
      textarea:-webkit-autofill,
      select:-webkit-autofill {
        background-color: #fff !important;
        -webkit-box-shadow: 0 0 0px 1000px white inset;
        -webkit-transition: background-color 5000s ease-in-out 0s;
        transition: background-color 5000s ease-in-out 0s;
      }
    </style>
  </head>

  <body>
    <div class="container">
      <div class="row">
        <div class="col-lg-12 text-center">
          <h1>Reset Password</h1>
        </div>
        <div class="col-lg-8 col-lg-offset-2 main_container">
          <form id="changePasswordForm" method="post" class="form-horizontal">
            <div class="form-group">
              <label class="col-sm-4 control-label">New Password</label>
              <div class="col-sm-6">
                <input
                  type="password"
                  class="form-control"
                  max="45"
                  name="new_pass"
                  id="new_pass"
                />
                <span class="error" id="validationError"></span>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-4 control-label">Confirm Password</label>
              <div class="col-sm-6">
                <input
                  type="password"
                  class="form-control"
                  max="45"
                  name="retype_new_pass"
                  id="retype_new_pass"
                />
                <span class="error" id="retype_validationError"></span>
              </div>
            </div>
            <div class="form-group right-form-btn">
              <div class="col-sm-offset-4 col-sm-6">
                <button class="btn btn_default" id="chng-pass">
                  Change password
                </button>
              </div>
            </div>
            <div class="form-group">
              <div id="message_div" class="col-sm-offset-4 col-sm-6"></div>
            </div>
          </form>
        </div>
      </div>
    </div>
    <script src="https://code.jquery.com/jquery-1.12.0.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.15.0/jquery.validate.min.js"></script>
    <script type="text/javascript">
      $(function () {
        /* Change Password */
        $("form#changePasswordForm").trigger("reset");
        var accountsecform = $("form#changePasswordForm");

        function resetFormError() {
          accountsecform.find($("#validationError")).text("");
          accountsecform.find($("#retype_validationError")).text("");
        }

        accountsecform.find($("#chng-pass")).click(function () {
          resetFormError();
          let password = accountsecform.find($("#new_pass")).val();
          let valid = true;
          if (password.trim().length < 6) {
            accountsecform
              .find($("#validationError"))
              .text("New Password require minimum 6 character");
            valid = false;
          }

          let repassword = accountsecform.find($("#retype_new_pass")).val();
          if (repassword != password) {
            accountsecform
              .find($("#retype_validationError"))
              .text("New Password and Confirm Password should be same.");
            valid = false;
          }
          if (!valid) {
            return false;
          }
          formSubmit();
        });

        var getUrlParameter = function getUrlParameter(sParam) {
          var sPageURL = decodeURIComponent(
              window.location.search.substring(1),
            ),
            sURLVariables = sPageURL.split("&"),
            sParameterName,
            i;

          for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split("=");

            if (sParameterName[0] === sParam) {
              return sParameterName[1] === undefined ? true : sParameterName[1];
            }
          }
        };

        function formSubmit() {
          accountsecform.find($("#message_div")).html("");
          $("#chng-pass").attr("disabled", "disabled");
          var new_pass = accountsecform.find($("#new_pass")).val();
          var arr = {
            id: getUrlParameter("u"),
            tempCode: getUrlParameter("t"),
            newPass: new_pass,
          };
          console.log(arr);

          $.ajax({
            url: "/verdaflow/api/v1/auth/updatePassword",
            type: "POST",
            dataType: "json",
            data: JSON.stringify(arr),
            contentType: "application/json; charset=utf-8",
            success: function (result) {
              console.log(result);
              if (result.responseCode == 200) {
                $("#chng-pass").removeAttr("disabled");
                accountsecform
                  .find($("#message_div"))
                  .html(
                    '<div id="msg-div"><h4>Password updated successfully.</h4></div>',
                  );
              } else {
                accountsecform
                  .find($("#message_div"))
                  .html('<div id="error-div">' + result.message + "</div>");
                $("#error-div")
                  .delay(2000)
                  .fadeOut("normal", function () {
                    accountsecform.find($("#message_div")).html("");
                    $("#chng-pass").removeAttr("disabled");
                  });
              }
            },
            error: function (request, status, error) {
              $("#chng-pass").html("Change password");
              accountsecform
                .find($("#message_div"))
                .html(
                  '<div id="error-div">' +
                    request.responseJSON.message +
                    "</div>",
                );
              $("#error-div")
                .delay(2000)
                .fadeOut("normal", function () {
                  accountsecform.find($("#message_div")).html("");
                  $("#chng-pass").removeAttr("disabled");
                });
            },
          });
        }
        /* End Change Password */
      });
    </script>
  </body>
</html>
