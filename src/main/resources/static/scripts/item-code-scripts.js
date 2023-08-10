window.addEventListener("load", function(event) {

    var selectItemCategoryEle = document.getElementById("select-item-category");
    var selectItemCategoryEleChildren = selectItemCategoryEle.children;
    var idCategory = 0;

    var selectItemTypeEle = document.getElementById("select-item-type");
    var selectItemTypeEleChildren = selectItemTypeEle.children;
    var idType = 0;

    var formAction = document.getElementById("form-filter-item-codes");

    selectItemCategoryEle.addEventListener("change", function() {
        idCategory = selectItemCategoryEle.value;
        selectItemTypeEle.value = 0;

        //reset options
        for (let i = 2; i < selectItemTypeEleChildren.length; i += 2) {
            selectItemTypeEleChildren[i].style.display = "block";
        }

        if (idCategory != 0) {
            formAction.action = '/item-codes/idItemCategory=' + idCategory;

            for (let i = 1; i < selectItemTypeEleChildren.length; i += 2) {
                var idCategoryFromType = selectItemTypeEleChildren[i].value;
                if (idCategoryFromType != idCategory) {
                    selectItemTypeEleChildren[i + 1].style.display = "none";
                }
            }
        }
        else {
            formAction.action = '/item-codes';
        }
    });

    selectItemTypeEle.addEventListener("change", function() {
        idType = selectItemTypeEle.value;

        //reset
        for (let i = 0; i < selectItemCategoryEleChildren.length; i ++) {
            selectItemCategoryEleChildren[i].style.display = "block";
        }

        if (idType != 0) {
            formAction.action = '/item-codes/idItemType=' + idType;
        }
        else {
            formAction.action = '/item-codes';
        }

    });
});

function openNewItemCodeModal() {
    var modal = document.getElementById('new-item-code-modal');
    modal.style.display = "block";
    addClosingAction(modal);
}

function openNewSupplierModal(idItemCode) {
    var modal = document.getElementById("new-supplier-modal");
    modal.style.display = 'block';
    addClosingAction(modal);

    var idInput = document.getElementById('new-item-code-supplier-id-item-code');
    idInput.value = idItemCode;
}

function openNewSellPriceModal(idItemCode) {
    var modal = document.getElementById('new-sell-price-modal');
    addClosingAction(modal);
    modal.style.display = 'block';

    var idInput = document.getElementById('new-sell-price-id-item-code');
    idInput.value = idItemCode;
}

function openDeleteItemCodeModal() {
    var modal = document.getElementById("delete-item-code-modal");
    addClosingAction(modal);
    modal.style.display = "block";
}

function openDeleteItemTypeModal() {
    var modal = document.getElementById("delete-item-type-modal");
    addClosingAction(modal);
    modal.style.display = "block";
}

function openDeleteItemCategoryModal() {
    var modal = document.getElementById("delete-item-category-modal");
    addClosingAction(modal);
    modal.style.display = "block";
}

function onSelectCategoryChange() {
    var categorySelect = document.getElementById("select-new-item-category");
    var typeSelect = document.getElementById("select-new-item-type");

    var curCategoryString = categorySelect.value;
    var newItemCategoryInput = document.getElementById("new-item-category-input");

    if (curCategoryString == "OTHER...") {
        newItemCategoryInput.style.display = "block";
        categorySelect.value = "";
    }
    else {
        newItemCategoryInput.style.display = "none";
        for (let i = 1; i < typeSelect.children.length; i += 2) {
            if (typeSelect.children[i].value == curCategoryString) {
                typeSelect.children[i + 1].style.display = "block";
            }
            else {
                typeSelect.children[i + 1].style.display = "none";
            }
        }
    }
}

function onSelectTypeChange() {
    var typeSelect = document.getElementById("select-new-item-type");
    var typeSelectString = typeSelect.value;
    var newItemTypeInput = document.getElementById("new-item-type-input");
    if (typeSelectString == "OTHER...") {
        typeSelect.value = "";
        newItemTypeInput.style.display = "block";
    }
    else {
        newItemTypeInput.style.display = "none";
    }
}

