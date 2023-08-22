
function openNewInventoryInModal() {
    let modal = document.getElementById('new-inventory-in-modal');
    addClosingAction(modal);

    modal.style.display = 'block';
}

let lastToDisplay;
let lastCheckMarkEle;
function filterInventories(curInventoryInGrid) {

    let idInventoryIn = curInventoryInGrid.getElementsByClassName('check-radio-id')[0].textContent;

    if (lastToDisplay != null) {
        lastToDisplay.style.display = 'none';
    }

    let inventoryGridArr = document.getElementsByClassName('id-inventory-in-key');
    for (let i = 0; i < inventoryGridArr.length; i ++) {
        let idInventoryInKey = inventoryGridArr[i].children[0].textContent;

        if (idInventoryInKey == idInventoryIn) {
            inventoryGridArr[i].style.display = 'block';
            lastToDisplay = inventoryGridArr[i];
            break;
        }
    }

    let checkMarkEle = curInventoryInGrid.getElementsByClassName('check-radio')[0];
    document.getElementById('delete-inventory-in-id-input').value = idInventoryIn;

    checkMarkEle.style.background = '#F9D997';

    if (lastCheckMarkEle != null && checkMarkEle != lastCheckMarkEle) {
        lastCheckMarkEle.style.background = 'white';
    }

    lastCheckMarkEle = checkMarkEle;
}


function openEditInventoryInModal(voucher, source, supplierSource, value, foreignValue, exchangeRate, idInventoryIn) {
    let modal = document.getElementById('edit-inventory-in-modal');
    addClosingAction(modal);

    modal.getElementsByClassName('voucher')[0].value = voucher;
    modal.getElementsByClassName('value-vnd')[0].value = value.split(" ")[0];
    if (foreignValue != null && foreignValue != "") {
        modal.getElementsByClassName('value-foreign')[0].value = foreignValue.split(" ")[0];
        modal.getElementsByClassName('foreign-unit')[0].value = foreignValue.split(" ")[1];
    }
    modal.getElementsByClassName('exchange-rate')[0].value = exchangeRate;
    modal.getElementsByClassName('id-inventory-in')[0].value = idInventoryIn;


    let selectArr = document.getElementsByClassName('select-edit-inventory-in');

    for (let i = 0; i < selectArr[0].children.length; i ++) {
        if (selectArr[0].children[i].value == source) {
            selectArr[0].selectedIndex = i;
            break;
        }
    }

    for (let i = 0; i < selectArr[1].children.length; i ++) {
        if (selectArr[1].children[i].value == supplierSource) {
            selectArr[1].selectedIndex = i;
            break;
        }
    }

    modal.style.display = 'block';

}

function openDeleteInventoryInModal() {
    let modal = document.getElementById('delete-inventory-in-modal');
    addClosingAction(modal);

    modal.style.display = 'block';
}