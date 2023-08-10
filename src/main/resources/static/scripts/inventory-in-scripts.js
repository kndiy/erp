
function openNewInventoryInModal() {
    var newInventoryInModal = document.getElementById('new-inventory-in-modal');
    var closeButton = newInventoryInModal.getElementsByClassName('close-button')[0];

    newInventoryInModal.style.display = 'block';

    newInventoryInModal.addEventListener("click", function(event) {
        if (event.target == newInventoryInModal) {
            newInventoryInModal.style.display = 'none';
        }
    });
    closeButton.addEventListener("click", function(event) {
        if (event.target == closeButton) {
            newInventoryInModal.style.display = 'none';
        }
    });
    window.addEventListener("keydown", function(event) {
        if (event.keyCode == 27) {
            newInventoryInModal.style.display = 'none';
        }
    });
}

window.addEventListener('load', function(event) {
    var inventoryInEles = document.getElementsByClassName('grid-inventory-in');
    var lastCheckMarkEle;

    for (let i = 0; i < inventoryInEles.length; i ++) {
        inventoryInEles[i].addEventListener('click', function() {
            var idInventoryIn = parseInt(inventoryInEles[i].children[0].children[1].textContent);
            var checkMarkEle = inventoryInEles[i].children[0].children[0];
            var deleteInventoryInIdInput = document.getElementById('delete-inventory-in-id-input');

            deleteInventoryInIdInput.value = idInventoryIn;

            checkMarkEle.style.background = '#F9D997';

            filterInventories(idInventoryIn);

            if (lastCheckMarkEle != null && checkMarkEle != lastCheckMarkEle) {
                lastCheckMarkEle.style.background = 'white';
            }
            lastCheckMarkEle = checkMarkEle;
        });
    }
});

var lastToDisplay;
function filterInventories(idInventoryIn) {
    var inventoryInEleArr = document.getElementsByClassName('id-inventory-in');
    var testEle = document.getElementById('test');

    if (lastToDisplay != null) {
        lastToDisplay.style.display = 'none';
    }

    for (let i = 0; i < inventoryInEleArr.length; i ++) {
        var idInventoryInCheck = parseInt(inventoryInEleArr[i].children[0].textContent);

        if (idInventoryInCheck == idInventoryIn) {
            inventoryInEleArr[i].style.display = 'block';
            var numberingEleArr = inventoryInEleArr[i].getElementsByClassName('numbering-filter');
            for (let j = 0; j < numberingEleArr.length; j ++) {
                numberingEleArr[j].textContent = (j + 1).toString();
            }

            if (lastToDisplay != null && lastToDisplay != inventoryInEleArr[i]) {
                lastToDisplay.style.display = 'none';
            }

            lastToDisplay = inventoryInEleArr[i];
            break;
        }
    }





}

function openEditInventoryInModal(voucher, source, supplierSource, value, foreignValue, unit, exchangeRate, idInventoryIn) {
    var modal = document.getElementById('edit-inventory-in-modal');
    var closeButton = modal.getElementsByClassName('close-button')[0];


    var inputArr = document.getElementsByClassName('input-edit-inventory-in');
    var selectArr = document.getElementsByClassName('select-edit-inventory-in');

    inputArr[0].value = voucher;
    inputArr[1].value = value
    inputArr[2].value = foreignValue;
    inputArr[3].value = unit;
    inputArr[4].value = exchangeRate;
    inputArr[5].value = idInventoryIn;

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

        modal.addEventListener("click", function(event) {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        });
        closeButton.addEventListener("click", function(event) {
            if (event.target == closeButton) {
                modal.style.display = 'none';
            }
        });
        window.addEventListener("keydown", function(event) {
            if (event.keyCode == 27) {
                modal.style.display = 'none';
            }
        });
}

function openDeleteInventoryInModal() {
    var modal = document.getElementById('delete-inventory-in-modal');
    var closeButton = modal.getElementsByClassName('close-button')[0];


    modal.style.display = 'block';

    modal.addEventListener("click", function(event) {
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    });
    closeButton.addEventListener("click", function(event) {
        if (event.target == closeButton) {
            modal.style.display = 'none';
        }
    });
    window.addEventListener("keydown", function(event) {
        if (event.keyCode == 27) {
            modal.style.display = 'none';
        }
    });
}