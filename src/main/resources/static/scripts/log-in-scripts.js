function openAdminPrompt() {
    var modalAdmin = document.getElementById("modal-admin");
    modalAdmin.style.display = "block";
}

function checkAdminPassword(adminPass) {
    var modalAdmin = document.getElementById("modal-admin");
    var modalRegister = document.getElementById("modal-register");

    var pass = document.getElementById("input-admin").value;

    if (pass == adminPass) {
        modalAdmin.style.display = "none";
        modalRegister.style.display = "block";
    }
    else {
        modalAdmin.style.display = "none";
    }
}

window.addEventListener("click", function(event) {
    var modalAdmin = document.getElementById("modal-admin");
    var modalRegister = document.getElementById("modal-register");

    if (event.target == modalRegister) {
        modalRegister.style.display = "none";
    }

    if (event.target == modalAdmin) {
        modalAdmin.style.display = "none";
    }
});

document.addEventListener("keydown", function(event) {
    var modalAdmin = document.getElementById("modal-admin");
    var modalRegister = document.getElementById("modal-register");

    if (event.keyCode == 27) {
        modalRegister.style.display = "none";
        modalAdmin.style.display = "none";
    }
});

function pressToClose() {
    var modalAdmin = document.getElementById("modal-admin");
    var modalRegister = document.getElementById("modal-register");

    modalRegister.style.display = "none";
    modalAdmin.style.display = "none";
}

function showPassword(ele) {

    let passwordEle = document.getElementById('password');
    let rePasswordEle = document.getElementById('re-password');

    if (ele.checked == true) {
        passwordEle.type = 'text';
        rePasswordEle.type = 'text';
    }
    else {
        passwordEle.type = 'password';
        rePasswordEle.type = 'password';
    }
}