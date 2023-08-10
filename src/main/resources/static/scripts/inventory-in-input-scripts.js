
window.addEventListener('load', function(event) {
    var currentLocation = window.location.href;
    var hiddenInput = document.getElementById('hidden-input-new-article-number');
    hiddenInput.value = "";

    const stringArr = currentLocation.split("/");
    const stringArrIdInventoryIn = stringArr[stringArr.length - 2].split("=");
    var idInventoryIn = stringArrIdInventoryIn[stringArrIdInventoryIn.length - 1];

    var inputIdInventoryInEles = document.getElementById('input-idInventoryIn');

    inputIdInventoryInEles.value = idInventoryIn;

    var form = document.getElementById('form-inventory');
    form.addEventListener('keydown', function(event) {
        if (event.keyCode == 13) {
            event.preventDefault();
        }
    });

    var inputNumberingEdit = document.getElementsByClassName('numbering-inventory-input-edit');
    let largest = 0;
    for (let i = 0; i < inputNumberingEdit.length; i ++) {
        if (inputNumberingEdit[i].value == "") {
            continue;
        }
        largest = Math.max(largest, inputNumberingEdit[i].value);
    }
    for (let i = 0; i < inputNumberingEdit.length; i ++) {
        if (inputNumberingEdit[i].value != "") {
            continue;
        }
        inputNumberingEdit[i].value = ++largest;
    }


    var inputNumbering = document.getElementsByClassName('numbering-inventory-input');
    for (let i = 0; i < inputNumbering.length; i ++) {
        inputNumbering[i].value = i + 1;
    }

});

function changeBelowItemCodeSelect(index, select) {

    var selectArr = document.getElementsByClassName("select-item-code");

    var textValue = select.options[select.selectedIndex].textContent;
    var value = select.options[select.selectedIndex].value;
    var indexToChange = select.selectedIndex;

    for (let i = parseInt(index) + 1; i < selectArr.length; i ++) {
        selectArr[i].selectedIndex = indexToChange;
    }
}

function changeBelowAddressSelect(index, select) {

    var selectArr = document.getElementsByClassName('select-address');

    var optionIndex = select.selectedIndex;
    for (let i = parseInt(index) + 1; i < selectArr.length; i ++) {
        selectArr[i].selectedIndex = optionIndex;
    }
}

function nextWarehousePlacement(index, currentEle) {
    if (event.key === 'Enter') {
        var inputArr = document.getElementsByClassName('input-warehouse');
        var nextIndex = parseInt(index) + 1;

        for (let i = nextIndex; i < inputArr.length; i ++) {
            inputArr[i].value = currentEle.value;
        }

        inputArr[parseInt(index) + 1].focus();
    }
}

function nextProductionCode(index, currentEle) {
    if (event.key === 'Enter') {
        var inputArr = document.getElementsByClassName('input-production-code');
        var nextIndex = parseInt(index) + 1;

        for (let i = nextIndex; i < inputArr.length; i ++) {
            inputArr[i].value = currentEle.value;
        }

        inputArr[parseInt(index) + 1].focus();
    }
}

function nextQuantity(index) {
    if (event.key === 'Enter') {
        var inputArr = document.getElementsByClassName('input-quantity');
        var nextIndex = parseInt(index) + 1;
        inputArr[parseInt(index) + 1].focus();
    }
}

function nextOrNewUnit(index, currentEle) {
    var selectUnitArr = document.getElementsByClassName('select-unit');
    var inputNewUnitArr = document.getElementsByClassName('input-new-unit');
    var divNewUnitArr = document.getElementsByClassName('new-unit-div');

    var indexInt = parseInt(index);
    var nextIndex = indexInt + 1;
    var optionIndex = currentEle.selectedIndex;

    if (currentEle.options[currentEle.selectedIndex].value == 'NewUnit') {
        divNewUnitArr[indexInt].style.display = 'block';
    }
    else {

        for (let i = nextIndex - 1; i < selectUnitArr.length; i ++) {
            selectUnitArr[i].selectedIndex = optionIndex;
            divNewUnitArr[i].style.display = 'none';
        }
    }
}

function copyNewUnit(index, currentEle) {

    if (event.keyCode == 13) {
        var indexInt = parseInt(index);
        var nextIndex = indexInt + 1;
        var newUnit = currentEle.value;

        var selectUnitArr = document.getElementsByClassName('select-unit');
        var inputNewUnitArr = document.getElementsByClassName('input-new-unit');
        var divNewUnitArr = document.getElementsByClassName('new-unit-div');

        var optionIndex = selectUnitArr[indexInt].selectedIndex;

        for (let i = nextIndex; i < selectUnitArr.length; i ++) {
            selectUnitArr[i].selectedIndex = optionIndex;
            divNewUnitArr[i].style.display = 'block';
            inputNewUnitArr[i].value = newUnit;
        }
    }
}

function openNewArticleNumberModal() {
    var modal = document.getElementById('new-article-number-modal');
    modal.style.display = 'block';

    var closeButton = modal.getElementsByClassName('close-button')[0];
    closeButton.addEventListener('click', function() {
        modal.style.display = 'none';
    });

    window.addEventListener('keydown', function(event) {
        if (event.keyCode == 27) {
            modal.style.display = "none";
        }
    });

    modal.addEventListener('click', function(event) {
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    });
}

function openNewArticleNumberEditModal() {
    var modal = document.getElementById('new-article-number-modal-edit');
    modal.style.display = 'block';

    var closeButton = modal.getElementsByClassName('close-button')[0];
    closeButton.addEventListener('click', function() {
        modal.style.display = 'none';
    });

    window.addEventListener('keydown', function(event) {
        if (event.keyCode == 27) {
            modal.style.display = "none";
        }
    });

    modal.addEventListener('click', function(event) {
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    });
}

function changeArticleNumberPost() {
    var hiddenInput = document.getElementById('hidden-input-new-article-number');
    var input = document.getElementById('input-new-article-number');
    var form = document.getElementById('form-inventory');

    hiddenInput.value = input.value;
    form.submit();
}

function changeArticleNumberEditPost() {
    var hiddenInput = document.getElementById('hidden-input-new-article-number');
    var input = document.getElementById('input-new-article-number-edit');
    var form = document.getElementById('form-inventory');

    hiddenInput.value = input.value;
    form.submit();
}

function openDeleteInventoryModal(idInventory) {
    var modal = document.getElementById('delete-inventory-modal');
    var idInput = document.getElementById('delete-id-inventory');

    idInput.value = idInventory;
    idInput.focus();
    modal.style.display = 'block';

    var closeButton = modal.getElementsByClassName('close-button')[0];
    closeButton.addEventListener('click', function() {
        modal.style.display = 'none';
    });

    window.addEventListener('keydown', function(event) {
        if (event.keyCode == 27) {
            modal.style.display = "none";
        }
    });

    modal.addEventListener('click', function(event) {
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    });


}
