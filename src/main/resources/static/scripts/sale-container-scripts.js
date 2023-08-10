

function openNewContainerModal() {
    var modal = document.getElementById('new-container-modal');
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

function openEditContainerModal() {
    var modal = document.getElementById('edit-container-modal');
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


var lastContainerCheckMark;
var lastLotGrid;
function onclickSaleContainer(curContainerEle) {

    var idContainer = curContainerEle.children[7].children[1].textContent;
    curIdSaleContainer = idContainer;

    //CHANGE URL
    let sourceSite = window.location.href.split('/')[0];
    let curSection = 'containers';
    let url = sourceSite + "/sales/" + curIdSale + "/articles/" + curIdSaleArticle + "/" + curSection + "/" + curIdSaleContainer + "/";
    window.history.pushState({}, null, url);

    //set temp id container
    document.getElementById('temp-id-container').textContent = idContainer;

    //set all modal id container
    var modalIdContainerArr = document.getElementsByClassName('modal-id-container');
    for (let i = 0; i < modalIdContainerArr.length; i ++) {
        modalIdContainerArr[i].value = idContainer;
    }
    var editInputArr = document.getElementById('edit-container-modal').getElementsByClassName('edit-input');
    var editSelectArr = document.getElementById('edit-container-modal').getElementsByClassName('edit-select');
    var sourceInputArr = curContainerEle.getElementsByClassName('input-source');
    //container name
    editInputArr[0].value = sourceInputArr[0].textContent;
    //claim
    editInputArr[1].checked = sourceInputArr[2].checked;
    //unit
    for (let i = 0 ; i < editSelectArr[0].children.length; i ++) {
        if (editSelectArr[0].children[i].value == sourceInputArr[1].textContent) {
            editSelectArr[0].selectedIndex = i;
            break;
        }
    }

    //hidden buttons
    var hiddenButtonArr = document.getElementById('container-grid-header').getElementsByClassName('hidden-button');
    var lotHiddenButton = document.getElementById('lot-grid-header').getElementsByClassName('hidden-button');
    var inventoryOutHiddenButtons = document.getElementById('inventory-out-grid-header').getElementsByClassName('hidden-button');

    for (let i = 0; i < hiddenButtonArr.length; i ++) {
        if (i == 0) {
            hiddenButtonArr[i].style.display = 'none';
        }
        else {
            hiddenButtonArr[i].style.display = 'inline';
        }
    }
    for (let i = 0; i < lotHiddenButton.length; i ++) {
        lotHiddenButton[i].style.display = 'none';
    }
    for (let i = 0; i < inventoryOutHiddenButtons.length; i ++) {
        inventoryOutHiddenButtons[i].style.display = 'none';
    }

    lotHiddenButton[0].style.display = 'inline';

    //checkMark
    var radioMark = curContainerEle.children[7].children[0]
    if (lastContainerCheckMark != null && lastContainerCheckMark != radioMark) {
        lastContainerCheckMark.style.background = 'white';
    }

    if (lastInventoryOutGrid != null) {
        lastInventoryOutGrid.style.display = 'none';
    }

    radioMark.style.background = '#F9D997';
    lastContainerCheckMark = radioMark;

    //filter Lot grid -- More like Lot Map
    var lotGridArr = document.getElementsByClassName('id-container-key');
    if (lastLotGrid != null) {
        lastLotGrid.style.display = 'none';
    }
    if (lastLotCheckMark != null) {
        lastLotCheckMark.style.background = 'white';
    }

    for (let i = 0; i < lotGridArr.length; i ++) {
        var lotMap = lotGridArr[i];
        if (lotMap.children[0].textContent == idContainer) {
            lotMap.style.display = 'block';
            lastLotGrid = lotMap;
            break;
        }
    }



}


function newUnitInput(curSelectEle) {
    var newUnitInputArr = document.getElementsByClassName('new-unit-input');

    if (curSelectEle[curSelectEle.selectedIndex].value == 'NewUnit') {
        for (let i = 0; i < newUnitInputArr.length; i ++) {
            newUnitInputArr[i].style.display = 'block';
        }
    }
    else {
        for (let i = 0; i < newUnitInputArr.length; i ++) {
            newUnitInputArr[i].style.display = 'none';
        }
    }
}


function openDeleteContainerModal() {
    var modal = document.getElementById('delete-container-modal');
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