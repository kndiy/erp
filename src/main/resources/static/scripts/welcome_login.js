
function openUserRegisterModal() {
    var modal = document.getElementById("modal-register");
    modal.style.display = "block";
}

window.addEventListener("click", function(event) {
    var modal = document.getElementById("modal-register");

    if (event.target == modal) {
        modal.style.display = "none";
    }
});