function onchangeNewSellPriceSelectContract() {
    var selectValueEle = document.getElementById('new-sell-price-select-contract');
    var newSellPriceContractInput = document.getElementById('new-sell-price-contract-input');
    var hiddenFields = document.getElementsByClassName('square-new-sell-price-hidden');

    if (selectValueEle.value == 'OTHER...') {
        selectValueEle.value = "";
        for (let i = 0; i < hiddenFields.length; i ++) {
            hiddenFields[i].style.display = 'flex';
        }
    }
    else {
        for (let i = 0; i < hiddenFields.length; i ++) {
            hiddenFields[i].style.display = 'none';
        }
        newSellPriceContractInput.value = "";
    }
}

function openDeleteSellPriceContractModal() {
    var modal = document.getElementById("delete-item-sell-price-contract-modal");
    addClosingAction(modal);
    modal.style.display = "block";
}

function openEditItemCodeModal(idItemCode, itemCategoryString, itemTypeString, itemCodeString) {

    var editItemCodeIdEle = document.getElementById('edit-item-code-id');
    var editItemCodeCateSelectEle = document.getElementById("select-edit-item-category");
    var editItemCodeTypeSelectEle = document.getElementById('select-edit-item-type');
    var editItemCodeEle = document.getElementById('edit-item-code-string-input');

    editItemCodeIdEle.value = idItemCode;
    editItemCodeEle.value = itemCodeString;

    for (let i = 0; i < editItemCodeCateSelectEle.children.length; i ++) {
        if (editItemCodeCateSelectEle.children[i].value == itemCategoryString) {
            editItemCodeCateSelectEle.children[i].selected = true;
            break;
        }
    }

    for (let i = 0; i < editItemCodeTypeSelectEle.children.length; i ++) {
        if (editItemCodeTypeSelectEle.children[i].value == itemTypeString) {
            editItemCodeTypeSelectEle.children[i].selected = true;
        }
    }

    editItemCodeCateSelectEle.addEventListener("change", function() {
        var categoryInput = document.getElementById('edit-item-category-input');
        if (editItemCodeCateSelectEle.value == "OTHER...") {
            categoryInput.style.display = "block";
            editItemCodeCateSelectEle.value = "";
        }
        else {
            categoryInput.style.display = 'none';
            categoryInput.value = "";
        }

        for (let i = 1; i < editItemCodeTypeSelectEle.children.length; i += 2) {
            if (editItemCodeTypeSelectEle.children[i].value == editItemCodeCateSelectEle.value) {
                editItemCodeTypeSelectEle.children[i + 1].style.display = 'block';
            }
            else {
                editItemCodeTypeSelectEle.children[i + 1].style.display = 'none';
            }
        }

    });

    editItemCodeTypeSelectEle.addEventListener("change", function() {
        var typeInput = document.getElementById("edit-item-type-input");
        if (editItemCodeTypeSelectEle.value == "OTHER...") {
            typeInput.style.display = "block";
            editItemCodeTypeSelectEle.value = "";
        }
        else {
            typeInput.style.display = 'none';
            typeInput.value = "";
        }
    });

    var modal = document.getElementById("edit-item-code-modal");
    addClosingAction(modal);
    modal.style.display = "block";
}

function openEditItemCodeSupplierModal(idItemCode, idSupplier, itemCodeSupplierString) {
    var modal = document.getElementById('edit-supplier-modal');
    addClosingAction(modal);

    var idItemCodeEle = document.getElementById('edit-supplier-id-item-code');
    var idSupplierEle = document.getElementById('edit-supplier-id-supplier');
    var newIdSupplierSelectEle = document.getElementById('edit-supplier-new-id-supplier');
    var itemCodeStringEle = document.getElementById('edit-supplier-string');

    idItemCodeEle.value = idItemCode;
    idSupplierEle.value = idSupplier;
    itemCodeStringEle.value = itemCodeSupplierString;

    for (let i = 0; i < newIdSupplierSelectEle.children.length; i ++) {
        if (newIdSupplierSelectEle.children[i].value == idSupplier) {
            newIdSupplierSelectEle.children[i].selected = true;
            break;
        }
    }

    modal.style.display = "block";
}

