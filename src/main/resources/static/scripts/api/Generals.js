
function addClosingAction(modal) {
    let closeButton = modal.getElementsByClassName('close-button')[0];

    window.addEventListener('keydown', function(event) {
        if (event.keyCode == 27) {
            modal.style.display = 'none';
        }
    });
    modal.addEventListener('click', function(event) {
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    });
    closeButton.addEventListener('click', function(event) {
        if (event.target == closeButton) {
            modal.style.display = 'none';
        }
    });
}

const months = new Map([
          ['Jan', '01'],
          ['Feb', '02'],
          ['Mar', '03'],
          ['Apr', '04'],
          ['May', '05'],
          ['Jun', '06'],
          ['Jul', '07'],
          ['Aug', '08'],
          ['Sep', '09'],
          ['Oct', '10'],
          ['Nov', '11'],
          ['Dec', '12']
          ]);

function openSalesReportingHeader() {
    let form = document.getElementById('header-sale-reporting-form');
    let toDate = new Date();
    let fromDate = new Date();
    fromDate.setDate(fromDate.getDate() - 7);
    let addedString = "From_" + fromDate.toISOString().split('T')[0] + "_To_" + toDate.toISOString().split('T')[0] + "/";
    form.action = form.action + addedString;

    form.submit();
}