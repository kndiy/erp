function toClose(modal) {

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


function openNewLotModal() {
    var modal = document.getElementById('new-lot-modal');
    var closeButton = modal.getElementsByClassName('close-button')[0];

    modal.style.display = 'block';

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

function openEditLotModal() {
    var modal = document.getElementById('edit-lot-modal');
    var closeButton = modal.getElementsByClassName('close-button')[0];

    modal.style.display = 'block';

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

function openDeleteLotModal() {
    var modal = document.getElementById('delete-lot-modal');
    var closeButton = modal.getElementsByClassName('close-button')[0];

    modal.style.display = 'block';

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

function onchangeSupplier(curSupplierSelect) {
    var departureSelectArr = document.getElementsByClassName('modal-departure-select');
    var sourceIdCompany = curSupplierSelect[curSupplierSelect.selectedIndex].value;

    for (let i = 0; i < departureSelectArr.length; i ++) {
        var curSelect = departureSelectArr[i];
        for (let j = 1; j < curSelect.children.length; j += 3) {
            var idCompany = curSelect.children[j].value;
            var companyType = curSelect.children[j + 1].value;
            if (idCompany == sourceIdCompany || companyType.includes('SELF')) {
                curSelect.children[j + 2].style.display = 'block';
            }
            else {
                curSelect.children[j + 2].style.display = 'none';
            }
        }
    }
}

function onchangeDestinationSelect(curDestinationSelect) {
    let receiverArr = document.getElementsByClassName('modal-receiver-select');
    let sourceIdAddress = curDestinationSelect[curDestinationSelect.selectedIndex].value;

    for (let i = 0; i < receiverArr.length; i ++) {
        let curSelect = receiverArr[i];
        for (let j = 1; j < curSelect.children.length; j += 2) {
            let idAddress = curSelect.children[j].value;
            if (idAddress == sourceIdAddress) {
                curSelect.children[j + 1].style.display = 'block';
            }
            else {
                curSelect.children[j + 1].style.display = 'none';
            }
        }
    }
}

var lastLotCheckMark;
var lastInventoryOutGrid;
function onclickSaleLot(curLotGrid) {

    var idSaleLot = curLotGrid.children[0].children[1].textContent;
    curIdSaleLot = idSaleLot;

    //CHANGE URL
    let sourceSite = window.location.href.split('/')[0];
    let curSection = 'lots';
    let url = sourceSite + "/sales/" + curIdSale + "/articles/" + curIdSaleArticle + "/containers/" + curIdSaleContainer + "/" + curSection + "/" + curIdSaleLot + "/";
    window.history.pushState({}, null, url);

    //set all modal-id-lot
    var modalIdLotArr = document.getElementsByClassName('modal-id-lot');
    for (let i = 0; i < modalIdLotArr.length; i ++) {
        modalIdLotArr[i].value = idSaleLot;
    }

    //set temp id lot
    document.getElementById('temp-id-lot').textContent = idSaleLot;

    var editInputArr = document.getElementById('edit-lot-modal').getElementsByClassName('edit-input');
    var editSelectArr = document.getElementById('edit-lot-modal').getElementsByClassName('edit-select');
    var sourceInputArr = curLotGrid.getElementsByClassName('input-source');

    //container name
    editInputArr[0].value = sourceInputArr[0].textContent;
    //orderQuantity
    editInputArr[1].value = sourceInputArr[1].textContent;
    //style
    editInputArr[2].value = sourceInputArr[4].textContent;
    //color
    editInputArr[3].value = sourceInputArr[7].textContent;
    //date
    let dateArr = sourceInputArr[2].textContent.split('/');
    dateArr[1] = months.get(dateArr[1]);
    editInputArr[4].value  = dateArr.join('-');
    //turn
    editInputArr[5].value = sourceInputArr[5].textContent;
    //note
    editInputArr[6].value = sourceInputArr[8].textContent;
    //supplierSettled
    editInputArr[7].checked = sourceInputArr[11].checked;
    //supplier
    for (let i = 1; i < editSelectArr[0].children.length; i += 2) {
        if (editSelectArr[0].children[i].value == sourceInputArr[9].textContent) {
            editSelectArr[0].selectedIndex = i + 1;
            break;
        }
    }
    //departureAddress
    for (let i = 3; i < editSelectArr[1].children.length; i += 3) {
        if (editSelectArr[1].children[i].textContent == sourceInputArr[3].textContent) {
            editSelectArr[1].selectedIndex = i;
            break;
        }
    }
    //destination
    for (let i = 2; i < editSelectArr[2].children.length; i += 2) {
        if (editSelectArr[2].children[i].textContent == sourceInputArr[6].textContent) {
            editSelectArr[2].selectedIndex = i;
            break;
        }
    }
    //receiver
    for (let i = 2; i < editSelectArr[3].children.length; i += 2) {
         if (editSelectArr[3].children[i].textContent == sourceInputArr[10].textContent) {
             editSelectArr[3].selectedIndex = i;
             break;
         }
     }

    //hidden buttons
    var hiddenButtonArr = document.getElementById('lot-grid-header').getElementsByClassName('hidden-button');
    var inventoryOutHiddenButtons = document.getElementById('inventory-out-grid-header').getElementsByClassName('hidden-button');

    for (let i = 0; i < hiddenButtonArr.length; i ++) {
        if (i == 0) {
            hiddenButtonArr[i].style.display = 'none';
        }
        else {
            hiddenButtonArr[i].style.display = 'inline';
        }
    }

    for (let i = 0; i < inventoryOutHiddenButtons.length; i ++) {
        inventoryOutHiddenButtons[i].style.display = 'none';
    }

    inventoryOutHiddenButtons[0].style.display = 'inline';

    //checkMark
    var radioMark = curLotGrid.getElementsByClassName('check-radio')[0];
    if (lastInventoryOutGrid != null) {
        lastInventoryOutGrid.style.display = 'none';
    }

    if (lastLotCheckMark != null) {
        lastLotCheckMark.style.background = 'white';
    }

    radioMark.style.background = '#F9D997';
    lastLotCheckMark = radioMark;

    //filter Inventory Out
    var inventoryOutGridArr = document.getElementsByClassName('id-lot-key');
    for (let i = 0; i < inventoryOutGridArr.length; i ++) {
        if (inventoryOutGridArr[i].children[0].textContent == idSaleLot) {
            inventoryOutGridArr[i].style.display = 'block';
            lastInventoryOutGrid = inventoryOutGridArr[i];

            let numbering = lastInventoryOutGrid.getElementsByClassName('inventory-out-number');
            for (let i = 0; i < numbering.length; i ++) {
                numbering[i].textContent = i + 1;
            }
            break;
        }
    }

    var addNewInventoryOutButton = document.getElementsByClassName('add-inventory-out')[0];
    addNewInventoryOutButton.style.display = 'inline';


}