function openDeleteItemCodeSupplierModal(idItemCode, idSupplier, itemCodeString, supplierString) {
    var modal = document.getElementById('delete-supplier-modal');
    addClosingAction(modal);

    var hiddenIdItemCode = document.getElementById('delete-supplier-id-item-code');
    var hiddenIdSupplier = document.getElementById('delete-supplier-id-supplier');
    var itemCodeStringInputEle = document.getElementById('delete-supplier-item-code-string');
    var supplierInputEle = document.getElementById('delete-supplier-supplier-string');

    hiddenIdItemCode.value = idItemCode;
    hiddenIdSupplier.value = idSupplier;
    itemCodeStringInputEle.value = itemCodeString;
    supplierInputEle.value = supplierString;

    modal.style.display = "block";
}

function openEditItemSellPriceModal(idSalesContract, idCustomer, contractName, fromDate, toDate, amount, unit) {
    var modal = document.getElementById('edit-sell-price-modal');
    addClosingAction(modal);

    var salesContractIdEle = document.getElementById('sales-contract-id');
    var customerEle = document.getElementById('sales-contract-customer');
    var contractNameEle = document.getElementById('sales-contract-name');
    var fromDateEle = document.getElementById('sales-contract-from-date');
    var toDateEle = document.getElementById('sales-contract-to-date');
    var amountEle = document.getElementById('sales-contract-amount');
    var unitEle = document.getElementById('sales-contract-unit');

    salesContractIdEle.value = idSalesContract;
    contractNameEle.value = contractName;
    fromDateEle.value = fromDate;
    toDateEle.value = toDate;
    amountEle.value = amount;
    unitEle.value = unit;

    for (let i = 0; i < customerEle.children.length; i ++) {
        if (customerEle.children[i].value == idCustomer) {
            customerEle.children[i].selected = true;
        }
    }

    modal.style.display = 'block';

}

function openRemoveLinkedContractModal(idSalesContract, idItemCode, contractName, itemCodeString) {
    var modal = document.getElementById('remove-linked-contract-modal');
    addClosingAction(modal);

    var idItemSellPriceEle = document.getElementById('remove-linked-contract-id-item-sell-price');
    var idItemCodeEle = document.getElementById('remove-linked-contract-id-item-code');
    var contractNameEle = document.getElementById('remove-linked-contract-name');
    var itemCodeStringEle = document.getElementById('remove-linked-contract-item-code-string');

    idItemSellPriceEle.value = idSalesContract;
    idItemCodeEle.value = idItemCode;
    contractNameEle.value = contractName;
    itemCodeStringEle.value = itemCodeString;

    modal.style.display = 'block';
}

let lastCheckMark;
let lastSupplierMap;
let lastPriceMap;
function onclickItemCodeGrid(curItemCodeGrid) {

let testEle = document.getElementById('test');

    let idItemCode = curItemCodeGrid.children[0].children[1].textContent;

    //CHECK MARK
    let checkMark = curItemCodeGrid.children[0].children[0];
    if (lastCheckMark != null && lastCheckMark != checkMark) {
        lastCheckMark.style.background = 'white';
    }
    checkMark.style.background = '#F9D997';
    lastCheckMark = checkMark;

    //FILTER SUPPLIER MAP
    let supplierMaps = document.getElementsByClassName('item-code-map supplier');
    if (lastSupplierMap != null) {
        lastSupplierMap.style.display = 'none';
    }
    for (let i = 0; i < supplierMaps.length; i ++) {
        if (supplierMaps[i].children[0].textContent == idItemCode) {
            supplierMaps[i].style.display = 'block';
            lastSupplierMap = supplierMaps[i];
            break;
        }
    }

    //FILTER PRICE MAP
    let priceMaps = document.getElementsByClassName('item-code-map price');
    if (lastPriceMap != null) {
        lastPriceMap.style.display = 'none';
    }
    for (let i = 0; i < priceMaps.length; i ++) {
        if (priceMaps[i].children[0].textContent == idItemCode) {
            priceMaps[i].style.display = 'block';
            lastPriceMap = priceMaps[i];
            break;
        }
    }
}
