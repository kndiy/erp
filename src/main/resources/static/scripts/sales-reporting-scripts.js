
function filterFromDateToDate() {

    let fromDate = document.getElementById('from-date').value;
    let toDate = document.getElementById('to-date').value;

    let form = document.getElementById('filter-date-form');
    form.action = form.action + "From_" + fromDate + "_To_" + toDate + "/";

    form.submit();
}

function goToSaleLot(idSale, idArticle, idContainer, idLot) {

    let form = document.getElementById('go-to-sale-lot');
    form.action = form.action + idSale + '/articles/' + idArticle + '/containers/' + idContainer + '/lots/' + idLot + '/';

    form.submit();
}

function onclickSummaryGrid(curGrid) {
    let dateInput = document.getElementById('date-input');
    let turnInput = document.getElementById('turn-input');
    let idSaleLotInput = document.getElementById('id-sale-lot');
    let saleSourceInput = document.getElementById('sale-source');
    let customerInput = document.getElementById('customer');

    turnInput.value = curGrid.getElementsByClassName('summary-turn')[0].textContent;
    let dateArr = curGrid.getElementsByClassName('summary-date')[0].textContent.split('-');
    dateArr[0] = new Date().getFullYear();
    dateArr[1] = months.get(dateArr[1]);
    dateInput.value = dateArr.join('-');

    idSaleLotInput.value = curGrid.getElementsByClassName('id-sale-lot')[0].value;
    saleSourceInput.value = curGrid.getElementsByClassName('sale-source')[0].value;
    customerInput.value = curGrid.getElementsByClassName('customer')[0].value;
}

function checkBeforePrint(reportName) {

    let dateInput = document.getElementById('date-input');
    let turnInput = document.getElementById('turn-input');

    let form = document.getElementById('print-form');

    document.getElementById('report-name').value = reportName;

    form.submit();
}

window.addEventListener('load', function() {

    let reportName = document.getElementById('report-name').value;

    if (reportName != null && reportName != "") {
        let form = document.getElementById('print-form');
        let originalAction = form.action;
        let actionArr = form.action.split('/');

        actionArr[actionArr.length - 1] = 'print-' + reportName;

        form.action = actionArr.join('/');
        form.submit();
        form.action = originalAction;
    }

});