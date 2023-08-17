
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




function openSalesReporting() {

    let form = document.getElementById('welcome-sales-reporting');
    let toDate = new Date();
    let fromDate = new Date();
    fromDate.setDate(fromDate.getDate() - 7);
    let addedString = "From_" + fromDate.toISOString().split('T')[0] + "_To_" + toDate.toISOString().split('T')[0] + "/";
    form.action = form.action + addedString;

    form.submit();
}