
window.addEventListener('load', function(event) {

    let inventoryOutGridArr = document.getElementsByClassName('inventory-out-grid main');
    let deliveredQuantity = document.getElementById('delivered-quantity');
    let orderQuantity = document.getElementById('order-quantity');
    let allowedQuantity = document.getElementById('allowed-quantity');
    let differential = document.getElementById('differential');
    let percentage = document.getElementById('percentage');
    let count = document.getElementById('count');



    for (let i = 0; i < inventoryOutGridArr.length; i ++) {

        let curGrid = inventoryOutGridArr[i];

        let takeAllCb = curGrid.getElementsByClassName('take-all-cb')[0];
        let splitCb = curGrid.getElementsByClassName('split-cb')[0];
        let remaining = curGrid.getElementsByClassName('equivalent')[0];
        let splitQuantity = curGrid.getElementsByClassName('split-quantity')[0];

        splitQuantity.readOnly = true;

        let remainingValue = remaining.value.split(" ")[0];

        takeAllCb.addEventListener('click', function() {

            if (takeAllCb.checked == true && splitCb.checked == true) {

                splitQuantity.value = splitQuantity.value == "" ? 0 : splitQuantity.value;
                deliveredQuantity.textContent = (new BigDecimal(deliveredQuantity.textContent, false, 2).minus(splitQuantity.value)).toString();

                splitCb.checked = false;
                splitQuantity.value = "";
                splitQuantity.readOnly = true;

                deliveredQuantity.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).plus(remainingValue).toString();
                differential.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(orderQuantity.textContent).toString();
                percentage.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(orderQuantity.textContent).divides(orderQuantity.textContent).times(100) + ' %';

            }
            else if (takeAllCb.checked == true) {
                count.textContent = parseInt(count.textContent) + 1;

                deliveredQuantity.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).plus(remainingValue).toString();
                differential.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(orderQuantity.textContent).toString();
                percentage.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(orderQuantity.textContent).divides(orderQuantity.textContent).times(100) + ' %';
            }
            else {
                count.textContent = parseInt(count.textContent) - 1;

                deliveredQuantity.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(remainingValue).toString();
                differential.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(orderQuantity.textContent).toString();
                percentage.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(orderQuantity.textContent).divides(orderQuantity.textContent).times(100) + ' %';
            }
        });

        splitCb.addEventListener('click', function() {

            if (takeAllCb.checked == true && splitCb.checked == true) {
                deliveredQuantity.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(remainingValue).toString();
                differential.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(orderQuantity.textContent).toString();
                percentage.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(orderQuantity.textContent).divides(orderQuantity.textContent).times(100) + ' %';

                takeAllCb.checked = false;
                splitQuantity.readOnly = false;
                splitQuantity.focus();
            }
            else if (splitCb.checked == true) {

                count.textContent = parseInt(count.textContent) + 1;

                splitQuantity.readOnly = false;
                splitQuantity.focus();
            }
            else {

                count.textContent = parseInt(count.textContent) - 1;

                deliveredQuantity.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(splitQuantity.value);
                differential.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(orderQuantity.textContent).toString();
                percentage.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(orderQuantity.textContent).divides(orderQuantity.textContent).times(100) + ' %';
                splitQuantity.value = "";
                splitQuantity.readOnly = true;
            }
        });

        let oldSplitQuantity;
        splitQuantity.addEventListener('focus', function() {
            oldSplitQuantity = splitQuantity.value == "" ? 0 : splitQuantity.value;
        });

        splitQuantity.addEventListener('change', function() {

            if (isNaN(parseFloat(splitQuantity.value))) {
                splitQuantity.value = 0;
            }

            if (parseFloat(splitQuantity.value) > parseFloat(remainingValue)) {
                splitQuantity.value = 0;
                alert("Split Quantity cannot be larger than Remaining Quantity!!");
            }

            if (oldSplitQuantity != null) {
                deliveredQuantity.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(oldSplitQuantity);
            }
            deliveredQuantity.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).plus(splitQuantity.value);
            differential.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(orderQuantity.textContent).toString();
            percentage.textContent = new BigDecimal(deliveredQuantity.textContent, false, 2).minus(orderQuantity.textContent).divides(orderQuantity.textContent).times(100) + ' %';

            oldSplitQuantity = splitQuantity.value;
        });
    }

});
